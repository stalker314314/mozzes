package org.mozzes.application.demo.mockups.services.internal;

import org.mozzes.application.server.client.*;

/**
 * This is the simple service that's configured as internal server's service so only other services can call this
 * service or {@link MozzesInternalClient}
 * 
 * @author vita
 */
public interface SimpleInternalService {

	int result = 321;

	/**
	 * @returns the {@link SimpleInternalService#result} value
	 */
	int getInteger();
}
