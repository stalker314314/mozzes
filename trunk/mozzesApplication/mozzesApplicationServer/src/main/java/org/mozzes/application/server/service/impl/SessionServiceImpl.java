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
package org.mozzes.application.server.service.impl;

import org.mozzes.application.common.exceptions.AuthorizationFailedException;
import org.mozzes.application.common.exceptions.ClientLoggingException;
import org.mozzes.application.common.service.SessionService;
import org.mozzes.application.plugin.authorization.AuthorizationManager;
import org.mozzes.application.server.internal.MozzesInternal;
import org.mozzes.application.server.session.impl.SessionContext;

import com.google.inject.Inject;

/**
 * The Class SessionServiceImpl is the implementation of the service layer around the service invocation.
 */
public class SessionServiceImpl implements SessionService {

	/** The session context. */
	private final SessionContext sessionContext;

	/** The authorization service. */
	private final AuthorizationManager authorizationManager;

	@Inject
	SessionServiceImpl(@MozzesInternal SessionContext sessionContext, AuthorizationManager authorizationManager) {
		this.sessionContext = sessionContext;
		this.authorizationManager = authorizationManager;
	}

	/*
	 * @see SessionService#login(String, String)
	 */
	@Override
	public String login(String username, String password) throws AuthorizationFailedException {
		if (sessionContext.isUserAuthorized())
			throw new ClientLoggingException("user is already authorized");

		long sessionExpirationTime = authorizationManager.authorize(username, password);

		sessionContext.setUserAuthorized(true);
		sessionContext.setSessionExpirationTime(sessionExpirationTime);
		return sessionContext.getSessionId();
	}

	/*
	 * @see SessionService#logout()
	 */
	@Override
	public void logout() {
		if (!sessionContext.isUserAuthorized())
			throw new ClientLoggingException("user is not authorized");
		sessionContext.setUserAuthorized(false);
	}
}
