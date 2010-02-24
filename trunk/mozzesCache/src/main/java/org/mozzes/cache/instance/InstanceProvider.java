package org.mozzes.cache.instance;

/**
 * Provides an instance of class T
 * 
 * @param <T> Instance class
 */
public interface InstanceProvider<T> {

	/**
	 * Provides an instance
	 * @return instance
	 * @throws Exception If instance of T could not be provided
	 */
	public T get() throws Exception;
	
}
