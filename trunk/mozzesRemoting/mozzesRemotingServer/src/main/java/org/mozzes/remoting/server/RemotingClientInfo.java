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
import java.util.Set;

/**
 * Provides info about remote client during execution of requested action. 
 */
public class RemotingClientInfo {
	
	private static final ThreadLocal<String> IP_ADDRESS = new ThreadLocal<String>();
	private static final ThreadLocal<String> CLIENT_ID = new ThreadLocal<String>();
	
	private static final Set<String> ACTIVE_CLIENTS = new HashSet<String>();
	
	public static String getIpAddress() {
		return IP_ADDRESS.get();
	}
	
	public static String getClientId() {
		return CLIENT_ID.get();
	}
	
	public static Set<String> getActiveClients() {
		Set<String> returnValue = new HashSet<String>();
		synchronized (ACTIVE_CLIENTS) {
			returnValue.addAll(ACTIVE_CLIENTS);
		}
		return returnValue;
	}
	
	static void setIpAddress(String ipAddress) {
		IP_ADDRESS.set(ipAddress);
		CLIENT_ID.set(Thread.currentThread().getName());
	}
	
	static void addClient(String client) {
		synchronized (ACTIVE_CLIENTS) {
			ACTIVE_CLIENTS.add(client);
		}
	}
	
	static void removeClient(String client) {
		synchronized (ACTIVE_CLIENTS) {
			ACTIVE_CLIENTS.remove(client);
		}
	}

}
 