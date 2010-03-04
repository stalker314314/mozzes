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

/**
 * Prosti wraper oko Filter-a. <br>
 * <br>
 * Ovaj filter ce prilikom provere objekta vracati istu vrednost kao i wrapovani filter, a ukoliko je potrebno drugacije
 * ponasanje onda se nasledjuje ova klasa i modifikuje metoda isAcceptable. <br>
 * <br>
 * npr. NotFilter ce biti wrapper oko drugog filter-a i vracace uvek suprotnu vrednost od wrappovanog filtera.
 * 
 * @author Perica Milosevic
 * @version 1.7
 */
public abstract class FilterWrapper<T> implements Filter<T> {

	/**
	 * Filter oko koga je napravljen wrapper
	 */
	private Filter<T> wrappedFilter = null;

	public FilterWrapper(Filter<T> wrappedFilter) {
		setWrappedFilter(wrappedFilter);
	}

	protected Filter<T> getWrappedFilter() {
		return wrappedFilter;
	}

	public void setWrappedFilter(Filter<T> wrappedFilter) {
		if (wrappedFilter == null)
			throw new NullPointerException();

		this.wrappedFilter = wrappedFilter;
	}

	/**
	 * Uklanja wrapovani filter. Nakon uklanjanja ovaj filter ce vracati true za sve objekte.
	 */
	public void deleteWrappedFilter() {
		setWrappedFilter(new FakeFilter<T>());
	}

	public boolean isAcceptable(T object) {
		return getWrappedFilter().isAcceptable(object);
	}


	@Override
	public boolean equals(Object o) {
		if (!this.getClass().isInstance(o))
			return false;

		if (!(o instanceof FilterWrapper<?>))
			return false;
		
		FilterWrapper<?> that = (FilterWrapper<?>) o;
		return this.getWrappedFilter().equals(that.getWrappedFilter());
	}

	@Override
	public int hashCode() {
		return this.getWrappedFilter().hashCode();
	}

	/**
	 * Ukoliko wrapovani filter odgovara prosledjenom filteru (equals vraca true) onda ce biti uklonjen <br>
	 * <br>
	 * Ukoliko je potrebno bezuslovno uklanjanje wrappovanog filtera onda koristiti metodu deleteWrappedFilter()
	 * 
	 * @param filter filter koji ce biti postavljen umesto vrapovanog ukoliko odgovara po kriterijumu (equals = true)
	 * @return da li je obavljena zamena?
	 */
	public boolean removeFilter(Filter<T> filter) {
		if (getWrappedFilter().equals(filter)) {
			setWrappedFilter(new FakeFilter<T>());
			return true;
		}

		if (getWrappedFilter() instanceof FilterWrapper<?>)
			return ((FilterWrapper<T>) getWrappedFilter()).removeFilter(filter);
		else
			return false;
	}

}
