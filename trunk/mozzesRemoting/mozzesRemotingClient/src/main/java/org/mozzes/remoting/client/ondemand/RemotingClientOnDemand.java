package org.mozzes.remoting.client.ondemand;

import org.mozzes.remoting.client.RemotingClient;
import org.mozzes.remoting.client.RemotingClientFactory;
import org.mozzes.remoting.client.RemotingExecutorProviderFactory;
import org.mozzes.remoting.common.RemotingAction;
import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.common.RemotingConfiguration;
import org.mozzes.remoting.common.RemotingException;
import org.mozzes.remoting.common.RemotingResponse;


/**
 * Wrapper around default implementation of {@link RemotingClient}. This on-demand executor enables automatically
 * connecting to remoting server, executing action, and disconnect after that. Only thing clients of this class must do
 * is explicitly calling action execution.
 * <p>
 * Example of executor usage:
 * 
 * <pre>
 * RemotingActionExecutor executor = new RemotingClientOnDemand(&quot;www.example.com&quot;, 6677);
 * executor.execute(...);
 * </pre>
 * 
 * This executor will connect to www.example.com on port 6677 and execute action. Connection closing is guaranteed after
 * action execution.
 * </p>
 * <p>
 * This executor should be used when clients know only one action is going to be executed and this helps reducing
 * boiler-plate code that should be written for regular {@link RemotingClient}. If clients suspects larger number of
 * actions are going to be executed, one should think about remoting client pool that can be configured with
 * {@link RemotingExecutorProviderFactory} since this implementation adds certain overhead while constantly connecting
 * and disconnecting.
 * </p>
 * 
 * @author Perica Milosevic
 * @author Kokan
 */
class RemotingClientOnDemand implements RemotingActionExecutor {
    
    /** Factory we used to create and wrap remoting clients */
    private final RemotingClientFactory clientFactory;

    private final RemotingConfiguration remotingConfiguration;

    /**
     * Default constructor for this executor
     */
    RemotingClientOnDemand(RemotingConfiguration remotingConfiguration, RemotingClientFactory clientFactory) {
        this.remotingConfiguration = remotingConfiguration;
        this.clientFactory = clientFactory;
    }

    @Override
    public RemotingResponse execute(RemotingAction action) throws RemotingException {
        RemotingClient client = clientFactory.create(remotingConfiguration);
        try {
            client.connect();
            return client.execute(action);
        } finally {
            client.disconnect();
        }
    }
}