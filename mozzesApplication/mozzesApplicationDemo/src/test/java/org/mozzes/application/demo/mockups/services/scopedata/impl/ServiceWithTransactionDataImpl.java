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
