/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
