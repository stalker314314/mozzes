package org.mozzes.application.demo.mockups.services.scoped.request;

import org.mozzes.application.module.scope.*;

/**
 * This is the specification of the service which implementation is using the {@link RequestScoped} This means that
 * instances of the service implementation class are unique in the single request so we can hold some data in the
 * attributes of the service class and they value will be preserver during the request.
 * 
 * @author vita
 */
public interface RequestScopedService2 {

	/**
	 * this is the method that increments the counter in the service attribute
	 */
	void increment();

	/**
	 * @return value from the service attribute
	 */
	int getCounter();
}
