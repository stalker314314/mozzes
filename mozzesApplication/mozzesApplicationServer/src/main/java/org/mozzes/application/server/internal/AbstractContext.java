package org.mozzes.application.server.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.mozzes.application.module.scope.ScopeCleanUp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class AbstractContext. <br>
 * All Context implementations extends this abstract context.
 */
public abstract class AbstractContext implements ScopeCleanUp {

	private static final Logger logger = LoggerFactory.getLogger(AbstractContext.class);

	/** data that's associated with the context. */
	private final Map<String, Object> data = new HashMap<String, Object>();

	/**
	 * Get data from the context for the given key.
	 */
	public Object get(String key) {
		return data.get(key);
	}

	/**
	 * Set some value in the context's data for the given key.
	 */
	public Object set(String key, Object value) {
		return data.put(key, value);
	}

	/**
	 * @return Context name used for logging
	 */
	protected abstract String getName();

	@Override
	public void scopeCleanUp() {
		for (Entry<String, Object> contextData : data.entrySet()) {
			Object contextValue = contextData.getValue();
			if ((contextValue != null) && (contextValue instanceof ScopeCleanUp)) {
				try {
					((ScopeCleanUp) contextValue).scopeCleanUp();
				} catch (Throwable t) {
					logger.error("Error during scope clean-up.\nName = " + getName() + ", key = "
							+ contextData.getKey() + ", value = " + contextValue, t);
				}
			}
		}
	}

}
