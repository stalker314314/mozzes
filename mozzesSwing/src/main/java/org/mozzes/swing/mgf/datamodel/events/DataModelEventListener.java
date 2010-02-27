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

/**
 * Interface which must be implemented in order to be able to react to {@link DataModel} changes({@link DataModelEvent})
 * 
 * @author milos
 * 
 * @param <T> The type of object on which the model operates
 */
public interface DataModelEventListener<T> {
	/**
	 * @param model {@link DataModel} which triggered the event
	 * @param event Event that happened (concrete subclass of {@link DataModelEvent})
	 */
	public void handleDataModelEvent(DataModel<T> model, DataModelEvent<T> event);
}
