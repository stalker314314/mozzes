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
 * Indicates that a field was moved in the model
 * 
 * @author milos
 * 
 * @param <T> The type of object on which the model operates
 */
public class FieldMovedEvent<T> extends DataModelEvent<T> {
	private final int from;
	private final int to;
	private final Field<T, ?> field;

	/**
	 * Constructs the event
	 * 
	 * @param from Index in the {@link DataModel} from which the field was moved
	 * @param to Index in the {@link DataModel} to which the field was moved
	 * @param field {@link Field} which was moved
	 */
	public FieldMovedEvent(int from, int to, Field<T, ?> field) {
		this.from = from;
		this.to = to;
		this.field = field;
	}

	/**
	 * @return Index in the {@link DataModel} from which the field was moved
	 */
	public int getFrom() {
		return from;
	}

	/**
	 * @return Index in the {@link DataModel} to which the field was moved
	 */
	public int getTo() {
		return to;
	}

	/**
	 * @return {@link Field} which was moved
	 */
	public Field<T, ?> getField() {
		return field;
	}
}
