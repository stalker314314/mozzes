/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
