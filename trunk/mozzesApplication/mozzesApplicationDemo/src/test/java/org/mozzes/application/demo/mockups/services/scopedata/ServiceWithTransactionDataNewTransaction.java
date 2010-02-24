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
	 * If the method is annotated with transactional execution of this method is called inside the new nested
	 * transaction
	 */
	@Transactional
	void incrementTransactionCounterInNewTransaction();

	/**
	 * This method is not invoked in the new transaction and it's just incrementing value of the counter in the
	 * transaction context.
	 */
	void incrementTransactionCounterInNewTransactionBad();
}
