package org.mozzes.application.common.exceptions;

/**
 * The Class MozzesInternalRuntimeException is base runtime exception in the framework which other runtime exceptions
 * extend
 */
public class MozzesRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -1360340051440896112L;

	public MozzesRuntimeException() {
		super();
	}

	public MozzesRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public MozzesRuntimeException(String message) {
		super(message);
	}

	public MozzesRuntimeException(Throwable cause) {
		super(cause);
	}
}
