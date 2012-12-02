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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.common.exceptions.MozzesRuntimeException;
import org.mozzes.application.common.service.LookupService;
import org.mozzes.application.common.service.SessionService;
import org.mozzes.application.module.ApplicationModule;
import org.mozzes.application.module.DefaultApplicationModule;
import org.mozzes.application.module.ServiceConfiguration;
import org.mozzes.application.module.scope.ApplicationScoped;
import org.mozzes.application.module.scope.RequestScoped;
import org.mozzes.application.module.scope.SessionScoped;
import org.mozzes.application.module.scope.TransactionScoped;
import org.mozzes.application.plugin.ApplicationPlugin;
import org.mozzes.application.plugin.authorization.AuthorizationManager;
import org.mozzes.application.plugin.authorization.AuthorizationPlugin;
import org.mozzes.application.plugin.request.RequestProcessor;
import org.mozzes.application.plugin.transaction.TransactionManager;
import org.mozzes.application.plugin.transaction.TransactionPlugin;
import org.mozzes.application.server.authorization.impl.AuthorizationManagerImpl;
import org.mozzes.application.server.client.MozzesInternalClient;
import org.mozzes.application.server.client.MozzesLocalClientConfiguration;
import org.mozzes.application.server.internal.AbstractContextScope;
import org.mozzes.application.server.internal.MozzesInternal;
import org.mozzes.application.server.lifecycle.MozzesServerLifeCycleManager;
import org.mozzes.application.server.lifecycle.MozzesServerLifeCycleStatus;
import org.mozzes.application.server.request.RequestManager;
import org.mozzes.application.server.request.impl.RequestContext;
import org.mozzes.application.server.request.impl.RequestContextProvider;
import org.mozzes.application.server.request.impl.RequestManagerImpl;
import org.mozzes.application.server.request.impl.RequestProcessorImpl;
import org.mozzes.application.server.service.InternalLookupService;
import org.mozzes.application.server.service.ServerLifeCycleService;
import org.mozzes.application.server.service.impl.ImplementationAnnotationInfo;
import org.mozzes.application.server.service.impl.LookupServiceImpl;
import org.mozzes.application.server.service.impl.ServerLifeCycleServiceImpl;
import org.mozzes.application.server.service.impl.SessionServiceImpl;
import org.mozzes.application.server.session.SessionManager;
import org.mozzes.application.server.session.impl.SessionContext;
import org.mozzes.application.server.session.impl.SessionContextProvider;
import org.mozzes.application.server.session.impl.SessionManagerImpl;
import org.mozzes.application.server.transaction.impl.TransactionContext;
import org.mozzes.application.server.transaction.impl.TransactionContextProvider;
import org.mozzes.application.server.transaction.impl.TransactionInterceptor;
import org.mozzes.application.server.transaction.impl.TransactionManagerImpl;
import org.mozzes.application.server.transaction.impl.TransactionStack;
import org.mozzes.application.server.transaction.impl.TransactionStackProvider;
import org.mozzes.invocation.common.InvocationProxy;
import org.mozzes.invocation.common.interceptor.InvocationInterceptor;
import org.mozzes.invocation.guice.InternalInjectorProvider;
import org.mozzes.invocation.guice.ServiceChainConfiguration;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Scopes;

/**
 * The Class MozzesServerConfiguration holds all the information that is needed to start the Mozzes Server.
 */
public class MozzesServerConfiguration {

  /**
   * transaction manager is responsible for adding support for transactions(default value is initially set).
   */
  private Class<? extends TransactionManager> transactionManager = TransactionManagerImpl.class;

  /**
   * Authorization service is used for logging on the application server(default value is initially set).
   */
  private Class<? extends AuthorizationManager> authorizationManager = AuthorizationManagerImpl.class;

  /**
   * Main application module
   */
  private final DefaultApplicationModule mainApplicationModule = new DefaultApplicationModule();

  /**
   * List of all application modules
   */
  private final List<ApplicationModule> applicationModules = new ArrayList<ApplicationModule>();

  /**
   * List of interceptors which are wrapping service calls to add some additional functionality.
   */
  private final List<Class<? extends InvocationInterceptor>> globalInterceptors = new ArrayList<Class<? extends InvocationInterceptor>>();

  /**
   * List of all custom Guice modules
   */
  private final List<Module> customGuiceModules = new ArrayList<Module>();

  /**
   * Server life cycle manager, performs tasks during server startup and shutdown
   */
  private final MozzesServerLifeCycleManager serverLifeCycleManager = new MozzesServerLifeCycleManager();

  /**
   * Is this configuration applied?
   */
  private boolean configurationApplied = false;

  public MozzesServerConfiguration() {

    addService(SessionService.class, SessionServiceImpl.class);
    addInternalService(ServerLifeCycleService.class, ServerLifeCycleServiceImpl.class);
    addGlobalInterceptor(TransactionInterceptor.class);

    /* sets the default implementations that can be overridden with the setter methods */
    applicationModules.add(mainApplicationModule);
  }

