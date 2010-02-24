package org.mozzes.application.demo.apps.scopedata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.common.exceptions.AuthorizationFailedException;
import org.mozzes.application.demo.apps.TestBase;
import org.mozzes.application.demo.mockups.services.scopedata.ServiceWithTransactionData;


public class TestTransactionScope extends TestBase {

	@Test
	public void testTransactionScope() {
		try {
			MozzesClient client = getClient();
			client.login(validUsername1, validPassword1);

			assertEquals(1, client.getService(ServiceWithTransactionData.class).incrementAndReturn());

			client.logout();

		} catch (AuthorizationFailedException e) {
			fail("user should login successfully");
		}
	}

	@Test
	public void testTransactionScopeWithoutLogging() {
		assertEquals(1, getClient().getService(ServiceWithTransactionData.class).incrementAndReturn());
	}

	@Test
	public void testTransactionScopeBadAnnotation() {
		try {
			MozzesClient client = getClient();
			client.login(validUsername1, validPassword1);

			// @Transactional is misplaced(in the implementation class not in the interface so it's 2 not 1
			assertEquals(2, client.getService(ServiceWithTransactionData.class).incrementAndReturnBad());

			client.logout();
		} catch (AuthorizationFailedException e) {
			fail("user should login successfully");
		}
	}
}
