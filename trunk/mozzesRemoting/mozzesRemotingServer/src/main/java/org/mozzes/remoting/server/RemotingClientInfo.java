package org.mozzes.remoting.server;

/**
 * Provides info about remote client during execution of requested action. 
 */
public class RemotingClientInfo {
	
	private static final ThreadLocal<String> IP_ADDRESS = new ThreadLocal<String>();
	
	public static String getIpAddress() {
		return IP_ADDRESS.get();
	}
	
	static void setIpAddress(String ipAddress) {
		IP_ADDRESS.set(ipAddress);
	}

}
 