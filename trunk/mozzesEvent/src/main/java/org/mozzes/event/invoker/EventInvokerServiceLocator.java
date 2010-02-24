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
