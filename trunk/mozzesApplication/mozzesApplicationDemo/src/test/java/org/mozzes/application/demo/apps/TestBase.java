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
package org.mozzes.application.demo.apps;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.demo.mockups.MAuthorizationManager;
import org.mozzes.application.demo.mockups.MTransactionManager;
import org.mozzes.application.demo.mockups.services.basic.BasicService;
import org.mozzes.application.demo.mockups.services.basic.ServiceThatInjectOtherService;
import org.mozzes.application.demo.mockups.services.basic.ServiceThatThrowsExceptions;
import org.mozzes.application.demo.mockups.services.basic.impl.BasicServiceImpl;
import org.mozzes.application.demo.mockups.services.basic.impl.ServiceThatInjectOtherServiceImpl;
import org.mozzes.application.demo.mockups.services.basic.impl.ServiceThatThrowsExceptionsImpl;
import org.mozzes.application.demo.mockups.services.internal.PublicServiceCallingInternal;
import org.mozzes.application.demo.mockups.services.internal.SimpleInternalService;
import org.mozzes.application.demo.mockups.services.internal.impl.PublicServiceCallingInternalImpl;
import org.mozzes.application.demo.mockups.services.internal.impl.SimpleInternalServiceImpl;
import org.mozzes.application.demo.mockups.services.scoped.request.RequestScopedService;
import org.mozzes.application.demo.mockups.services.scoped.request.RequestScopedService2;
import org.mozzes.application.demo.mockups.services.scoped.request.RequestScopedService3;
import org.mozzes.application.demo.mockups.services.scoped.request.impl.RequestScopedService2Impl;
import org.mozzes.application.demo.mockups.services.scoped.request.impl.RequestScopedService3Impl;
import org.mozzes.application.demo.mockups.services.scoped.request.impl.RequestScopedServiceImpl;
import org.mozzes.application.demo.mockups.services.scoped.session.SessionScopedService;
import org.mozzes.application.demo.mockups.services.scoped.session.impl.SessionScopedServiceImpl;
import org.mozzes.application.demo.mockups.services.scopedata.ServiceWithRequestData;
import org.mozzes.application.demo.mockups.services.scopedata.ServiceWithRequestData2;
import org.mozzes.application.demo.mockups.services.scopedata.ServiceWithSessionData;
import org.mozzes.application.demo.mockups.services.scopedata.ServiceWithTransactionData;
import org.mozzes.application.demo.mockups.services.scopedata.ServiceWithTransactionDataNewTransaction;
import org.mozzes.application.demo.mockups.services.scopedata.impl.ServiceWithRequestData2Impl;
import org.mozzes.application.demo.mockups.services.scopedata.impl.ServiceWithRequestDataImpl;
import org.mozzes.application.demo.mockups.services.scopedata.impl.ServiceWithSessionDataImpl;
import org.mozzes.application.demo.mockups.services.scopedata.impl.ServiceWithTransactionDataImpl;
import org.mozzes.application.demo.mockups.services.scopedata.impl.ServiceWithTransactionDataNewTransactionImpl;
import org.mozzes.application.plugin.authorization.SimpleAuthorizationPlugin;
import org.mozzes.application.plugin.transaction.SimpleTransactionPlugion;
import org.mozzes.application.remoting.client.RemoteClientConfiguration;
import org.mozzes.application.remoting.server.RemotingPlugin;
import org.mozzes.application.server.MozzesServer;
import org.mozzes.application.server.MozzesServerConfiguration;
import org.mozzes.remoting.common.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base Testing class every other test class extends this class.
 * 
 * Before starting every test we're configuring and starting the server.
 * 
 * @author vita
 */
public abstract class TestBase {

  protected static final Logger logger = LoggerFactory.getLogger(TestBase.class);

  protected MozzesServer server;

  protected MozzesServerConfiguration serverConfig;

  protected final String validUsername1 = "chuck";

  protected final String validPassword1 = "norris";

  protected final String validUsername2 = "prabhu";

  protected final String validPassword2 = "deva";

