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
package org.mozzes.event.configuration;

import java.lang.reflect.InvocationHandler;
import java.util.Iterator;
import java.util.List;

import org.mozzes.event.dispatcher.EventDispatchManager;
import org.mozzes.event.invoker.EventInvoker;
import org.mozzes.event.invoker.EventInvokerException;
import org.mozzes.event.invoker.EventInvokerService;
import org.mozzes.event.manager.EventManager;
import org.mozzes.event.manager.EventManagerException;
import org.mozzes.event.manager.EventManagerService;
import org.mozzes.event.service.DefaultServiceConfiguration;
import org.mozzes.event.service.ServiceConfiguration;
import org.mozzes.event.service.ServiceException;
import org.mozzes.event.service.ServiceFactory;
import org.mozzes.event.service.ServiceRegistry;
import org.mozzes.event.service.SingletonServiceFactory;


/**
 * This class does registration of {@link EventManager} and {@link EventInvoker} on a given configuration
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see EventConfiguration
 */
public class EventServiceConfigurator {

	/** unique event dispatch manager instance */
	private static EventDispatchManager eventDispatchManager = null;

	/**
	 * Registers {@link EventManager} and {@link EventInvoker} services based on event configuration
	 * 
	 * @param serviceRegistry Register to keep all event services
	 * @param configuration Configuration on which to do registration
	 * @throws EventConfigurationException If configuration is invalid
	 */
	protected static void configure(ServiceRegistry serviceRegistry, EventConfiguration configuration)
			throws EventConfigurationException {

		if (configuration == null)
			throw new EventConfigurationException("Invalid event configuration - null");

		EventDispatchManager eventDispatchManager = createEventDispatchManager(configuration);
		configure(serviceRegistry, configuration.getEvents(), eventDispatchManager);
	}

	/**
	 * Creates unique instance of {@link EventDispatchManager} if it doesn't exists
	 * 
	 * @param configuration Configuration on which to do creation
	 * @return {@link EventDispatchManager} unique instance
	 */
	private static synchronized EventDispatchManager createEventDispatchManager(EventConfiguration configuration) {
		if (eventDispatchManager == null) {
			eventDispatchManager = new EventDispatchManager(configuration.getEventDispatcher(), configuration
					.getNumberOfDispatchThreads());
		}
		return eventDispatchManager;
	}

	/**
	 * Configures local {@link EventManager}s and {@link EventInvoker}s
	 * 
	 * @param serviceRegistry Register in which to add all local events
	 * @param events List of locally configured events
	 * @throws EventConfigurationException If configuration is invalid
	 */
	private static void configure(ServiceRegistry serviceRegistry, List<Class<?>> events,
			EventDispatchManager eventDispatchManager) throws EventConfigurationException {
		if (events == null)
			return;

		for (Iterator<Class<?>> eventsIterator = events.iterator(); eventsIterator.hasNext();) {
			Class<?> eventHandlerInterface = eventsIterator.next();

			InvocationHandler invocationHandler = registerManager(serviceRegistry, eventHandlerInterface,
					eventDispatchManager);
			registerEventInvoker(serviceRegistry, eventHandlerInterface, invocationHandler);
		}
	}

	/**
	 * Registers local {@link EventManagerService} for a given event interface
	 * 
	 * @param serviceRegistry Register in which to add interface
	 * @param eventHandlerInterface Event interface for which local {@link EventManagerService} to be register
	 * @return invocationHandler Proxy handler needed for local {@link EventInvokerService}
	 * @throws EventConfigurationException If configuration is invalid
	 */
	private static InvocationHandler registerManager(ServiceRegistry serviceRegistry, Class<?> eventHandlerInterface,
			EventDispatchManager eventDispatchManager) throws EventConfigurationException {
		EventManager eventManager;
		try {
			eventManager = new EventManager(eventDispatchManager, eventHandlerInterface);
		} catch (EventManagerException e) {
			throw new EventConfigurationException("Unable to configure event manager service", e);
		}

		registerService(serviceRegistry, EventManagerService.SERVICE_TYPE, eventHandlerInterface, eventManager);
		return eventManager;
	}

	/**
	 * Registers {@link EventInvokerService} for a given event interface
	 * 
	 * @param serviceRegistry Register in which to add interface
	 * @param eventHandlerInterface Event interface for which local {@link EventInvokerService} to be register
	 * @param invocationHandler Proxy to process events and give them to subscribed handlers
	 * @throws EventConfigurationException If configuration is invalid
	 */
	private static <T> void registerEventInvoker(ServiceRegistry serviceRegistry, Class<T> eventHandlerInterface,
			InvocationHandler invocationHandler) throws EventConfigurationException {
		EventInvoker<T> eventInvoker;
		try {
			eventInvoker = new EventInvoker<T>(eventHandlerInterface, invocationHandler);
		} catch (EventInvokerException e) {
			throw new EventConfigurationException("Unable to configure event invoker service", e);
		}

		registerService(serviceRegistry, EventInvokerService.SERVICE_TYPE, eventHandlerInterface, eventInvoker);
	}

	/**
	 * Registration of service for working with events
	 * 
	 * @param serviceRegistry Register in which to add service
	 * @param serviceType Service type to register (manager or invoker)
	 * @param eventInterface Event interface type for a given service
	 * @param serviceImplementation Service implementation
	 * @throws EventConfigurationException If configuration is invalid
	 */
	private static void registerService(ServiceRegistry serviceRegistry, String serviceType, Class<?> eventInterface,
			Object serviceImplementation) throws EventConfigurationException {
		try {
			ServiceFactory svcFactory = new SingletonServiceFactory(serviceImplementation);
			ServiceConfiguration svcConfig = new DefaultServiceConfiguration(svcFactory);
			serviceRegistry.register(serviceType + eventInterface.getName(), svcConfig);
		} catch (ServiceException e) {
			throw new EventConfigurationException("Event service registration failed", e);
		}
	}
}