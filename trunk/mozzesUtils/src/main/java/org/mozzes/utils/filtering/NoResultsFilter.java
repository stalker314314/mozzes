package org.mozzes.utils.filtering;

/**
 * Kvazi-filter koji ne vraca nijedan objekat. Zgodan za pravljenje slozenih filtera.
 * 
 * @author Ivan Ivankovic
 * @version 1.7
 */
public class NoResultsFilter<T> implements Filter<T> {

	public boolean isAcceptable(T object) {
		return false;
	}

	@Override
	public boolean equals(Object that) {
		return (that instanceof NoResultsFilter<?>);
	}

	@Override
	public int hashCode() {
		return 0;
	}

}
