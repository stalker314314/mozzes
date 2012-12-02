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
package org.mozzes.application.demo.mockups.services.scopedata;

import org.mozzes.application.common.transaction.*;
import org.mozzes.application.demo.mockups.scopedata.*;
import org.mozzes.application.module.scope.*;

/**
 * This is the service that is incrementing the value of the simple counter stored in the {@link TransactionScoped}
 * object {@link MTransactionData}.
 * 
 * There are two methods: {@link ServiceWithTransactionDataNewTransaction#incrementTransactionCounterInNewTransaction()}
 * is annotated with {@link Transactional} so calls to that method will
 * 
 * @author vita
 */
public interface ServiceWithTransactionDataNewTransaction {

  /**
   * If the method is annotated with transactional execution of this method is called inside the new nested transaction
   */
  @Transactional
  void incrementTransactionCounterInNewTransaction();

  /**
   * This method is not invoked in the new transaction and it's just incrementing value of the counter in the
   * transaction context.
   */
  void incrementTransactionCounterInNewTransactionBad();
}
