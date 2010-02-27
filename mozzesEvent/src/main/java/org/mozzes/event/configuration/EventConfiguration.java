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

import java.util.ArrayList;
import java.util.List;

import org.mozzes.event.Event;
import org.mozzes.event.dispatcher.DefaultEventDispatcher;
import org.mozzes.event.dispatcher.EventDispatcher;


/**
 * Event configuration in system. Currently enables only one work mode - in same JVM (local events) and in same
 * transaction.
 * 
 * @author Perica Milosevic
 * @see Event
 */
public class EventConfiguration {

	private static final EventDispatcher DEFAULT_EVENT_DISPATCHER = new DefaultEventDispatcher();
//	private static final int DEFAULT_NUMBER_OF_DISPATCH_THREADS = 25;

	private List<Class<?>> events;
	private EventDispatcher eventDispatcher = DEFAULT_EVENT_DISPATCHER;
	private int numberOfDispatchThreads = 0;

	/**
	 * Creates new configuration for events.
	 */
	public EventConfiguration() {
		this.events = new ArrayList<Class<?>>();
	}

	/**
	 * Adds new local interface for events. Clients will be able to subscribe for this event interface and invoking
	 * them.
	 * 
	 * @param eventHandlerInterface Interface for adding
	 */
	public EventConfiguration addLocal(Class<?> eventHandlerInterface) throws EventConfigurationException {
		if (eventHandlerInterface == null)
			throw new EventConfigurationException("Unable to add null interface");

		this.events.add(eventHandlerInterface);
		return this;
	}

	/**
	 * Set event dispatcher that will be used for event processing
	 * 
	 * @param eventDispatcher EventDispather for event processing
	 * @throws EventConfigurationException If bad event dispatcher is given
	 */
	public EventConfiguration setEventDispatcher(EventDispatcher eventDispatcher) throws EventConfigurationException {
		if (eventDispatcher == null)
			throw new EventConfigurationException("Invalid event dispatcher - null");

		this.eventDispatcher = eventDispatcher;
		return this;
	}

	/**
	 * Sets thread number to process events in system
	 * 
	 * @param numberOfDispatchThreads Number of threads to process events
	 * @throws EventConfigurationException If less then 1 thread is given
	 */
	public EventConfiguration setNumberOfDispatchThreads(int numberOfDispatchThreads)
			throws EventConfigurationException {
		/* izbaciti ovo kada pocnemo da koristimo queue */
		if (numberOfDispatchThreads != 0) {
			throw new EventConfigurationException("This feature is not implemented currently");
		}
//		if (numberOfDispatchThreads < 1) {
//			throw new EventConfigurationException(
//					"Invalid number of dispatch threads - at least one dispatcher required");
//		}

		this.numberOfDispatchThreads = numberOfDispatchThreads;
		return this;
	}

	List<Class<?>> getEvents() {
		return events;
	}

	EventDispatcher getEventDispatcher() {
		return eventDispatcher;
	}

	int getNumberOfDispatchThreads() {
		return numberOfDispatchThreads;
	}

}