package org.mozzes.application.server.request.impl;

import org.mozzes.application.server.internal.MozzesAbstractProvider;
import org.mozzes.application.server.request.RequestManager;

public class RequestContextProvider extends MozzesAbstractProvider<RequestContext> {

	public RequestContextProvider(RequestManager requestManager) {
		super(requestManager);
	}

	@Override
	public RequestContext get() {
		return getRequestManager().get();
	}

	@Override
	public String toString() {
		return "RequestContextProvider";
	}
}
