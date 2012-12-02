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

import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SessionManagerCleanupThread is cleaning expired sessions.
 */
class SessionManagerCleanupThread extends Thread {

  private static final Logger logger = LoggerFactory.getLogger(SessionManagerCleanupThread.class);

  private ConcurrentMap<String, SessionContext> sessions;

  private static final long DEFAULT_SESSION_CLEANUP_INTERVAL = 60000; // 1 minute

  /** time period between two cleanups of expired sessions . */
  private long runInterval = DEFAULT_SESSION_CLEANUP_INTERVAL;

  private boolean run = true;

  SessionManagerCleanupThread(ConcurrentMap<String, SessionContext> sessions) {
    super("SessionManagerCleanupThread");
    this.sessions = sessions;
    this.setDaemon(true);
    this.setPriority(MIN_PRIORITY);
  }

  @Override
  public void run() {
    while (run) {
      try {
        try {
          sleep(runInterval);
        } catch (InterruptedException e) {
          interrupted();
        }
        cleanup();
      } catch (Throwable thr) {
        logger.error("Exception during session cleanup", thr);
      }
    }
  }

  /**
   * Cleanup the expired sessions.
   */
  private void cleanup() {
    Set<Entry<String, SessionContext>> entries = sessions.entrySet();

    for (Entry<String, SessionContext> sessionEntry : entries) {
      SessionContext sessionContext = sessionEntry.getValue();
      if (sessionContext.isExpired()) {
        logger.debug("Session expired: " + sessionContext);
        sessionContext.scopeCleanUp();
        entries.remove(sessionEntry);
      }
    }
  }

  void stopRunning() {
    this.run = false;
  }

  void setCleanupInterval(long runInterval) {
    this.runInterval = runInterval;
  }
}