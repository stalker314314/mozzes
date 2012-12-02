/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
          logger.error("Error during scope clean-up.\nName = " + getName() + ", key = " + contextData.getKey()
              + ", value = " + contextValue, t);
        }
      }
    }
  }

}
