package org.mozzes.application.demo.apps.scopedata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.common.exceptions.AuthorizationFailedException;
import org.mozzes.application.demo.apps.TestBase;
import org.mozzes.application.demo.mockups.services.scopedata.ServiceWithRequestData;


/**
 * @author vita
 */
public class TestRequestScope extends TestBase {

	@Test
	public void testRequestScope() {
		try {

			MozzesClient client = getClient();
			client.login(validUsername1, validPassword1);

			int requestCounter = client.getService(ServiceWithRequestData.class)
					.incrementAndReturnInTheRequestContext();
			assertEquals(2, requestCounter);

			int requestCounter2 = client.getService(ServiceWithRequestData.class)
					.incrementAndReturnInTheRequestContext();
			assertEquals(2, requestCounter2);

			client.logout();

		} catch (AuthorizationFailedException e) {
			fail("user should login successfully");
		}
	}

	@Test
	public void testRequestScopeWithoutLogging() {
		MozzesClient client = getClient();

		int requestCounter = client.getService(ServiceWithRequestData.class).incrementAndReturnInTheRequestContext();
		assertEquals(2, requestCounter);

		int requestCounter2 = client.getService(ServiceWithRequestData.class).incrementAndReturnInTheRequestContext();
		assertEquals(2, requestCounter2);
	}
}
