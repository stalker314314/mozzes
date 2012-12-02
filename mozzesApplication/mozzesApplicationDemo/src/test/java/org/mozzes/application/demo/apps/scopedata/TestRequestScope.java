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

      int requestCounter = client.getService(ServiceWithRequestData.class).incrementAndReturnInTheRequestContext();
      assertEquals(2, requestCounter);

      int requestCounter2 = client.getService(ServiceWithRequestData.class).incrementAndReturnInTheRequestContext();
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
