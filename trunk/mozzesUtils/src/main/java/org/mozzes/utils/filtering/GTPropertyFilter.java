package org.mozzes.utils.filtering;

import java.util.Comparator;

/**
 * Filter zahteva da vrednost property-ja objekta bude veca od inicijalno odredjene vrednosti
 * 
 * @author Perica Milosevic
 * @version 1.7
 */
public class GTPropertyFilter<ObjectType, PropertyType> extends ComparePropertyFilter<ObjectType, PropertyType> {

	public GTPropertyFilter(String propertyName, PropertyType compareValue) {
		super(propertyName, compareValue);
	}

	public GTPropertyFilter(String propertyName, PropertyType compareValue, Comparator<PropertyType> comparator) {
		super(propertyName, compareValue, comparator);
	}

	public boolean isAcceptable(ObjectType object) {
		if (object == null)
			return false;

		return compareProperty(object) > 0;
	}

}
