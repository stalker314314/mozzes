package org.mozzes.application.hibernate.transaction;

import org.mozzes.application.plugin.transaction.TransactionManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class HibernateTransactionManager implements TransactionManager {
	
	@Inject
	private Provider<HibernateTransaction> transactionProvider;

	@Override
	public void begin(boolean nested) {
		transactionProvider.get().begin();
	}

	@Override
	public void commit() {
		transactionProvider.get().commit();
	}

	@Override
	public void rollback() {
		transactionProvider.get().rollback();
	}

	@Override
	public void finalizeTransaction(boolean successful) {
		transactionProvider.get().clear();
	}

}
