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
package org.mozzes.remoting.server;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Klasa koja prati aktivne RemotingClientListener-e na serverskoj strani.
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 */
class RemotingClientManager {
  private static final Logger logger = LoggerFactory.getLogger(RemotingClientManager.class);

  /**
   * Trenutno aktivni RemotingClientListener-i
   */
  private Set<RemotingClientListener> activeListeners = new HashSet<RemotingClientListener>();

  /**
   * Da li je server aktivan
   */
  private boolean active = true;

  /**
   * Dodaje novog RemotingClientListener-a
   * 
   * @param listener
   *          RemotingClientListener
   * @return true - ukoliko je dodavanje uspelo, a dodavanje nece uspeti ukoliko je server zaustavljen ili ukoliko je
   *         RemotingClientListener vec dodat
   */
  boolean add(RemotingClientListener listener) {
    if (logger.isDebugEnabled()) {
      logger.debug("add() before synchronized: " + listener.toString());
    }

    boolean returnValue = false;
    synchronized (activeListeners) {
      if (logger.isDebugEnabled()) {
        logger.debug("add() after synchronized: " + listener.toString());
      }
      if (active) {
        if (logger.isDebugEnabled()) {
          logger.debug("add() active, should be accepted " + listener.toString());
        }
        returnValue = activeListeners.add(listener);
      }
    }
    if (returnValue)
      RemotingClientInfo.addClient(listener.getName());
    return returnValue;
  }

  /**
   * Uklanja RemotingClientListener-a nakon sto je zavrsio sa radom
   * 
   * @param listener
   *          RemotingClientListener
   */
  void remove(RemotingClientListener listener) {
    boolean removed = false;
    synchronized (activeListeners) {
      if (activeListeners.remove(listener)) {
        removed = true;
        if ((!active) && (activeListeners.size() == 0))
          activeListeners.notifyAll();
      }
    }
    if (removed)
      RemotingClientInfo.removeClient(listener.getName());
  }

  /**
   * Vraca informaciju o tome da li je server jos uvek aktivan
   * 
   * @return true - ukoliko je server aktivan
   */
  boolean isActive() {
    synchronized (activeListeners) {
      return active;
    }
  }

  /**
   * Vraca broj trenutno aktivnih RemotingClientListener-a
   * 
   * @return broj aktivnih RemotingClientListener-a
   */
  int numberOfListeners() {
    synchronized (activeListeners) {
      return activeListeners.size();
    }
  }

  /**
   * Izdaje nalog svim RemotingClientListener-ima da prekinu sa radom. Ova metoda se trenutno izvrsava i ne ceka da se
   * RemotingClientListener-i stvarno zaustave.
   */
  void stopAll() {
    if (active) {
      synchronized (activeListeners) {
        if (active) {
          active = false;
          for (Iterator<RemotingClientListener> iter = activeListeners.iterator(); iter.hasNext();) {
            iter.next().stopListening();
          }
        }
      }
    }
  }

  /**
   * Metoda blokira sve dok svi RemotingClientListener-i ne zavrse sa radom
   */
  void waitClientsToStop() {
    synchronized (activeListeners) {
      while (activeListeners.size() > 0) {
        try {
          activeListeners.wait();
        } catch (InterruptedException e) {
          Thread.interrupted();
        }
      }
    }
  }
}
