package org.mozzes.cache.instance;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * InstanceCache is simple InstanceProvider which acts like a caching proxy for another, real, InstanceProvider.
 *  
 * @param <T> Instance class
 */
public class InstanceCache<T> implements InstanceProvider<T> {
	
	private T instance = null;
	private volatile boolean instanceInitialized;
	private final InstanceProvider<T> instanceProvider;

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	/**
	 * InstanceCache creation.
	 * @param instanceProvider "Real" provider
	 */
	public InstanceCache(InstanceProvider<T> instanceProvider) {
		this.instanceInitialized = false;
		this.instanceProvider = instanceProvider;
	}

	/**
	 * Provides a instance. Instance is lazily initialized and cached.  
	 * @return instance
	 * @throws Exception If instance could not be initialized
	 */
	@Override
	public T get() throws Exception {
		lock.readLock().lock();
		
		if (instanceInitialized == false) {
			lock.readLock().unlock();
			try {
				lock.writeLock().lock();
				if (instanceInitialized == false) {
					instance = instanceProvider.get();
					instanceInitialized = true;
				}
				lock.readLock().lock();
			} finally {
				lock.writeLock().unlock();
			}
		}
		
		try {
			return instance;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Clears cache and returns current cache instance.
	 * @return current cache instance or null
	 */
	public T clear() {
		lock.writeLock().lock();
		try {
			if (instanceInitialized) {
				T returnInstance = instance;
				instanceInitialized = false;
				instance = null;
				return returnInstance;
			} else
				return null;
			
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	boolean isInstanceInitialized() {
		lock.readLock().lock();
		try {
			return instanceInitialized;
		} finally {
			lock.readLock().unlock();
		}
	}

}
