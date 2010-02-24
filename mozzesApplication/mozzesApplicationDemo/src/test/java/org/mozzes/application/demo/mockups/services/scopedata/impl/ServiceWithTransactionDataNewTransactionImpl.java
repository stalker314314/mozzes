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
