package org.mozzes.utils.filtering;

/**
 * Wraper oko filter-a koji implementira logicku operaciju NOT
 * 
 * @author Perica Milosevic
 * @version 1.7
 */
public class NotFilter<T> extends FilterWrapper<T> {

	public NotFilter(Filter<T> wrappedFilter) {
		super(wrappedFilter);
	}

	@Override
	public boolean isAcceptable(T object) {
		return !getWrappedFilter().isAcceptable(object);
	}

}
