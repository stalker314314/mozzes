package org.mozzes.utils.filtering;

public class FilterContainer<T> extends FilterWrapper<T> {
	public FilterContainer() {
		super(new FakeFilter<T>());
	}

	public FilterContainer(Filter<T> wrappedFilter) {
		super(wrappedFilter);
	}

	@Override
	public void setWrappedFilter(Filter<T> wrappedFilter) {
		Filter<T> f = wrappedFilter == null ? new FakeFilter<T>() : wrappedFilter;
		super.setWrappedFilter(f);
	}

	@Override
	public void deleteWrappedFilter() {
		setWrappedFilter(null);
	}
}