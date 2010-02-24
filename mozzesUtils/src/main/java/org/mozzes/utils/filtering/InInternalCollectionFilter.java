package org.mozzes.utils.filtering;

import java.util.Collection;

/**
 * [LC-IMPL] [TLB-2810] [nenadl] Filter koji proverava da li se odredjena vrednost nalazi u kolekciji vrednosti u okviru
 * objekta koji se filtrira.
 * 
 * @author nenadl
 */
public class InInternalCollectionFilter<ObjectType, PropertyType> extends PropertyFilter<ObjectType, PropertyType> {

	/**
	 * Konstruktor.
	 * 
	 * @param collectionName - naziv kolekcije vrednosti u okviru objekta.
	 * @param compareValue - vrednost za koju se proverava da li postoji u kolekciji.
	 */
	public InInternalCollectionFilter(String collectionName, PropertyType compareValue) {
		super(collectionName, compareValue);
	}

	/**
	 * Proverava da li odredjeni objekat prolazi kroz filter.
	 * 
	 * @param object - objekat koji treba proveriti.
	 * @return - da li objekat prolazi kroz filter.
	 */
	public boolean isAcceptable(ObjectType object) {

		if (object == null) {
			return false;
		}

		Collection<PropertyType> propertyCollection = getPropertyValueCollection(object);

		boolean filterPassed = false;
		if (propertyCollection != null) {
			filterPassed = propertyCollection.contains(getCompareValue());
		}

		return filterPassed;
	}
}
