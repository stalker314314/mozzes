package org.mozzes.application.plugin.transaction;

import org.mozzes.application.plugin.ApplicationPlugin;

/**
 * AuthorizationPlugin is {@link ApplicationPlugin} that provides {@link TransactionManager} for an application.
 */
public abstract class TransactionPlugin extends ApplicationPlugin {
	
	public abstract Class<? extends TransactionManager> getTransactionManager();

}
