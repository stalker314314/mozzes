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
