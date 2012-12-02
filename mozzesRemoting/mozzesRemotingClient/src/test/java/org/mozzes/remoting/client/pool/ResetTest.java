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
package org.mozzes.remoting.client.pool;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozzes.remoting.client.RemotingClient;
import org.mozzes.remoting.client.core.DefaultRemotingClientFactory;
import org.mozzes.remoting.common.RemotingAction;
import org.mozzes.remoting.common.RemotingConfiguration;
import org.mozzes.remoting.common.RemotingException;

/**
 * Tests if remoting client issues reset() method and clearing object cache.
 * 
 * @author Kokan
 */
public class ResetTest extends TestCase {

  private MockupServer mockupServer;

  @Override
  @Before
  public void setUp() throws Exception {
    mockupServer = new MockupServer(2, 10000);
    mockupServer.start();
    Thread.sleep(1000);
  }

  @Override
  @After
  public void tearDown() throws Exception {
    mockupServer.join();
  }

  /**
   * Sends two same objects, but with their state modified between two successive calls. This will show us whether java
   * is sending only references through network, or is it always sending whole fat objects.
   */
  @Test
  public void testSendModifiedAction() throws RemotingException {
    RemotingConfiguration rc = new RemotingConfiguration("localhost", 10000);
    RemotingClient client = new DefaultRemotingClientFactory().create(rc);
    try {
      client.connect();
      Map<String, Integer> params = new HashMap<String, Integer>();
      /* sending first action */
      params.put("one", Integer.valueOf(1));
      RemotingAction action = new RemotingAction("actionName", params);
      client.execute(action);
      assertNull(mockupServer.getLastError());
      assertTrue(mockupServer.getLastSentObject() instanceof RemotingAction);
      Map<? extends Object, ? extends Object> receivedParams1 = ((RemotingAction) mockupServer.getLastSentObject())
          .getParams();
      assertEquals(1, receivedParams1.size());
      assertEquals(Integer.valueOf(1), receivedParams1.get("one"));
      /* sending same action for the second time */
      params.put("two", Integer.valueOf(2));
      client.execute(action);
      assertNull(mockupServer.getLastError());
      assertTrue(mockupServer.getLastSentObject() instanceof RemotingAction);
      Map<? extends Object, ? extends Object> receivedParams2 = ((RemotingAction) mockupServer.getLastSentObject())
          .getParams();
      assertEquals(2, receivedParams2.size());
      assertEquals(Integer.valueOf(1), receivedParams2.get("one"));
      assertEquals(Integer.valueOf(2), receivedParams2.get("two"));
    } finally {
      if (client != null) {
        client.disconnect();
      }
    }
  }
}
