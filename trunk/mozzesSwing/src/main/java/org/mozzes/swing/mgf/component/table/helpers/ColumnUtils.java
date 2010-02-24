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
