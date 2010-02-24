package org.mozzes.application.server.session.impl;

import org.mozzes.application.common.session.SessionException;
import org.mozzes.application.server.internal.AbstractContext;


/**
 * The Class SessionContext is associated with the session and hold's the data important for the session.
 */
public class SessionContext extends AbstractContext {

	private static final long DEFAULT_SESSION_EXPIRATION_TIME = 30 * 60 * 1000;

	private long expirationTime = DEFAULT_SESSION_EXPIRATION_TIME;

	private String sessionId;

	private boolean userAuthorized = false;

	/** Time of user's last activity. */
	private long lastActivityTime;

	/** True if there is some request currently in progress. */
	private boolean requestInProgress = false;

	public SessionContext() {
		this.lastActivityTime = System.currentTimeMillis();
	}

	/**
	 * With this method session is informed that new request is started.
	 */
	public synchronized void requestStarted() {
		if (requestInProgress)
			throw new SessionException("Request already in progress");

		this.requestInProgress = true;
	}

	/**
	 * With this method session is informed that currently running request is finished
	 */
	public synchronized void requestFinished() {
		if (!requestInProgress)
			throw new SessionException("No request in progress");

		this.lastActivityTime = System.currentTimeMillis();
		this.requestInProgress = false;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setUserAuthorized(boolean userAuthorized) {
		this.userAuthorized = userAuthorized;
	}

	public boolean isUserAuthorized() {
		return userAuthorized;
	}

	public boolean isExpired() {
		if (requestInProgress)
			return false;

		if (expirationTime == 0){
			return false;
		}

		return (System.currentTimeMillis() - lastActivityTime) > expirationTime;
	}

	public void setSessionExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	@Override
	protected String getName() {
		return "SessionContext";
	}

	boolean isRequestInProgress() {
		return requestInProgress;
	}

	@Override
	public String toString() {
		return "Session[" + sessionId + "]";
	}
}
