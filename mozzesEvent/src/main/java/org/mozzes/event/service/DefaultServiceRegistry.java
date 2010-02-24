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
