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
package org.mozzes.application.demo.apps;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.common.exceptions.AuthorizationFailedException;
import org.mozzes.application.common.service.LookupService;

/**
 * In this test case we're testing is the {@link LookupService} working properly. It should return 1 service that exists
 * in every server.
 * 
 * @author vita
 */
public class TestLookupService extends TestBase {

  @Test
  public void testLookupService() {
    try {
      MozzesClient client = getClient();
      client.login(validUsername1, validPassword1);
      List<String> serviceList = client.getService(LookupService.class).getServices();
      assertEquals(1, serviceList.size());
      client.logout();
    } catch (AuthorizationFailedException e) {
      logger.warn(e.getMessage(), e);
    }
  }

  @Test
  public void testLookupServiceWithoutLoggingIn() {
    List<String> serviceList = getClient().getService(LookupService.class).getServices();
    assertEquals(1, serviceList.size());
  }

  /**
   * Here we remove all services from the server configuration because we want to test weather the {@link LookupService}
   * is returning default services.
   * 
   * @see TestBase#getServices()
   */
  @Override
  protected HashMap<Class<?>, Class<?>> getServices() {
    return new HashMap<Class<?>, Class<?>>();
  }

  /**
   * Here we remove all internal services from the server configuration because we want to test weather the
   * {@link LookupService} is returning default services.
   * 
   * @see TestBase#getServices()
   */
  @Override
  protected HashMap<Class<?>, Class<?>> getInternalServices() {
    return new HashMap<Class<?>, Class<?>>();
  }
}
