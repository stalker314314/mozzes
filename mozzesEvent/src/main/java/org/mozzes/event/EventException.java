package org.mozzes.event;

/**
 * Exception thrown for everything in relationship with events
 * 
 * @author Perica Milosevic
 */
public class EventException extends Exception {

	public static final long serialVersionUID = 182L;

	public EventException(String message) {
		super(message);
	}

	public EventException(Throwable cause) {
		super(cause);
	}

	public EventException(String message, Throwable cause) {
		super(message, cause);
	}

}