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
package org.mozzes.utils.reflection;

/**
 * This is a wrapper for any Java exception connected to reflection and is used by the {@link ReflectionUtils} so that
 * it is easier to catch the exceptions thrown by its methods
 * 
 * @author milos
 */
public class ReflectionException extends RuntimeException {
  private static final long serialVersionUID = 15L;

  public ReflectionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ReflectionException(Throwable cause) {
    super(cause);
  }

}
