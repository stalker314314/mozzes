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

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * Scope that is based on some AbstractContext.<br>
 * In the AbstractContextScope instance is first requested from the context and then if it doesn't exists, it's created
 * and put in the context.
 * 
 * @author vita
 */
public class AbstractContextScope implements Scope {

  private final Provider<? extends AbstractContext> contextProvider;

  private final String scopeName;

  public AbstractContextScope(String scopeName, MozzesAbstractProvider<? extends AbstractContext> contextProvider) {
    this.scopeName = scopeName;
    this.contextProvider = contextProvider;
  }

  /**
   * get Provider from context if it exists otherwise create new one and put into context
   * 
   * @see Scope#scope(Key, Provider)
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
    return new Provider<T>() {
      @Override
      public T get() {
        // get the context
        AbstractContext scopeContext = contextProvider.get();
        synchronized (scopeContext) {

          // get the provider
          T t = (T) scopeContext.get(key.toString());

          // if it doesn't exists
          if (t == null) {
            t = creator.get();
            // add new one
            scopeContext.set(key.toString(), t);
          }
          return t;
        }
      }
    };
  }

  @Override
  public String toString() {
    return scopeName;
  }
}