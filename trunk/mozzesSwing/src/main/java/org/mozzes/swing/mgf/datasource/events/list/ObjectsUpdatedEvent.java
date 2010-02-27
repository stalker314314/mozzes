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
 * Event which indicates that an object from ListDataSource was replaced with another one
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public class ObjectsUpdatedEvent<T> extends ListDataSourceEvent<T> {
	private final Collection<T> objects;

	/**
	 * @param objects Objects that were updated
	 */
	public ObjectsUpdatedEvent(Collection<T> objects) {
		this.objects = objects;
	}

	/**
	 * @return Objects that were updated
	 */
	public Collection<T> getObjects() {
		return objects;
	}
}
