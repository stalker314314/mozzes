package org.mozzes.utils.filtering;

/**
 * Filtriranje objekata, tj. provera da li objekat ispunjava zeljene uslove.
 * 
 * @author Perica Milosevic
 * @version 1.7
 */
public interface Filter<T> {

	/**
	 * Da li objekat ispunjava zeljene uslove
	 * 
	 * @param object - objekat koji se proverava
	 * @return true - ukoliko objekat ispunjava potrebne uslove
	 */
	public boolean isAcceptable(T object);
}
