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

import org.mozzes.application.common.transaction.TransactionIgnored;
import org.mozzes.application.common.transaction.Transactional;
import org.mozzes.application.plugin.transaction.TransactionManager;
import org.mozzes.application.server.internal.MozzesInternal;
import org.mozzes.invocation.common.Invocation;
import org.mozzes.invocation.common.handler.InvocationHandler;
import org.mozzes.invocation.common.interceptor.InvocationInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * The Class TransactionInterceptor adds transactional support to the service invocations.
 * 
 * @see InvocationInterceptor
 */
public class TransactionInterceptor implements InvocationInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(TransactionInterceptor.class);

  /** The transaction stack. */
  private final Provider<TransactionStack> transactionStackProvider;

  /** The transaction provider. */
  private final Provider<TransactionManager> transactionProvider;

  /**
   * @param transactionStackProvider
   *          Stack of transactions that are associated with the request.<br>
   *          {@link TransactionStack} is bound to the MozzesScopes RequestScope so it's always the same in the same
   *          request
   */
  @Inject
  TransactionInterceptor(@MozzesInternal Provider<TransactionStack> transactionStackProvider,
      Provider<TransactionManager> provider) {
    this.transactionStackProvider = transactionStackProvider;
    this.transactionProvider = provider;
  }

  /**
   * This method adds the transaction support to the server service invocation and then calls the requested service
   * method<br>
   * 
   * @see InvocationInterceptor#invoke(Invocation, InvocationHandler)
   */
  @Override
  public <I> Object invoke(Invocation<? super I> invocation, InvocationHandler<I> target) throws Throwable {
    // does transaction already exist?
    TransactionStack transactionStack = transactionStackProvider.get();
    final boolean transactionStarted = transactionStarted(transactionStack);

    // if we didn't started the transaction and we don't need new one we just simply call the service.
    if (transactionStarted && (!newTransactionRequired(invocation)))
      return target.invoke(invocation);

    final TransactionManager tm = transactionProvider.get();

    Object returnValue = null;
    boolean trasactionSuccessful = false;
    try {
      // add new transaction context and start the transaction
      transactionStack.push(new TransactionContext());
      tm.begin(transactionStarted);

      try {
        returnValue = target.invoke(invocation);
        /*
         * after invocation if we're here that means the transaction didn't throw any exception and therefore it's
         * successful
         */
        trasactionSuccessful = true;
      } catch (Throwable thr) {
        /*
         * if transaction threw exception we will propagate the exception but only if that's not the "normal" behavior
         * of the service
         */
        if (transactionIgnoredByException(thr))
          trasactionSuccessful = true;

        throw thr;
      }
    } finally {
      // transaction is always finished, no matter what
      finishTransaction(transactionStack, tm, trasactionSuccessful);
    }
    // this can be null when service is not returning anything
    return returnValue;
  }

  private boolean transactionStarted(TransactionStack transactionStack) {
    return !transactionStack.isEmpty();
  }

  /**
   * this method just checks if the method the client wants to invoke is annotated with Transactional annotation and
   * because of that we need to create new nested transaction.
   */
  private <I> boolean newTransactionRequired(Invocation<? super I> invocation) throws NoSuchMethodException {
    return invocation.getMethod().isAnnotationPresent(Transactional.class);
  }

  /**
   * this method just checks if the method the client wants to invoke is annotated with TransactionIgnored annotation
   * and therefore we don't trigger the rollback even if exception is thrown.
   */
  private boolean transactionIgnoredByException(Throwable thr) {
    return thr.getClass().isAnnotationPresent(TransactionIgnored.class);
  }

  /**
   * This method is called on the end of transaction.<br>
   * It commits/rollbacks the transaction and then removes the transaction from the current requests context
   */
  private void finishTransaction(final TransactionStack transactionStack, final TransactionManager tm,
      final boolean trasactionSuccessful) {
    boolean commitSuccessful = false;
    try {
      if (trasactionSuccessful) {
        tm.commit();
        commitSuccessful = true;
      } else
        tm.rollback();
    } finally {
      try {
        tm.finalizeTransaction(commitSuccessful);
      } catch (Throwable e) {
        logger.error("Error during transaction finalization", e);
      }
      transactionStack.pop().scopeCleanUp();
    }
  }
}
