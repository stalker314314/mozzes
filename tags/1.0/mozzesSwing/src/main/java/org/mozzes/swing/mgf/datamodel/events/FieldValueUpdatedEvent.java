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
package org.mozzes.swing.mgf.datamodel.events;

import org.mozzes.swing.mgf.datamodel.Field;

/**
 * Indicates that a value of a field in the model has changed
 * 
 * @author milos
 * 
 * @param <T> The type of object on which the model operates
 */
public class FieldValueUpdatedEvent<T> extends DataModelEvent<T> {
	private final int fieldIndex;
	private final Field<T, Object> field;
	private final Object from;
	private final Object to;
	private final T forObject;

	/**
	 * Constructs the event
	 * 
	 * @param fieldIndex Index of the {@link Field} which was updated
	 * @param field {@link Field} which was updated
	 * @param forObject Object for which the field was updated
	 * @param from Previous value of the field for specified object (<i>forObject</i>)
	 * @param to Current value of the field for specified object (<i>forObject</i>)
	 */
	public FieldValueUpdatedEvent(int fieldIndex, Field<T, Object> field, T forObject, Object from, Object to) {
		this.fieldIndex = fieldIndex;
		this.field = field;
		this.from = from;
		this.to = to;
		this.forObject = forObject;
	}

	/**
	 * @return Index of the {@link Field} which was updated
	 */
	public int getFieldIndex() {
		return fieldIndex;
	}

	/**
	 * @return {@link Field} which was updated
	 */
	public Field<T, Object> getField() {
		return field;
	}

	/**
	 * @return {@link Field} which was updated
	 */
	public Object getFrom() {
		return from;
	}

	/**
	 * @return {@link Field} which was updated
	 */
	public Object getTo() {
		return to;
	}

	/**
	 * @return Object for which the field was updated
	 */
	public T getForObject() {
		return forObject;
	}
}
