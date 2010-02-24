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
