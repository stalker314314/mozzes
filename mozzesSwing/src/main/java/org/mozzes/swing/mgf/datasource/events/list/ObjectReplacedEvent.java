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

/**
 * Event which indicates that an object from ListDataSource was replaced with another one
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public class ObjectReplacedEvent<T> extends ListDataSourceEvent<T> {
	private final T replaced;
	private final T with;
	private final int index;

	/**
	 * @param index Index at which the replace was conducted
	 * @param replaced Old object which was replaced
	 * @param with New object which has replaced the old one
	 */
	public ObjectReplacedEvent(int index, T replaced, T with) {
		this.replaced = replaced;
		this.with = with;
		this.index = index;
	}

	/**
	 * @return Old object which was replaced
	 */
	public T getReplaced() {
		return replaced;
	}

	/**
	 * @return New object which has replaced the old one
	 */
	public T getWith() {
		return with;
	}

	/**
	 * @return Index at which the replace was conducted
	 */
	public int getIndex() {
		return index;
	}
}
