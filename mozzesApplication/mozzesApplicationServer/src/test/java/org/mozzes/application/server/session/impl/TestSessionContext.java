package org.mozzes.application.server.session.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mozzes.application.server.session.impl.SessionContext;

public class TestSessionContext {

	private SessionContext context;
	
	@Before
	public void before(){
		context = new SessionContext();
	}
	
	@Test
	public void testThrowingExceptionIfRequestInProgress() {
		context = new SessionContext();
		context.requestStarted();
		try {
			context.requestStarted();
			fail("shoul throw exception if request in progres and we start new one and old is not finished");
		} catch (Exception e) {
			// ignore it's OK
		}
	}

	@Test
	public void testThrowingExceptionIfRequestNotInProgress() {
		context = new SessionContext();
		context.requestStarted();
		context.requestFinished();
		try {
			context.requestFinished();
			fail("shoul throw exception if request in progres and we start new one and old is not finished");
		} catch (Exception e) {
			// ignore it's OK
		}
	}

	@Test
	public void testNormalWorking() {
		context = new SessionContext();
		context.requestStarted();
		context.requestFinished();
		context.requestStarted();
		context.requestFinished();
		context.requestStarted();
		context.requestFinished();
	}

	@Test
	public void testExpiring() {
		try {
			context = new SessionContext();
			context.setSessionExpirationTime(1);// 1ms
			context.requestStarted();

			Thread.sleep(100);
			// session is now technically expired but because request is started it shouldn't be expired
			assertFalse(context.isExpired());

			context.requestFinished();

			Thread.sleep(100);
			// session is now expired and now request is not i progress so it should be expired really
			assertTrue(context.isExpired());

		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
	}
}
