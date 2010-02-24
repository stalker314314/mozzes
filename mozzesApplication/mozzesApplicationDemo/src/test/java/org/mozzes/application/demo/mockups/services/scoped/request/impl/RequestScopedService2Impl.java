package org.mozzes.application.demo.mockups.services.scoped.request.impl;

import org.mozzes.application.demo.mockups.services.scoped.request.*;
import org.mozzes.application.module.scope.*;


/**
 * This service is annotated with the {@link RequestScoped} and so in the request there's one single instance of this
 * class and all attributes of this class are preserved during the request.
 * 
 * @author vita
 * 
 * @see RequestScopedService
 */
@RequestScoped
public class RequestScopedService2Impl implements RequestScopedService2 {

	private int counter = 0;

	/**
	 * @see RequestScopedService#increment()
	 */
	@Override
	public void increment() {
		counter++;
	}

	/**
	 * @see RequestScopedService#getCounter()
	 */
	@Override
	public int getCounter() {
		return counter;
	}
}