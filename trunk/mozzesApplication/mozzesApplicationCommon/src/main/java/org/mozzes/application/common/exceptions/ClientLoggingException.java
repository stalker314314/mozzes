package org.mozzes.application.common.exceptions;


/**
 * This exception is thrown when user is trying to login when he's already logged in or when the client is trying to log
 * out but he didn't logged in
 * 
 * @author vita
 */
public class ClientLoggingException extends MozzesRuntimeException {

	private static final long serialVersionUID = 1L;

	public ClientLoggingException() {
		super();
	}

	public ClientLoggingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientLoggingException(String message) {
		super(message);
	}

	public ClientLoggingException(Throwable cause) {
		super(cause);
	}
}
