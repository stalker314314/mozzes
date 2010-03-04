/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.mozzes.remoting.client.pool;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

import org.mozzes.remoting.client.RemotingClient;
import org.mozzes.remoting.client.RemotingClientFactory;
import org.mozzes.remoting.common.RemotingAction;
import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.common.RemotingActionExecutorProvider;
import org.mozzes.remoting.common.RemotingConfiguration;
import org.mozzes.remoting.common.RemotingException;


/**
 * Holds pool of remoting clients. Pool acts as a executor repository from where users can take executors and also as a
 * module that make sure executors are always refreshed and connected. Remoting clients are never disconnected once they
 * are connected, and clients <b>must</b> return RemotingClients upon action execution. Clients are initialized lazily,
 * meaning that when user ask for the first time for executor, it will then be created and connected. If remoting server
 * is not available, exception will be thrown upon request to take executors. If there are no available executors,
 * method for taking executor blocks. Taking executors is guaranteed to be fair, meaning that if two clients asks for
 * remoting executor and both blocks waiting for one to become available, first one to get it will be the one who asked
 * first.
 * 
 * <p>
 * This class is thread-safe
 * </p>
 * 
 * @author Kokan
 */
public class RemotingClientPool implements RemotingActionExecutorProvider {
    
    /*
     * Implementation details:
     * This pool is organized as producer-consumer pattern. There is one thread that acts as remoting
     * clients producer triggered with semaphore from {@link #getClient} method. {@link #getClient} acts as a consumer.
     * Fairness is guaranteed by using {@link ArrayBlockingQueue}. There is one lock that keeps two invariants. First
     * invariant is that {@link #clientsQueue} size is never larger then {@link #usedClientsSize},
     * <code>clientsQueue.size() <= usedClientsSize</code>. Second invariant is that sum of available and used clients must
     * not be larger then pool size, <code>clientsQueue.size() + clientsQueue.size() <= maxSize</code>. In this concept,
     * {@link #usedClientsSize} is used for performance, and loosely reflects real size of used executors. Refreshing
     * clients is done by generating traffic to remoting server, sending nonexistent action to execution. Refreshing itself
     * is done by acting as a client to this pool, taking and returning remoting executors for refreshing.
     * 
     */
    
    /** time to wait before refreshing clients */
    private static final long TIME_TO_WAIT_BETWEEN_PING = 5 * 60 * 1000;

    /** factory used to create remoting clients */
    private final RemotingClientFactory clientFactory;

    private final RemotingConfiguration remotingConfiguration;

    /** blocking queue in which we hold all unused remoting clients */
    private BlockingQueue<RemotingClient> clientsQueue;
    /** list in which we hold currently used remoting clients */
    private LinkedList<RemotingClient> usedClients;
    /**
     * Number of currently used clients. <b>Do not</b> use <code>usedClients.size()</code> to determine this number.
     * This field is used for performance optimization.
     */
    private int usedClientsSize = 0;

    /** Semaphore that signalizes producer thread to create remoting client */
    private Semaphore clientCreationSemaphore = new Semaphore(0);
    /** Lock to keep pool's invariants */
    private static Object getAndActLock = new Object();

    /** Action that refreshing thread uses to generate traffic to remoting server */
    private final RemotingAction remoteAction = new RemotingAction("nonExistentActionToGenerateTraffic",
            new HashMap<Object, Object>());

    /**
     * Pool constructor
     * 
     * @param clientFactory Factory that should be used to create remoting clients
     * @param maxSize Maximum number of the remoting clients in pool
     */
    public RemotingClientPool(RemotingConfiguration remotingConfiguration, RemotingClientFactory clientFactory,
            int maxSize) {
        this.remotingConfiguration = remotingConfiguration;
        this.clientFactory = clientFactory;

        clientsQueue = new ArrayBlockingQueue<RemotingClient>(maxSize, true);
        usedClients = new LinkedList<RemotingClient>();

        /* starts producer thread */
        ProducerThread producerThread = new ProducerThread();
        producerThread.setDaemon(true);
        producerThread.start();

        /* starts refresh thread */
        RefreshClientThread refreshClientThread = new RefreshClientThread();
        refreshClientThread.setDaemon(true);
        refreshClientThread.start();
    }

