package org.mozzes.application.demo.mockups.services.scopedata;

import org.mozzes.application.demo.mockups.scopedata.*;
import org.mozzes.application.module.scope.*;


/**
 * This is the example of the server service interface that's using data in the request context.<br>
 * 
 * It's simply incrementing the counter value that's stored in the {@link MTransactionData} class that's annotated with
 * {@link RequestScoped} so it's stored in the transaction context
 * 
 * @author vita
 */
public interface ServiceWithRequestData2 {

	/**
	 * Increment the value of the counter stored in the transaction context
	 */
	void incrementRequestCounter();
}
