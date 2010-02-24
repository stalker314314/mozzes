package org.mozzes.application.jdbc.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.mozzes.application.jdbc.required.DbConnectionManager;
import org.mozzes.application.plugin.transaction.TransactionException;
import org.mozzes.application.plugin.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ServerManagedTransactionManager implements TransactionManager {

	private static final Logger logger = LoggerFactory.getLogger(ServerManagedTransactionManager.class);
	
	@Inject
	private Provider<ServerManagedTransactionContext> transactionContextProvider;

	@Inject
	private DbConnectionManager connectionManager;

	@Override
	public void begin(boolean nested) {
		// ignored 
	}

	@Override
	public void commit() {
		Connection dbConnection = transactionContextProvider.get().getRealConnection();
		if (dbConnection != null) {
			try {
				dbConnection.commit();
			} catch (SQLException e) {
				throw new TransactionException("Commit failed", e);
			}
		}
	}

	@Override
	public void rollback() {
		Connection dbConnection = transactionContextProvider.get().getRealConnection();
		if (dbConnection != null) {
			try {
				dbConnection.rollback();
			} catch (SQLException e) {
				logger.error("Rollback failed", e);
			}
		}
	}
	
	@Override
	public void finalizeTransaction(boolean successful) {
		Connection dbConnection = transactionContextProvider.get().clearConnection();
		if (dbConnection != null) 
			connectionManager.close(dbConnection);
	}

}
