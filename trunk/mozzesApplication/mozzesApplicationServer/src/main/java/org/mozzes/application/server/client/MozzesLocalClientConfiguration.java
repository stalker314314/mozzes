package org.mozzes.application.server.client;

import org.mozzes.application.common.client.MozzesClientConfiguration;
import org.mozzes.application.common.session.SessionIdProvider;
import org.mozzes.application.plugin.request.RequestProcessor;
import org.mozzes.invocation.common.handler.InvocationHandler;

import com.google.inject.Inject;

/**
 * The Class MozzesLocalClientConfiguration is responsible for configuring the local mozzes client running on the mozzes
 * server.
 */
public class MozzesLocalClientConfiguration extends MozzesClientConfiguration {

	private final RequestProcessor requestProcessor;

	@Inject
	MozzesLocalClientConfiguration(RequestProcessor requestProcessor) {
		this.requestProcessor = requestProcessor;
	}

	/**
	 * Invocation is handled by a localInvocationHanlder that simply passes the call to the requestProcessor.
	 * 
	 * @see MozzesClientConfiguration#getInvocationHandler(Class, SessionIdProvider)
	 */
	@Override
	protected <I> InvocationHandler<I> getInvocationHandler(Class<I> ignore, SessionIdProvider sessionIDProvider) {
		return new LocalInvocationHandler<I>(requestProcessor, sessionIDProvider);
	}
}
