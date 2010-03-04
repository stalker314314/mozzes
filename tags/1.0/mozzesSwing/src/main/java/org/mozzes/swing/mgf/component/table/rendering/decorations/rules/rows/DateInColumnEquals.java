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
package org.mozzes.swing.mgf.component.table.rendering.decorations.rules.rows;

import java.util.Date;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule;
import org.mozzes.utils.misc.DateComparator;


/**
 * Compares dates ignoring time parts.
 * 
 * @author draganm
 */
public class DateInColumnEquals extends CellDecorationRule {

	private Date value;
	private final int column;
	
	/**
	 * Constructor
	 * 
	 * @param column Column index which value is being compared / evaluated
	 * @param value Value that is compared with value at provided colum
	 */
	public DateInColumnEquals(int column, Date value) {
		if (value == null)
			throw new IllegalStateException("Value must not be null");
		
		this.column = column;
		
		this.value = value;
	}

	@Override
	public boolean appliesTo(Context context) {
		Object valueAt = context.getTable().getValueAt(context.getRow(), column);
		
		if (valueAt == null && value == null)
			return true;
		else if (valueAt == null || value == null)
			return false;

		DateComparator dc = new DateComparator();
		return dc.compare((Date)valueAt, value) == 0;
	}

}
