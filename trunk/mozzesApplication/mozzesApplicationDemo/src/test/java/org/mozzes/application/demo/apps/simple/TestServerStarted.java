package org.mozzes.application.demo.apps.simple;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mozzes.application.demo.apps.TestBase;


/**
 * Simple test that checks if the server instance is not null (checks actually if the beforeTest() method throw some
 * exception so server attribute is still null or it's started)
 * 
 * @author vita
 */
public class TestServerStarted extends TestBase {

	@Test
	public void testServerStarted() {
		assertNotNull(getServer());
	}
}
