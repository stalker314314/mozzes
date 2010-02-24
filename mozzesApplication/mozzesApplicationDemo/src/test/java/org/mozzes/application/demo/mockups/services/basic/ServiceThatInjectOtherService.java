package org.mozzes.application.demo.mockups.services.basic;

import org.mozzes.application.demo.mockups.services.basic.impl.*;

/**
 * This is the sample interface which implementation is delegating call to the injected {@link BasicService}
 * 
 * @author vita
 */
public interface ServiceThatInjectOtherService {

	/**
	 * delegates call to the {@link BasicServiceImpl#getIntegerFromServer()}
	 */
	int getIntegerFromInjectedService();
}