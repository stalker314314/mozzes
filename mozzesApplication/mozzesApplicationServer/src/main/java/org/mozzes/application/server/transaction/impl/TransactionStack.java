package org.mozzes.application.server.transaction.impl;

import java.util.LinkedList;

import org.mozzes.application.plugin.transaction.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class TransactionStack is used for managing the transactions that are in the same request.<br>
 * Because the transactions can be nested we use stack(LIFO list) rather than simple list(FIFO)
 */
public class TransactionStack {
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionStack.class);

	/** The transactions that are on the stack. */
	private final LinkedList<TransactionContext> transactions;

	public TransactionStack() {
		this.transactions = new LinkedList<TransactionContext>();
	}

	/**
	 * Add new transaction to the stack.
	 */
	void push(TransactionContext transactionContext) {
		transactions.add(transactionContext);
		logger.debug("TransactionStack.push: size=" + transactions.size());
	}

	/**
	 * remove the top(most nested) transaction
	 */
	TransactionContext pop() {
		if (transactions.isEmpty())
			throw new TransactionException("Transaction not started");
		
		logger.debug("TransactionStack.pop: size=" + (transactions.size() - 1));

		return transactions.removeLast();
	}

	/**
	 * get the top(most nested) transaction
	 */
	public TransactionContext peek() {
		if (transactions.isEmpty())
			throw new TransactionException("Transaction not started");

		return transactions.getLast();
	}

	boolean isEmpty() {
		return transactions.isEmpty();
	}
}
