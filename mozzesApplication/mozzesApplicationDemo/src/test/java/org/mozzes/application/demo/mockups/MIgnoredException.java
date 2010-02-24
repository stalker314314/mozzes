package org.mozzes.application.demo.mockups;

import org.mozzes.application.common.transaction.*;

/**
 * When this exception occurs in some service method execution it will NOT cause transaction rollback because it's
 * annotated with {@link TransactionIgnored}
 * 
 * @author vita
 */
@TransactionIgnored
public class MIgnoredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MIgnoredException() {
		super();
	}

	public MIgnoredException(String message, Throwable cause) {
		super(message, cause);
	}

	public MIgnoredException(String message) {
		super(message);
	}

	public MIgnoredException(Throwable cause) {
		super(cause);
	}
}