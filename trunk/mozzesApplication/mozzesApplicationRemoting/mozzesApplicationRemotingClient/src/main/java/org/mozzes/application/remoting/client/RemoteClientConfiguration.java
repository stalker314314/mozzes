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
package org.mozzes.application.remoting.client;

import org.mozzes.application.common.client.MozzesClientConfiguration;
import org.mozzes.application.common.session.SessionIdProvider;
import org.mozzes.invocation.common.handler.InvocationHandler;
import org.mozzes.remoting.client.core.DefaultRemotingClientFactory;
import org.mozzes.remoting.client.pool.RemotingClientPool;
import org.mozzes.remoting.common.RemotingActionExecutorProvider;
import org.mozzes.remoting.common.RemotingConfiguration;


/**
 * The Class RemoteClientConfiguration extends basic {@link MozzesClientConfiguration} with the remoting executor
 * as a {@link InvocationHandler} so this basically means that every action execution will be sent on the remote server
 * for execution.
 */
public class RemoteClientConfiguration extends MozzesClientConfiguration {

	/** The client provider. */
	private final RemotingActionExecutorProvider clientProvider;

	public RemoteClientConfiguration(String serverHost, int serverPort) {
		this(serverHost, serverPort, 1);
	}

	public RemoteClientConfiguration(String serverHost, int serverPort, int clientPoolSize) {
		this.clientProvider = new RemotingClientPool(new RemotingConfiguration(serverHost, Integer.valueOf(serverPort)),
				new DefaultRemotingClientFactory(), clientPoolSize);
	}

	/*
	 * @see MozzesClientConfiguration#getInvocationHandler(Class, SessionIdProvider)
	 */
	@Override
	protected <I> InvocationHandler<I> getInvocationHandler(Class<I> invocationClass,
			SessionIdProvider sessionIDProvider) {

		return new RemoteInvocationHandler<I>(clientProvider, sessionIDProvider);
	}
}
