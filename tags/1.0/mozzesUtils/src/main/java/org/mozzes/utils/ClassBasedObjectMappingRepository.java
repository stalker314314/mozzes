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
package org.mozzes.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * A class-based delegation object repository.<br>
 * In case of a class that is not in the map it performs a BFS up the class hierarchy to find a class for which an
 * object was specified.<br />
 * 
 * @author Michael Bar-Sinai mich.barsinai@gmail.com
 */
public class ClassBasedObjectMappingRepository<T> {
	/** Cached objects, stored in a Class->Renderer map. */
	private final Map<Class<?>, T> objectsCache = new HashMap<Class<?>, T>();

	/**
	 * The Class->Object map the user specified. We need this to find the appropriate object for classes we have'nt yet
	 * met.
	 */
	private final Map<Class<?>, T> explicitObjects = new HashMap<Class<?>, T>();

	public ClassBasedObjectMappingRepository(T defaultObject) {
		setObjects(Object.class, defaultObject);
	}

	/**
	 * Sets an object for a specific java class (or interface).
	 * 
	 * @param clazz the class for which we set the object
	 * @param object an object instance that will be provided for the class
	 */
	public void setObjects(Class<?> clazz, T object) {
		if (clazz == null)
			throw new IllegalArgumentException("Clazz parameter cannot be null!");

		objectsCache.put(clazz, object);
		explicitObjects.put(clazz, object);
	}

	public void removeObject(Class<?> clazz) {
		if (!explicitObjects.containsKey(clazz))
			return;
		explicitObjects.remove(clazz);
		objectsCache.clear();
	}

	/**
	 * Gets an object for a specific java class (or interface).
	 * 
	 * @param clazz the class for which we set the renderer
	 * @return An object for a specific java class (or interface).
	 */
	public T getObject(Class<?> clazz) {
		if (clazz == null)
			throw new IllegalArgumentException("Parameter \"clazz\" cannot be null!");

		T delegate = null;
		// Try to get the object from the cache
		delegate = objectsCache.get(clazz);
		if (delegate == null) {
			// Cache miss. Find the the proper object for the class using BFS.
			delegate = findObjectForClass(clazz);
			objectsCache.put(clazz, delegate);
		}

		if (delegate == null) {
			delegate = explicitObjects.get(Object.class);
		}
		return delegate;
	}

	/**
	 * Returns the "most appropriate" object for instances of <code>valClass</code> We do a BFS run over the
	 * super-classes and interfaces of the object.
	 * 
	 * @param clazz Class for which we should do the search
	 * @return Adequate object
	 */
	protected T findObjectForClass(Class<?> clazz) {
		Queue<Class<?>> queue = new LinkedList<Class<?>>(); // the BFS' "to be visited" queue
		Set<Class<?>> visited = new HashSet<Class<?>>(); // the class objects we have visited

		queue.add(clazz);
		visited.add(clazz);

		while (!queue.isEmpty()) {
			Class<?> curClass = queue.remove();

			// get the super types to visit.
			List<Class<?>> supers = new LinkedList<Class<?>>();

			// Add a super-class
			Class<?> superClass = curClass.getSuperclass(); // this would be null for interfaces.
			if (superClass != null) {
				supers.add(superClass);
			}
			// Add interfaces
			addInterfaces(curClass, supers);

			for (Class<?> ifs : supers) {
				if (explicitObjects.containsKey(ifs)) {
					return explicitObjects.get(ifs);
				}
				if (!visited.contains(ifs)) {
					queue.add(ifs);
					visited.add(ifs);
				}
			}

		}

		return explicitObjects.get(Object.class);
	}

	private void addInterfaces(Class<?> curClass, List<Class<?>> supers) {
		for (Class<?> itrfce : curClass.getInterfaces()) {
			supers.add(itrfce);
		}
		for (Class<?> itrfce : curClass.getInterfaces()) {
			addInterfaces(itrfce, supers);
		}
	}
}
