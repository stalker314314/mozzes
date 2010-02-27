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
package org.mozzes.swing.mgf.component.table.rendering.decorations.rules.value;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule;

public class MoreThan<T> extends CellDecorationRule {
	private final Comparable<T> than;
	private final InstanceOf valueClass;

	public MoreThan(Comparable<T> than) {
		if (than == null)
			throw new IllegalArgumentException("Comparsion value cannot be null!");
		this.than = than;
		valueClass = new InstanceOf(than.getClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean appliesTo(Context context) {
		Object value = context.getValue();
		if (value == null)
			return false;
		return valueClass.appliesTo(context) && than.compareTo((T) value) < 0;
	}
}
