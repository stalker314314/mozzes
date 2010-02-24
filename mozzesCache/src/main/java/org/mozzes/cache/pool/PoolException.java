package org.mozzes.cache.pool;

/**
 * Exception throws during {@link InstancePool} operations 
 */
public class PoolException extends Exception {

	private static final long serialVersionUID = 7001265251547583766L;

	public PoolException() {
		super();
	}

	public PoolException(String message, Throwable cause) {
		super(message, cause);
	}

	public PoolException(String message) {
		super(message);
	}

	public PoolException(Throwable cause) {
		super(cause);
	}

}
