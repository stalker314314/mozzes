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
package org.mozzes.application.remoting.client;

import static org.mozzes.application.remoting.common.RemoteInvocationConstants.KEY_ACTION_NAME;
import static org.mozzes.application.remoting.common.RemoteInvocationConstants.KEY_METHOD_INVOCATION;
import static org.mozzes.application.remoting.common.RemoteInvocationConstants.KEY_RESULT;
import static org.mozzes.application.remoting.common.RemoteInvocationConstants.KEY_SESSION_ID;

import java.util.HashMap;

import org.mozzes.application.common.session.SessionIdProvider;
import org.mozzes.invocation.common.Invocation;
import org.mozzes.invocation.common.handler.InvocationHandler;
import org.mozzes.remoting.common.RemotingAction;
import org.mozzes.remoting.common.RemotingActionExecutorProvider;
import org.mozzes.remoting.common.RemotingException;
import org.mozzes.remoting.common.RemotingResponse;

/**
 * Client side for remote invocation
 * 
 * Accepts the service call and sends request to the server by using the MozzesRemoting mechanism.Response from the
 * MozzesServer is returned as a result of the execution.
 * 
 * @see InvocationHandler
 * 
 * @author Kokan
 * @author Perica
 */
class RemoteInvocationHandler<I> implements InvocationHandler<I> {

  /** The session id provider. */
  private final SessionIdProvider sessionIdProvider;

  /** The client provider. */
  private final RemotingActionExecutorProvider clientProvider;

  RemoteInvocationHandler(RemotingActionExecutorProvider clientProvider, SessionIdProvider sessionIdProvider) {
    this.sessionIdProvider = sessionIdProvider;
    this.clientProvider = clientProvider;
  }

  /**
   * @see InvocationHandler#invoke(Invocation)
   */
  public Object invoke(Invocation<? super I> methodInvocation) throws Throwable {
    try {
      // set action's params that will be sent to the server
      HashMap<Object, Object> params = new HashMap<Object, Object>();
      params.put(KEY_METHOD_INVOCATION, methodInvocation);
      params.put(KEY_SESSION_ID, sessionIdProvider.getSessionId());

      // executing the action and getting response
      RemotingResponse response = clientProvider.get().execute(new RemotingAction(KEY_ACTION_NAME, params));

      // get the result from the response
      return response.getParam(KEY_RESULT);

    } catch (RemotingException ex) {
      throw ex.getCause() != null ? ex.getCause() : ex;
    }
  }

}
