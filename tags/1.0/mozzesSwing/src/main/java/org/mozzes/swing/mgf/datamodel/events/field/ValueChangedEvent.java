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
package org.mozzes.swing.mgf.datamodel.events.field;

/**
 * Indicates that field value for an object has changed
 * 
 * @author milos
 * 
 * @param <T> The type of object on which the field operates
 * @param <F> The type of the field value
 */
public class ValueChangedEvent<T, F> extends FieldEvent<T, F> {
	private final F from;
	private final F to;
	private final T forObject;

	/**
	 * @param forObject Object for which the field was updated
	 * @param from Previous value of the field for specified object (<i>forObject</i>)
	 * @param to Current value of the field for specified object (<i>forObject</i>)
	 */
	public ValueChangedEvent(T forObject, F from, F to) {
		this.from = from;
		this.to = to;
		this.forObject = forObject;
	}

	/**
	 * @return Previous value of the field for specified object (<i>forObject</i>)
	 */
	public F getFrom() {
		return from;
	}

	/**
	 * @return Current value of the field for specified object (<i>forObject</i>)
	 */
	public F getTo() {
		return to;
	}

	/**
	 * @return Object for which the field was updated
	 */
	public T getForObject() {
		return forObject;
	}
}
