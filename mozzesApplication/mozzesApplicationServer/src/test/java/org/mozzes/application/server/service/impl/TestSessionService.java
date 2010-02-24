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
