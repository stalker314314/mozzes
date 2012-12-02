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
package org.mozzes.application.server.session.impl;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.mozzes.application.common.session.SessionExpiredException;
import org.mozzes.application.server.client.MozzesInternalClient;
import org.mozzes.application.server.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SessionManagerImpl implements Session Layer in the Mozzes Server.
 * 
 * @see SessionManager
 * @see SessionContext
 */
public final class SessionManagerImpl implements SessionManager {

  private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

  private final SessionIdGenerator idGenerator = new SessionIdGenerator();

  /** Map of sessionContexts associated with sessionIds. */
  private final ConcurrentMap<String, SessionContext> sessions = new ConcurrentHashMap<String, SessionContext>();

  SessionManagerImpl() throws NoSuchAlgorithmException {
    logger.debug("Session Manager created");
    new SessionManagerCleanupThread(sessions).start();
  }

  /**
   * @see SessionManager#requestStarted(String)
   */
  @Override
  public SessionContext requestStarted(String sessionId) {
    SessionContext sessionContext;

    /* if there's no sessionId create new session */
    if (sessionId != null && (!sessionId.equals(MozzesInternalClient.INTERNAL_CLIENT_ID)))
      sessionContext = get(sessionId);
    else
      sessionContext = create();

    sessionContext.requestStarted();
    return sessionContext;
  }

  /**
   * @see SessionManager#requestFinished(SessionContext)
   */
  @Override
  public void requestFinished(SessionContext sessionContext) {
    sessionContext.requestFinished();

    if (!sessionContext.isUserAuthorized()) {
      sessionContext.scopeCleanUp();
      sessions.remove(sessionContext.getSessionId());
      logger.debug("Session finished: " + sessionContext);
    }

  }

  int getSessionCount() {
    return sessions.size();
  }

  /**
   * @return newly created session context.
   */
  private SessionContext create() {

    final SessionContext sessionContext = new SessionContext();
    String sessionId;
    do {
      sessionId = idGenerator.generateSessionId();
    } while (sessions.putIfAbsent(sessionId, sessionContext) != null);
    sessionContext.setSessionId(sessionId);
    logger.debug("Session started: " + sessionContext);
    return sessionContext;
  }

  /**
   * @return session context for provided sessionId
   */
  private SessionContext get(String sessionId) {
    SessionContext returnValue = sessions.get(sessionId);
    if (returnValue != null)
      return returnValue;

    throw new SessionExpiredException();
  }
}
