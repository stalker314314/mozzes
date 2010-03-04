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
package org.mozzes.application.server.client;

import java.util.ArrayList;
import java.util.List;

import org.mozzes.application.common.client.MozzesClientConfiguration;
import org.mozzes.application.common.session.SessionIdProvider;
import org.mozzes.application.plugin.request.RequestProcessor;
import org.mozzes.application.server.service.InternalLookupService;
import org.mozzes.invocation.common.handler.InvocationHandler;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * The Class MozzesLocalClientConfiguration is responsible for configuring the local mozzes client running on the mozzes
 * server.
 */
public class MozzesInternalClientConfiguration extends MozzesClientConfiguration {

	private final RequestProcessor requestProcessor;

	@Inject
	MozzesInternalClientConfiguration(RequestProcessor requestProcessor) {
		this.requestProcessor = requestProcessor;
	}

	/**
	 * @see MozzesClientConfiguration#getServices(com.google.inject.Injector)
	 */
	@Override
	protected List<String> getServices(Injector injector) {
		List<Class<?>> internalServices = injector.getInstance(InternalLookupService.class).getInternalServices();
		List<String> list = new ArrayList<String>();
		for (Class<?> clazz : internalServices) {
			list.add(clazz.getName());
		}
		return list;
	}

	/**
	 * Binds core Interfaces to their implementations
	 */
	@Override
	protected AbstractModule createDefaultModule(final SessionIdProvider sessionIDProvider) {
		return new AbstractModule() {

			@Override
			protected void configure() {
				bind(InternalLookupService.class).toProvider(
						getImplementationProvider(InternalLookupService.class, sessionIDProvider));
			}
		};
	}

	@Override
	protected <I> InvocationHandler<I> getInvocationHandler(Class<I> ignore, SessionIdProvider sessionIDProvider) {
		return new LocalInvocationHandler<I>(requestProcessor, new SessionIdProvider() {

			@Override
			public String getSessionId() {
				return MozzesInternalClient.INTERNAL_CLIENT_ID;
			}
		});
	}
}
