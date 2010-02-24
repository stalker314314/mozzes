package org.mozzes.application.server.request.impl;

import org.mozzes.application.server.request.RequestManager;
import org.mozzes.application.server.session.impl.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class RequestManagerImpl is responsible for starting and stopping request's during the session. <br>
 * <br>
 * To start a new request RequestManagerImpl needs a {@link SessionContext} and creates a new {@link RequestContext}.
 * Every request is in separate Thread and this single instance uses {@link ThreadLocal} for storing
 * {@link RequestContext}.
 * 
 * @see ThreadLocal
 * @see RequestContext
 */
public class RequestManagerImpl implements RequestManager {

	private static final Logger logger = LoggerFactory.getLogger(RequestManagerImpl.class);

	/** The context of request. */
	private final ThreadLocal<RequestContext> requestContext = new ThreadLocal<RequestContext>();
	
	public RequestManagerImpl() {
		logger.debug("RequestManager created");
	}

	/*
	 * @see RequestManager#start(SessionContext)
	 */
	public void start(SessionContext sc) {
		requestContext.set(new RequestContext(sc));
		logger.debug("Request started");
	}

	/*
	 * @see RequestManager#get()
	 */
	public RequestContext get() {
		return requestContext.get();
	}

	/*
	 * @see RequestManager#finish()
	 */
	public void finish() {
		logger.debug("Request context destroyed");
		get().scopeCleanUp();
		requestContext.set(null);
		logger.debug("Request finished");
	}
}
