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
package org.mozzes.application.demo.mockups.services.scoped.request.impl;

import org.mozzes.application.demo.mockups.services.scoped.request.*;
import org.mozzes.application.module.scope.*;


/**
 * This service is annotated with the {@link RequestScoped} and so in the request there's one single instance of this
 * class and all attributes of this class are preserved during the request.
 * 
 * @author vita
 * 
 * @see RequestScopedService
 */
@RequestScoped
public class RequestScopedService2Impl implements RequestScopedService2 {

	private int counter = 0;

	/**
	 * @see RequestScopedService#increment()
	 */
	@Override
	public void increment() {
		counter++;
	}

	/**
	 * @see RequestScopedService#getCounter()
	 */
	@Override
	public int getCounter() {
		return counter;
	}
}