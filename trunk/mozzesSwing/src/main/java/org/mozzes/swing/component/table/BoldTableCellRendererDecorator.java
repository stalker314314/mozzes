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

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * [AMD-58] Bold table cell renderer dekorator. Dodaje bold aspekt na postojecji
 * table cell renderer u toku izvrshavanja.
 * 
 * @author nenadl
 * 
 */
public class BoldTableCellRendererDecorator extends TableCellRendererDecorator {

	public BoldTableCellRendererDecorator(
			TableCellRenderer cellRendererToDecorate) {
		super(cellRendererToDecorate);
	}

	/**
	 * Dodaje bold aspekt na font postojecjeg cell renderera.
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		Component component = cellRendererToDecorate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		component.setFont(component.getFont().deriveFont(Font.BOLD));
		
		return component;
	}
}
