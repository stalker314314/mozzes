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
package org.mozzes.cache.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.mozzes.cache.instance.InstanceCache;


/**
 * Simple {@link MapProvider} which acts like a caching proxy for another, real, {@link MapProvider}.
 * {@link MapCache} uses key based lock and doesn't lock the whole map.
 *  
 * @param <T> Instance class
 */
public class MapCache<K, T> implements MapProvider<K, T> {
	
	private final ConcurrentMap<K, InstanceCache<T>> cacheInstances;
	private final MapProvider<K, T> parentProvider;
	
	/**
	 * MapCache creation.
	 * @param mapInstanceProvider "Real" provider
	 */
	public MapCache(MapProvider<K, T> mapInstanceProvider) {
		this.cacheInstances = new ConcurrentHashMap<K, InstanceCache<T>>();
		this.parentProvider = mapInstanceProvider;
	}
	
	/**
	 * Provides an instance based on value key. Instance is lazily initialized and cached.
	 * @param key Instance key
	 * @return instance
	 * @throws Exception If instance of T could not be provided
	 */
	@Override
	public T get(K key) throws Exception {
		// small optimization, eliminates unnecessary InstanceCache creation
		if (!cacheInstances.containsKey(key))
			cacheInstances.putIfAbsent(key, new InstanceCache<T>(new MapInstanceProviderAdapter<K, T>(parentProvider, key)));
		
		return cacheInstances.get(key).get();
	}
	
}
