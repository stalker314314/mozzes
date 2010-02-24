package org.mozzes.application.server.mockups;

import org.mozzes.application.server.transaction.impl.TransactionStack;

import com.google.inject.Provider;

public class MockupTransactionStackProvider implements Provider<TransactionStack> {
	
	private final TransactionStack stack = new TransactionStack();

	@Override
	public TransactionStack get() {
		return stack;
	}

}
