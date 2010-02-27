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