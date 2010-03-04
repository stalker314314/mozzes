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
