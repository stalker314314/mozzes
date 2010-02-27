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
package org.mozzes.application.demo.mockups.services.scoped.session;

import org.mozzes.application.module.scope.*;

/**
 * This is the specification of the service interface which implementation class is annotated with {@link SessionScoped}
 * annotation. This means that during the whole session server will use the same instance of the service implementation
 * to process the service method calls. So with that we can store some session specific data in the service attributes
 * and different session will not interfere and the value of service attributes will be preserved between the service
 * method calls.
 * 
 * @author vita
 */
public interface SessionScopedService {

	/**
	 * this method increments value of the integer attribute
	 */
	void increment();

	/**
	 * @return value of the integer attribute
	 */
	int getCounter();
}
