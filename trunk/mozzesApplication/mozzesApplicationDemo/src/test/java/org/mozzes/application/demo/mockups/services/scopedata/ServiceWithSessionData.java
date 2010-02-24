package org.mozzes.application.demo.mockups.services.scopedata;

import org.mozzes.application.demo.mockups.scopedata.*;
import org.mozzes.application.module.scope.*;


/**
 * This is the specification of the service that uses the {@link SessionScoped} annotated {@link MSessionData} class.
 * Because this server is using the attribute which type is annotated with {@link SessionScoped} this service is
 * actually preserving the value of the counter between invocations in the same session.
 * 
 * @author vita
 */
public interface ServiceWithSessionData {

	/**
	 * This method is incrementing value of the attribute counter in the {@link MSessionData}
	 * 
	 * Subsequent calls to this method in the same session would preserve and value is not lost so after 3calls value
	 * should be 3.If client logout and then login again it's restarted to 0 again because it's the new session.
	 */
	void incrementSessionCounter();

	/**
	 * This method is returning value of the attribute counter in the {@link MSessionData}
	 */
	int getSessionCounterValue();
}
