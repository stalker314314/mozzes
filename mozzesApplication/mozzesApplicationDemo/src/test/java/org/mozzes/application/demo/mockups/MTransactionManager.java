package org.mozzes.application.demo.mockups;

import org.mozzes.application.plugin.transaction.TransactionManager;

/**
 * Mockup implementation of the server transaction manager.<br>
 * In the testing we're only interested for calls of the {@link TransactionManager} methods so we need some boolean
 * variables to store that information
 * 
 * @author vita
 */
public class MTransactionManager implements TransactionManager {

	public static boolean commited = false;

	public static boolean rollbacked = false;

	public static boolean started = false;

	/**
	 * if we're starting new transaction commited and rollbacked are false again
	 */
	@Override
	public void begin(boolean nested) {
		started = true;
		rollbacked = false;
		commited = false;
	}

	/**
	 * @see TransactionManager#commit()
	 */
	@Override
	public void commit() {
		commited = true;
	}

	/**
	 * @see TransactionManager#rollback()
	 */
	@Override
	public void rollback() {
		rollbacked = true;
	}
	
	@Override
	public void finalizeTransaction(boolean successful) {
		
	}
}
