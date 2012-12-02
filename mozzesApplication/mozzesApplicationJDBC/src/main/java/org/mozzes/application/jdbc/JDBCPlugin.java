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
package org.mozzes.application.jdbc;

import org.mozzes.application.jdbc.exposed.DbConnectionProvider;
import org.mozzes.application.jdbc.impl.ServerManagedConnectionProvider;
import org.mozzes.application.jdbc.impl.ServerManagedTransactionManager;
import org.mozzes.application.jdbc.required.DbConnectionManager;
import org.mozzes.application.plugin.transaction.TransactionManager;
import org.mozzes.application.plugin.transaction.TransactionPlugin;

import com.google.inject.Binder;

/**
 * {@link JDBCPlugin} provides server managed transactions for JDBC connections.<br>
 * <br>
 * User of this plugin must provide {@link DbConnectionManager}. {@link DbConnectionManager} is usually implemented as
 * connection pool. <br>
 * <br>
 * This plugin exposes {@link DbConnectionProvider} which is used to obtain JDBC connection for current transaction.
 */
public class JDBCPlugin extends TransactionPlugin {

  private final Class<? extends DbConnectionManager> connectionManager;

  /**
   * Creates {@link JDBCPlugin}
   * 
   * @param connectionManager
   *          Connection manager used to create and close database connections.
   */
  public JDBCPlugin(Class<? extends DbConnectionManager> connectionManager) {
    super();
    this.connectionManager = connectionManager;
  }

  @Override
  public Class<? extends TransactionManager> getTransactionManager() {
    return ServerManagedTransactionManager.class;
  }

  @Override
  public void doCustomBinding(Binder binder) {
    super.doCustomBinding(binder);
    binder.bind(DbConnectionManager.class).to(connectionManager);
    binder.bind(DbConnectionProvider.class).to(ServerManagedConnectionProvider.class);
  }
}
