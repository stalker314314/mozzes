package org.mozzes.utils.filtering;

/**
 * Filter koji zahteva jednakost vrednosti property-ja objekta sa odredjenom vrednoscu
 * 
 * @author Perica Milosevic
 * @version 1.7
 */
public class EQPropertyFilter<ObjectType, PropertyType> extends PropertyFilter<ObjectType, PropertyType> {

	public EQPropertyFilter(String propertyName, PropertyType compareValue) {
		super(propertyName, compareValue);
	}

	public boolean isAcceptable(ObjectType object) {
		if (object == null)
			return false;

		if (getCompareValue() == null)
			return getPropertyValue(object) == null;

		return getCompareValue().equals(getPropertyValue(object));
	}

}