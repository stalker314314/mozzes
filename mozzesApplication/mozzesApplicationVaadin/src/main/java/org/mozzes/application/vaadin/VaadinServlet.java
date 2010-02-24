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