package org.mozzes.application.plugin.authorization;

import org.mozzes.application.common.exceptions.AuthorizationFailedException;

/**
 * The Interface AuthorizationManager is responsible for authorizing client to the server..
 */
public interface AuthorizationManager {

	/**
	 * Authorize client with the specified username and password.
	 * 
	 * @return innactivity time after which user's session should be invalidated
	 */
	public long authorize(String username, String password) throws AuthorizationFailedException;
}
