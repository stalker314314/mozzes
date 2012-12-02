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
package org.mozzes.remoting.client.simple;

import org.mozzes.remoting.client.RemotingClient;
import org.mozzes.remoting.client.RemotingClientFactory;
import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.common.RemotingActionExecutorProvider;
import org.mozzes.remoting.common.RemotingConfiguration;
import org.mozzes.remoting.common.RemotingException;

/**
 * Simple client provider that holds only one client and connects it at the startup. Ideal for uses where only one
 * client is needed and we don't want a pool.
 * 
 * @author Kokan
 */
public class SimpleClientProvider implements RemotingActionExecutorProvider {

  private final RemotingClient client;

  /**
   * Default constructor
   * 
   * @param remotingConfiguration
   *          Configuration for which client provider is created
   * @param clientFactory
   *          Factory for creating clients
   * @throws RemotingException
   *           If remoting server is not available or any network problem while trying to establish connection
   */
  public SimpleClientProvider(RemotingConfiguration remotingConfiguration, RemotingClientFactory clientFactory)
      throws RemotingException {
    this.client = clientFactory.create(remotingConfiguration);
    this.client.connect();
  }

  @Override
  public RemotingActionExecutor get() {
    return client;
  }

}