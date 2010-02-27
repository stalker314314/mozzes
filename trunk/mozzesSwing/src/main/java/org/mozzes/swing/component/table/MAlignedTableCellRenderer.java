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
package org.mozzes.swing.component.table;

import javax.swing.JTable;

/**
 * [AMD-57]
 * MTable Cell renderer za kolone sa proizvoljnim poravnanjem teksta.
 * 
 */
public class MAlignedTableCellRenderer extends MDefaultTableCellRenderer {

	private static final long serialVersionUID = 5769524582663361700L;
	
	/**
	 * Poravnanje teksta.
	 */
	private int alignment;

	/**
	 * Param konstruktor.
	 * @param orderColumn - da li se upotrebljava kolona sa rednim brojem.
	 */
	public MAlignedTableCellRenderer(Boolean orderColumn, int alignment) {
		super(orderColumn);
		this.alignment = alignment;
	}

	/**
	 * Custom obrada prikaza.
	 */
	@Override
	protected void doCustom(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setHorizontalAlignment(alignment);
	}
}
