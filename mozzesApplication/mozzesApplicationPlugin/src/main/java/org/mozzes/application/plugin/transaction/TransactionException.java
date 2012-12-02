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
package org.mozzes.application.plugin.transaction;

import org.mozzes.application.common.exceptions.MozzesRuntimeException;

/**
 * The Class TransactionException is base class that is used in the transaction management. <br>
 * <br>
 * Currently used only when dealing with TransactionStack
 */
public class TransactionException extends MozzesRuntimeException {

  private static final long serialVersionUID = 8496852023029486159L;

  public TransactionException() {
    super();
  }

  public TransactionException(String message, Throwable cause) {
    super(message, cause);
  }

  public TransactionException(String message) {
    super(message);
  }

  public TransactionException(Throwable cause) {
    super(cause);
  }
}
