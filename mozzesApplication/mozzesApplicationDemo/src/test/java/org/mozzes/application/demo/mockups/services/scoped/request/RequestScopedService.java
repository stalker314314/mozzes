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
package org.mozzes.application.demo.mockups.services.scoped.request;

import org.mozzes.application.module.scope.*;

/**
 * This is the specification of the service which implementation is using the {@link RequestScoped} This means that
 * instances of the service implementation class are unique in the single request so we can hold some data in the
 * attributes of the service class and they value will be preserver during the request.
 * 
 * @author vita
 */
public interface RequestScopedService {

  /**
   * this is the method that increments the counter in the service attribute
   */
  void increment();

  /**
   * @return value from the service attribute
   */
  int getCounter();

  /**
   * This method does 3 things:<br>
   * <br>
   * 1) Calls {@link RequestScopedService2#increment()} on injected service object.<br>
   * <br>
   * 2) Calls {@link RequestScopedService3#incrementInInjectedService()}(that also calls
   * {@link RequestScopedService2#increment()})<br>
   * <br>
   * 3) Returns value by calling {@link RequestScopedService2#getCounter()}.<br>
   * 
   * Because the {@link RequestScopedService2} is annotated with {@link RequestScoped} instances in the
   * {@link RequestScopedService} and {@link RequestScopedService3} should be the same so the incrementing should occur
   * on the same instance
   */
  int incrementInjectedAndReturnValue();
}
