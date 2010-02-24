package org.mozzes.application.server.transaction.impl;

import org.mozzes.application.server.internal.AbstractContext;

/**
 * The Class TransactionContext is associated with the currently running transaction
 */
public class TransactionContext extends AbstractContext {

	// contains transaction scoped objects
	
	@Override
	protected String getName() {
		return "TransactionContext";
	}
}
