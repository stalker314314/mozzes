package org.mozzes.application.demo.mockups.services.scopedata;

import org.mozzes.application.demo.mockups.scopedata.*;
import org.mozzes.application.module.scope.*;


/**
 * This is the service that's working with the data stored in the request context. It's using {@link MRequestData} class
 * that's annotated with {@link RequestScoped} annotation.<br>
 * 
 * It has only one method that's incrementing the counter value of the {@link MRequestData} instance and then calling
 * the {@link ServiceWithRequestData2#incrementRequestCounter()} method that's also incrementing the
 * {@link MRequestData} instance counter value. Because this two invocations are in the same request they should
 * increment the same counter so the returned value should be 2
 * 
 * @author vita
 */
public interface ServiceWithRequestData {

	/**
	 * Increment data in the request context and then call other service that's doing the same and return value.Should
	 * return 2
	 */
	int incrementAndReturnInTheRequestContext();
}
