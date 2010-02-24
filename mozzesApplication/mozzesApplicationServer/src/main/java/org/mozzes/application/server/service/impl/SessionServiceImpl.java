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