    @Override
    public RemotingActionExecutor get() {
        return new RemotingClientPoolWrapper(this);
    }

    /**
     * Gets executor from pool. Connection with remoting server is not guaranteed to be established. Provided executor
     * blocks until executor is available. When finished with using it, users must return instance of this executor back
     * to pool. This is usually done with finally idiom:
     * 
     * <pre>
     * RemotingActionExecutor executor = null;
     * try{
     * 	executor = pool.getExecutor();
     * 	executor.execute(...);
     * }finally{
     * 	if (executor != null) pool.closeExecutor(executor);
     * }
     * </pre>
     * 
     * @return Executor, connected and ready to execute action. Users should not cast, connect or disconnect this
     *         executor on their own.
     */
    protected RemotingClient getClient() {
        return getClient(true);
    }

    /**
     * Returns executor back to pool
     * 
     * @param executor Executor taken with {@link #getClient()} method
     */
    protected void closeClient(RemotingClient executor) {
        synchronized (getAndActLock) {
            usedClients.remove(executor);
            usedClientsSize--;
            clientsQueue.offer(executor);
        }
    }

    /**
     * Helper method for taking executor that will not may not block if executors are currently not available, depending
     * on arguments
     * 
     * @param blockIfNotExist If <code>true</code>, method will block waiting for some remoting client to be available.
     *            If <code>false</code>, method will return immediately, returning null
     * @return Executor Executor from queue, can be <code>null</code> if blockIfNotExist is <code>false</code> and there
     *         are no available remoting clients.
     */
    private RemotingClient getClient(boolean blockIfNotExist) {
        RemotingClient client = null;
        /* checks if we there is available remoting clients, and if we should create one */
        synchronized (getAndActLock) {
            int availableClients = clientsQueue.size();
            if ((availableClients == 0) && (blockIfNotExist == false)) {
                return null;
            }
            if ((availableClients == 0) && (usedClientsSize < clientsQueue.remainingCapacity())) {
                clientCreationSemaphore.release();
            }
            usedClientsSize++;
        }
        /* take remoting client from queue and block if one is not yet available */
        try {
            client = clientsQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        /* make sure user get connected client */
        assert client !=null;
        if (client.isConnected() == false) {
            try {
                client.connect();
            } catch (RemotingException e) {
                /* swallow this exception and return client that is not connected....life sucks:( */
            }
        }
        /* actually add client to used clients list */
        usedClients.add(client);
        return client;
    }

    /**
     * Simple producer thread that waits for semaphore and created remoting clients by putting them in queue
     * 
     * @author Kokan
     */
    class ProducerThread extends Thread {
        @Override
        public void run() {
            while (true) {
                clientCreationSemaphore.acquireUninterruptibly();
                clientsQueue.offer(clientFactory.create(remotingConfiguration));
            }
        }
    }

    /**
     * Refresh thread. Gets executors from pool and executes non-existent action to generate traffic, keeping remoting
     * clients always connected
     * 
     * @author Kokan
     */
    class RefreshClientThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(TIME_TO_WAIT_BETWEEN_PING);
                } catch (InterruptedException e) {
                }
                RemotingClient client = null;
                try {
                    /*
                     * get executor only if one is available. We don't want refresh thread to create executors
                     * unnecessarily
                     */
                    client = getClient(false);
                    if (client == null) {
                        continue;
                    }
                    if (client.isConnected() == false) {
                        client.connect();
                    }
                    client.execute(remoteAction);
                } catch (RemotingException e) {
                } finally {
                    if (client != null)
                        closeClient(client);
                }
            }
        }
    }
}
