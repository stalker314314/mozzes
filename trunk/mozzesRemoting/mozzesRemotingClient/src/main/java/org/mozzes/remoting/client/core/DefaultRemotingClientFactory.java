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
package org.mozzes.remoting.client.core;

import org.mozzes.remoting.client.RemotingClient;
import org.mozzes.remoting.client.RemotingClientFactory;
import org.mozzes.remoting.common.RemotingConfiguration;

/**
 * Simple remote factory that always returns standard remote client implementation. Users using this class must always
 * explicitly connect before action execution and disconnect (in finally block) after action execution
 * 
 * @author Kokan
 */
public class DefaultRemotingClientFactory implements RemotingClientFactory {

  public DefaultRemotingClientFactory() {
  }

  @Override
  public RemotingClient create(RemotingConfiguration remotingConfiguration) {
    return new RemotingClientImpl(remotingConfiguration);
  }
}
