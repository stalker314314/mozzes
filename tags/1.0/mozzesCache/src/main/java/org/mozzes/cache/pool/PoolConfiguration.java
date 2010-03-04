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

import org.mozzes.cache.instance.InstanceProvider;

/**
 * Instance pool configuration
 * 
 * @param <T> Class of pooled instances
 */
public class PoolConfiguration<T> {

	private final String name;
	private final InstanceProvider<? extends T> provider;
	private final int capacity;
	private final int validationInterval;
	private final InstanceValidator<? super T> validator;

	/**
	 * Instance pool configuration
	 * 
	 * @param name Pool name
	 * @param capacity Pool capacity
	 * @param instanceProvider Provider of pool instances
	 */
	public PoolConfiguration(String name, int capacity, InstanceProvider<? extends T> instanceProvider) {
		this(name, capacity, instanceProvider, -1, null);
	}

	/**
	 * Instance pool configuration with enabled instance validation
	 * 
	 * @param name Pool name
	 * @param capacity Pool capacity
	 * @param instanceProvider Provider of pool instances
	 * @param validationInterval Interval in which pool instances validation will be performed. Invalid instances will
	 *            be removed from the pool.
	 * @param validator Instance validator
	 */
	public PoolConfiguration(String name, int capacity, InstanceProvider<? extends T> instanceProvider,
			int validationInterval, InstanceValidator<? super T> validator) {
		this.name = name;
		this.provider = instanceProvider;
		this.capacity = capacity;
		this.validationInterval = validationInterval;
		this.validator = validator;
	}

	public String getName() {
		return name;
	}

	public int getCapacity() {
		return capacity;
	}

	public InstanceProvider<? extends T> getProvider() {
		return provider;
	}

	public int getValidationInterval() {
		return validationInterval;
	}

	public InstanceValidator<? super T> getValidator() {
		return validator;
	}

	boolean isValidationEnabled() {
		return validationInterval > 0;
	}
}
