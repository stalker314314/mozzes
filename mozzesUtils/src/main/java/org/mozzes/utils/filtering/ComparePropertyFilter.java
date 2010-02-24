package org.mozzes.utils.filtering;

import java.util.Comparator;

/**
 * Apstraktni filter koji se koristi ukoliko je potrebno obaviti poredjenje vrednosti property-ja sa odredjenom
 * vrednoscu.
 * 
 * @author Perica Milosevic
 * @version 1.7
 */
public abstract class ComparePropertyFilter<ObjectType, PropertyType> extends PropertyFilter<ObjectType, PropertyType> {

	/**
	 * Komparator koji se koristi za poredjenje vrednosti property-ja.
	 */
	private Comparator<PropertyType> comparator = null;

	public ComparePropertyFilter(String propertyName, PropertyType compareValue) {
		this(propertyName, compareValue, null);
	}

	/**
	 * Kreiranje filter-a gde ce za poredjenje property-ja biti koriscen prosledjeni Comparator. Ukoliko je komparator
	 * null vrednost onda ce vrednost property-a biti tretirana kao Comparable pa ce biti pokusano direktno poredjenje,
	 * a ukoliko to nije moguce onda ce prilikom poredjenja biti bacen Exception
	 * 
	 * @param propertyName naziv property-ja koji se poredi
	 * @param compareValue vrednost sa kojom se vrsi poredjenje
	 * @param comparator komparator koji se koristi za poredjenje
	 */
	public ComparePropertyFilter(String propertyName, PropertyType compareValue, Comparator<PropertyType> comparator) {
		super(propertyName, compareValue);
		setComparator(comparator);
	}

	/**
	 * Poredi vrednost navedenog property-ja iz prosledjenog objekta sa inicijalno odredjenom vrednoscu.
	 * 
	 * @param object objekat ciji se property poredi
	 * @return isto kao kod Java Comparator-a
	 */
	@SuppressWarnings("unchecked")
	public int compareProperty(ObjectType object) {
		PropertyType propertyValue = getPropertyValue(object);

		if (getCompareValue() == null)
			return propertyValue == null ? 0 : 1;
		else if(propertyValue == null)
			return -1;
		
		if (getComparator() != null)
			return getComparator().compare(propertyValue, getCompareValue());

		if (propertyValue instanceof Comparable<?>)
			return ((Comparable<PropertyType>) propertyValue).compareTo(getCompareValue());

		throw new RuntimeException("Unable to compare values!");
	}

	public Comparator<PropertyType> getComparator() {
		return comparator;
	}

	private void setComparator(Comparator<PropertyType> comparator) {
		this.comparator = comparator;
	}

}
