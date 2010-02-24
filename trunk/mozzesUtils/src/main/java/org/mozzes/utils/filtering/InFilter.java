package org.mozzes.utils.filtering;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Filter koji proverava da li se prosledjeni objekat nalazi u odgovarajucoj kolekciji objekata
 * 
 * @author Perica Milosevic
 * @version 1.7
 */
public class InFilter<T> implements Filter<T> {

	private Set<T> inSet;

	/**
	 * Konstruise InFilter<br>
	 * <b>[NOTE] inSet ce biti kopiran i bilo kakve izmene nad njim(dodavanje, uklanjanje elemenata)<br>
	 * izvrsene nakon inicijalizacije se nece reflektovati u filteru, mozete ga modifikovati tako<br>
	 * sto koristeci metodu {@link InFilter#getInSet()} uzmete referencu na inSet i radite sa njom</b>
	 */
	public InFilter(Collection<? extends T> inSet) {
		if (inSet == null)
			throw new IllegalArgumentException("Collection required!");

		this.inSet = new HashSet<T>(inSet);
	}

	public boolean isAcceptable(T object) {
		return inSet.contains(object);
	}

	public Set<T> getInSet() {
		return inSet;
	}
}
