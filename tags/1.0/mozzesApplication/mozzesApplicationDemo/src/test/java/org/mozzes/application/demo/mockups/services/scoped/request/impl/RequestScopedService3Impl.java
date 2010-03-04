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

import com.google.inject.*;

/**
 * This service just delegates call to the {@link RequestScopedService2#increment()} service method.<br>
 * 
 * Because {@link RequestScopedService2Impl} is annotated with {@link RequestScoped} this should be the same instance as
 * in the {@link RequestScopedServiceImpl} service.
 * 
 * @author vita
 * 
 * @see RequestScopedService
 * @see RequestScopedServiceImpl
 * 
 * @see RequestScopedService2
 * @see RequestScopedService2Impl
 */
public class RequestScopedService3Impl implements RequestScopedService3 {

	@Inject
	private RequestScopedService2 injectedService;

	@Override
	public void incrementInInjectedService() {
		injectedService.increment();
	}
}