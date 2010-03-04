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

import java.util.LinkedList;
import java.util.List;

import org.mozzes.invocation.common.Invocation;
import org.mozzes.invocation.common.handler.InvocationHandler;
import org.mozzes.invocation.common.interceptor.InvocationInterceptor;
import org.mozzes.invocation.common.interceptor.InvocationInterceptorChain;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;

/**
 * The Class InterceptorChainConfiguration adds interceptors.
 * 
 * @see InvocationInterceptor
 * @see InternalInjectorProvider
 */
public class ServiceChainConfiguration {

	/** List of invocation interceptors currently added to interceptor chain */
	private final List<Class<? extends InvocationInterceptor>> interceptors = new LinkedList<Class<? extends InvocationInterceptor>>();

	/** {@link Provider} for the Guice {@link Injector} */
	private final Provider<Injector> injectorProvider;

	public ServiceChainConfiguration(Provider<Injector> injectorProvider) {
		this.injectorProvider = injectorProvider;
	}

	/**
	 * Add interceptors to the chain
	 */
	public ServiceChainConfiguration addInterceptors(final List<Class<? extends InvocationInterceptor>> interceptorList) {
		if(interceptorList!=null)
			this.interceptors.addAll(interceptorList);
		return this;
	}

	/**
	 * Create new invocation chain(with already specified interceptors) for the provided Key
	 */
	public <I> InvocationInterceptorChain<I> create(Key<? extends I> implementationKey) {
		return create(getInvocationHandler(implementationKey));
	}

	/**
	 * Create new invocation chain(with already specified interceptors) for the provided implementation class
	 */
	public <I> InvocationInterceptorChain<I> create(Class<? extends I> implementationClass) {
		return create(getInvocationHandler(implementationClass));
	}

	/**
	 * Create new invocation chain(with already specified interceptors) for the provided invocation handler
	 */
	public <I> InvocationInterceptorChain<I> create(InvocationHandler<? extends I> invocationHandler) {
		InvocationInterceptorChain<I> returnValue = new InvocationInterceptorChain<I>(invocationHandler);

		for (Class<? extends InvocationInterceptor> interceptorClass : interceptors)
			returnValue.add(injectorProvider.get().getInstance(interceptorClass));

		return returnValue;
	}

	private <I> InvocationHandler<I> getInvocationHandler(Key<? extends I> implementationKey) {
		return new ServiceKeyHandler<I>(injectorProvider, implementationKey);
	}

	private <I> InvocationHandler<I> getInvocationHandler(Class<? extends I> implementationClass) {
		return new ServiceClassHandler<I>(injectorProvider, implementationClass);
	}

	private abstract static class ServiceHandler<I> implements InvocationHandler<I> {

		protected final Provider<Injector> injectorProvider;
		private I implementationInstance;

		protected ServiceHandler(Provider<Injector> injectorProvider) {
			this.injectorProvider = injectorProvider;
		}

		@Override
		public final Object invoke(Invocation<? super I> invocation) throws Throwable {
			return invocation.invoke(getImplementationInstance());
		}

		protected abstract I createImplementationInstance();

		private I getImplementationInstance() {
			if (implementationInstance == null)
				implementationInstance = createImplementationInstance();

			return implementationInstance;
		}
	}

	private static class ServiceKeyHandler<I> extends ServiceHandler<I> {

		private final Key<? extends I> implementationKey;

		ServiceKeyHandler(Provider<Injector> injectorProvider, Key<? extends I> implementationKey) {
			super(injectorProvider);
			this.implementationKey = implementationKey;
		}

		@Override
		protected I createImplementationInstance() {
			return injectorProvider.get().getInstance(implementationKey);
		}
	}

	private static class ServiceClassHandler<I> extends ServiceHandler<I> {

		private final Class<? extends I> implementationClass;

		ServiceClassHandler(Provider<Injector> injectorProvider, Class<? extends I> implementationClass) {
			super(injectorProvider);
			this.implementationClass = implementationClass;
		}

		@Override
		protected I createImplementationInstance() {
			return injectorProvider.get().getInstance(implementationClass);
		}
	}

}
