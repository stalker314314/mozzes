package org.mozzes.application.common.session;

import org.mozzes.application.common.exceptions.MozzesRuntimeException;

/**
 * The Class SessionException is thrown when there is some problem in the session support layer.
 */
public class SessionException extends MozzesRuntimeException {

	private static final long serialVersionUID = 3422680255426318014L;

	public SessionException() {
		super();
	}

	public SessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public SessionException(String message) {
		super(message);
	}

	public SessionException(Throwable cause) {
		super(cause);
	}
}
