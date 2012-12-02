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

import org.mozzes.application.demo.mockups.services.basic.*;

import com.google.inject.*;

/**
 * This is the good implementation of the interface because it's injecting another service interface and not direct
 * implementation
 * 
 * @author vita
 */
public class ServiceThatInjectOtherServiceImpl implements ServiceThatInjectOtherService {

  /**
   * this is OK. This should be injected
   */
  @Inject
  BasicServiceImpl service1;

  /**
   * @see ServiceThatInjectOtherService#getIntegerFromInjectedService()
   */
  @Override
  public int getIntegerFromInjectedService() {
    return service1.getIntegerFromServer().intValue();
  }
}