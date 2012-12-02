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
package org.mozzes.application.demo.mockups.services.basic.impl;

import java.io.*;

import org.mozzes.application.demo.mockups.*;
import org.mozzes.application.demo.mockups.services.basic.*;

public class ServiceThatThrowsExceptionsImpl implements ServiceThatThrowsExceptions {

  /**
   * @see ServiceThatThrowsExceptions#serviceWhichThrowsException()
   */
  @Override
  public String serviceWhichThrowsException() throws IOException {
    throw new IOException("example exception");
  }

  /**
   * @see ServiceThatThrowsExceptions#serviceWhichThrowsRuntimeException()
   */
  @Override
  public String serviceWhichThrowsRuntimeException() {
    throw new NumberFormatException("example exception");
  }

  /**
   * @see ServiceThatThrowsExceptions#serviceWhichThrowsIgnoredException()
   */
  @Override
  public void serviceWhichThrowsIgnoredException() {
    throw new MIgnoredException();
  }
}