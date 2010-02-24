package org.mozzes.application.server.request.impl;

import org.mozzes.application.server.internal.AbstractContext;
import org.mozzes.application.server.session.impl.SessionContext;
import org.mozzes.application.server.transaction.impl.TransactionStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class RequestContext is associated with the request and hold's the data important for the request.
 * <br><br>
 * Most importantly it holds the stack transactions running in the request.
 * 
 * @see TransactionStack
 * @see SessionContext
 */
public class RequestContext extends AbstractContext {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestContext.class);

	/** The session context in the request is started. */
	private final SessionContext sessionContext;
	
	/** The stack of transactions running in this request. */
	private final TransactionStack transactionStack;

	RequestContext(SessionContext sessionContext) {
		logger.debug("Request context created");
		this.sessionContext = sessionContext;
		this.transactionStack = new TransactionStack();
	}

	public SessionContext getSessionContext() {
		return sessionContext;
	}
	
	public TransactionStack getTransactionStack() {
		return transactionStack;
	}

	@Override
	protected String getName() {
		return "RequestContext";
	}
}
