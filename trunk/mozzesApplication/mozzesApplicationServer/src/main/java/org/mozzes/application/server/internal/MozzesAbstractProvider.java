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
package org.mozzes.application.server.internal;

import org.mozzes.application.server.request.RequestManager;

import com.google.inject.Provider;

/**
 * The Class MozzesAbstractProvider is superclass for all providers that are using {@link RequestManager} for
 * implementing the {@link Provider} interface.(SessionContext,RequestContext,TransactionContext,TransactionStack
 * providers)
 */
public abstract class MozzesAbstractProvider<T> implements Provider<T> {

  private RequestManager requestManager;

  protected MozzesAbstractProvider(RequestManager requestManager) {
    this.requestManager = requestManager;
  }

  protected RequestManager getRequestManager() {
    return requestManager;
  }
}
