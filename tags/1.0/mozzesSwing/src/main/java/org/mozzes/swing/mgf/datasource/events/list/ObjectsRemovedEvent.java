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
package org.mozzes.swing.mgf.datasource.events.list;

import java.util.Collection;

/**
 * Event which indicates that objects were removed from ListDataSource
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public class ObjectsRemovedEvent<T> extends ListDataSourceEvent<T> {
	private final Collection<T> objects;

	/**
	 * @param objects Objects which were removed
	 */
	public ObjectsRemovedEvent(Collection<T> objects) {
		this.objects = objects;
	}

	/**
	 * @return Objects which were removed
	 */
	public Collection<T> getObjects() {
		return objects;
	}
}
