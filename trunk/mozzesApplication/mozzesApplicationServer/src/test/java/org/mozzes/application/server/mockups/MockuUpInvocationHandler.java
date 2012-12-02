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
package org.mozzes.application.server.mockups;

import org.mozzes.invocation.common.Invocation;
import org.mozzes.invocation.common.handler.InvocationHandler;

public class MockuUpInvocationHandler implements InvocationHandler<ServerService1> {

  private boolean fail = false;

  private boolean isIgnored = false;

  public MockuUpInvocationHandler(boolean fail) {
    this.fail = fail;
  }

  public MockuUpInvocationHandler(boolean fail, boolean isIgnored) {
    this.fail = fail;
    this.isIgnored = isIgnored;
  }

  public MockuUpInvocationHandler() {
    // empty
  }

  @Override
  public Object invoke(Invocation<? super ServerService1> invocation) throws Throwable {

    Object returnValue = invocation.invoke(null);

    if (fail) {
      if (isIgnored)
        throw new MockUpIgnoredException();

      throw new RuntimeException();
    }
    return returnValue;
  }

}