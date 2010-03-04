/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.mozzes.cache.pool;

import java.util.Iterator;
import java.util.LinkedList;

import org.mozzes.cache.instance.InstanceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Classical instance pool.
 * 
 * @param <T> Class of pooled instances
 */
public class InstancePool<T> {

	private static final Logger logger = LoggerFactory.getLogger(InstancePool.class);

	private final String name;
	private final int capacity;
	private final InstanceProvider<? extends T> provider;
	private final InstanceValidator<? super T> validator;

	private final LinkedList<PooledInstance<T>> pool = new LinkedList<PooledInstance<T>>();// @GuardedBy pool
	private final LinkedList<RequestOnWait<T>> waitingRequests = new LinkedList<RequestOnWait<T>>();// @GuardedBy pool
	private final LinkedList<PooledInstance<T>> usedInstances = new LinkedList<PooledInstance<T>>();

	private int currentSize = 0;// @GuardedBy pool

	/**
	 * Instance pool creation based on configuration
	 * 
	 * @param configuration pool configuration
	 */
	public InstancePool(PoolConfiguration<T> configuration) {
		this.name = configuration.getName() + "Pool";
		this.capacity = Math.max(1, configuration.getCapacity());
		this.provider = configuration.getProvider();
		this.validator = configuration.getValidator();
		if (configuration.isValidationEnabled())
			new PoolValidationThread(name, configuration.getValidationInterval()).start();
	}

	/**
	 * Gets an instance from the pool.<br>
	 * Instance <b>has to be returned</b> to the pool after usage.<br>
	 * <br>
	 * It there's an available instance in the pool it will be returned.<br>
	 * If there are no available instances and the pool capacity is not reached, new instance will be created and
	 * returned.<br>
	 * If there is no available instance and the pool capacity is reached Thread will wait until some instance becomes
	 * available.
	 * 
	 * @return Instance from the pool
	 * @throws PoolException if pool is unable to create new instance
	 */
	public T get() throws PoolException {

		PooledInstance<T> pooledInstance = null;

		RequestOnWait<T> requestOnWait = new RequestOnWait<T>();
		synchronized (requestOnWait) {

			synchronized (pool) {
				if (!pool.isEmpty()) {
					pooledInstance = pool.removeFirst();
					logger.debug(name + " size: " + pool.size());
				} else if (isAllowedToAddInstance()) {
					pooledInstance = createNewInstance();
					currentSize++;
				} else
					waitingRequests.add(requestOnWait);
			}

			if (pooledInstance == null)
				return wait(requestOnWait);
		}

		return assignInstance(pooledInstance, Thread.currentThread());
	}

	/**
	 * Returns an instance to the pool.
	 * 
	 * @param instance Instance taken from the pool.
	 */
	public void release(T instance) {
		if (instance == null)
			return;

		RequestOnWait<T> requestOnWait = null;

		PooledInstance<T> pooledInstance = releaseInstance(instance);
		synchronized (pool) {
			if (!isValid(pooledInstance)) {
				currentSize--;
				return;
			}

			if (waitingRequests.isEmpty())
				pool.add(pooledInstance);
			else
				requestOnWait = waitingRequests.removeFirst();
		}

		if (requestOnWait != null)
			handleRequestOnWait(requestOnWait, pooledInstance);
	}

	private boolean isAllowedToAddInstance() {
		synchronized (pool) {
			return (currentSize < capacity);
		}
	}

	private PooledInstance<T> createNewInstance() throws PoolException {
		try {
			PooledInstance<T> pooledInstance = new PooledInstance<T>(provider.get());
			return pooledInstance;
		} catch (Exception e) {
			throw new PoolException(e);
		}
	}

	private T wait(RequestOnWait<T> requestOnWait) {
		try {
			logger.info(name + " is fully used, waiting for a instance.");
			requestOnWait.wait();
		} catch (InterruptedException ignore) {
			Thread.interrupted();
		}

		return requestOnWait.getInstance();
	}