  /**
   * Add new service to the list of provided server services
   * 
   * @param <I>
   *          serviceInterface that specifies service behavior.
   * 
   * @param serviceInterface
   *          Class object of serviceInterface.
   * @param serviceImplementation
   *          Class object of the class that implements serviceInterface
   */
  public <I> void addService(Class<I> serviceInterface, Class<? extends I> serviceImplementation) {
    mainApplicationModule.addService(new ServiceConfiguration<I>(serviceInterface, serviceImplementation));
  }

  public <I> void addInternalService(Class<I> serviceInterface, Class<? extends I> serviceImplementation) {
    mainApplicationModule.addService(new ServiceConfiguration<I>(serviceInterface, serviceImplementation, true));
  }

  /**
   * Add new service interceptor to the user's list of interceptors
   */
  public void addServiceInterceptor(Class<? extends InvocationInterceptor> interceptor) {
    mainApplicationModule.addServiceInterceptor(interceptor);
  }

  /**
   * Add new interceptor applied to all application modules
   */
  public void addGlobalInterceptor(Class<? extends InvocationInterceptor> interceptor) {
    globalInterceptors.add(interceptor);
  }

  /**
   * Add new application module
   */
  public void addApplicationModule(ApplicationModule applicationModule) {
    handleModule(applicationModule);
  }

  /**
   * Add new application plugin
   */
  public void addApplicationModule(ApplicationPlugin applicationPlugin) {
    handlePlugin(applicationPlugin);
  }

  /**
   * Add new application plugin
   */
  public void addApplicationModule(AuthorizationPlugin authorizationPlugin) {
    setAuthorizationManager(authorizationPlugin.getAuthorizationManager());
    handlePlugin(authorizationPlugin);
  }

  /**
   * Add new application plugin
   */
  public void addApplicationModule(TransactionPlugin transactionPlugin) {
    setTransactionManager(transactionPlugin.getTransactionManager());
    handlePlugin(transactionPlugin);
  }

  Injector createInjector() {
    checkApplied();

    final InternalInjectorProvider iip = new InternalInjectorProvider();
    Injector injector = Guice.createInjector(getModules(iip));
    iip.setInjector(injector);

    return injector;
  }

  MozzesServerLifeCycleManager getLifeCycleManager() {
    return serverLifeCycleManager;
  }

  private void setTransactionManager(Class<? extends TransactionManager> transactionManager) {
    if (transactionManager == null)
      return;

    this.transactionManager = transactionManager;
  }

  private void setAuthorizationManager(Class<? extends AuthorizationManager> authorizationManager) {
    if (authorizationManager == null)
      return;

    this.authorizationManager = authorizationManager;
  }

  private void handlePlugin(ApplicationPlugin applicationPlugin) {
    Module customGuiceModule = applicationPlugin.getCustomGuiceModule();
    if (customGuiceModule != null)
      customGuiceModules.add(customGuiceModule);

    List<Class<? extends InvocationInterceptor>> pluginGlobalInterceptors = applicationPlugin
        .getGlobalServiceInterceptors();
    if (pluginGlobalInterceptors != null)
      globalInterceptors.addAll(pluginGlobalInterceptors);

    handleModule(applicationPlugin);
  }

  private void handleModule(ApplicationModule applicationModule) {
    applicationModules.add(applicationModule);
  }

  private List<Module> getModules(final InternalInjectorProvider iip) {
    List<Module> modules = new ArrayList<Module>();
    modules.add(new MozzesServerModule(iip));
    modules.addAll(customGuiceModules);
    return modules;
  }

  private synchronized void checkApplied() {
    if (configurationApplied)
      throw new MozzesRuntimeException("Mozzes server configuration can only be applied once");

    configurationApplied = true;
  }

  /**
   * This is the main module for the Mozzes Server that's responsible for binding the core classes into the Guice
   * framework.
   */
  private class MozzesServerModule extends AbstractModule {

    /**
     * The Request manager is responsible for managing the requests in the single session
     */
    private final RequestManager requestManager;

    /**
     * session scope for configuring instance creation in the session (with google guice)
     */
    private final Scope REQUEST;

    /**
     * session scope for configuring instance creation in the session (with google guice)
     */
    private final Scope SESSION;

    /**
     * transaction scope for configuring instance creation in the session (with google guice)
     */
    private final Scope TRANSACTION;

    private final InternalInjectorProvider iip;

    private MozzesServerModule(InternalInjectorProvider iip) {
      this.iip = iip;
      requestManager = new RequestManagerImpl();
      REQUEST = new AbstractContextScope("MozzesScopes.REQUEST", new RequestContextProvider(requestManager));
      SESSION = new AbstractContextScope("MozzesScopes.SESSION", new SessionContextProvider(requestManager));
      TRANSACTION = new AbstractContextScope("MozzesScopes.TRANSACTION", new TransactionContextProvider(requestManager));
    }

