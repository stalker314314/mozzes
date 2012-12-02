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
package org.mozzes.application.server.lifecycle;

/**
 * Thread that keeps server alive
 */
class KeepAliveThread extends Thread {

  // 100 years :)
  private static final long MAX_ALIVE_TIME = 1000 * 60 * 60 * 24 * 365 * 100;

  private volatile boolean alive = true;

  KeepAliveThread() {
    super("KeepAliveThread");
    setPriority(MIN_PRIORITY);
    setDaemon(false);
  }

  @Override
  public void run() {
    while (alive) {
      try {
        Thread.sleep(MAX_ALIVE_TIME);
      } catch (InterruptedException e) {
        interrupted();
      }
    }
  }

  synchronized boolean die() {
    if (!alive)
      return false;

    alive = false;
    interrupt();
    return true;
  }
}