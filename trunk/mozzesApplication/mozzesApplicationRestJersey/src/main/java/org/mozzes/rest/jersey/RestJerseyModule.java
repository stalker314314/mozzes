package org.mozzes.rest.jersey;

import java.util.ArrayList;
import java.util.List;

import org.mozzes.application.module.ApplicationModule;
import org.mozzes.application.module.ServerLifecycleListener;

import com.google.inject.Binder;
import com.google.inject.Provider;
import com.google.inject.Scopes;

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
		binder.bind(RestJerseyConfiguration.class).toProvider(new Provider<RestJerseyConfiguration>() {
			@Override
			public RestJerseyConfiguration get() {
				return configuration;

			}
		}).in(Scopes.SINGLETON);
	}

	@Override
	public List<Class<? extends ServerLifecycleListener>> getServerListeners() {
		List<Class<? extends ServerLifecycleListener>> list = new ArrayList<Class<? extends ServerLifecycleListener>>();
		list.add(RestJerseyServerListener.class);
		return list;
	}
}