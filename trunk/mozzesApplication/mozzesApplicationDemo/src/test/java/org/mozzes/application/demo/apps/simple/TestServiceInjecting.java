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