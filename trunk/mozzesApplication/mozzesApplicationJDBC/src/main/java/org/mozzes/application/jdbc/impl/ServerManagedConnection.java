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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

class ServerManagedConnection implements InvocationHandler {

  private static final Map<String, String> forbiddedMethods = createForbiddenMethods();

  private final Connection dbConnection;

  ServerManagedConnection(Connection dbConnection) {
    this.dbConnection = dbConnection;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    final String methodName = method.getName();
    if (forbiddedMethods.containsKey(methodName))
      throw new SQLException(forbiddedMethods.get(methodName));

    try {
      return method.invoke(dbConnection, args);
    } catch (InvocationTargetException ite) {
      throw ite.getCause();
    }
  }

  private static final Map<String, String> createForbiddenMethods() {
    final Map<String, String> returnValue = new HashMap<String, String>();
    returnValue.put("setAutoCommit",
        "Transactions are automatically managed by the framework, autocommit status can not be changed");
    returnValue.put("commit",
        "Transactions are automatically managed by the framework, it is forbidden to call commit explicitly");
    returnValue.put("rollback",
        "Transactions are automatically managed by the framework, it is forbidden to call rollback explicitly");
    returnValue.put("close",
        "Connections are automatically closed by the framework, it is forbidden to call close explicitly");
    return returnValue;
  }

}
