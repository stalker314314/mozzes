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
 * This is the specification of the service that is using values stored in transaction context. Because the
 * {@link MTransactionData} is annotated with {@link TransactionScoped} the same instance is used in the whole
 * transaction
 * 
 * @author vita
 */
public interface ServiceWithTransactionData {

	/**
	 * This method is incrementing value of the {@link TransactionScoped} counter and then calling
	 * {@link ServiceWithTransactionDataNewTransaction#incrementTransactionCounterInNewTransaction()} method that also
	 * increment the {@link TransactionScoped} counter.<br>
	 * 
	 * But the {@link ServiceWithTransactionDataNewTransaction#incrementTransactionCounterInNewTransaction()} method is
	 * annotated with {@link Transactional} so the incrementing will be in the new transaction so the returned value
	 * should be 1
	 */
	int incrementAndReturn();

	/**
	 * This is the same as {@link ServiceWithTransactionData#incrementAndReturn()} but the called service method is
	 * {@link ServiceWithTransactionDataNewTransaction#incrementTransactionCounterInNewTransactionBad()} that is not
	 * annotated with the @Transactional so the incrementing will happen twice in the same transaction so the returned
	 * value should be 2.
	 */
	int incrementAndReturnBad();

}
