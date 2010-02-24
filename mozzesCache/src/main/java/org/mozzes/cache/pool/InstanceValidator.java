package org.mozzes.cache.pool;

/**
 * {@link InstancePool} validator, used to check instances in the pool.
 *  
 * @param <T> Class of pooled instances
 */
public interface InstanceValidator<T> {

	boolean isValid(T instance);
	
}
