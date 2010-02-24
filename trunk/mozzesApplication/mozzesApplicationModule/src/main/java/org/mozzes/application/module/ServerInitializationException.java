package org.mozzes.application.module;

/**
 * Exception thrown during server startup. It this exception is thrown server startup will fail.
 */
public class ServerInitializationException extends Exception {

	private static final long serialVersionUID = 3998428839364442574L;

	public ServerInitializationException() {
		super();
	}

	public ServerInitializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerInitializationException(String message) {
		super(message);
	}

	public ServerInitializationException(Throwable cause) {
		super(cause);
	}
}
