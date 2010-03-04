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

import org.mozzes.application.common.transaction.*;
import org.mozzes.application.demo.mockups.scopedata.*;
import org.mozzes.application.demo.mockups.services.scopedata.*;

import com.google.inject.*;

/**
 * This is the internal service that {@link ServiceWithTransactionDataImpl} is using and this service is using
 * {@link MTransactionData} object to store simple counter value.
 * 
 * @author vita
 */
public class ServiceWithTransactionDataNewTransactionImpl implements ServiceWithTransactionDataNewTransaction {

	@Inject
	MTransactionData transactionData;

	/**
	 * @see ServiceWithTransactionDataNewTransaction#incrementTransactionCounterInNewTransaction()
	 */
	@Override
	public void incrementTransactionCounterInNewTransaction() {
		transactionData.increment();
	}

	/**
	 * This is not OK..Transactional should be in the interface
	 * 
	 * @see ServiceWithTransactionDataNewTransaction#incrementTransactionCounterInNewTransactionBad()
	 */
	@Transactional
	@Override
	public void incrementTransactionCounterInNewTransactionBad() {
		transactionData.increment();
	}
}
