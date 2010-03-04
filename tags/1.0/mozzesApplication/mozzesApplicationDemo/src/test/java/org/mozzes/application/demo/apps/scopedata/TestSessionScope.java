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
package org.mozzes.application.demo.apps.scopedata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.common.exceptions.AuthorizationFailedException;
import org.mozzes.application.demo.apps.TestBase;
import org.mozzes.application.demo.mockups.scopedata.MSessionData;
import org.mozzes.application.demo.mockups.services.scopedata.ServiceWithSessionData;


/**
 * In this test case we're testing whether the sessionScoped object are really stored in the session scope.
 * 
 * @author vita
 */
public class TestSessionScope extends TestBase {

	/**
	 * When the client is logged in, we're calling the incrementSessionCounter 3 times and then getCounter value
	 * 
	 * if the {@link MSessionData} is stored in the session
	 */
	@Test
	public void testClientSession() {
		try {
			MozzesClient client = getClient();

			client.login(validUsername1, validPassword1);
			assertEquals(3, clientCallService(client, 3));
			client.logout();

			client.login(validUsername1, validPassword1); // because we logged out counter is restarted
			assertEquals(1, clientCallService(client, 1));
			client.logout();

			client.login(validUsername1, validPassword1);// because we didn't logged out counter is NOT restarted
			assertEquals(1, clientCallService(client, 1));
			assertEquals(4, clientCallService(client, 3));
			client.logout();

		} catch (AuthorizationFailedException e) {
			fail("user should login successfully");
		}
	}

	@Test
	public void testClientSessionLostWithoutLogging() {
		MozzesClient client = getClient();

		// between service calls nothing is preserved so it's always 0 because when we call serviceMethod
		// getSessionCounterValue session is restarted and it's 0 again;
		assertEquals(0, clientCallService(client, 3));
	}

	private int clientCallService(MozzesClient client, int callCount) {
		ServiceWithSessionData service = client.getService(ServiceWithSessionData.class);
		for (int i = 0; i < callCount; i++) {
			service.incrementSessionCounter();
		}
		return service.getSessionCounterValue();
	}
}
