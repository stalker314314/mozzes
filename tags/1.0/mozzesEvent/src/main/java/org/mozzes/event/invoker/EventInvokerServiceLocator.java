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
package org.mozzes.event.invoker;

import org.mozzes.event.Event;
import org.mozzes.event.service.ServiceException;
import org.mozzes.event.service.ServiceRegistry;

/**
 * Wrapper oko ServiceLocator-a koji pruza pristup EventInvokerService-ima
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see EventInvokerService
 * @see Event
 */
public class EventInvokerServiceLocator {

	/**
	 * Vraca EventInvokerService za odredjeni event interface
	 * 
	 * @param eventHandlerInterface event interface za koji se trazi EventInvokerService
	 * @return EventInvokerService koji omogucava okidanje dogadjaja
	 * @throws EventInvokerException ukoilko nije uspelo dobijanje EventInvokerService-a
	 */
	@SuppressWarnings("unchecked")
	public static <T> EventInvokerService<T> lookup(ServiceRegistry serviceRegistry, Class<T> eventHandlerInterface)
			throws EventInvokerException {
		if (eventHandlerInterface == null){
			throw new EventInvokerException("Invalid event interface: null");
		}

		if (!eventHandlerInterface.isInterface()){
			throw new EventInvokerException("Invalid event interface: class instead of interface ("
					+ eventHandlerInterface.getName() + ")");
		}

		try {
			return (EventInvokerService<T>) serviceRegistry.get(getServiceName(eventHandlerInterface.getName()));
		} catch (ServiceException ex) {
			throw new EventInvokerException("Event invoker service lookup failed", ex);
		}
	}

	private static String getServiceName(String eventInterfaceName) {
		return EventInvokerService.SERVICE_TYPE + eventInterfaceName;
	}

}
