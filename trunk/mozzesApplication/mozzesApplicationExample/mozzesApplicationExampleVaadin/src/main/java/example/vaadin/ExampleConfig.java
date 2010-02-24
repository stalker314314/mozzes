package example.vaadin;

import org.mozzes.application.server.MozzesServerConfiguration;
import org.mozzes.application.vaadin.VaadinServletConfig;

import com.vaadin.Application;

import example.server.ExampleServer;

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
