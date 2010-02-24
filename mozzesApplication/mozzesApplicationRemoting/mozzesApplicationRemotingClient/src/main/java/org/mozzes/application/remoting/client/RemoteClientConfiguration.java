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
