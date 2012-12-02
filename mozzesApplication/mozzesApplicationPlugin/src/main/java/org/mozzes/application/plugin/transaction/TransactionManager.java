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
package org.mozzes.application.plugin.transaction;

/**
 * TransactionManager is responsible for managing the transaction lifecycle.
 */
public interface TransactionManager {

  /**
   * This method is called when new transaction is starting.
   * 
   * @param nested
   *          is this nested transaction?
   */
  public void begin(boolean nested);

  /**
   * This method is called to commit the current transaction
   */
  public void commit();

  /**
   * This method is called to rollback the current transaction
   */
  public void rollback();

  /**
   * This method is called after transaction is finished.
   * 
   * @param successful
   *          true - if transaction is successfully committed
   */
  public void finalizeTransaction(boolean successful);
}
