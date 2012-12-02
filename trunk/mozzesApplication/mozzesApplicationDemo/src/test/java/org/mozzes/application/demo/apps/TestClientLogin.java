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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.common.exceptions.AuthorizationFailedException;
import org.mozzes.application.common.exceptions.ClientLoggingException;
import org.mozzes.application.demo.mockups.MAuthorizationManager;
import org.mozzes.application.remoting.client.RemoteClientConfiguration;
import org.mozzes.remoting.common.RemotingException;

/**
 * Test for client logging in and out.
 * 
 * Client can login only once but two(or more) different client can login with same credentials if authorization manager
 * allows. In this test we're using the {@link MAuthorizationManager} that is allowing that.
 * 
 * @author vita
 */
public class TestClientLogin extends TestBase {

  /**
   * Here we test whether the client can log in with valid username and password.<br>
   * Also after logging in the user should logout successfully.<br>
   * 
   * With the isUserLogged variable checking that the call to the AuthorizationManger is made.
   */
  @Test
  public void testClientLoginSuccess() {
    MozzesClient client = null;
    try {
      client = getClient();
      client.login(validUsername1, validPassword1);
      assertTrue(MAuthorizationManager.isUserLogged);
      client.logout();
    } catch (AuthorizationFailedException e) {
      fail("user should login successfully");
    }
  }

  /**
   * Client shoudn't login with wrong credentials
   */
  @Test
  public void testClientLoginFailure() {
    try {
      getClient().login(wrongUsername, wrongPassword);
      fail("user shouldn't login successfully");
    } catch (AuthorizationFailedException ok) {
    }
    assertFalse(MAuthorizationManager.isUserLogged);
  }

  /**
   * With this test is checked that after one login client can't login again with same credentials,with different(but
   * good) credentials or even with wrong credentials.
   */
  @Test
  public void testClientLoginMultipleFailing() {
    MozzesClient client = null;
    try {
      client = getClient();
      client.login(validUsername1, validPassword1);
      assertTrue(MAuthorizationManager.isUserLogged);

      try {
        client.login(validUsername1, validPassword1);
        fail("user shouldn't login successfully with the same credentials");
      } catch (ClientLoggingException ok) {
      }

      try {
        client.login(validUsername2, validPassword2);
        fail("user shouldn't login successfully second time with different credentials");
      } catch (ClientLoggingException ok) {
      }
      try {
        client.login(wrongUsername, wrongPassword);
        fail("user shouldn't login successfully second time with wrong credentials");
      } catch (ClientLoggingException ok) {
      }

    } catch (AuthorizationFailedException ok) {
      fail("shouln't be thrown");
    }

    assert client != null;
    assertTrue(MAuthorizationManager.isUserLogged);

    try {
      client.logout();
    } catch (ClientLoggingException ok) {
    }
  }

  /**
   * This test shows that different client should be able to login/logout with the same credentials
   */
  @Test
  public void testClientLoginMultiple2() {
    MozzesClient client = null;

    /* first client logging in with validUsername1/validPassword1 combination */
    try {
      client = getClient();
      client.login(validUsername1, validPassword1);
      assertTrue(MAuthorizationManager.isUserLogged);

    } catch (AuthorizationFailedException ok) {
      fail("should log in successfully");
    }

    /* second client logging in with the same credentials */
    try {
      MozzesClient client2 = new MozzesClient(new RemoteClientConfiguration("localhost", 7890, false));
      client2.login(validUsername1, validPassword1);
      client2.logout();
    } catch (AuthorizationFailedException e) {
      fail("should log in");
    } catch (RemotingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    assert client != null;

    /* first client should be able to successfully logout */
    try {
      client.logout();
    } catch (ClientLoggingException e) {
      fail("should log out successfully");
    }
  }

  /**
   * Client shouldn't be able to logout without prior logging in.
   */
  @Test
  public void testClientLogoutWithoutLogging() {
    try {
      getClient().logout();
      fail("shouldn't logged out because we're not logged in");
    } catch (ClientLoggingException ok) {
    }
    /* user shouldn't be logged in */
    assertFalse(MAuthorizationManager.isUserLogged);
  }
}
