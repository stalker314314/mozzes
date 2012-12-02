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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectiveMethod<ObjectType, ReturnType> {
  private final Method method;

  public ReflectiveMethod(Method method) {
    if (method == null)
      throw new IllegalArgumentException("Method must not be null!");
    this.method = method;
  }

  @SuppressWarnings("unchecked")
  public ReturnType invoke(ObjectType object, Object... args) {
    Object result = null;
    try {
      result = method.invoke(object, args);
    } catch (IllegalArgumentException e) {
      throw e;
    } catch (IllegalAccessException e) {
      throw new ReflectionException(e);
    } catch (InvocationTargetException e) {
      throw new IllegalStateException(String.format("Exception occured while invoking reflective method \"%s\".\n"
          + "Exception: %s; Message: %s", method, e.getCause().getClass().getSimpleName(), e.getCause().getMessage()));
    }
    return (ReturnType) result;
  }

  public Method getMethod() {
    return method;
  }

  public boolean isAccessible() {
    return method.isAccessible();
  }

  public void setAccessible(boolean accessible) {
    method.setAccessible(accessible);
  }

  @SuppressWarnings("unchecked")
  public Class<ReturnType> getReturnType() {
    return (Class<ReturnType>) method.getReturnType();
  }

  public Class<?>[] getParameterTypes() {
    return method.getParameterTypes();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((method == null) ? 0 : method.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof ReflectiveMethod<?, ?>))
      return false;
    ReflectiveMethod<?, ?> other = (ReflectiveMethod<?, ?>) obj;
    if (method == null) {
      if (other.method != null)
        return false;
    } else if (!method.equals(other.method))
      return false;
    return true;
  }
}