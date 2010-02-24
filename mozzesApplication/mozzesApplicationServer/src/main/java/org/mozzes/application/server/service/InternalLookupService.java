package org.mozzes.application.server.service;

import java.util.List;

/**
 * The Interface LookupService is responsible for getting list of server's services available to the client.
 */
public interface InternalLookupService {

	/**
	 * @return list of server's services available only to the client.
	 */
	List<Class<?>> getInternalServices();
}
