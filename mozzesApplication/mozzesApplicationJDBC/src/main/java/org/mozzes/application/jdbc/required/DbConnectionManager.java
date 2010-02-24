package org.mozzes.application.jdbc.required;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * {@link DbConnectionManager} creates and closes database connections. 
 */
public interface DbConnectionManager {

	/**
	 * Creates new database connection (or gets one from the pool)
	 * @return Connection database connection
	 * @throws SQLException if database connection can't be established
	 */
	public Connection get() throws SQLException;
	
	/**
	 * Closes database connection (or returns it to the pool)
	 * @param connection database connection to be closed
	 */
	public void close(Connection connection);
	
}
