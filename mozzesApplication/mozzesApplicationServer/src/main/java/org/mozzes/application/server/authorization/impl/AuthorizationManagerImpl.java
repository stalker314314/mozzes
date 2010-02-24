package org.mozzes.application.server.authorization.impl;

import org.mozzes.application.plugin.authorization.AuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class AuthorizationManagerImpl add Authorization support to the application framework. This default version don't
 * do anything it's authorizing every user so users of the framework are required to specify their implementation
 */
public class AuthorizationManagerImpl implements AuthorizationManager {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationManager.class);

	private static final int SESSION_EXPIRATION_TIME = 30 * 60 * 1000; //30 minutes
	
	/*
	 * @see AuthorizationManager#authorize(String, String)
	 */
	@Override
	public long authorize(String username, String password) {
		logger.debug("User " + username + "/" + password + " authorized");
		return SESSION_EXPIRATION_TIME;
	}
}
