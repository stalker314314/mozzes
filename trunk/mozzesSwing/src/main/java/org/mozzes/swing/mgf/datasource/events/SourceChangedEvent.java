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
package org.mozzes.swing.mgf.datasource.events;

/**
 * Indicates that data source is now providing object which is different from the one provided before
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public class SourceChangedEvent<T> extends DataSourceEvent<T> {
	private final T from;
	private final T to;

	/**
	 * @param from Old object which was provided by the data source
	 * @param to New object which is provided by the data source
	 */
	public SourceChangedEvent(T from, T to) {
		this.from = from;
		this.to = to;
	}

	/**
	 * @return Old object which was provided by the data source
	 */
	public T getFrom() {
		return from;
	}

	/**
	 * @return New object which is provided by the data source
	 */
	public T getTo() {
		return to;
	}

}
