package org.mozzes.application.demo.mockups.services.scoped.session.impl;

import org.mozzes.application.demo.mockups.services.scoped.session.*;
import org.mozzes.application.module.scope.*;


/**
 * This is the service implementation that is annotated with {@link SessionScoped} and because of that counter value is
 * "preserved" between the service methods calls in the same session.<br>
 * 
 * Actually the google guice is instantiating the one instance of this class for the same session so all the attributes
 * are "preserved"
 * 
 * @author vita
 * 
 * @see SessionScopedService
 */
@SessionScoped
public class SessionScopedServiceImpl implements SessionScopedService {

	private int counter = 0;

	/**
	 * @see SessionScopedService#increment()
	 */
	@Override
	public void increment() {
		counter++;
	}

	/**
	 * @see SessionScopedService#getCounter()
	 */
	@Override
	public int getCounter() {
		return counter;
	}
}
