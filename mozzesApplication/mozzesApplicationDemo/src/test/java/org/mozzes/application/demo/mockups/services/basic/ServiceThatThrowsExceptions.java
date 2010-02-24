package org.mozzes.application.demo.mockups.services.basic;

import java.io.*;

import org.mozzes.application.common.transaction.*;


public interface ServiceThatThrowsExceptions {

	/**
	 * This service method is throwing checked exception so we can test if exception propagation to the client is
	 * working
	 */
	String serviceWhichThrowsException() throws IOException;

	/**
	 * This service method is throwing runtime exception so we can test if exception propagation to the client is
	 * working
	 */
	String serviceWhichThrowsRuntimeException();

	/**
	 * This service method is throwing exception which type is annotated with {@link TransactionIgnored} annotation so
	 * the transaction in which this call occurred will not be rollbacked.
	 */
	void serviceWhichThrowsIgnoredException();
}
