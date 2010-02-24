package org.mozzes.application.module.scope;

/**
 * If you have to perform some tasks just before your instance is removed from the scope then you should implement this
 * interface in scoped class.
 */
public interface ScopeCleanUp {

	/**
	 * This method is called just before some instance is removed from the scope.
	 */
	public void scopeCleanUp();

}
