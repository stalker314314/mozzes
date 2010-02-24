package org.mozzes.application.common.service;

import java.util.List;

/**
 * The Interface LookupService is responsible for getting list of server's services available to the client.
 */
public interface LookupService {

	/**
	 * @return list of server's services available to the client.
	 */
	List<String> getServices();
}
