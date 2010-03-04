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

/**
 * Object will pass if the field.getValue(object).toString() starts with the specified comparison string
 * 
 * @author milos
 * 
 * @param <T> Type of the bean
 * @param <Q> Type of the field value
 */
public class FieldLikeFilter<T, Q> extends FieldFilter<T, Q> {
	private boolean caseSensitive;
	private String comparisonString;

	public FieldLikeFilter() {
	}

	/**
	 * @param field Field whose value for an object is to be compared
	 * @param comparisonString String with which the {@link Field} value will be compared
	 */
	public FieldLikeFilter(Field<T, Q> field, String comparisonString) {
		super(field);
		this.comparisonString = comparisonString;
	}

	@Override
	public boolean isAcceptable(T object) {
		if (getField() == null)
			throw new IllegalStateException("Filter Field must be set!");

		if (object == null)
			return false;

		if (comparisonString == null || comparisonString.isEmpty())
			return true;

		Q propertyValue = getField().getValue(object);

		if (propertyValue == null)
			return false;

		String startString;
		String valueString;
		if (isCaseSensitive()) {
			startString = comparisonString;
			valueString = propertyValue.toString();
		} else {
			startString = comparisonString.toLowerCase();
			valueString = propertyValue.toString().toLowerCase();
		}

		return valueString.startsWith(startString);
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setComparisonString(String comparisonString) {
		this.comparisonString = comparisonString;
	}

	public String getComparisonString() {
		return comparisonString;
	}
}
