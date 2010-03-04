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

import org.mozzes.swing.mgf.datamodel.Field;

/**
 * 
 * Interface which must be implemented in order to be able to react to {@link Field} updates ({@link FieldEvent})
 * 
 * @author milos
 * 
 * @param <T> The type of object on which the model operates
 * @param <F> The type of the field value
 */
public interface FieldEventListener<T, F> {
	/**
	 * @param field {@link Field} which triggered the event
	 * @param event Event that happened (concrete subclass of {@link FieldEvent})
	 */
	public void handleModelFieldEvent(Field<T, F> field, FieldEvent<T, F> event);
}
