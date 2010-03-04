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
package org.mozzes.application.demo.mockups.services.scopedata.impl;

import org.mozzes.application.demo.mockups.scopedata.*;
import org.mozzes.application.demo.mockups.services.scopedata.*;

import com.google.inject.*;

/**
 * Implements the {@link ServiceWithRequestData2} service.<br>
 * 
 * It's just incrementing the value of the counter in the transaction context
 * 
 * @author vita
 */
public class ServiceWithRequestData2Impl implements ServiceWithRequestData2 {

	@Inject
	MRequestData requestData;

	/**
	 * @see ServiceWithRequestData2#incrementRequestCounter()
	 */
	@Override
	public void incrementRequestCounter() {
		requestData.increment();
	}
}
