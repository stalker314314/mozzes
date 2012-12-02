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
package org.mozzes.application.demo.mockups.services.scopedata;

import org.mozzes.application.demo.mockups.scopedata.*;
import org.mozzes.application.module.scope.*;

/**
 * This is the service that's working with the data stored in the request context. It's using {@link MRequestData} class
 * that's annotated with {@link RequestScoped} annotation.<br>
 * 
 * It has only one method that's incrementing the counter value of the {@link MRequestData} instance and then calling
 * the {@link ServiceWithRequestData2#incrementRequestCounter()} method that's also incrementing the
 * {@link MRequestData} instance counter value. Because this two invocations are in the same request they should
 * increment the same counter so the returned value should be 2
 * 
 * @author vita
 */
public interface ServiceWithRequestData {

  /**
   * Increment data in the request context and then call other service that's doing the same and return value.Should
   * return 2
   */
  int incrementAndReturnInTheRequestContext();
}
