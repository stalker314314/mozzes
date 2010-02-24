package org.mozzes.event.manager;

import org.mozzes.event.EventException;

/**
 * Exception happened during creation or during using with {@link EventManager}
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see EventException
 */
public class EventManagerException extends EventException {

	public static final long serialVersionUID = 182L;

	public EventManagerException(String message) {
		super(message);
	}

	public EventManagerException(Throwable cause) {
		super(cause);
	}

	public EventManagerException(String message, Throwable cause) {
		super(message, cause);
	}

}