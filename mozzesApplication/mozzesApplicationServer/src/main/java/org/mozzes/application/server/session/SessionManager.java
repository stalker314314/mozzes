package org.mozzes.application.server.session;

import org.mozzes.application.server.session.impl.SessionContext;

/**
 * The Interface SessionManager specifies the methods responsible for working with sessions in the mozzes server.
 */
public interface SessionManager {

	/**
	 * Returns the session context in which the new request is
	 */
	public SessionContext requestStarted(String sessionId);

	/**
     */
	public void requestFinished(SessionContext sessionContext);
}
