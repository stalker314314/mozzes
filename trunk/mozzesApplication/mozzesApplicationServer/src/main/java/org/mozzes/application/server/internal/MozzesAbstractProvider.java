package org.mozzes.application.server.internal;

import org.mozzes.application.server.request.RequestManager;

import com.google.inject.Provider;

/**
 * The Class MozzesAbstractProvider is superclass for all providers that are using {@link RequestManager} for
 * implementing the {@link Provider} interface.(SessionContext,RequestContext,TransactionContext,TransactionStack
 * providers)
 */
public abstract class MozzesAbstractProvider<T> implements Provider<T> {

	private RequestManager requestManager;

	protected MozzesAbstractProvider(RequestManager requestManager) {
		this.requestManager = requestManager;
	}

	protected RequestManager getRequestManager() {
		return requestManager;
	}
}
