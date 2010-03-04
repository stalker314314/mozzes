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

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.Field;

/**
 * Indicates that a field was added to the model
 * 
 * @author milos
 * 
 * @param <T> The type of object on which the model operates
 */
public class FieldAddedEvent<T> extends DataModelEvent<T> {
	private final int fieldIndex;
	private final Field<T, ?> field;

	/**
	 * Constructs the event
	 * 
	 * @param fieldIndex Index in the {@link DataModel} at which the field was added
	 * @param field {@link Field} which was added to the {@link DataModel}
	 */
	public FieldAddedEvent(int fieldIndex, Field<T, ?> field) {
		this.fieldIndex = fieldIndex;
		this.field = field;
	}

	/**
	 * @return Index in the {@link DataModel} at which the field was added
	 */
	public int getFieldIndex() {
		return fieldIndex;
	}

	/**
	 * @return {@link Field} which was added to the {@link DataModel}
	 */
	public Field<T, ?> getField() {
		return field;
	}
}