	private T assignInstance(PooledInstance<T> instance, Thread requestingThread) {
		instance.setUsedByThread(requestingThread);
		synchronized (usedInstances) {
			this.usedInstances.add(instance);
		}
		return instance.getInstance();
	}

	private PooledInstance<T> releaseInstance(T instance) {
		PooledInstance<T> pooledInstance = new PooledInstance<T>(instance);
		synchronized (usedInstances) {
			if (usedInstances.remove(pooledInstance))
				logger.debug("Instance returned to pool \"" + name + "\"(" + this.usedInstances.size()
						+ " instances in use)");
			else {
				throw new IllegalArgumentException("Unknown instance returned to pool \"" + name + "\"");
			}
		}
		return pooledInstance;
	}

	private final void validatePool() {
		synchronized (pool) {
			for (Iterator<PooledInstance<T>> poolIterator = pool.iterator(); poolIterator.hasNext();) {
				if (!isValid(poolIterator.next())) {
					poolIterator.remove();
					currentSize--;
				}
			}
		}
	}

	private boolean isValid(PooledInstance<T> pooledInstance) {
		return validator.isValid(pooledInstance.getInstance());
	}

	private void handleRequestOnWait(RequestOnWait<T> requestOnWait, PooledInstance<T> pooledInstance) {
		assignInstance(pooledInstance, requestOnWait.getRequestingThread());
		requestOnWait.setInstance(pooledInstance.getInstance());
		synchronized (requestOnWait) {
			requestOnWait.notify();
		}
	}
	
	@Override
	public String toString() {
		return name;
	}

	private final void checkUsedInstances() {
		LinkedList<PooledInstance<T>> instancesToRemove = new LinkedList<PooledInstance<T>>();

		synchronized (usedInstances) {
			for (Iterator<PooledInstance<T>> i = usedInstances.iterator(); i.hasNext();) {
				PooledInstance<T> usedInstance = i.next();
				if (!usedInstance.getUsedByThread().isAlive())
					instancesToRemove.add(usedInstance);
			}
		}

		for (Iterator<PooledInstance<T>> i = instancesToRemove.iterator(); i.hasNext();) {
			PooledInstance<T> usedInstance = i.next();
			logger.warn("Thread \"" + usedInstance.getUsedByThread() + "\" didn't return instance to pool \"" + name
					+ "\"");
			release(usedInstance.getInstance());
		}
	}

	private static class PooledInstance<T> {

		private final T instance;
		private Thread usedByThread;

		PooledInstance(T instance) {
			this.instance = instance;
			this.usedByThread = null;
		}

		public T getInstance() {
			return instance;
		}

		public Thread getUsedByThread() {
			return usedByThread;
		}

		public void setUsedByThread(Thread usedByThread) {
			this.usedByThread = usedByThread;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof PooledInstance<?>))
				return false;

			PooledInstance<?> that = (PooledInstance<?>) o;
			return this.instance == that.instance;
		}
		
		@Override
		public int hashCode() {
			return instance != null ? instance.hashCode() : 0;
		}
	}

	private static class RequestOnWait<T> {

		private T instance;
		private final Thread requestingThread;

		RequestOnWait() {
			this.requestingThread = Thread.currentThread();
		}

		T getInstance() {
			return instance;
		}

		void setInstance(T instance) {
			this.instance = instance;
		}

		Thread getRequestingThread() {
			return requestingThread;
		}
	}

	private class PoolValidationThread extends Thread {

		private final int validationTime;

		protected PoolValidationThread(String poolName, int validationTime) {
			super(poolName + "ValidationThread");
			setDaemon(true);
			this.validationTime = validationTime;
		}

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(validationTime);
				} catch (InterruptedException ignore) {
				}
				validatePool();
				checkUsedInstances();
			}
		}
	}
}
