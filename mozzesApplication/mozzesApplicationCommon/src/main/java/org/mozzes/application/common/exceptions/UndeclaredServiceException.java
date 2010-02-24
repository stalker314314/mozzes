package org.mozzes.application.common.exceptions;

/**
 * Wrapper exception for runtime exceptions thrown on server side  
 */
public class UndeclaredServiceException extends MozzesRuntimeException {

	private static final long serialVersionUID = -6804346073910462141L;

	public UndeclaredServiceException(Throwable cause) {
		super(cause);
	}

}
