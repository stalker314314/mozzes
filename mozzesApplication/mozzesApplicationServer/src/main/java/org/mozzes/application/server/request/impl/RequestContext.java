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

import org.mozzes.application.server.internal.AbstractContext;
import org.mozzes.application.server.session.impl.SessionContext;
import org.mozzes.application.server.transaction.impl.TransactionStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class RequestContext is associated with the request and hold's the data important for the request. <br>
 * <br>
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
