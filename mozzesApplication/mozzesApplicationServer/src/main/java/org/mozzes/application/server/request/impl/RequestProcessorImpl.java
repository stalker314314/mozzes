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

import org.mozzes.application.common.exceptions.ClientLoggingException;
import org.mozzes.application.common.exceptions.UndeclaredServiceException;
import org.mozzes.application.plugin.request.RequestProcessor;
import org.mozzes.application.server.internal.MozzesInternal;
import org.mozzes.application.server.lifecycle.MozzesServerLifeCycleStatus;
import org.mozzes.application.server.request.RequestManager;
import org.mozzes.application.server.session.SessionManager;
import org.mozzes.application.server.session.impl.SessionContext;
import org.mozzes.invocation.common.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * The Class RequestProcessor is responsible for processing new request(starting request,finishing request,informing
 * associated session about request status etc).
 */
public class RequestProcessorImpl implements RequestProcessor {

  private static final Logger logger = LoggerFactory.getLogger(RequestProcessorImpl.class);

  /** Responsible for session layer support. */
  private final SessionManager sessionManager;

  /** Responsible for request layer support. */
  private final RequestManager requestManager;

  private final Injector injector;

  private final MozzesServerLifeCycleStatus serverStatus;

  @Inject
  RequestProcessorImpl(@MozzesInternal SessionManager sessionManager, RequestManager requestManager, Injector injector,
      MozzesServerLifeCycleStatus serverStatus) {
    this.sessionManager = sessionManager;
    this.requestManager = requestManager;
    this.injector = injector;
    this.serverStatus = serverStatus;
  }

  /*
   * @see IRequestProcessor#process(String,Invocation)
   */
  public <I> Object process(String sessionId, Invocation<I> invocation) throws Throwable {
    if (sessionId == null && !serverStatus.isStarted())
      throw new ClientLoggingException("server is not started");

    if (requestManager.get() == null)
      return processInvocationInNewRequest(sessionId, invocation);

    return processInvocation(invocation);
  }

  private <I> Object processInvocationInNewRequest(String sessionId, Invocation<I> invocation) throws Throwable {
    // get session context(if exists otherwise create new) and inform that request is in progress
    SessionContext sc = sessionManager.requestStarted(sessionId);

    try {
      // start new request and associate new request context
      requestManager.start(sc);
      try {
        // and then process invocation
        return processInvocation(invocation);
      } catch (Throwable thr) {

        throw handleException(invocation, thr);

      } finally {
        // always finish request
        requestManager.finish();
      }
    } finally {
      // inform session context that request is finished
      sessionManager.requestFinished(sc);
    }
  }

  /**
   * Process invocation and return the result of the invocation.
   */
  private <I> Object processInvocation(Invocation<I> invocation) throws Throwable {
    if (logger.isDebugEnabled())
      logger.debug("Processing request: " + invocation.getInterface().getName() + "." + invocation.getMethodName());
    return invocation.invoke(injector.getInstance(invocation.getInterface()));
  }

  /**
   * Logs Exception if not declared in method signature
   * 
   * @param <I>
   *          Invocation type - service interface
   * @param invocation
   *          invocation
   * @param thr
   *          Exception thrown during method execution
   */
  private <I> Throwable handleException(Invocation<I> invocation, Throwable thr) {
    if (isDeclaredException(invocation, thr)) {
      logger.info("Service exception", thr);
      return thr;
    }

    if (thr instanceof RuntimeException) {
      logger.error("Runtime service exception", thr);
      return thr;
    }

    logger.error("Undeclared service exception", thr);
    return new UndeclaredServiceException(thr);
  }

  private <I> boolean isDeclaredException(Invocation<I> invocation, Throwable thr) {
    try {
      Class<?>[] declaredExceptions = invocation.getMethod().getExceptionTypes();

      for (int i = 0; i < declaredExceptions.length; i++)
        if (declaredExceptions[i].isInstance(thr))
          return true;

    } catch (SecurityException e) {
      logger.error("Unable to find service method", e);
    } catch (NoSuchMethodException e) {
      logger.error("Unable to find service method", e);
    }

    return false;
  }
}
