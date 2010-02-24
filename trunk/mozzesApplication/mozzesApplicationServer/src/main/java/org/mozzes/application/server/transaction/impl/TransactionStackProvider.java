package org.mozzes.application.server.transaction.impl;

import org.mozzes.application.server.internal.MozzesAbstractProvider;
import org.mozzes.application.server.request.RequestManager;

public class TransactionStackProvider extends MozzesAbstractProvider<TransactionStack> {

	public TransactionStackProvider(RequestManager requestManager) {
		super(requestManager);
	}

	public TransactionStack get() {
		return getRequestManager().get().getTransactionStack();
	}

	@Override
	public String toString() {
		return "TransactionStackProvider";
	}

}
