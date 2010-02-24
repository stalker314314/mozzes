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
