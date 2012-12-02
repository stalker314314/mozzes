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
package org.mozzes.application.server.transaction.impl;

import java.util.LinkedList;

import org.mozzes.application.plugin.transaction.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class TransactionStack is used for managing the transactions that are in the same request.<br>
 * Because the transactions can be nested we use stack(LIFO list) rather than simple list(FIFO)
 */
public class TransactionStack {

  private static final Logger logger = LoggerFactory.getLogger(TransactionStack.class);

  /** The transactions that are on the stack. */
  private final LinkedList<TransactionContext> transactions;

  public TransactionStack() {
    this.transactions = new LinkedList<TransactionContext>();
  }

  /**
   * Add new transaction to the stack.
   */
  void push(TransactionContext transactionContext) {
    transactions.add(transactionContext);
    logger.debug("TransactionStack.push: size=" + transactions.size());
  }

  /**
   * remove the top(most nested) transaction
   */
  TransactionContext pop() {
    if (transactions.isEmpty())
      throw new TransactionException("Transaction not started");

    logger.debug("TransactionStack.pop: size=" + (transactions.size() - 1));

    return transactions.removeLast();
  }

  /**
   * get the top(most nested) transaction
   */
  public TransactionContext peek() {
    if (transactions.isEmpty())
      throw new TransactionException("Transaction not started");

    return transactions.getLast();
  }

  boolean isEmpty() {
    return transactions.isEmpty();
  }
}
