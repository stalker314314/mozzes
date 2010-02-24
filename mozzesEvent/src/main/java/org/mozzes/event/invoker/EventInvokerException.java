package org.mozzes.event.invoker;

import org.mozzes.event.EventException;

/**
 * Exception happened during creation or during using {@link EventInvoker}
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see EventException
 */
public class EventInvokerException extends EventException {

	public static final long serialVersionUID = 182L;

	public EventInvokerException(String message) {
		super(message);
	}

	public EventInvokerException(String message, Throwable cause) {
		super(message, cause);
	}

}
