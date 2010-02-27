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
package org.mozzes.swing.mgf.datarenderer.table.sumrow;

import java.util.ArrayList;
import java.util.List;

import org.mozzes.swing.mgf.datamodel.fields.CalculatedField;
import org.mozzes.utils.filtering.Filter;


public abstract class SumCalculationField<T, F> extends CalculatedField<List<T>, F> {
	private SumRowBuilder<T> builder;
	private final List<T> tempList = new ArrayList<T>();

	public SumCalculationField(Class<F> fieldType) {
		super(fieldType);
	}

	@Override
	public final F getValue(List<T> object) {
		Filter<T> filter = getFilter();
		tempList.clear();
		for (T t : object) {
			if (filter.isAcceptable(t))
				tempList.add(t);
		}
		return getSum(tempList);
	}

	/**
	 * Implement this method to do custom sum calculation
	 * 
	 * @param objects List of objects for which the sum should be calculated
	 * @return Calculated sum
	 */
	public abstract F getSum(List<T> objects);

	protected boolean shouldProcess(T object) {
		return getFilter().isAcceptable(object);
	}

	private Filter<T> getFilter() {
		return getBuilder().getAttachedTableRenderModel().getFilteringFacility().getFilter();
	}

	void setBuilder(SumRowBuilder<T> builder) {
		if (this.builder != null)
			throw new IllegalStateException("Builder should not be set twice!\n" +
					"(Hint: You cannot use same instance of SumCalculationField for two SumRowBuilders!)");
		this.builder = builder;
	}

	SumRowBuilder<T> getBuilder() {
		return builder;
	}
}