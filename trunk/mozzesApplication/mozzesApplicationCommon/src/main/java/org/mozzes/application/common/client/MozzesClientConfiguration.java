package org.mozzes.application.common.client;

import java.util.ArrayList;
import java.util.List;

import org.mozzes.application.common.service.LookupService;
import org.mozzes.application.common.session.SessionIdProvider;
import org.mozzes.invocation.common.InvocationProxy;
import org.mozzes.invocation.common.handler.InvocationHandler;
import org.mozzes.invocation.common.interceptor.InvocationInterceptor;
import org.mozzes.invocation.guice.InternalInjectorProvider;
import org.mozzes.invocation.guice.ServiceChainConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;

/**
 * The Class MozzesClientConfiguration is used to configure the GoogleGuice framework on the client side and to
 * configure {@link Injector} on the client side
 */
public abstract class MozzesClientConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(MozzesClientConfiguration.class);
	
	/** Interceptors that will be called when client executes server's service methods. */
	protected final List<Class<? extends InvocationInterceptor>> userInterceptors = new ArrayList<Class<? extends InvocationInterceptor>>();

	/**
	 * adds new interceptor that will wrap around client calls to the server service methods
	 */
	public final void addServiceInterceptor(Class<? extends InvocationInterceptor> interceptor) {
		userInterceptors.add(interceptor);
	}

	protected List<String> getServices(Injector injector) {
		return injector.getInstance(LookupService.class).getServices();
	}

	/**
	 * Creates injector for the provided sessionId. Injector is configured with the available server's services.
	 */
	protected Injector createInjector(SessionIdProvider sessionIDProvider, List<Module> customModules) {

		// first we need to create "partial" injector to reach the server and see what services are available
		AbstractModule defaultModule = createDefaultModule(sessionIDProvider);

		// now create empty injector provider
		InternalInjectorProvider injectorProvider = new InternalInjectorProvider();

		// create interceptor configuration
		ServiceChainConfiguration serviceChainConfiguration = new ServiceChainConfiguration(injectorProvider);
		serviceChainConfiguration.addInterceptors(userInterceptors);

		List<Module> allModules = new ArrayList<Module>(customModules.size() + 2);

		allModules.add(defaultModule);
		allModules.add(new UserModule(getServices(Guice.createInjector(defaultModule)), sessionIDProvider,
				serviceChainConfiguration));

		allModules.addAll(customModules);

		// create full injector with userModule that's binding server's services and interceptors
		Injector returnValue = Guice.createInjector(allModules);

		// injectorProvider is now configured properly
		injectorProvider.setInjector(returnValue);

		// now we can return fully configured injector
		return returnValue;
	}

	/**
	 * This is the extension point for the subclasses that specifies real implementators of invocationInterface for the
	 * specified sessionId
	 */
	protected abstract <I> InvocationHandler<I> getInvocationHandler(Class<I> invocationInterface,
			SessionIdProvider sessionIDProvider);

	/**
	 * Binds core Interfaces to their implementations
	 */
	protected AbstractModule createDefaultModule(final SessionIdProvider sessionIDProvider) {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(SessionIdProvider.class).toInstance(sessionIDProvider);
				bind(LookupService.class).toProvider(getImplementationProvider(LookupService.class, sessionIDProvider));
			}
		};
	}

	/**
	 * Returns the object responsible for creating implementations of the invocationInterface.
	 */
	protected <I> Provider<I> getImplementationProvider(Class<I> invocationInterface,
			SessionIdProvider sessionIDProvider) {

		return new InvocationProxyProvider<I>(invocationInterface, getInvocationHandler(invocationInterface,
				sessionIDProvider));
	}

	/**
	 * The Class InvocationProxyProvider Provides {@link InvocationProxy} for interface I.
	 * 
	 * @see <a
	 *      href="http://google-guice.googlecode.com/svn/trunk/javadoc/index.html?com/google/inject/Provider.html">Provider
	 *      API</a>
	 * 
	 * @author Perica Milosevic
	 */
	private class InvocationProxyProvider<I> implements Provider<I> {

		/** The invocation interface. */
		private final Class<I> invocationInterface;

		/** The invocation interface implementation. */
		private final InvocationHandler<I> invocationHandler;

		public InvocationProxyProvider(Class<I> invocationInterface, InvocationHandler<I> invocationHandler) {
			this.invocationInterface = invocationInterface;
			this.invocationHandler = invocationHandler;
		}

		/*
		 * @see Provider#get()
		 */
		@Override
		public I get() {
			return InvocationProxy.newInstance(invocationInterface, invocationHandler);
		}
	}

	/**
	 * Configures GoogleGuice injector for the specified sessionId(sessionIdProvider) with list of services and
	 * interceptor chain.
	 */
	protected class UserModule extends AbstractModule {
		/** The services. */
		private final List<String> services;
		private final SessionIdProvider sessionIDProvider;
		private final ServiceChainConfiguration serviceChainConfig;

		public UserModule(List<String> services, SessionIdProvider sessionIDProvider,
							ServiceChainConfiguration serviceChainConfig) {
			this.services = services;
			this.sessionIDProvider = sessionIDProvider;
			this.serviceChainConfig = serviceChainConfig;
		}

		/*
		 * go thru the services and bind them to the intercepted implementations
		 * 
		 * @see AbstractModule#configure()
		 */
		@Override
		protected void configure() {
			Binder binder = binder();
			for (String service : services){
				try {
					Class<?> clazz = Class.forName(service);
					bindService(binder, clazz);
				} catch(LinkageError e){
					logger.warn("Class " + service + " not found, binding will NOT be done", e);
				} catch (ClassNotFoundException e){
					logger.warn("Class " + service + " not found, binding will NOT be done", e);
				}
			}
		}

		private <I> void bindService(Binder binder, Class<I> invocationInterface) {
			binder.bind(invocationInterface).toProvider(getIIP(invocationInterface));
		}

		/**
		 * @return the implementation(actually implementation provider) for the invocationInterface that's wrapping the
		 *         service call with the interceptor chain specified in the interceptorChainConfig.<br>
		 */
		private <I> InterceptedImplementationProvider<I> getIIP(Class<I> invocationInterface) {
			return new InterceptedImplementationProvider<I>(invocationInterface, serviceChainConfig,
					new InvocationHandlerProvider<I>(invocationInterface, sessionIDProvider));
		}

	}

	/**
	 * Provides implementation for the interface I that's configured with all interceptors.<br>
	 * Basically it returns proxy instance that returns for the interface I implementation that consists of the
	 * interceptor chain and then interceptor chain calls real implementation after wrapping with interceptors.
	 */
	private class InterceptedImplementationProvider<I> implements Provider<I> {

		/** The invocation interface. */
		private final Class<I> invocationInterface;

		/** The handler provider. */
		private final InvocationHandlerProvider<I> handlerProvider;

		/** The interceptorChainConfig. */
		private final ServiceChainConfiguration serviceChainConfig;

		/**
		 * Instantiates a new intercepted implementation provider.
		 */
		InterceptedImplementationProvider(Class<I> invocationInterface, ServiceChainConfiguration serviceChainConfig,
											InvocationHandlerProvider<I> handlerProvider) {

			this.invocationInterface = invocationInterface;
			this.handlerProvider = handlerProvider;
			this.serviceChainConfig = serviceChainConfig;
		}

		/*
		 * @see Provider#get()
		 */
		@Override
		public I get() {
			return InvocationProxy.newInstance(invocationInterface, serviceChainConfig.create(handlerProvider.get()));
		}
	}

	/**
	 * Provides invocation for the specified interface.<br>
	 * This class is using abstract method from {@link MozzesClientConfiguration} so specific
	 * {@link MozzesClientConfiguration} will implement this method and thus provide implementations for server's
	 * services.
	 */
	private class InvocationHandlerProvider<I> implements Provider<InvocationHandler<I>> {

		/** The invocation class. */
		private final Class<I> invocationClass;

		/** The session ID provider. */
		private final SessionIdProvider sessionIDProvider;

		/**
		 * Instantiates a new invocation handler provider.
		 */
		public InvocationHandlerProvider(Class<I> invocationClass, SessionIdProvider sessionIDProvider) {
			this.invocationClass = invocationClass;
			this.sessionIDProvider = sessionIDProvider;
		}

		/*
		 * @see Provider#get()
		 */
		@Override
		public InvocationHandler<I> get() {
			return getInvocationHandler(invocationClass, sessionIDProvider);
		}
	}
}
