package org.mozzes.utils.reflection;

/**
 * This is a wrapper for any Java exception connected to reflection and is used by the {@link ReflectionUtils} so that
 * it is easier to catch the exceptions thrown by its methods
 * 
 * @author milos
 */
public class ReflectionException extends RuntimeException {
	private static final long serialVersionUID = 15L;

	public ReflectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReflectionException(Throwable cause) {
		super(cause);
	}

}
