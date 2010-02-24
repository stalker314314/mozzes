package org.mozzes.utils.filtering;

/**
 * Filter tretira vrednost property-ja kao string i proverava da li je pocetak string-a jednak zadatom podstringu. Po
 * default-u pretraga nije case sensitive.
 * 
 * @author Perica Milosevic
 * @version 1.7
 */
public class LikePropertyFilter<ObjectType, PropertyType> extends ComparePropertyFilter<ObjectType, PropertyType> {

	/**
	 * Da li je poredjenje case sensitive?
	 */
	private boolean caseSensitive = false;
	private boolean trim = true;
	private boolean useContains = false;
	private String comparisonString;

	public LikePropertyFilter(String propertyName, String compareValue) {
		super(propertyName, null);
		if (compareValue == null) {
			throw new IllegalArgumentException();
		}
		this.comparisonString = compareValue;
	}

	public LikePropertyFilter(String propertyName, String compareValue, boolean useContains) {
		super(propertyName, null);
		if (compareValue == null) {
			throw new IllegalArgumentException();
		}
		this.comparisonString = compareValue;
		this.useContains = useContains;
	}

	public boolean isAcceptable(ObjectType object) {
		if (object == null)
			return false;

		if (comparisonString.isEmpty()) {
			return true;
		}

		if (comparisonString.isEmpty())
			return true;

		Object propertyValue = getPropertyValue(object);

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
		if (trim) {
			startString = startString.trim();
			valueString = valueString == null ? "" : valueString.trim();
		}
		return useContains ?
				valueString.indexOf(startString) != -1 :
				valueString.startsWith(startString);
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public LikePropertyFilter<ObjectType, PropertyType> setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		return this;
	}

	public LikePropertyFilter<ObjectType, PropertyType> setTrim(boolean trim) {
		this.trim = trim;
		return this;
	}

	public boolean isTrim() {
		return trim;
	}

	public LikePropertyFilter<ObjectType, PropertyType> setUseContains(boolean useContains) {
		this.useContains = useContains;
		return this;
	}

	public boolean isUseContains() {
		return useContains;
	}

}
