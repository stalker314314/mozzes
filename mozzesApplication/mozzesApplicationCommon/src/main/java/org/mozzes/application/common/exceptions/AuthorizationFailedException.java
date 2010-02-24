package org.mozzes.application.common.exceptions;

/**
 * The Class AuthorizationFailedException. This exception occurs when user tries to login unsuccessfully.
 */
public class AuthorizationFailedException extends MozzesException {

	private static final long serialVersionUID = 5234925841194197427L;

	public AuthorizationFailedException() {
		super();
	}

	public AuthorizationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthorizationFailedException(String message) {
		super(message);
	}

	public AuthorizationFailedException(Throwable cause) {
		super(cause);
	}
}
