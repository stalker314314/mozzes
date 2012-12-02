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
package org.mozzes.application.plugin.request;

import org.mozzes.invocation.common.Invocation;

/**
 * Server entry point.<br>
 * <br>
 * Plugins implemented as different client adapters can inject this interface and use it to submit a client request to
 * server for processing.
 */
public interface RequestProcessor {

  /**
   * Process the invocation in the current request.
   */
  public <I> Object process(String sessionId, Invocation<I> invocation) throws Throwable;
}