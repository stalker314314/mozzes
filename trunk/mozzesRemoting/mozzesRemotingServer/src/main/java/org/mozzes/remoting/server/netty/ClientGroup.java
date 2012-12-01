package org.mozzes.remoting.server.netty;

import java.util.HashSet;
import java.util.Set;

/**
 * Thread-safe set that contains client IDs. This set cannot contain two identical IDs. 
 * 
 * @author Vladimir Todorovic
 */
final class ClientGroup {

	private final Set<Integer> clients;
	
	ClientGroup() {
		clients = new HashSet<Integer>();
	}
	
	/**
	 * Adds the client ID to this set if client ID is not already present.
	 * 
	 * @param clientId - client ID
	 * @return {@code true} if this set did not already contain the client ID, {@code false} otherwise  
	 * @throws IllegalArgumentException if client ID is null
	 */
	synchronized boolean addIfAbsent(Integer clientId) throws IllegalArgumentException {
		if (clientId == null) {
			throw new IllegalArgumentException("Client ID cannot be null.");
		}
		
		return clients.add(clientId);
	}
	
	/**
	 * Removes client ID from this set. 
	 * 
	 * @param clientId - client ID
	 */
	synchronized void remove(Integer clientId) {
		if (clientId != null) {
			clients.remove(clientId);
		}
	}
}
