package org.mozzes.application.demo.apps.simple;

import org.junit.Test;
import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.common.exceptions.AuthorizationFailedException;
import org.mozzes.application.demo.apps.TestBase;
import org.mozzes.application.demo.mockups.SimpleOjbect;
import org.mozzes.application.demo.mockups.services.basic.BasicService;


/**
 * This test case shows how client is calling server's service methods.
 * 
 * @author vita
 */
public class TestServiceCalls2 extends TestBase {

	/**
	 * Client is calling service method that simply is returning 123 value.
	 */
	@Test
	public void testServiceMethodCall2() {
		MozzesClient client = getClient();
		try {
			client.login(validUsername1, validPassword1);

			SimpleOjbect a = new SimpleOjbect();
			a.setSimpleAttribute("aaa");
			client.getService(BasicService.class).setA(a);

			a.setSimpleAttribute("bbbb");
			client.getService(BasicService.class).setA(a);
			client.logout();
		} catch (AuthorizationFailedException e) {
			logger.warn(e.getMessage(),e);
		}
	}

	/**
	 * Client is calling service method that simply is returning 123 value.
	 */
	@Test
	public void testServiceMethodCall22() {
		MozzesClient client = getClient();
		try {
			client.login(validUsername1, validPassword1);

			SimpleOjbect a = new SimpleOjbect();
			a.setSimpleAttribute("aaa");
			client.getService(BasicService.class).setA(a);

			SimpleOjbect a1 = new SimpleOjbect();
			a1.setSimpleAttribute("bbbb");
			client.getService(BasicService.class).setA(a1);
			client.logout();
		} catch (AuthorizationFailedException e) {
			logger.warn(e.getMessage(),e);
		}
	}

}
