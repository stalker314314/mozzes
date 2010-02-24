package org.mozzes.application.demo.mockups.services.basic.impl;

import org.mozzes.application.demo.mockups.SimpleOjbect;
import org.mozzes.application.demo.mockups.services.basic.BasicService;

/**
 * This is the implementation class for the {@link BasicService} service interface.
 * 
 * @author vita
 */
public class BasicServiceImpl implements BasicService {

	/**
	 * @see BasicService#getIntegerFromServer()
	 */
	@Override
	public Integer getIntegerFromServer() {
		return returnedValue;
	}

	@Override
	public void setA(SimpleOjbect a) {
	}
}
