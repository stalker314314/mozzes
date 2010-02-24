package org.mozzes.application.demo.apps.scoped;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.common.exceptions.AuthorizationFailedException;
import org.mozzes.application.demo.apps.TestBase;
import org.mozzes.application.demo.mockups.services.scoped.session.SessionScopedService;
import org.mozzes.application.demo.mockups.services.scoped.session.impl.SessionScopedServiceImpl;
import org.mozzes.application.module.scope.RequestScoped;
import org.mozzes.application.module.scope.SessionScoped;
import org.mozzes.application.module.scope.TransactionScoped;


/**
 * This test cast is used to demonstrate how the service implementation classes are scoped. If the Service
 * Implementation class is annotated with {@link SessionScoped}, {@link RequestScoped} or {@link TransactionScoped} the
 * server will use the provided scope for instantiating the service implementation class.
 * 
 * For example, If the service class is annotated with {@link SessionScoped} it would be the same instance of the
 * service class between the service invocations in the same session. If there's some attribute in the service
 * implementation class it would have the state preserved between the service calls in the same session
 * 
 * @author vita
 */
public class TestSessionScopedService extends TestBase {

	/**
	 * Test if the value of the attribute in the {@link SessionScopedServiceImpl} service class is preserved between the
	 * service calls
	 */
	@Test
	public void testSessionScopedService() {
		try {
			// login
			MozzesClient client = getClient();
			client.login(validUsername1, validPassword1);

			callServiceAndTestResult(client);

			// logout and close the session
			client.logout();

			// repeat in the new session and now value should be the same
			client.login(validUsername1, validPassword1);
			callServiceAndTestResult(client);

		} catch (AuthorizationFailedException e) {
			fail("should login");
		}
	}

	private void callServiceAndTestResult(MozzesClient client) {

		// get the service class and execute method twice
		SessionScopedService service = client.getService(SessionScopedService.class);
		service.increment();
		service.increment();

		// value should be 2 because both service invocations are in the same session
		assertEquals(2, service.getCounter());
	}
}
