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
