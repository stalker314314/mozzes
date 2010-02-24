package org.mozzes.remoting.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.mozzes.remoting.client.ondemand.RemotingOnDemandClientProvider;
import org.mozzes.remoting.client.pool.RemotingClientPool;
import org.mozzes.remoting.common.RemotingActionExecutorProvider;
import org.mozzes.remoting.common.RemotingConfiguration;


/**
 * Main class for utilizing remoting clients providers. <br>
 * Clients first need to configure pool with either of two supplied methods:
 * {@link #addProvider(String, int, int)} and {@link #addProvider(String, int)}.
 * First one configures factory to return pool provider and former on-demand providers. Remote action executor can then
 * be picked from providers with {@link RemotingActionExecutorProvider#get()}. Nothing else is required from clients.
 * 
 * <p>
 * This class is thread-safe
 * </p>
 * 
 * @author Kokan
 */
public class RemotingExecutorProviderFactory {

    /** map of configuration -> executor provider */
    private ConcurrentMap<RemotingConfiguration, RemotingActionExecutorProvider> pools = new ConcurrentHashMap<RemotingConfiguration, RemotingActionExecutorProvider>();

    /** Factory for creating remoting clients */
    private final RemotingClientFactory clientFactory;

    /**
     * Default constructor
     */
    public RemotingExecutorProviderFactory(RemotingClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    /**
     * Adds new configuration to factory that will use pool to provide remoting clients. Pool is good when we know that
     * large number of users/threads will execute action on remoting server and this minimize overhead needed for
     * constant connection/disconnecting. If identical configuration already exists, throws
     * {@link IllegalArgumentException}
     * 
     * @param host server address
     * @param port server listening port
     * @param poolSize Maximum size of the pool for this configuration
     */
    public RemotingExecutorProviderFactory addProvider(String host, int port, int poolSize) {
        RemotingConfiguration configuration = new RemotingConfiguration(host, port);
        if (poolSize <= 0) {
            addConfiguration(configuration, new RemotingOnDemandClientProvider(configuration, clientFactory));
        } else {
            addConfiguration(configuration, new RemotingClientPool(configuration, clientFactory, poolSize));
        }
        return this;
    }

    /**
     * Adds new configuration to factory that will return on-demand providers. On-demand providers should be used when
     * very small number of actions is going to be executed on remoting server If identical configuration already
     * exists, throws {@link IllegalArgumentException}
     */
    public RemotingExecutorProviderFactory addProvider(String host, int port) {
        addProvider(host, port, 0);
        return this;
    }

    /**
     * Maps configuration with supplied provider
     */
    private void addConfiguration(RemotingConfiguration configuration, RemotingActionExecutorProvider executorProvider) {
        if (pools.putIfAbsent(configuration, executorProvider) != null) {
            throw new IllegalArgumentException("Configuration already exists");
        }
    }

    /**
     * @return <code>true</code> if specified configuration already exists, <code>false</code> otherwise
     */
    public boolean configurationExists(RemotingConfiguration configuration) {
        return pools.containsKey(configuration);
    }

    /**
     * Gets the action execution provider for specified configuration. If this configuration is not previously
     * configured with this manager, {@link IllegalArgumentException} is thrown
     * 
     * @param host server address
     * @param port server listening port
     * @return Provider that knows how to provide remoting clients form given configuration
     */
    public RemotingActionExecutorProvider getActionExecutionProvider(String host, int port) {
        RemotingActionExecutorProvider pool = pools.get(new RemotingConfiguration(host, port));
        if (pool == null) {
            throw new IllegalArgumentException("Configuration does not exists");
        }
        return pool;
    }
}