package org.mozzes.application.demo.mockups.scopedata;

/**
 * Abstract class that represents data stored in the scope. Basically in the scope is hold only the simple integer
 * 
 * @author vita
 */
public abstract class AbstractScopeData {

	private int counter = 0;

	public int getCounter() {
		return counter;
	}

	public void increment() {
		counter++;
	}
}
