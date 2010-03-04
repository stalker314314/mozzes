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
package org.mozzes.swing.mgf.datamodel;

import java.util.List;

import org.mozzes.swing.mgf.datamodel.events.DataModelEventListener;


/**
 * Defines a DataModel of some bean for use by a GUI component
 * 
 * @author milos
 * 
 * @param <T> Type of a bean for which this model is providing fields({@link Field})
 */
public interface DataModel<T> {

	/**
	 * Adds a listener to the list that's notified each time a change to the data model occurs.
	 * 
	 * @param listener The listener to be notified about model events
	 */
	public void addEventListener(DataModelEventListener<T> listener);

	/**
	 * Removes a listener from the list that's notified each time a change to the data model occurs.
	 * 
	 * @param listener The listener to be removed
	 */
	public void removeEventListener(DataModelEventListener<T> listener);

	/**
	 * Adds a {@link Field} at the end of the fields list
	 * 
	 * @param field {@link Field} to be added
	 * @return This {@link DataModel} for chaining
	 */
	public DataModel<T> addField(Field<T, ?> field);

	/**
	 * Inserts a {@link Field} at <i>index</i> position in the fields list
	 * 
	 * @param index Index at which the {@link Field} should be inserted
	 * @param field The {@link Field} which should be inserted
	 * @return This {@link DataModel} for chaining
	 * @throws IndexOutOfBoundsException If the <i>index</i> is <0 or >=fields.size()
	 * @throws IllegalArgumentException If the <i>field</i> is <i>null</i>
	 */
	public DataModel<T> addField(int index, Field<T, ?> field);

	/**
	 * Removes the specified <i>field</i> from the {@link DataModel}
	 * 
	 * @param field {@link Field} that should be removed
	 * @return <b>true</b> if the <i>field</i> was in the list and was removed, otherwise <b>false</b>
	 */
	public boolean removeField(Field<T, ?> field);

	/**
	 * Removes the {@link Field} at specified <i>index</i> from the {@link DataModel}
	 * 
	 * @param index Index at which the field should be removed
	 * @return {@link Field} that was removed
	 * @throws IndexOutOfBoundsException If the <i>index</i> is <0 or >=fields.size()
	 */
	public Field<T, ?> removeField(int index);

	/**
	 * Moves the field from index <i>from</i> to index <i>to</i>
	 * 
	 * @param from Index from which to take the {@link Field} for moving
	 * @param to Index to which the {@link Field} should be moved
	 * @throws IndexOutOfBoundsException If the <i>from</i> or <i>to</i> is <0 or >=fields.size()
	 */
	public void moveField(int from, int to);

	/**
	 * Moves the specified <i>field</i> to index <i>to</i>
	 * 
	 * @param field {@link Field} that should be moved
	 * @param to Index to which the {@link Field} should be moved
	 * @throws IndexOutOfBoundsException If the <i>to</i> is <0 or >=fields.size()
	 * @throws IllegalArgumentException if the <i>field</i> is <i>null</i> or if it doesn't exist in the fields list of
	 *             the model
	 */
	public void moveField(Field<T, ?> field, int to);

	/**
	 * Returns the {@link Field} from specified <i>index</i>
	 * 
	 * @param index Index of the {@link Field} in {@link DataModel}
	 * @return {@link Field} from specified <i>index</i>
	 * @throws IndexOutOfBoundsException If the <i>to</i> is <0 or >=fields.size()
	 */
	public Field<T, Object> getField(int index);

	/**
	 * Returns the {@link Field} with specified <i>name</i>
	 * 
	 * @param name Name of the {@link Field} in {@link DataModel}
	 * @return {@link Field} from specified <i>index</i>
	 * @throws IndexOutOfBoundsException If the <i>to</i> is <0 or >=fields.size()
	 */
	public Field<T, Object> getField(String name);

	/**
	 * Returns the {@link Field} with specified <i>name</i> and safely casts it to the field of type specified by
	 * <i>clazz</i> parameter
	 * 
	 * @param <F> The type of the value that field is returning/generating
	 * @param name Name of the {@link Field} in {@link DataModel}
	 * @param clazz Class of the of the value that field is returning/generating
	 * @return {@link Field} from specified <i>index</i> and safely casts it to the field of type specified by
	 *         <i>clazz</i> parameter
	 * @throws IndexOutOfBoundsException If the <i>to</i> is <0 or >=fields.size()
	 * @throws ClassCastException If the actual type of the field is not the type specified by <i>clazz</i> parameter
	 */
	public <F> Field<T, F> getField(String name, Class<F> clazz);

	/**
	 * Returns the {@link Field} from specified <i>index</i> and safely casts it to the field of type specified by
	 * <i>clazz</i> parameter
	 * 
	 * @param <F> The type of the value that field is returning/generating
	 * @param index Index of the {@link Field} in {@link DataModel}
	 * @param clazz Class of the of the value that field is returning/generating
	 * @return {@link Field} from specified <i>index</i> and safely casts it to the field of type specified by
	 *         <i>clazz</i> parameter
	 * @throws IndexOutOfBoundsException If the <i>to</i> is <0 or >=fields.size()
	 * @throws ClassCastException If the actual type of the field is not the type specified by <i>clazz</i> parameter
	 */
	public <F> Field<T, F> getField(int index, Class<F> clazz);

	/**
	 * Calls the {@link Field#getValue(Object)} of the {@link Field} found at specified <i>index</i> with specified
	 * <i>object</i> as argument.
	 * 
	 * @param index Index of the {@link Field} in {@link DataModel}
	 * @param object Object for which the value of the {@link Field} should be returned
	 * @return The value of the {@link Field} found at specified <i>index</i> for specified <i>object</i>
	 * @throws IndexOutOfBoundsException If the <i>to</i> is <0 or >=fields.size()
	 */
	public Object getFieldValue(int index, T object);

	/**
	 * Calls the {@link Field#getValue(Object)} of the {@link Field} found at specified <i>index</i> with specified
	 * <i>object</i> as argument and safely casts it to the type specified by the <i>clazz</i> parameter
	 * 
	 * @param <F> The type of the value that the {@link Field} returns
	 * @param index Index of the {@link Field} in {@link DataModel}
	 * @param object Object for which the value of the {@link Field} should be returned
	 * @param clazz Class of the of the value that field is returning/generating
	 * @return The value of the {@link Field} found at specified <i>index</i> for specified <i>object</i> cast to the
	 *         type specified by <i>clazz</i> parameter
	 * @throws IndexOutOfBoundsException If the <i>to</i> is <0 or >=fields.size()
	 * @throws ClassCastException If the actual type of the field is not the type specified by <i>clazz</i> parameter
	 */
	public <F> F getFieldValue(int index, T object, Class<F> clazz);

	/**
	 * @return Unmodifiable {@link List} of the fields contained by this model
	 */
	public List<Field<T, ?>> getFields();

	/**
	 * @return The number of the fields contained by the model
	 */
	public int getFieldsNumber();

	/**
	 * @param name Name of the field
	 * @return Index of the field in model with specified name, -1 i there is no such field
	 */
	public int indexOf(String name);
	
	/**
	 * @param field Field for which to get index
	 * @return Index of the field in model, -1 i there is no such field
	 */
	public int indexOf(Field<T, ?> field);

}
