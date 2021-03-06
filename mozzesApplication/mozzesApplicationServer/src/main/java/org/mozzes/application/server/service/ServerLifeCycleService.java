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
package org.mozzes.application.server.service;

import org.mozzes.application.module.ServerInitializationException;
import org.mozzes.application.module.ServerLifecycleListener;

/**
 * This is the service that's responsible for informing the provided server listener about server's starting and
 * stopping
 */
public interface ServerLifeCycleService {

  /**
   * This method is called on the server's startup
   * 
   * @param serverListener
   *          listener that will be informed about server startup
   * @throws ServerInitializationException
   *           when there's some problem with informing the listener@param serverListener list of listeners that will be
   *           informed about server startup
   */
  void startup(Class<? extends ServerLifecycleListener> serverListener) throws ServerInitializationException;

  /**
   * @param serverListener
   *          list of listeners that will be informed about server shutdown
   */
  void shutdown(Class<? extends ServerLifecycleListener> serverListener);
}
