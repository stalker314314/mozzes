package org.mozzes.application.jdbc.impl;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import org.mozzes.application.jdbc.required.DbConnectionManager;
import org.mozzes.application.module.scope.TransactionScoped;

import com.google.inject.Inject;

@TransactionScoped
class ServerManagedTransactionContext {

	private Connection connection;
	
	@Inject
	private DbConnectionManager connectionManager;

	Connection getConnection() throws SQLException {
		if (connection == null) {
			connection = connectionManager.get();
			connection.setAutoCommit(false);
		}
		return (Connection) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				new Class[] { Connection.class }, new ServerManagedConnection(connection));
	}
	
	Connection getRealConnection() {
		return connection;
	}

	Connection clearConnection() {
		Connection returnValue = connection;
		connection = null;
		return returnValue;
	}

}
