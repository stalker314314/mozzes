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
package org.mozzes.event.manager;

import org.mozzes.event.service.ServiceException;
import org.mozzes.event.service.ServiceRegistry;

/**
 * Wrapper oko ServiceLocator-a koji pruza pristup EventManagerService-ima
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see EventManagerService
 */
public class EventManagerServiceLocator {

	/**
	 * Vraca EventManagerService za odredjeni event interface
	 * 
	 * @param eventHandlerInterface event interface za koji se trazi EventManagerService
	 * @return EventManagerService koji omogucava pretplatu na dogadjaje
	 * @throws EventManagerException ukoilko nije uspelo dobijanje EventManagerService-a
	 */
	public static EventManagerService lookup(ServiceRegistry serviceRegistry, Class<?> eventHandlerInterface)
			throws EventManagerException {
		if (eventHandlerInterface == null){
			throw new EventManagerException("Invalid event interface: null");
		}

		if (!eventHandlerInterface.isInterface()){
			throw new EventManagerException("Invalid event interface: class instead of interface ("
					+ eventHandlerInterface.getName() + ")");
		}

		try {
			return (EventManagerService) serviceRegistry.get(getServiceName(eventHandlerInterface.getName()));
		} catch (ServiceException ex) {
			throw new EventManagerException("Event manager service lookup failed", ex);
		}
	}

	private static String getServiceName(String eventInterfaceName) {
		return EventManagerService.SERVICE_TYPE + eventInterfaceName;
	}

}