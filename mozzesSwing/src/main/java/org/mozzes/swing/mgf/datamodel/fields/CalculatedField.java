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
package org.mozzes.swing.mgf.datamodel.fields;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datarenderer.DataRenderModel;

/**
 * Returns the value for passed object calculated using specified method<br>
 * <b>Usage:</b><br>
 * 
 * <code>
 * <pre>
 * model.addField(new CalculatedField&lt;YourObjectType, CalcResultType&gt;() {
 * 		&#064;Override
 * 		public CalcResultType getValue(YourObjectType object) {
 * 			return //your calculation;
 * 		}
 * 	});
 * </pre>
 * </code>
 * 
 * 
 * 
 * @author milos
 * 
 * @param <T> The type of the bean that the field works with
 * @param <F> The type of the value that the field returns
 */
public abstract class CalculatedField<T, F> extends Field<T, F> {
	/**
	 * Can be used to provide additional data to be taken in to account by calculation method
	 */
	private Object context;

	/**
	 * Constructs the {@link CalculatedField}
	 * 
	 * @param fieldType The Class of the value that the field returns or sets
	 */
	public CalculatedField(Class<F> fieldType) {
		super(fieldType, true);
	}

	/**
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field's name
	 */
	public CalculatedField(Class<F> fieldType, String header) {
		super(fieldType, header, true);
	}

	/**
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param sortable Specifies is the field can be used for sorting objects<br>
	 *            <b>(if not, calling {@link Field#compare(Object, Object)} will throw
	 *            {@link UnsupportedOperationException})</b>
	 */
	public CalculatedField(Class<F> fieldType, String header, boolean sortable) {
		super(fieldType, header, true, sortable);
	}

	/**
	 * Constructs the {@link CalculatedField}
	 * 
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 */
	public CalculatedField(String name, Class<F> fieldType) {
		super(name, fieldType, true);
	}

	/**
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field's name
	 */
	public CalculatedField(String name, Class<F> fieldType, String header) {
		super(name, fieldType, header, true);
	}

	/**
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param sortable Specifies is the field can be used for sorting objects<br>
	 *            <b>(if not, calling {@link Field#compare(Object, Object)} will throw
	 *            {@link UnsupportedOperationException})</b>
	 */
	public CalculatedField(String name, Class<F> fieldType, String header, boolean sortable) {
		super(name, fieldType, header, true, sortable);
	}

	@Override
	protected void valueSetter(T object, F value) {
		throw new UnsupportedOperationException("Calculated fields cannot set value by default!");
	}

	/**
	 * Sets some object as the {@link CalculatedField#context context} of the calculation
	 * 
	 * @param context See {@link CalculatedField#context context}
	 * @see CalculatedField#context
	 */
	public void setContext(Object context) {
		this.context = context;
	}

	/**
	 * @return {@link CalculatedField#context Context} of the calculation
	 */
	public Object getContext() {
		return context;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.getClass() == null) ? 0 : this.getClass().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof CalculatedField<?, ?>))
			return false;
		CalculatedField<?, ?> other = (CalculatedField<?, ?>) obj;
		if (this.getClass() == null) {
			if (other.getClass() != null)
				return false;
		} else if (!this.getClass().equals(other.getClass()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CalculatedField [context=" + context + ", getFieldType()=" + getFieldType() + ", getHeader()="
				+ getHeader() + ", getName()=" + getName() + ", isReadOnly()=" + isReadOnly() + ", isSortable()="
				+ isSortable() + "]";
	}

}
