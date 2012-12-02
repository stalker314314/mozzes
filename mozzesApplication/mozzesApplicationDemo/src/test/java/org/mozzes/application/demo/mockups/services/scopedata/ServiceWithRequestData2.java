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
 * This is the example of the server service interface that's using data in the request context.<br>
 * 
 * It's simply incrementing the counter value that's stored in the {@link MTransactionData} class that's annotated with
 * {@link RequestScoped} so it's stored in the transaction context
 * 
 * @author vita
 */
public interface ServiceWithRequestData2 {

  /**
   * Increment the value of the counter stored in the transaction context
   */
  void incrementRequestCounter();
}
