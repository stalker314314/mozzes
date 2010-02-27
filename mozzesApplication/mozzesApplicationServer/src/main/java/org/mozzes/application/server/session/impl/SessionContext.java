/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
