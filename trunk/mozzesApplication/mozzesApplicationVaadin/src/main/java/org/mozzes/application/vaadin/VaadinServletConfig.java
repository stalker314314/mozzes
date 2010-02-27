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
package org.mozzes.application.vaadin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.server.MozzesServer;
import org.mozzes.application.server.MozzesServerConfiguration;

import com.google.inject.BindingAnnotation;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;
import com.vaadin.Application;

/**
 * Base class for Vaadin configuration.<br>
 * Extend this class and provide server configuration and Vaadin application class.<br>
 * <br>
 * Create web.xml using this template. Just replace<ul> 
 * <li>APPLICATION_NAME with your application name and</li>
 * <li>CONFIGURATION_CLASS_NAME with the name of a class witch extends this class.</li></ul>
 * <pre>
 * {@code
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	id="WebApp_ID" version="2.5">
	
	<display-name>APPLICATION_NAME</display-name>
	<context-param>
		<description>Vaadin production mode</description>
		<param-name>productionMode</param-name>
		<param-value>false</param-value>
	</context-param>
	<filter>
		<filter-name>guiceFilter</filter-name>
		<filter-class>com.google.inject.servlet.GuiceFilter</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>guiceFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

	<listener>
		<listener-class>CONFIGURATION_CLASS_NAME</listener-class>
	</listener>
</web-app>
 * }
 * </pre>
 */
public abstract class VaadinServletConfig extends GuiceServletContextListener {
	
	protected abstract MozzesServerConfiguration getServerConfiguration();

	protected abstract Class<? extends Application> getVaadinApplication();
	
	@Override
	protected final Injector getInjector() {
		return Guice.createInjector(new ClientAppServletModule(getVaadinApplication(), getMozzesClientProvider()));
	}
	
	private Provider<MozzesClient> getMozzesClientProvider() {
		return new MozzesClientProvider(startMozzesServer());
	}
	
	private MozzesServer startMozzesServer() {
		MozzesServer server = new MozzesServer(getServerConfiguration());
		server.start();
		return server;
	}

	private static final class ClientAppServletModule extends ServletModule {
		
		private final Provider<MozzesClient> clientProvider;
		private final Class<? extends Application> vaadinApplicationClass;
		
		ClientAppServletModule(Class<? extends Application> vaadinApplicationClass, Provider<MozzesClient> clientProvider) {
			this.vaadinApplicationClass = vaadinApplicationClass;
			this.clientProvider = clientProvider;
		}

		@Override
		protected void configureServlets() {
			serve("/*").with(VaadinServlet.class);
			bind(Application.class).to(vaadinApplicationClass).in(ServletScopes.SESSION);
			bind(Class.class).annotatedWith(VaadinAppClass.class).toInstance(vaadinApplicationClass);
			bind(MozzesClient.class).toProvider(clientProvider).in(ServletScopes.SESSION);
		}
	}

	private static final class MozzesClientProvider implements Provider<MozzesClient> {

		private final MozzesServer server;

		MozzesClientProvider(MozzesServer server) {
			super();
			this.server = server;
		}

		public MozzesClient get() {
			return server.getLocalClient();
		}
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD, ElementType.PARAMETER })
	@BindingAnnotation
	@interface VaadinAppClass {

	}

}
