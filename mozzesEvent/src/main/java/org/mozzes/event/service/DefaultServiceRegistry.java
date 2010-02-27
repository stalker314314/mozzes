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
package org.mozzes.event.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementacija ServiceRegistry-ja.
 * <p>
 * Cuva servise u mapi: nazivServisa -> serviceFactory
 * <p>
 * Implementacija je thread-safe
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see ServiceRegistry
 */
public class DefaultServiceRegistry implements ServiceRegistry {
	private static final Logger logger = LoggerFactory.getLogger(DefaultServiceRegistry.class);

	private HashMap<String, ServiceFactory> serviceRegistry;

	public DefaultServiceRegistry() {
		serviceRegistry = new HashMap<String, ServiceFactory>();
	}

	public void register(String serviceName, ServiceConfiguration serviceConfiguration) throws ServiceException {

		checkServiceName(serviceName);

		if (serviceConfiguration == null)
			throw new ServiceException("Invalid service configuration for service \"" + serviceName
					+ "\", null configuration");

		ServiceFactory serviceFactory = serviceConfiguration.getServiceFactory();
		if (serviceFactory == null)
			throw new ServiceException("Invalid service configuration for service \"" + serviceName
					+ "\", unable to create ServiceFactory");

		synchronized (serviceRegistry) {
			if (serviceRegistry.containsKey(serviceName))
				throw new ServiceException("Service \"" + serviceName + "\" already exists");

			serviceRegistry.put(serviceName, serviceFactory);
		}

		logger.debug("Service registered: " + serviceName);
	}

	public Object get(String serviceName) throws ServiceException {
		checkServiceName(serviceName);

		ServiceFactory serviceFactory = serviceRegistry.get(serviceName);
		if (serviceFactory == null)
			synchronized (serviceRegistry) {
				serviceFactory = serviceRegistry.get(serviceName);
			}

		if (serviceFactory == null)
			throw new ServiceException("Service \"" + serviceName + "\" does not exist");

		logger.debug("Service requested: " + serviceName);
		return serviceFactory.create();
	}

	public void unregister(String serviceName) throws ServiceException {
		checkServiceName(serviceName);

		ServiceFactory serviceFactory = null;
		synchronized (serviceRegistry) {
			serviceFactory = serviceRegistry.remove(serviceName);
		}

		if (serviceFactory == null)
			throw new ServiceException("Service \"" + serviceName + "\" does not exist");

		logger.debug("Service removed: " + serviceName);
	}

	/**
	 * Proverava ispravnost naziva servisa
	 * 
	 * @param serviceName naziv koji se proverava
	 * @throws ServiceException ukoliko naziv nije ispravan
	 */
	private void checkServiceName(String serviceName) throws ServiceException {
		if (serviceName == null)
			throw new ServiceException("Invalid service name: null");

		if ("".equals(serviceName))
			throw new ServiceException("Invalid service name: \"\"");
	}
}
