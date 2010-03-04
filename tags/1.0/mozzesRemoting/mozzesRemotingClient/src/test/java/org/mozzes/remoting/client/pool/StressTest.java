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
import java.util.concurrent.CountDownLatch;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozzes.remoting.client.pool.RemotingClientPool;
import org.mozzes.remoting.common.RemotingAction;
import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.common.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Stress test of remoting client pool. All tests are using prime numbers so congruence between number of clients in
 * pool and number of thread doesn't have effect
 * 
 * @author Kokan
 */
public class StressTest extends TestCase {

	private Logger logger = LoggerFactory.getLogger(StressTest.class);
	
    @Override
    @Before
    public void setUp() throws Exception {
    }

    @Override
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Simple test, with maximum of one client in pool, and 1 request to it
     */
    @Test
    public void testSimple() {
        runTest(1, 1, 1, 1000);
    }

    /**
     * Pool stress test with only 2 clients in pool and 100 threads executing 100 times each. Every action takes 1 ms,
     * so this method should end in minimally (97 * 97) / 2 active clients ~= 5s (6-15s in practice)
     */
    @Test
    public void testSmallPool() {
        runTest(2, 53, 51, 1);
    }

    /**
     * This test have pool larger then all actions (namely 100 clients in pool, and 5 threads that executes 5 times)
     */
    @Test
    public void testLargePool() {
        runTest(97, 5, 5, 10);
    }

    /**
     * Large pool and large number of threads
     */
    @Test
    public void testSuperstressMe() {
        runTest(97, 93, 97, 1);
    }

    /**
     * Concrete test runner
     * 
     * @param poolMaxSize Max size of the pool to be configured with
     * @param numThreads Number of threads to spawn to execute client actions
     * @param loopsInThread Number of times each of the thread should execute action
     * @param timeoutInExecute How much to wait in action execution
     */
    private void runTest(int poolMaxSize, int numThreads, int loopsInThread, int timeoutInExecute) {
        RemotingClientPool pool = new RemotingClientPool(null, new MockupRemotingClientFactory(timeoutInExecute), 2);
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(numThreads);
        for (int i = 0; i < numThreads; i++) {
            Thread t = new StressTestThread(pool, i + 1, loopsInThread, startGate, endGate);
            t.start();
        }
        startGate.countDown();
        try {
            endGate.await();
        } catch (InterruptedException e) {
        }
    }

    /**
     * Stress test thread that iterates specified number of times in a loop, gets client from pool and executes action
     * 
     * @author Kokan
     */
    class StressTestThread extends Thread {
        private final RemotingClientPool pool;
        private final int id;
        private final int nLoops;

        private final CountDownLatch startGate;
        private final CountDownLatch endGate;

        /**
         * Constructs stress test thread
         * 
         * @param pool Pool to get clients from
         * @param id Id of this thread (for debug purposes only)
         * @param nLoops Number of iteration loops
         * @param startGate Latch gate that tells this thread it can start iteration
         * @param endGate Latch gate that callback this thread ended execution
         */
        StressTestThread(RemotingClientPool pool, int id, int nLoops, CountDownLatch startGate, CountDownLatch endGate) {
            this.pool = pool;
            this.id = id;
            this.nLoops = nLoops;
            this.startGate = startGate;
            this.endGate = endGate;
        }

        @Override
        public void run() {
            try {
                startGate.await();
                for (int i = 0; i < nLoops; i++) {
                    try {
                        RemotingActionExecutor executor = pool.get();
                        try {
                            executor.execute(new RemotingAction("ping", new HashMap<Object, Object>()));
                            logger.info("Thread id " + id + " executed " + (i + 1) + ". time");
                            System.out.flush();
                        } catch (RemotingException e) {
                            fail();
                        }
                    } finally {
                    }
                }
                endGate.countDown();
            } catch (InterruptedException e1) {
            }
        }
    }
}
