package org.mozzes.application.demo.mockups.services.basic;

import org.mozzes.application.demo.mockups.SimpleOjbect;

/**
 * This is the example of the server service interface.
 * 
 * @author vita
 */
public interface BasicService {

	Integer returnedValue = 123;

	/**
	 * this is simple method that returns 123 from server
	 */
	Integer getIntegerFromServer();

	void setA(SimpleOjbect a);

}
