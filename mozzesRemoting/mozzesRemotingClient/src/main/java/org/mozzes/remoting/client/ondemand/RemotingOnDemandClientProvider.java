package org.mozzes.remoting.client.ondemand;

import org.mozzes.remoting.client.RemotingClientFactory;
import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.common.RemotingActionExecutorProvider;
import org.mozzes.remoting.common.RemotingConfiguration;


/**
 * Executor provider that always return on-demand executor
 * 
 * @author Kokan
 */
public class RemotingOnDemandClientProvider implements RemotingActionExecutorProvider {
    /** Client factory that we provide to on-demand client */
    private final RemotingClientFactory clientFactory;

    private final RemotingConfiguration remotingConfiguration;

    /**
     * Default constructor that takes client factory
     */
    public RemotingOnDemandClientProvider(RemotingConfiguration remotingConfiguration,
            RemotingClientFactory clientFactory) {
        this.clientFactory = clientFactory;
        this.remotingConfiguration = remotingConfiguration;
    }

    @Override
    public RemotingActionExecutor get() {
        return new RemotingClientOnDemand(remotingConfiguration, clientFactory);
    }
}
