package org.mozzes.remoting.client;

import org.mozzes.remoting.client.core.DefaultRemotingClientFactory;
import org.mozzes.remoting.common.RemotingConfiguration;


/**
 * Interface that all factory form creating remoting clients should implement. For default implementation, look at
 * {@link DefaultRemotingClientFactory}
 * 
 * @author Perica Milosevic
 * @author Kokan
 */
public interface RemotingClientFactory {

    /**
     * Creates remoting client
     * 
     * @return RemotingClient
     */
    public RemotingClient create(RemotingConfiguration remotingConfiguration);
}