package org.mozzes.application.demo.apps.simple;

import static org.junit.Assert.*;

import org.junit.*;
import org.mozzes.application.demo.apps.*;
import org.mozzes.application.demo.mockups.services.basic.*;
import org.mozzes.application.demo.mockups.services.scopedata.*;


/**
 * In this test case we're testing that Server's services can call each other
 * 
 * {@link ServiceWithRequestData2} is calling {@link BasicService} in the implementation.
 * 
 * @author vita
 */
public class TestServiceInjecting extends TestBase {

	/**
	 * Test that value is returned from the injected service
	 */
	@Test
	public void testServiceMethodCall() {
		int result = getClient().getService(ServiceThatInjectOtherService.class).getIntegerFromInjectedService();
		assertEquals(BasicService.returnedValue.intValue(), result);
	}
}