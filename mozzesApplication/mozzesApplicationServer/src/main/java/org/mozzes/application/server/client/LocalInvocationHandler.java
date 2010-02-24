package org.mozzes.application.server.client;

import org.mozzes.application.common.session.SessionIdProvider;
import org.mozzes.application.plugin.request.RequestProcessor;
import org.mozzes.invocation.common.Invocation;
import org.mozzes.invocation.common.handler.InvocationHandler;


/**
 * The Class LocalInvocationHandler is responsible for invoking server services from the local Mozzes client that's
 * running on the server. It's simply delegating the {@link Invocation} to the {@link RequestProcessor}.
 */
class LocalInvocationHandler<I> implements InvocationHandler<I> {

	private final RequestProcessor requestProcessor;

	private final SessionIdProvider sessionIdProvider;

	LocalInvocationHandler(RequestProcessor requestProcessor, SessionIdProvider sessionIdProvider) {
		this.requestProcessor = requestProcessor;
		this.sessionIdProvider = sessionIdProvider;
	}

	/**
	 * Simply delegates to the requestProcessor
	 * 
	 * @see InvocationHandler#invoke(Invocation)
	 */
	@Override
	public synchronized Object invoke(Invocation<? super I> invocation) throws Throwable {
		return requestProcessor.process(sessionIdProvider.getSessionId(), invocation);
	}
}
