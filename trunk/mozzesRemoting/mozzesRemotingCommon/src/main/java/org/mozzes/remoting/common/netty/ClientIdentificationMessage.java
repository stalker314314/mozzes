package org.mozzes.remoting.common.netty;

import java.io.Serializable;

/**
 * Client uses this message for identification on remoting server.
 * 
 * @author Vladimir Todorovic
 */
public final class ClientIdentificationMessage implements Serializable {

	private static final long serialVersionUID = -8446761234541725707L;

	private final Integer clientId;
	
	/**
	 * Creates identification message.
	 * 
	 * @param clientId - ID of the client
	 * @throws IllegalArgumentException if client ID is null
	 */
	public ClientIdentificationMessage(Integer clientId) throws IllegalArgumentException {
		if (clientId == null) {
			throw new IllegalArgumentException("Client ID can not be null.");
		}
		
		this.clientId = clientId;
	}
	
	/**
	 * Returns client ID.
	 * 
	 * @return ID of the client
	 */
	public Integer getClientId() {
		return this.clientId;
	}
}
