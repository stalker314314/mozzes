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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mozzes.application.common.exceptions.AuthorizationFailedException;
import org.mozzes.application.server.mockups.MockUpAuthorizationService;
import org.mozzes.application.server.service.impl.SessionServiceImpl;
import org.mozzes.application.server.session.impl.SessionContext;


public class TestSessionService {

	private SessionServiceImpl service;

	private SessionContext context;

	private MockUpAuthorizationService auth;

	@Before
	public void before() {
		context = new SessionContext();
		auth = new MockUpAuthorizationService();
		service = new SessionServiceImpl(context, auth);
	}

	@Test
	public void testLoginSuccess() {
		try {
			service.login("", "");
			assertTrue(context.isUserAuthorized());
		} catch (AuthorizationFailedException e) {
			fail("should login successfully");
		}
	}

	@Test
	public void testLoginFailure() {
		try {
			auth.setAuthorize(false);
			service.login("", "");
			fail("should login successfully");
		} catch (AuthorizationFailedException e) {
			// ignore it's OK
			assertFalse(context.isUserAuthorized());
		}
	}

	@Test
	public void testLogoutSuccess() {
		try {
			service.login("", "");
			service.logout();

		} catch (AuthorizationFailedException e) {
			fail("should login successfully");
		}
		assertFalse(context.isUserAuthorized());
	}
}
