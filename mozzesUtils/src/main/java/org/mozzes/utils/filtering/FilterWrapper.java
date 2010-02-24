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
