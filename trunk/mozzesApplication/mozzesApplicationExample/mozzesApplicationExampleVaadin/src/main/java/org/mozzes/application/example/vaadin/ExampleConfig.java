package org.mozzes.application.example.vaadin;

import org.mozzes.application.example.server.ExampleServer;
import org.mozzes.application.server.MozzesServerConfiguration;
import org.mozzes.application.vaadin.VaadinServletConfig;

import com.vaadin.Application;


public class ExampleConfig extends VaadinServletConfig {

	@Override
	protected MozzesServerConfiguration getServerConfiguration() {
		return new ExampleServer().createServerConfiguration();
	}

	@Override
	protected Class<? extends Application> getVaadinApplication() {
		return ExampleApplication.class;
	}

}
