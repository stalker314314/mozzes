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
package org.mozzes.application.demo.mockups.services.scopedata.impl;

import org.mozzes.application.demo.mockups.scopedata.*;
import org.mozzes.application.demo.mockups.services.scopedata.*;
import org.mozzes.application.module.scope.*;

import com.google.inject.*;

/**
 * This is the implementation of the {@link ServiceWithTransactionData} interface that is preserving value of the simple
 * counter between invocations in the same session
 * 
 * @author vita
 */
public class ServiceWithTransactionDataImpl implements ServiceWithTransactionData {

  /**
   * This is how the transaction data is obtained. (Data stored in the session is annotated with the
   * {@link TransactionScoped} annotation.
   */
  @Inject
  private MTransactionData transactionData;

  /**
   * This is the injecting of the some other service. Type should be the interface and not the service implementation
   * class.<br>
   * 
   * <code>@Inject <br>private MServerService2Impl service2; </code> this is not good
   */
  @Inject
  private ServiceWithTransactionDataNewTransaction serviceThatStartsNewTransaction;

  /**
   * Here we increment the integer in the transactionData object that is stored in the transaction and then we call
   * another service that is annotated with Transactional annotation and that service also increment the same counter.
   * But that incrementing is in the new transaction so this value shouldn't change because of the method call.
   * 
   * @see ServiceWithTransactionDataNewTransaction#incrementTransactionCounterInNewTransactionBad()
   * @see ServiceWithTransactionData#incrementAndReturn()
   */
  @Override
  public int incrementAndReturn() {
    transactionData.increment();
    serviceThatStartsNewTransaction.incrementTransactionCounterInNewTransaction();
    return transactionData.getCounter();
  }

  /**
   * This is the same as {@link ServiceWithTransactionDataImpl#incrementAndReturn()} but because
   * ServiceWithTransactionDataNewTransaction#incrementTransactionCounterInNewTransactionBad() is not annotated with @Transactional
   * this will not work so the result will be 2 and not 1
   * 
   * @see ServiceWithTransactionDataNewTransaction#incrementTransactionCounterInNewTransactionBad()
   * @see ServiceWithTransactionData#incrementAndReturnBad()
   */
  @Override
  public int incrementAndReturnBad() {
    transactionData.increment();
    serviceThatStartsNewTransaction.incrementTransactionCounterInNewTransactionBad();
    return transactionData.getCounter();
  }
}
