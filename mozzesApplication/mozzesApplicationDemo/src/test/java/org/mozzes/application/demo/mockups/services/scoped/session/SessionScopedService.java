package org.mozzes.application.demo.mockups.services.scoped.session;

import org.mozzes.application.module.scope.*;

/**
 * This is the specification of the service interface which implementation class is annotated with {@link SessionScoped}
 * annotation. This means that during the whole session server will use the same instance of the service implementation
 * to process the service method calls. So with that we can store some session specific data in the service attributes
 * and different session will not interfere and the value of service attributes will be preserved between the service
 * method calls.
 * 
 * @author vita
 */
public interface SessionScopedService {

	/**
	 * this method increments value of the integer attribute
	 */
	void increment();

	/**
	 * @return value of the integer attribute
	 */
	int getCounter();
}
