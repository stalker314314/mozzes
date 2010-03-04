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
