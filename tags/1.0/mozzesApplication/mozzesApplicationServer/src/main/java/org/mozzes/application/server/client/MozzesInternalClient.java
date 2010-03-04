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
package org.mozzes.application.server.client;

import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.common.exceptions.MozzesRuntimeException;
import org.mozzes.application.common.service.SessionService;

import com.google.inject.Inject;

/**
 * This is the internal client that runs on the server
 * 
 * @author vita
 */
public class MozzesInternalClient extends MozzesClient {

	public static final String INTERNAL_CLIENT_ID = "INTERNAL_CLIENT_ID";

	@Inject
	public MozzesInternalClient(MozzesInternalClientConfiguration clientConfiguration) {
		super(clientConfiguration);
		setSessionId(INTERNAL_CLIENT_ID);
	}

	/**
	 * Internal client shouldn't store its state on the server so {@link #login(String, String)} method shouldn't be
	 * called.
	 * 
	 * @see MozzesClient#login(String, String)
	 * @see SessionService#login(String, String)
	 */
	@Override
	public void login(String username, String password) {
		throw new MozzesRuntimeException("this method is not enabled for internal client");
	}

	/**
	 * Because {@link #login(String, String)} shouldn't be called also {@link #logout()} shouldn't be called.
	 * 
	 * @see MozzesClient#logout()
	 * @see SessionService#logout()
	 */
	@Override
	public void logout() {
		throw new MozzesRuntimeException("this method is not enabled for internal client");
	}
}
