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
import org.mozzes.remoting.client.RemotingClientFactory;
import org.mozzes.remoting.common.RemotingAction;
import org.mozzes.remoting.common.RemotingConfiguration;
import org.mozzes.remoting.common.RemotingResponse;

/**
 * Implementation of the remoting client mock up factory. This factory creates mock up client that does not connect to
 * server and it can be specified in this constructor number of milliseconds to wait in execution
 * 
 * @author Kokan
 */
public class MockupRemotingClientFactory implements RemotingClientFactory {

  /** timeout to wait in execution */
  private final long timeoutInExecute;

  /**
   * Constructs mock up remoting client factory
   * 
   * @param timeoutInExecute
   *          Time in milliseconds to wait in mock up client execution
   */
  public MockupRemotingClientFactory(long timeoutInExecute) {
    this.timeoutInExecute = timeoutInExecute;
  }

  @Override
  public RemotingClient create(RemotingConfiguration remotingConfiguration) {
    return new RemotingClient() {
      private boolean isConnected = false;

      @Override
      public void connect() {
        isConnected = true;
      }

      @Override
      public void disconnect() {
        isConnected = false;
      }

      @Override
      public boolean isConnected() {
        return isConnected;
      }

      @Override
      public RemotingResponse execute(RemotingAction action) {
        try {
          Thread.sleep(timeoutInExecute);
        } catch (InterruptedException e) {
        }
        return new RemotingResponse();
      }
    };
  }
}
