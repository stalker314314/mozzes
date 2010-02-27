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
 * Slozeni filter koji sadrzi logicki izraz nad skupom filtera
 * 
 * @author Perica Milosevic
 * @version 1.7
 */
public class ExpressionFilter<T> extends FilterWrapper<T> {

	/**
	 * Creates an expression filter operand that lets everything pass throgh
	 */
	public ExpressionFilter() {
		this(new FakeFilter<T>());
	}

	/**
	 * Creates an expression filter operand that wrapps passed in filter
	 */
	public ExpressionFilter(Filter<T> filter) {
		super(filter);
	}

	/**
	 * Dodavanje novog uslova u filter koji ce biti vezan logickom vezom AND sa prethodno definisanim filterom
	 */
	public ExpressionFilter<T> and(Filter<T> filter) {
		setWrappedFilter(new AndFilterExpression<T>(getWrappedFilter(), filter));
		return this;
	}

	/**
	 * Dodavanje novog uslova u filter koji ce biti vezan logickom vezom OR sa prethodno definisanim filterom
	 */
	public ExpressionFilter<T> or(Filter<T> filter) {
		setWrappedFilter(new OrFilterExpression<T>(getWrappedFilter(), filter));
		return this;
	}

}

abstract class BinaryFilterExpression<T> extends FilterWrapper<T> {
	private Filter<T> filterOperand2;

	BinaryFilterExpression(Filter<T> filterOperand1, Filter<T> filterOperand2) {
		super(filterOperand1);
		setFilterOperand2(filterOperand2);
	}

	Filter<T> getFilterOperand1() {
		return getWrappedFilter();
	}

	Filter<T> getFilterOperand2() {
		return filterOperand2;
	}

	void setFilterOperand2(Filter<T> filterOperand2) {
		if (filterOperand2 == null)
			throw new NullPointerException();

		this.filterOperand2 = filterOperand2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((filterOperand2 == null) ? 0 : filterOperand2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().isInstance(o))
			return false;
		if (!(o instanceof BinaryFilterExpression<?>))
			return false;
		BinaryFilterExpression<?> that = (BinaryFilterExpression<?>) o;
		return this.getFilterOperand1().equals(that.getFilterOperand1())
				&& this.getFilterOperand2().equals(that.getFilterOperand2());
	}

	@Override
	public boolean removeFilter(Filter<T> filter) {
		if (super.removeFilter(filter))
			return true;

		if (getFilterOperand2().equals(filter)) {
			setFilterOperand2(new FakeFilter<T>());
			return true;
		}

		if (getFilterOperand2() instanceof FilterWrapper<?>)
			return ((FilterWrapper<T>) getFilterOperand2()).removeFilter(filter);
		else
			return false;

	}

}

class AndFilterExpression<T> extends BinaryFilterExpression<T> {

	AndFilterExpression(Filter<T> filterOperand1, Filter<T> filterOperand2) {
		super(filterOperand1, filterOperand2);
	}

	@Override
	public boolean isAcceptable(T object) {
		return getFilterOperand1().isAcceptable(object)
				&& getFilterOperand2().isAcceptable(object);
	}
}

class OrFilterExpression<T> extends BinaryFilterExpression<T> {

	OrFilterExpression(Filter<T> filterOperand1, Filter<T> filterOperand2) {
		super(filterOperand1, filterOperand2);
	}

	@Override
	public boolean isAcceptable(T object) {
		return getFilterOperand1().isAcceptable(object)
				|| getFilterOperand2().isAcceptable(object);
	}
}
