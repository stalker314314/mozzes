package org.mozzes.application.plugin.transaction;

/**
 * Simple {@link TransactionManager} holder.
 */
public class SimpleTransactionPlugion extends TransactionPlugin {

	private final Class<? extends TransactionManager> transactionManager;
	
	public SimpleTransactionPlugion(Class<? extends TransactionManager> transactionManager) {
		super();
		this.transactionManager = transactionManager;
	}

	@Override
	public Class<? extends TransactionManager> getTransactionManager() {
		return transactionManager;
	}
}
