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
package org.mozzes.application.server;

import java.util.concurrent.CountDownLatch;

import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.module.ServerInitializationException;
import org.mozzes.application.server.client.MozzesInternalClient;
import org.mozzes.application.server.lifecycle.MozzesServerLifeCycleManager;
import org.mozzes.application.server.lifecycle.MozzesServerLifeCycleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

/**
 * The Class MozzesServer is the main class for the mozzes application server.
 */
public class MozzesServer {

  private static final Logger logger = LoggerFactory.getLogger(MozzesServer.class);

  /**
   * The google guice injector that's responsible for creating instances of server objects.
   * 
   * @see <a
   *      href="http://google-guice.googlecode.com/svn/trunk/javadoc/index.html?com/google/inject/Injector.html">Injector
   *      API</a>
   */
  protected final Injector injector;

  protected final MozzesServerLifeCycleManager lifeCycleManager;

  public MozzesServer(MozzesServerConfiguration config) {
    injector = config.createInjector();
    lifeCycleManager = config.getLifeCycleManager();
  }

  /**
   * Starts the server.
   */
  public void start() {
    try {
      lifeCycleManager.start(getInternalClient());
      logger.info("Server startup finished");

      injector.getInstance(MozzesServerLifeCycleStatus.class).setStarted();

    } catch (ServerInitializationException e) {
      logger.error("Server startup failed", e);
    }
  }

  public void start(CountDownLatch signal) {
    try {
      lifeCycleManager.start(getInternalClient());
      logger.info("Server startup finished");
      signal.countDown();
      injector.getInstance(MozzesServerLifeCycleStatus.class).setStarted();

    } catch (ServerInitializationException e) {
      logger.error("Server startup failed", e);
    }
  }

  /**
   * Stop the server
   */
  public void stop() {
    lifeCycleManager.stop(getInternalClient());
    logger.info("Server shutdown finished");
  }

  public void stop(CountDownLatch signal) {
    lifeCycleManager.stop(getInternalClient());
    logger.info("Server shutdown finished");
    signal.countDown();
  }

  /**
   * @return the client that runs internally in the server itself.
   */
  public MozzesClient getLocalClient() {
    return injector.getInstance(MozzesClient.class);
  }

  /**
   * @return the client that runs internally in the server itself.
   */
  public MozzesInternalClient getInternalClient() {
    return injector.getInstance(MozzesInternalClient.class);
  }

  /**
   * @see <a
   *      href="http://google-guice.googlecode.com/svn/trunk/javadoc/index.html?com/google/inject/Injector.html">Injector
   *      API</a>
   */
  public <I> I getInstance(Class<I> clazz) {
    return injector.getInstance(clazz);
  }
}