    /**
     * @see AbstractModule#configure()
     */
    @Override
    protected void configure() {
      bindScope(ApplicationScoped.class, Scopes.SINGLETON);
      bindScope(SessionScoped.class, SESSION);
      bindScope(RequestScoped.class, REQUEST);
      bindScope(TransactionScoped.class, TRANSACTION);

      bind(RequestContext.class).annotatedWith(MozzesInternal.class).toProvider(
          new RequestContextProvider(requestManager));

      bind(SessionContext.class).annotatedWith(MozzesInternal.class).toProvider(
          new SessionContextProvider(requestManager));

      bind(TransactionContext.class).annotatedWith(MozzesInternal.class).toProvider(
          new TransactionContextProvider(requestManager));

      bind(TransactionStack.class).annotatedWith(MozzesInternal.class).toProvider(
          new TransactionStackProvider(requestManager));

      bind(RequestManager.class).toInstance(requestManager);
      bind(RequestProcessorImpl.class).in(Scopes.SINGLETON);

      bind(MozzesServerLifeCycleStatus.class).in(Scopes.SINGLETON);

      bind(RequestProcessor.class).to(RequestProcessorImpl.class).in(Scopes.SINGLETON);

      bind(SessionManagerImpl.class).in(Scopes.SINGLETON);
      bind(SessionManager.class).annotatedWith(MozzesInternal.class).to(SessionManagerImpl.class).in(Scopes.SINGLETON);

      bind(CountDownLatch.class).toInstance(new CountDownLatch(1));
      bind(MozzesLocalClientConfiguration.class).in(Scopes.SINGLETON);
      bind(MozzesClient.class).toProvider(MozzesLocalClientProvider.class);

      bind(InternalInjectorProvider.class).toInstance(iip);
      bind(TransactionManager.class).to(transactionManager).in(REQUEST);
      bind(AuthorizationManager.class).to(authorizationManager);

      LookupServiceImpl lookupService = new LookupServiceImpl();
      ImplementationAnnotationInfo implementationAnnotationInfo = new ImplementationAnnotationInfo();
      Binder binder = binder();
      for (ApplicationModule module : applicationModules)
        bindModule(binder, module, lookupService, implementationAnnotationInfo);

      bind(LookupService.class).toInstance(lookupService);
      bind(InternalLookupService.class).toInstance(lookupService);
      bind(MozzesInternalClient.class).in(Scopes.SINGLETON);
      bind(ImplementationAnnotationInfo.class).toInstance(implementationAnnotationInfo);
    }

    private void bindModule(Binder binder, ApplicationModule module, LookupServiceImpl lookupService,
        ImplementationAnnotationInfo implementationAnnotationInfo) {
      /* do bindings for services */
      List<ServiceConfiguration<?>> services = module.getServices();

      if (services != null) {
        final ServiceChainConfiguration scc = new ServiceChainConfiguration(iip).addInterceptors(globalInterceptors)
            .addInterceptors(module.getServiceInterceptors());

        // for every service do the binding to the service implementation provider
        for (ServiceConfiguration<?> service : services) {
          bindService(binder, service, scc);
          implementationAnnotationInfo.addImplementationAnnotationInfo(service);

          /* if the service is internal service, add to the list of internal services */
          if (service.isInternal())
            lookupService.addInternalService(service.getServiceInterface());

          /* if the service is regular service, add to the list of regular services exposed to the clients */
          else
            lookupService.addService(service.getServiceInterface().getName());
        }
      }

      module.doCustomBinding(binder);
      serverLifeCycleManager.addServerListeners(module.getServerListeners());
    }

    private <I> void bindService(Binder binder, ServiceConfiguration<I> serviceConfig, ServiceChainConfiguration scc) {
      Key<? extends I> serviceImplementationKey = serviceConfig.bind(binder);
      Class<I> serviceInterface = serviceConfig.getServiceInterface();

      binder.bind(serviceInterface).toProvider(new ServiceProvider<I>(serviceInterface, serviceImplementationKey, scc));
    }
  }

  /**
   * This class is provider for the specified interface I that provides intercepted implementation
   */
  private static class ServiceProvider<I> implements Provider<I> {

    private final Class<I> serviceInterface;

    private final Key<? extends I> serviceImplementationKey;

    private final ServiceChainConfiguration scc;

    ServiceProvider(Class<I> serviceInterface, Key<? extends I> serviceImplementationKey, ServiceChainConfiguration scc) {
      this.serviceInterface = serviceInterface;
      this.serviceImplementationKey = serviceImplementationKey;
      this.scc = scc;
    }

    /*
     * here we make interceptor chain but because chain is provided with the serviceImplementationKey target will be
     * created lazy in the last interceptor call. So the target implementation can access all the contexts that are
     * available
     * 
     * @see Provider#get()
     */
    @Override
    public I get() {
      return InvocationProxy.newInstance(serviceInterface, scc.create(serviceImplementationKey));
    }
  }

  /**
   * The Class MozzesLocalClientProvider implements {@link Provider} for {@link MozzesClient}s.
   */
  private static class MozzesLocalClientProvider implements Provider<MozzesClient> {

    /** google guice {@link Injector} */
    private final Injector injector;

    @Inject
    MozzesLocalClientProvider(Injector injector) {
      this.injector = injector;
    }

    /**
     * @see Provider#get()
     */
    @Override
    public MozzesClient get() {
      return new MozzesClient(injector.getInstance(MozzesLocalClientConfiguration.class));
    }
  }
}
