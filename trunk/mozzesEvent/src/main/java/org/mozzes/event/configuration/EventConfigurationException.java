package org.mozzes.event.configuration;

import org.mozzes.event.EventException;

/**
 * Any exception thrown during event configuration
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see EventException
 */
public class EventConfigurationException extends EventException {
	public static final long serialVersionUID = 182L;

	EventConfigurationException(String message) {
		super(message);
	}

	EventConfigurationException(Throwable cause) {
		super(cause);
	}

	EventConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}