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
