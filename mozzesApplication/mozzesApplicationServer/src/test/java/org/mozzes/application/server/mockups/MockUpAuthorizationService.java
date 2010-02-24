package org.mozzes.application.server.mockups;

import org.mozzes.application.common.exceptions.AuthorizationFailedException;
import org.mozzes.application.plugin.authorization.AuthorizationManager;


public class MockUpAuthorizationService implements AuthorizationManager {

	private boolean authorize = true;

	public void setAuthorize(boolean authorize) {
		this.authorize = authorize;
	}

	@Override
	public long authorize(String username, String password) throws AuthorizationFailedException {
		if (authorize == false)
			throw new AuthorizationFailedException();
		
		return Long.MAX_VALUE;
	}
}
