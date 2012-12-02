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

import org.mozzes.application.common.client.MozzesClientConfiguration;
import org.mozzes.application.common.session.SessionIdProvider;
import org.mozzes.application.plugin.request.RequestProcessor;
import org.mozzes.invocation.common.handler.InvocationHandler;

import com.google.inject.Inject;

/**
 * The Class MozzesLocalClientConfiguration is responsible for configuring the local mozzes client running on the mozzes
 * server.
 */
public class MozzesLocalClientConfiguration extends MozzesClientConfiguration {

  private final RequestProcessor requestProcessor;

  @Inject
  MozzesLocalClientConfiguration(RequestProcessor requestProcessor) {
    this.requestProcessor = requestProcessor;
  }

  /**
   * Invocation is handled by a localInvocationHanlder that simply passes the call to the requestProcessor.
   * 
   * @see MozzesClientConfiguration#getInvocationHandler(Class, SessionIdProvider)
   */
  @Override
  protected <I> InvocationHandler<I> getInvocationHandler(Class<I> ignore, SessionIdProvider sessionIDProvider) {
    return new LocalInvocationHandler<I>(requestProcessor, sessionIDProvider);
  }
}
