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
package org.mozzes.application.common.service;

import org.mozzes.application.common.exceptions.AuthorizationFailedException;
import org.mozzes.application.common.exceptions.ClientLoggingException;

/**
 * The Interface SessionService specifies functionalities related to the client session.<br>
 * If client executes some action on the server if the client wasn't logged in before the service call all session data
 * would be lost as if the client is logged out directly after service call.
 */
public interface SessionService {

  /**
   * Login service method is called when client wants to log into the mozzart application server.
   * 
   * @return sessionId that's been assigned to the user
   * 
   * @throws AuthorizationFailedException
   *           when logging is not successful.
   * 
   * @throws ClientLoggingException
   *           when client is already logged in.
   */
  String login(String username, String password) throws AuthorizationFailedException;

  /**
   * Logout method logs out the user from the server.
   * 
   * @throws ClientLoggingException
   *           when client is not logged in.
   */
  void logout();
}
