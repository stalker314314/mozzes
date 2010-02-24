package org.mozzes.application.server.transaction.impl;

import org.mozzes.application.server.internal.MozzesAbstractProvider;
import org.mozzes.application.server.request.RequestManager;

public class TransactionContextProvider extends MozzesAbstractProvider<TransactionContext> {

	public TransactionContextProvider(RequestManager requestManager){
		super(requestManager);
	}
	
	@Override
	public TransactionContext get() {
		return getRequestManager().get().getTransactionStack().peek();
	}

	@Override
	public String toString() {
		return "TransactionContextProvider";
	}

}
