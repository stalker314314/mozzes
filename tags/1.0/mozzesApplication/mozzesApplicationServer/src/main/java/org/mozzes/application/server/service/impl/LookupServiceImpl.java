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
package org.mozzes.application.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.mozzes.application.common.service.LookupService;
import org.mozzes.application.server.service.InternalLookupService;


/**
 * The Class LookupServiceImpl is the implementation of the built-in {@link InternalLookupService}
 */
public class LookupServiceImpl implements LookupService, InternalLookupService {

	/** The services that exists on the server. */
	private final List<String> services = new ArrayList<String>();

	/** The services that exists on the server. */
	private final List<Class<?>> internalServices = new ArrayList<Class<?>>();

	/**
	 * Adds the services to the list of available services.
	 */
	public void addService(String service) {
		services.add(service);
	}

	/**
	 * Adds the services to the list of available internal services that are visible only to the internal.
	 */
	public void addInternalService(Class<?> serviceClasses) {
		internalServices.add(serviceClasses);
	}

	/**
	 * @see LookupService#getServices()
	 */
	@Override
	public List<String> getServices() {
		return services;
	}

	/**
	 * @see InternalLookupService#getInternalServices()
	 */
	@Override
	public List<Class<?>> getInternalServices() {
		return internalServices;
	}
}
