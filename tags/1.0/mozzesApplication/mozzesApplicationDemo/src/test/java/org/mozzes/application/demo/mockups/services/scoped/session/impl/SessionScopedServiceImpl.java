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
package org.mozzes.application.demo.mockups.services.scoped.session.impl;

import org.mozzes.application.demo.mockups.services.scoped.session.*;
import org.mozzes.application.module.scope.*;


/**
 * This is the service implementation that is annotated with {@link SessionScoped} and because of that counter value is
 * "preserved" between the service methods calls in the same session.<br>
 * 
 * Actually the google guice is instantiating the one instance of this class for the same session so all the attributes
 * are "preserved"
 * 
 * @author vita
 * 
 * @see SessionScopedService
 */
@SessionScoped
public class SessionScopedServiceImpl implements SessionScopedService {

	private int counter = 0;

	/**
	 * @see SessionScopedService#increment()
	 */
	@Override
	public void increment() {
		counter++;
	}

	/**
	 * @see SessionScopedService#getCounter()
	 */
	@Override
	public int getCounter() {
		return counter;
	}
}
