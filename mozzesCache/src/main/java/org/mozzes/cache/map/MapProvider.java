package org.mozzes.cache.map;

/**
 * Provides an instance of class T, based on a key of class K
 *
 * @param <K> Key class
 * @param <T> Instance class
 */
public interface MapProvider<K, T> {

	/**
	 * Provides an instance based on a key
	 * @param key Instance key
	 * @return instance
	 * @throws Exception If instance of T could not be provided
	 */
	public T get(K key) throws Exception;
	
}
