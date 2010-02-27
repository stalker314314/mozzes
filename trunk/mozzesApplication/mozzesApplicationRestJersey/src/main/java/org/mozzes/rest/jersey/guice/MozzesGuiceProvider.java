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
package org.mozzes.rest.jersey.guice;

import com.google.inject.Injector;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.model.AbstractResource;
import com.sun.jersey.server.spi.component.ResourceComponentProvider;

/**
 * @author stalker
 */
public class MozzesGuiceProvider implements ResourceComponentProvider {

	private AbstractResource abstractResource;
	private Injector guiceInjector = null;

	public MozzesGuiceProvider(Injector guiceInjector) {
		this.guiceInjector = guiceInjector;
	}

	@Override
	public Object getInstance(HttpContext hc) {
		return guiceInjector.getInstance(abstractResource.getResourceClass());
	}

	@Override
	public void init(AbstractResource abstractResource) {
		this.abstractResource = abstractResource;
	}

	@Override
	public Object getInstance() {
		return guiceInjector.getInstance(abstractResource.getResourceClass());
	}

	@Override
	public void destroy() {
	}
}