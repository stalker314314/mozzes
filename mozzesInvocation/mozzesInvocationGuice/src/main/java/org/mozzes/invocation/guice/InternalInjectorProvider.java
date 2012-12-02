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
package org.mozzes.invocation.guice;

import com.google.inject.Injector;
import com.google.inject.Provider;

/**
 * The Class InternalInjectorProvider holds the reference to the Guice {@link Injector} class so the reference to the
 * injector is "lazy loaded" and the internal service reference can be made.
 * 
 * @see <a href="http://google-guice.googlecode.com/svn/trunk/javadoc/index.html?com/google/inject/Injector.html">Google
 *      Guice</a>
 */
public class InternalInjectorProvider implements Provider<Injector> {

  /**
   * The injector.
   * 
   * @see <a
   *      href="http://google-guice.googlecode.com/svn/trunk/javadoc/index.html?com/google/inject/Injector.html">Injector
   *      API</a>
   */
  private Injector injector;

  /*
   * @see Provider#get()
   */
  @Override
  public Injector get() {
    return injector;
  }

  public void setInjector(Injector injector) {
    this.injector = injector;
  }
}
