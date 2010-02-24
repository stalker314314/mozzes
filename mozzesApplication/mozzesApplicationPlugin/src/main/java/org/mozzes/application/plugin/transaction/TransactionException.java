package org.mozzes.application.plugin.transaction;

import org.mozzes.application.common.exceptions.MozzesRuntimeException;

/**
 * The Class TransactionException is base class that is used in the transaction management. <br>
 * <br>
 * Currently used only when dealing with TransactionStack
 */
public class TransactionException extends MozzesRuntimeException {

	private static final long serialVersionUID = 8496852023029486159L;

	public TransactionException() {
		super();
	}

	public TransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransactionException(String message) {
		super(message);
	}

	public TransactionException(Throwable cause) {
		super(cause);
	}
}
