package org.mozzes.remoting.client.core;

import org.mozzes.remoting.client.RemotingClient;
import org.mozzes.remoting.client.RemotingClientFactory;
import org.mozzes.remoting.common.RemotingConfiguration;


/**
 * Simple remote factory that always returns standard remote client implementation. Users using this class must always
 * explicitly connect before action execution and disconnect (in finally block) after action execution
 * 
 * @author Kokan
 */
public class DefaultRemotingClientFactory implements RemotingClientFactory {

    public DefaultRemotingClientFactory() {
    }

    @Override
    public RemotingClient create(RemotingConfiguration remotingConfiguration) {
        return new RemotingClientImpl(remotingConfiguration);
    }
}
