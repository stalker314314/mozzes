package org.mozzes.application.server.transaction.impl;

import org.mozzes.application.plugin.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class TransactionManagerImpl is the default implementation of the TransactionManager interface and adds support
 * for transaction management in the application.Default version don't do anything so MozzesServer framework users need
 * to specify their specific version of the TransactionManagment.
 */
public class TransactionManagerImpl implements TransactionManager {

	private static final Logger logger = LoggerFactory.getLogger(TransactionManager.class);

	/*
	 * @see TransactionManager#begin()
	 */
	@Override
	public void begin(boolean nested) {
		logger.debug(nested ? "Nested transaction started" : "Transaction started");
	}

	/*
	 * @see TransactionManager#commit()
	 */
	@Override
	public void commit() {
		logger.debug("Transaction comitted");
	}

	/*
	 * @see TransactionManager#rollback()
	 */
	@Override
	public void rollback() {
		logger.debug("Transaction rollbacked");
	}
	
	/*
	 * @see TransactionManager#finalizeTransaction()
	 */
	@Override
	public void finalizeTransaction(boolean successful) {
		logger.debug("Transaction " + (successful ? "" : "un") + "successfully finalized" );
	}

}
