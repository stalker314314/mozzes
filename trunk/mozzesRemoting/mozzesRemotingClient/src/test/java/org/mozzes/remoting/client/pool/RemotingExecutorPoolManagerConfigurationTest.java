package org.mozzes.remoting.client.pool;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozzes.remoting.client.RemotingExecutorProviderFactory;
import org.mozzes.remoting.common.RemotingConfiguration;


/**
 * Test configuration of remoting executor pool manager effectively creating remoting executor pool manager's execution
 * policy
 * 
 * @author Kokan
 */
public class RemotingExecutorPoolManagerConfigurationTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Tests if manager properly determines that configuration does not exists
     */
    @Test
    public void testConfigurationDoesNotExists() {
        RemotingExecutorProviderFactory providerFacotry = new RemotingExecutorProviderFactory(
                new MockupRemotingClientFactory(1));
        RemotingConfiguration conf = new RemotingConfiguration("localhost", 10000);
        assertEquals(false, providerFacotry.configurationExists(conf));
    }

    /**
     * Tests that adding new configurations works and checking that they exists
     */
    @Test
    public void testAddConfiguration() {
        RemotingExecutorProviderFactory providerFactory = new RemotingExecutorProviderFactory(
                new MockupRemotingClientFactory(1));
        /* adds new configurations */
        for (int i = 1000; i < 2000; i++) {
            try {
                providerFactory.addProvider("localhost", i, 10);
            } catch (Throwable t) {
                fail();
            }
        }
        /* checks they exists */
        for (int i = 1000; i < 2000; i++) {
            RemotingConfiguration conf = new RemotingConfiguration("localhost", i);
            assertEquals(true, providerFactory.configurationExists(conf));
        }
        /* checks others don't exists */
        for (int i = 2000; i < 3000; i++) {
            RemotingConfiguration conf = new RemotingConfiguration("localhost", i);
            assertEquals(false, providerFactory.configurationExists(conf));
        }
    }

    /**
     * This test checks that adding configuration that exists fails. Indirectly is also tested hashCode and equals
     * methods of {@link RemotingConfiguration} class
     */
    @Test
    public void testAddWhenConfigurationExists() {
        RemotingExecutorProviderFactory providerFactory = new RemotingExecutorProviderFactory(
                new MockupRemotingClientFactory(1));
        providerFactory.addProvider("localhost", 10000, 10);
        try {
            providerFactory.addProvider("localhost", 10000, 10);
            fail();
        } catch (Throwable t) {
        }
        try {
            providerFactory.addProvider("localhost", 10000, 10);
            fail();
        } catch (Throwable t) {
        }
    }
}