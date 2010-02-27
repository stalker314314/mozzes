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
