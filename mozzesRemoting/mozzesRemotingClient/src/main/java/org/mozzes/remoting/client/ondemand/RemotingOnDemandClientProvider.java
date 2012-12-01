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
	 * 
	 * @param remotingConfiguration Remote configuration with which on demand client should be created
	 * @param clientFactory Facory for creating on demand clients
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
