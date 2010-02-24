package org.mozzes.application.plugin.request;

import org.mozzes.invocation.common.Invocation;

/**
 * Server entry point.<br>
 * <br>
 * Plugins implemented as different client adapters can inject this interface and use it to submit a client request to
 * server for processing.
 */
public interface RequestProcessor {

	/**
	 * Process the invocation in the current request.
	 */
	public <I> Object process(String sessionId, Invocation<I> invocation) throws Throwable;
}