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
