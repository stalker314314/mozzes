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