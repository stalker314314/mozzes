package org.mozzes.application.server.request;

import org.mozzes.application.server.request.impl.RequestContext;
import org.mozzes.application.server.session.impl.SessionContext;

public interface RequestManager {

	/**
	 * Start a new Request for the given SessionContext(in the current thread).
	 */
	public void start(SessionContext sc);

	/**
	 * Get the RequestContext of the Request(running in current thread).
	 */
	public RequestContext get();

	/**
	 * Finish the request(running in current thread).
	 */
	public void finish();

}