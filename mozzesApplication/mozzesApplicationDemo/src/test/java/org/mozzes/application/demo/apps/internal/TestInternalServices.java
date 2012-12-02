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
import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.demo.apps.TestBase;
import org.mozzes.application.demo.mockups.services.internal.PublicServiceCallingInternal;
import org.mozzes.application.demo.mockups.services.internal.SimpleInternalService;
import org.mozzes.application.demo.mockups.services.internal.impl.PublicServiceCallingInternalImpl;
import org.mozzes.application.demo.mockups.services.internal.impl.SimpleInternalServiceImpl;

/**
 * This test demonstrates how to use internal services.
 * 
 * Internal service is the service that can be only used on the server side and clients can't call its methods.
 * 
 * There is {@link SimpleInternalService} service interface and {@link SimpleInternalServiceImpl} service implementation
 * and in the {@link TestBase#getInternalServices()	} {@link SimpleInternalService} is added to the list of internal
 * services
 * 
 * Also there is {@link PublicServiceCallingInternal} service interface and {@link PublicServiceCallingInternalImpl}
 * service implementation added as a regular service in the {@link TestBase#getServices()}.
 * 
 * Client should be able to call {@link PublicServiceCallingInternal} service and shouldn't be able to call
 * {@link SimpleInternalService}.
 * 
 * @author vita
 */
public class TestInternalServices extends TestBase {

  /**
   * In the {@link PublicServiceCallingInternalImpl} class we're injecting {@link SimpleInternalService} service and
   * calling it's method so with this we test injecting of internal services in the "regular" services.
   */
  @Test
  public void testInternalService() {
    try {
      MozzesClient client = getClient();
      client.login(validUsername1, validPassword1);
      int result = client.getService(PublicServiceCallingInternal.class).getFromInternal();
      assertEquals(SimpleInternalService.result, result);
      client.logout();

    } catch (Exception e) {
      fail();
    }
  }

  /**
   * Internal client is singleton on the server and it's state is not preserved so
   * {@link MozzesClient#login(String, String)} and {@link MozzesClient#logout()} methods shouldn't be called and will
   * throw exception if called.
   */
  @Test
  public void testInternalClientLoggingFailure() {
    try {
      MozzesClient client = getServer().getInternalClient();
      client = getServer().getInternalClient();
      client.login(validUsername1, validPassword1);
      fail();
      client.logout();
      fail();
    } catch (Exception ok) {
    }
  }

  /**
   * This demonstrates that internal client can call internal services directly.
   */
  @Test
  public void testInternalClientCallingInternalMethods() {
    try {
      MozzesClient client = getServer().getInternalClient();
      int result = client.getService(SimpleInternalService.class).getInteger();
      assertEquals(SimpleInternalService.result, result);
    } catch (Exception e) {
      fail();
    }
  }

  /**
   * This demonstrates that internal client can't call publicly available services and can call only internal services.
   */
  @Test
  public void testInternalClientCallingNonInternalMethodsFailing() {
    try {
      MozzesClient client = getServer().getInternalClient();
      client.getService(PublicServiceCallingInternal.class).getFromInternal();
      fail();
    } catch (Exception ok) {
    }
  }

  /**
   * Same as {@link TestInternalServices#testInternalService()} but without logging in.
   */
  @Test
  public void testInternalServiceWithoutLoggingIn() {
    MozzesClient client = getClient();
    int result = client.getService(PublicServiceCallingInternal.class).getFromInternal();
    assertEquals(SimpleInternalService.result, result);
  }

  /**
   * Here client tries to call {@link SimpleInternalService} ant it should fail because it's internal service.
   */
  @Test
  public void testInternalServiceFailing() {
    try {
      MozzesClient client = getClient();
      client.login(validUsername1, validPassword1);
      int result = client.getService(SimpleInternalService.class).getInteger();
      assertEquals(SimpleInternalService.result, result);
      client.logout();
      fail("shouldn't be able to call internal service");

    } catch (Exception ok) {
    }
  }
}
