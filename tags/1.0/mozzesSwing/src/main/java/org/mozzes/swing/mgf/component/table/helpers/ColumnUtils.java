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
package org.mozzes.swing.mgf.component.table.helpers;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class ColumnUtils {
	public static void packColumns(JTable table) {
		final int margin = 1;
		TableColumnModel columnModel = table.getColumnModel();
		for (int columnIndex = 0, columnCount = columnModel.getColumnCount(); columnIndex < columnCount; columnIndex++) {
			TableColumn column = columnModel.getColumn(columnIndex);
			int width = column.getPreferredWidth();

			TableCellRenderer renderer = column.getHeaderRenderer();
			if (renderer == null) {
				renderer = table.getTableHeader().getDefaultRenderer();
			}
			Component comp = renderer.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, 0, 0);
			width = comp.getPreferredSize().width;

			// Get maximum width of column data
			for (int rowIndex = 0, rowCount = table.getRowCount(); rowIndex < rowCount; rowIndex++) {
				Component component = renderer.getTableCellRendererComponent(table, table.getValueAt(rowIndex,
						columnIndex), false, false, rowIndex, columnIndex);
				int prefferedWidht = component.getPreferredSize().width;
				width = Math.max(width, prefferedWidht);
			}
			// Add margin
			width += 2 * margin;
			// Set the width
			column.setPreferredWidth(width);
//			column.setMinWidth(width);
//			column.setMaxWidth(width);
//			column.setWidth(width);
		}
		table.getTableHeader().validate();
		table.getTableHeader().repaint();
	}
}
