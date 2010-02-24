package org.mozzes.application.common.session;

/**
 * Exception thrown when session expired, e.g. there was timeout
 * 
 * @author Kokan
 */
public final class SessionExpiredException extends SessionException {

	private static final long serialVersionUID = -3225154612547949816L;

	public SessionExpiredException() {
		super();
	}

	public SessionExpiredException(String message) {
		super(message);
	}

	public SessionExpiredException(Throwable cause) {
		super(cause);
	}

	public SessionExpiredException(String message, Throwable cause) {
		super(message, cause);
	}
}
