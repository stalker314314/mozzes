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
     * @param listener RemotingClientListener
     * @return true - ukoliko je dodavanje uspelo, a dodavanje nece uspeti ukoliko je server zaustavljen ili ukoliko je
     *         RemotingClientListener vec dodat
     */
    boolean add(RemotingClientListener listener) {
    	if (logger.isDebugEnabled()) {
    		logger.debug("add() before synchronized: " + listener.toString());
    	}
        synchronized (activeListeners) {
        	if (logger.isDebugEnabled()) {
        		logger.debug("add() after synchronized: " + listener.toString());
        	}
            if (active) {
            	if (logger.isDebugEnabled()) {
            		logger.debug("add() active, should be accepted " + listener.toString());
            	}
                return activeListeners.add(listener);
            }
            return false;
        }
    }

    /**
     * Uklanja RemotingClientListener-a nakon sto je zavrsio sa radom
     * 
     * @param listener RemotingClientListener
     */
    void remove(RemotingClientListener listener) {
        synchronized (activeListeners) {
            if (activeListeners.remove(listener))
                if ((!active) && (activeListeners.size() == 0))
                    activeListeners.notifyAll();
        }
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
