package org.mozzes.application.server.session.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;
import org.mozzes.application.server.session.impl.SessionContext;
import org.mozzes.application.server.session.impl.SessionManagerImpl;

public class TestSessionManager {

	private SessionManagerImpl manager;

	@Before
	public void before() {
		try {
			manager = new SessionManagerImpl();
		} catch (NoSuchAlgorithmException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test1() {
		try {
			manager.requestStarted(Math.random() + "");
			fail("should throw exception if we want to start request in session that not exists(sesisonId is unknown)");
		} catch (Throwable t) {
			// ignore it's OK
		}
	}

	@Test
	public void test2() {
		try {
			SessionContext context = manager.requestStarted(null);
			assertTrue(context.isRequestInProgress());
			manager.requestFinished(context);
		} catch (Throwable t) {
			fail("shouldn't throw exception if we want to start request in new session(sesisonId is null so manager should create new session)");
		}
	}

	@Test
	public void test3() {
		try {
			assertEquals(0, manager.getSessionCount()); // there's no session started
			SessionContext context = manager.requestStarted(null);
			context.setUserAuthorized(true);
			assertEquals(1, manager.getSessionCount()); // now is one session active
			assertTrue(context.isRequestInProgress());// it need to have request in progress
			manager.requestFinished(context);
			assertEquals(1, manager.getSessionCount());// there's still one session because user is authorized

		} catch (Throwable t) {
			fail("shouldn't throw exception if we want to start request in new session(sesisonId is null so manager should create new session)");
		}
	}
}
