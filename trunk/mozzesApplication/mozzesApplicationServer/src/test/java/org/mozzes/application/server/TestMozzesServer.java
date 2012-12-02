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
package org.mozzes.application.server;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.server.MozzesServer;
import org.mozzes.application.server.MozzesServerConfiguration;
import org.mozzes.application.server.mockups.ServerService1;
import org.mozzes.application.server.mockups.ServerService1Impl;

public class TestMozzesServer extends MozzesTestBase {

  private MozzesServer server;

  @Before
  public void beforeTest() {
    MozzesServerConfiguration serverConfig = new MozzesServerConfiguration();

    serverConfig.addService(ServerService1.class, ServerService1Impl.class);
    server = new MozzesServer(serverConfig);
    server.start();
  }

  @After
  public void afterTest() {
    server.stop();
  }

  @Test
  public void testServiceMethodCall() {
    assertNotNull(server);
    MozzesClient client = server.getLocalClient();
    assertNotNull(client);
    client.getService(ServerService1.class).service1Method1();
  }

}
