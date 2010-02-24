package org.mozzes.utils.filtering;

import java.util.Comparator;

/**
 * Filter zahteva da vrednost property-ja objekta bude jednaka ili veca od inicijalno odredjene vrednosti
 * 
 * @author Perica Milosevic
 * @version 1.7
 */
public class GEPropertyFilter<ObjectType, PropertyType> extends ComparePropertyFilter<ObjectType, PropertyType> {

	public GEPropertyFilter(String propertyName, PropertyType compareValue) {
		super(propertyName, compareValue);
	}

	public GEPropertyFilter(String propertyName, PropertyType compareValue, Comparator<PropertyType> comparator) {
		super(propertyName, compareValue, comparator);
	}

	public  boolean isAcceptable(ObjectType object) {
		if (object == null)
			return false;

		return compareProperty(object) >= 0;
	}

}
