package org.mozzes.utils.filtering;

import java.util.Set;

/**
 * Filter koji proverava da li se vrednost propertija nalazi u odgovarajucem skupu vrednosti
 * 
 * @author Perica Milosevic
 * @version 1.7
 */
public class InPropertyFilter<ObjectType, PropertyType> extends PropertyFilter<ObjectType, PropertyType> {

	private Set<PropertyType> compareValues;

	public InPropertyFilter(String propertyName, Set<PropertyType> compareValues) {
		super(propertyName, null);
		this.compareValues = compareValues;
	}

	public boolean isAcceptable(ObjectType object) {
		if (object == null)
			return false;

		return compareValues.contains(getPropertyValue(object));
	}

	public Set<PropertyType> getInSet() {
		return compareValues;
	}
}
