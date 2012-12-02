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
      logger.warn(e.getMessage(), e);
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
      logger.warn(e.getMessage(), e);
    }
  }

}
