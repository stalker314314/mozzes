package org.mozzes.application.jdbc.exposed;

import java.sql.Connection;
import java.sql.SQLException;

public interface DbConnectionProvider {
	
	/**
	 * @return Database connection used for currently executing transaction. Server will automatically close this
	 * connection after transaction is completed.
	 * 
	 * @throws SQLException If connection cannot be obtained.
	 */
	Connection getDBConnection() throws SQLException;

}
