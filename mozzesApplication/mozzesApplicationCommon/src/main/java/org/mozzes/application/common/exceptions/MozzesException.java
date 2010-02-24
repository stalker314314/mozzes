package org.mozzes.application.common.exceptions;

/**
 * The Class MozzesException is base exception in the framework which other exceptions extends.
 */
public class MozzesException extends Exception {

	private static final long serialVersionUID = 5197627322348048790L;

	public MozzesException() {
		super();
	}

	public MozzesException(String message, Throwable cause) {
		super(message, cause);
	}

	public MozzesException(String message) {
		super(message);
	}

	public MozzesException(Throwable cause) {
		super(cause);
	}
}
