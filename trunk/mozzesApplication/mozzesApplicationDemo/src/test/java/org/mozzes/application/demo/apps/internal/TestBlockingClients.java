package org.mozzes.application.demo.apps.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mozzes.application.demo.apps.TestBase;
import org.mozzes.application.demo.mockups.services.internal.PublicServiceCallingInternal;
import org.mozzes.application.demo.mockups.services.internal.SimpleInternalService;


public class TestBlockingClients extends TestBase {

	@Test
	public void testNormalClientWorkingAfterStartup() {
		try {
			int result = server.getLocalClient().getService(PublicServiceCallingInternal.class).getFromInternal();
			assertEquals(SimpleInternalService.result, result);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testInternalClientWorkingDuringStartup() {
		int result = server.getInternalClient().getService(SimpleInternalService.class).getInteger();
		assertEquals(SimpleInternalService.result, result);
	}

//	@Test
//	public void testNormalClientFailingDuringStartup() {
//		try {
//			int result = server.getLocalClient().getService(PublicServiceCallingInternal.class).getFromInternal();
//			assertEquals(SimpleInternalService.result, result);
//			fail("server is not started.it should take about 500ms");
//		} catch (Exception ok) {
//		}
//	}
}
