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

import org.mozzes.cache.instance.InstanceProvider;

/**
 * Adapter class which allows {@link MapProvider} to act like {@link InstanceProvider}.
 *
 * @param <K> Key class
 * @param <T> Instance class
 */
public class MapInstanceProviderAdapter<K, T> implements InstanceProvider<T> {
	
	private final MapProvider<K, T> mapProvider;
	private final K key;
	
	public MapInstanceProviderAdapter(MapProvider<K, T> mapProvider, K key) {
		this.mapProvider = mapProvider;
		this.key = key;
	}

	@Override
	public T get() throws Exception {
		return mapProvider.get(key);
	}

}
