package org.mozzes.application.remoting.client;

import org.mozzes.application.common.client.MozzesClientConfiguration;
import org.mozzes.application.common.session.SessionIdProvider;
import org.mozzes.invocation.common.handler.InvocationHandler;
import org.mozzes.remoting.client.netty.NettyRemotingClient;
import org.mozzes.remoting.client.netty.NettyRemotingClientProvider;

import org.mozzes.remoting.common.RemotingActionExecutorProvider;
import org.mozzes.remoting.common.RemotingConfiguration;
import org.mozzes.remoting.common.RemotingException;

public class NettyRemoteClientConfiguration extends MozzesClientConfiguration {

	private RemotingActionExecutorProvider clientProvider;

	public NettyRemoteClientConfiguration(RemotingConfiguration remotingConfiguration, Integer clientId) throws RemotingException {
		this.clientProvider = new NettyRemotingClientProvider(new NettyRemotingClient(remotingConfiguration));
	}
	
	public NettyRemoteClientConfiguration(RemotingActionExecutorProvider clientProvider) {
		this.clientProvider = clientProvider;
	}

	@Override
	protected <I> InvocationHandler<I> getInvocationHandler(Class<I> invocationInterface, SessionIdProvider sessionIDProvider) {
		return new RemoteInvocationHandler<I>(clientProvider, sessionIDProvider);
	}

}
