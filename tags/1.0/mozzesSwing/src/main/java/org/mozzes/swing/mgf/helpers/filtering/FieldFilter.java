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
package org.mozzes.swing.mgf.helpers.filtering;

import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.utils.filtering.Filter;


/**
 * Base class for all filters which use a {@link Field} in the process of filtering
 * 
 * @author milos
 * 
 * @param <T> Type of the bean
 * @param <Q> Type of the field value
 */
abstract class FieldFilter<T, Q> implements Filter<T> {

	private Field<T, Q> field;

	protected FieldFilter() {
	}

	protected FieldFilter(Field<T, Q> field) {
		this.field = field;
	}

	public void setField(Field<T, Q> field) {
		this.field = field;
	}

	public Field<T, Q> getField() {
		return field;
	}
}
