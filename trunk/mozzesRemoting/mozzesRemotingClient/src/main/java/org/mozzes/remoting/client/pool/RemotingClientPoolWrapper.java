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
package org.mozzes.remoting.client.pool;

import org.mozzes.remoting.client.RemotingClient;
import org.mozzes.remoting.client.RemotingExecutorProviderFactory;
import org.mozzes.remoting.common.RemotingAction;
import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.common.RemotingException;
import org.mozzes.remoting.common.RemotingResponse;

/**
 * Action executor useful in conjunction with remoting clients pool. Wraps
 * {@link RemotingClientPoolWrapper#execute(RemotingAction)} action by picking client from pool, calling execution and
 * returning client back to pool. Clients shouldn't use this class, for further direction look at
 * {@link RemotingExecutorProviderFactory}
 * 
 * @author Kokan
 */
class RemotingClientPoolWrapper implements RemotingActionExecutor {

  /** Pool to use for getting remoting clients */
  private final RemotingClientPool pool;

  /**
   * Constructor with specified pool
   */
  RemotingClientPoolWrapper(RemotingClientPool pool) {
    this.pool = pool;
  }

  @Override
  public RemotingResponse execute(RemotingAction action) throws RemotingException {
    RemotingClient remotingClient = null;
    try {
      remotingClient = pool.getClient();
      return remotingClient.execute(action);
    } finally {
      if (remotingClient != null) {
        pool.closeClient(remotingClient);
      }
    }
  }
}
