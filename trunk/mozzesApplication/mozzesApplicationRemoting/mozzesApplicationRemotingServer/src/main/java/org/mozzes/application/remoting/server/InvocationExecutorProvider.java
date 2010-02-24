package org.mozzes.application.remoting.server;

import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.common.RemotingActionExecutorProvider;

import com.google.inject.Inject;

/**
 * Provider for {@link RemotingActionExecutor} that is executing remote service calls on the server
 */
class InvocationExecutorProvider implements RemotingActionExecutorProvider {

	private final InvocationActionExecutor executor;

	@Inject
	public InvocationExecutorProvider(InvocationActionExecutor executor) {
		this.executor = executor;
	}

	/*
	 * @see RemotingActionExecutorProvider#get()
	 */
	@Override
	public RemotingActionExecutor get() {
		return executor;
	}
}
