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

import javax.servlet.http.HttpServletRequest;

import org.mozzes.application.vaadin.VaadinServletConfig.VaadinAppClass;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;

@Singleton
final class VaadinServlet extends AbstractApplicationServlet {

	private static final long serialVersionUID = 4139254351629086946L;

	private final Provider<Application> applicationProvider;
	private final Class<? extends Application> applicationClass;
	
	@SuppressWarnings({ "cast", "unchecked" })
	@Inject
    VaadinServlet(Provider<Application> applicationProvider, 
    		@VaadinAppClass Class applicationClass) {
		super();
		this.applicationProvider = applicationProvider;
		this.applicationClass = (Class<? extends Application>)applicationClass;
	}

	@Override
    protected Class<? extends Application> getApplicationClass() {
        return applicationClass;
    }

    @Override
    protected Application getNewApplication(HttpServletRequest request) {
		return applicationProvider.get();
    }
}