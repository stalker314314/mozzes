package org.mozzes.application.plugin.authorization;

import org.mozzes.application.plugin.ApplicationPlugin;

/**
 * AuthorizationPlugin is {@link ApplicationPlugin} that provides {@link AuthorizationManager} for an application.
 */
public abstract class AuthorizationPlugin extends ApplicationPlugin {
	
	public abstract Class<? extends AuthorizationManager> getAuthorizationManager();

}