  protected final String wrongUsername = "vita";

  protected final String wrongPassword = "vita!";

  /**
   * This method is executed before every test method.
   */
  @Before
  public void beforeTest() {
    MAuthorizationManager.isUserLogged = false;
    MAuthorizationManager.addValidCredentials(validUsername1, validPassword1);
    MAuthorizationManager.addValidCredentials(validUsername2, validPassword2);
    server = new MozzesServer(createServerConfiguration());

    CountDownLatch signal = new CountDownLatch(1);
    server.start(signal);
    try {
      signal.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is executed after every test method
   */
  @After
  public void afterTest() {
    CountDownLatch signal = new CountDownLatch(1);
    server.stop(signal);
    try {
      signal.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * @return server configuration that is needed for running the server
   */
  @SuppressWarnings("unchecked")
  protected MozzesServerConfiguration createServerConfiguration() {

    serverConfig = new MozzesServerConfiguration();

    /* adding custom module that enables remote client logging */
    serverConfig.addApplicationModule(new RemotingPlugin(7890));

    /* setting up the application service implementations */
    serverConfig.addApplicationModule(new SimpleAuthorizationPlugin(MAuthorizationManager.class));
    serverConfig.addApplicationModule(new SimpleTransactionPlugion(MTransactionManager.class));

    /* adding internal services to the server */
    for (Class interfaceClass : getInternalServices().keySet())
      serverConfig.addInternalService(interfaceClass, getInternalServices().get(interfaceClass));

    /* adding services to the server */
    for (Class interfaceClass : getServices().keySet())
      serverConfig.addService(interfaceClass, getServices().get(interfaceClass));

    return serverConfig;
  }

  /**
   * If some test case should override the implementation of some interface it should override this method.
   */
  protected HashMap<Class<?>, Class<?>> getInternalServices() {
    HashMap<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
    map.put(SimpleInternalService.class, SimpleInternalServiceImpl.class);
    return map;
  }

  /**
   * If some test case should override the implementation of some interface it should override this method.
   */
  protected HashMap<Class<?>, Class<?>> getServices() {
    HashMap<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();

    // basic
    map.put(BasicService.class, BasicServiceImpl.class);
    map.put(ServiceThatInjectOtherService.class, ServiceThatInjectOtherServiceImpl.class);
    map.put(ServiceThatThrowsExceptions.class, ServiceThatThrowsExceptionsImpl.class);

    // scoped
    map.put(SessionScopedService.class, SessionScopedServiceImpl.class);

    map.put(RequestScopedService.class, RequestScopedServiceImpl.class);
    map.put(RequestScopedService2.class, RequestScopedService2Impl.class);
    map.put(RequestScopedService3.class, RequestScopedService3Impl.class);

    // scope data
    map.put(ServiceWithRequestData2.class, ServiceWithRequestData2Impl.class);
    map.put(ServiceWithRequestData.class, ServiceWithRequestDataImpl.class);
    map.put(ServiceWithSessionData.class, ServiceWithSessionDataImpl.class);
    map.put(ServiceWithTransactionData.class, ServiceWithTransactionDataImpl.class);
    map.put(ServiceWithTransactionDataNewTransaction.class, ServiceWithTransactionDataNewTransactionImpl.class);

    map.put(PublicServiceCallingInternal.class, PublicServiceCallingInternalImpl.class);

    return map;
  }

  /**
   * @return the server used in the test cases.
   */
  public MozzesServer getServer() {
    return server;
  }

  /**
   * Method for getting the client. In typical test case we're using the remote client and if we need local client in
   * some use case we can override isClientLocal() method
   */
  protected MozzesClient getClient() {
    if (isClientLocal())
      return server.getLocalClient();

    try {
      return new MozzesClient(new RemoteClientConfiguration("localhost", 7890, true));
    } catch (RemotingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * This method should be overridden if specific test case is using remote clients
   * 
   * @return true if in the test should be used local client or false if remote client should be used
   */
  protected boolean isClientLocal() {
    return false;
  }
}
