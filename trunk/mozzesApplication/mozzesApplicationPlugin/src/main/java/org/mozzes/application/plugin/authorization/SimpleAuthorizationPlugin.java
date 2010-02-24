package org.mozzes.application.plugin.authorization;

/**
 * Simple {@link AuthorizationManager} holder.
 */
public class SimpleAuthorizationPlugin extends AuthorizationPlugin {
	
	private final Class<? extends AuthorizationManager> authorizationManager;
	
	public SimpleAuthorizationPlugin(Class<? extends AuthorizationManager> authorizationManager) {
		super();
		this.authorizationManager = authorizationManager;
	}

	@Override
	public Class<? extends AuthorizationManager> getAuthorizationManager() {
		return authorizationManager;
	}

}
