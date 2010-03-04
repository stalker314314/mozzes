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
