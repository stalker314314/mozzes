package org.mozzes.application.server.mockups;

import org.mozzes.application.server.request.RequestManager;
import org.mozzes.application.server.request.impl.RequestContext;
import org.mozzes.application.server.session.impl.SessionContext;

public class MockUpRequestManager implements RequestManager{

	@Override
	public void finish() {
		// ignore
	}

	@Override
	public RequestContext get() {
		return null;
	}

	@Override
	public void start(SessionContext sc) {
		// ignore
	}
}
