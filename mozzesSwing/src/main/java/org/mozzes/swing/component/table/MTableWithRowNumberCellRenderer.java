package org.mozzes.swing.component.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Cell renderer za kolonu koja predstavlja redni broj.
 * 
 * @author neda
 * 
 */
public class MTableWithRowNumberCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		Object newValue = null;
		if (column == 0) {
			newValue = Integer.valueOf(row + 1);
			setHorizontalAlignment(RIGHT);
		} else {
			newValue = value;
		}

		return super.getTableCellRendererComponent(table, newValue, isSelected, hasFocus, row, column);
	}
}
