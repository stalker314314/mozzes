package org.mozzes.application.plugin.transaction;

/**
 * TransactionManager is responsible for managing the transaction lifecycle.
 */
public interface TransactionManager {

	/**
	 * This method is called when new transaction is starting.
	 * @param nested is this nested transaction?
	 */
	public void begin(boolean nested);

	/**
	 * This method is called to commit the current transaction
	 */
	public void commit();

	/**
	 * This method is called to rollback the current transaction
	 */
	public void rollback();
	
	/**
	 * This method is called after transaction is finished.
	 * @param successful true - if transaction is successfully committed
	 */
	public void finalizeTransaction(boolean successful);
}
