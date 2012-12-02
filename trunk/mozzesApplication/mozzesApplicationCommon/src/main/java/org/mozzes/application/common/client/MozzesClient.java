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
package org.mozzes.application.common.client;

import java.util.ArrayList;
import java.util.List;

import org.mozzes.application.common.exceptions.AuthorizationFailedException;
import org.mozzes.application.common.service.SessionService;
import org.mozzes.application.common.session.SessionIdProvider;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;

/**
 * The Class MozzesClient is the class that act's as a link to the service implementations on the server's side. <br>
 * User just calls {@link #getService(Class)} method when he needs to execute something on the server.
 * 
 * @see <a href="http://code.google.com/p/google-guice/">Google Guice</a>
 * @see <a
 *      href="http://google-guice.googlecode.com/svn/trunk/javadoc/index.html?com/google/inject/Injector.html">Injector
 *      API</a>
 */
public class MozzesClient implements SessionIdProvider {

  private String sessionId;

  private final Injector injector;

  /**
   * Instantiates a new mozzes client based on provided configuration.
   */
  public MozzesClient(MozzesClientConfiguration clientConfiguration) {
    this(clientConfiguration, new ArrayList<Module>());
  }

  /**
   * Instantiates a new mozzes client based on provided configuration with additional custom guice modules.
   */
  public MozzesClient(MozzesClientConfiguration clientConfiguration, List<Module> customModules) {
    injector = clientConfiguration.createInjector(this, customModules);
  }

  /**
   * Method for logging user on the server.
   * 
   * @see SessionService#login(String, String)
   */
  public void login(String username, String password) throws AuthorizationFailedException {
    sessionId = getService(SessionService.class).login(username, password);
  }

  /**
   * @see SessionService#logout()
   */
  public void logout() {
    getService(SessionService.class).logout();
    sessionId = null;
  }

  /**
   * @see <a
   *      href="http://google-guice.googlecode.com/svn/trunk/javadoc/index.html?com/google/inject/Injector.html">Injector
   *      API</a>
   */
  public final <I> I getService(Class<I> serviceInterface) {
    return injector.getInstance(serviceInterface);
  }

  /**
   * @see <a
   *      href="http://google-guice.googlecode.com/svn/trunk/javadoc/index.html?com/google/inject/Injector.html">Injector
   *      API</a>
   */
  public final void injectServices(Object o) {
    injector.injectMembers(o);
  }

  /**
   * @see <a
   *      href="http://google-guice.googlecode.com/svn/trunk/javadoc/index.html?com/google/inject/Injector.html">Injector
   *      API</a>
   */
  public final <I> Provider<I> getServiceProvider(Class<I> serviceInterface) {
    return injector.getProvider(serviceInterface);
  }

  /*
   * @see SessionIdProvider#getSessionId()
   */
  @Override
  public String getSessionId() {
    return sessionId;
  }

  protected void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
}
