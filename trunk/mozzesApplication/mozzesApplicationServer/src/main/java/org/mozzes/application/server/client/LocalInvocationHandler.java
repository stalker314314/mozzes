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
package org.mozzes.application.server.client;

import org.mozzes.application.common.session.SessionIdProvider;
import org.mozzes.application.plugin.request.RequestProcessor;
import org.mozzes.invocation.common.Invocation;
import org.mozzes.invocation.common.handler.InvocationHandler;

/**
 * The Class LocalInvocationHandler is responsible for invoking server services from the local Mozzes client that's
 * running on the server. It's simply delegating the {@link Invocation} to the {@link RequestProcessor}.
 */
class LocalInvocationHandler<I> implements InvocationHandler<I> {

  private final RequestProcessor requestProcessor;

  private final SessionIdProvider sessionIdProvider;

  LocalInvocationHandler(RequestProcessor requestProcessor, SessionIdProvider sessionIdProvider) {
    this.requestProcessor = requestProcessor;
    this.sessionIdProvider = sessionIdProvider;
  }

  /**
   * Simply delegates to the requestProcessor
   * 
   * @see InvocationHandler#invoke(Invocation)
   */
  @Override
  public synchronized Object invoke(Invocation<? super I> invocation) throws Throwable {
    return requestProcessor.process(sessionIdProvider.getSessionId(), invocation);
  }
}
