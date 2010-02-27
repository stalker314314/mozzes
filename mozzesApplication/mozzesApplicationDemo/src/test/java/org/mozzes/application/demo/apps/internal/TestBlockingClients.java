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
