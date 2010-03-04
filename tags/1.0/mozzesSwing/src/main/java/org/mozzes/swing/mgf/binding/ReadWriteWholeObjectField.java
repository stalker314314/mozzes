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
package org.mozzes.swing.mgf.binding;

import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datasource.BeanDataSource;

/**
 * Returns the passed object, but also lets you replace it id the source
 * 
 * @author milos
 */
class ReadWriteWholeObjectField<T> extends Field<T, T> {
	private BeanDataSource<T> source;

	/**
	 * Constructs the {@link ReadWriteWholeObjectField}
	 * 
	 * @param source {@link BeanDataSource Source} that will be used for storing and retrieving data
	 */
	public ReadWriteWholeObjectField(BeanDataSource<T> source) {
		this(source, true);
	}

	/**
	 * @param source {@link BeanDataSource Source} that will be used for storing and retrieving data
	 */
	public ReadWriteWholeObjectField(BeanDataSource<T> source, boolean readOnly) {
		super(source.getDataType(), readOnly);
		this.source = source;
	}

	@Override
	public T getValue(T object) {
		return object;
	}

	@Override
	protected void valueSetter(T object, T value) {
		source.setData(value);
	}

	@Override
	public boolean isEditableFor(T object) {
		boolean result = false;
		T data = source.getData();
		if (data == null && object == null)
			result = true;
		if (data != null && object != null)
			result = data.equals(object);
		return !isReadOnly() && result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ReadWriteWholeObjectField<?>))
			return false;
		ReadWriteWholeObjectField<?> other = (ReadWriteWholeObjectField<?>) obj;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ReadWriteWholeObjectField [getFieldType()=" + getFieldType() + ", getHeader()="
				+ getHeader() + ", getName()=" + getName() + ", isReadOnly()=" + isReadOnly() + ", isSortable()="
				+ isSortable() + "]";
	}
}
