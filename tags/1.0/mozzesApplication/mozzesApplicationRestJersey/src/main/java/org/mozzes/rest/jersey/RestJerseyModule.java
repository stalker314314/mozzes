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
package org.mozzes.rest.jersey;

import java.util.ArrayList;
import java.util.List;

import org.mozzes.application.module.ApplicationModule;
import org.mozzes.application.module.ServerLifecycleListener;

import com.google.inject.Binder;

/**
 * Mozzes application module for REST Jersey implementation 
 * @author stalker
 */
public class RestJerseyModule extends ApplicationModule {

	private final RestJerseyConfiguration configuration;

	public RestJerseyModule(String baseUri, String rootResourcePackage) {
		configuration = new RestJerseyConfiguration(baseUri, rootResourcePackage);
	}

	@Override
	public void doCustomBinding(Binder binder) {
		binder.bind(RestJerseyConfiguration.class).toInstance(configuration);
	}

	@Override
	public List<Class<? extends ServerLifecycleListener>> getServerListeners() {
		List<Class<? extends ServerLifecycleListener>> list = new ArrayList<Class<? extends ServerLifecycleListener>>();
		list.add(RestJerseyServerListener.class);
		return list;
	}
}