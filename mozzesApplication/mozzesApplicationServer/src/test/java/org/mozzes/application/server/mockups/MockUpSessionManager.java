package org.mozzes.application.server.mockups;

import org.mozzes.application.server.session.SessionManager;
import org.mozzes.application.server.session.impl.SessionContext;

public class MockUpSessionManager implements SessionManager {

	private boolean requestStarted;

	@Override
	public void requestFinished(SessionContext sessionContext) {
		// ignore
	}

	@Override
	public SessionContext requestStarted(String sessionId) {
		SessionContext c = new SessionContext();
		c.setSessionId(sessionId);
		requestStarted = true;
		return c;
	}

	public boolean isRequestStarted() {
		return requestStarted;
	}
}
