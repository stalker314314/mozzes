package org.mozzes.application.server.session.impl;

import org.mozzes.application.server.internal.MozzesAbstractProvider;
import org.mozzes.application.server.request.RequestManager;

public class SessionContextProvider extends MozzesAbstractProvider<SessionContext>{

	public SessionContextProvider(RequestManager requestManager){
		super(requestManager);
	}
		
	@Override
	public SessionContext get() {
		return getRequestManager().get().getSessionContext();
	}

	@Override
	public String toString() {
		return "SessionContextProvider";
	}
}
