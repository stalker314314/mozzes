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
