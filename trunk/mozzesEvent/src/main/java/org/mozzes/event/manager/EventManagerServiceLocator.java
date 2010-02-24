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