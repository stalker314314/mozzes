package org.mozzes.application.jdbc.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.mozzes.application.jdbc.exposed.DbConnectionProvider;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ServerManagedConnectionProvider implements DbConnectionProvider {

	private final Provider<ServerManagedTransactionContext> transactionContextProvider;
	
	@Inject
	ServerManagedConnectionProvider(Provider<ServerManagedTransactionContext> transactionContextProvider) {
		this.transactionContextProvider = transactionContextProvider;
	}
	
	@Override
	public Connection getDBConnection() throws SQLException {
		return transactionContextProvider.get().getConnection();
	}

}
