package org.mozzes.swing.mgf.datarenderer.table;

import javax.swing.JScrollPane;
import javax.swing.JTable;

class ScrollPaneWithRowHeader extends JScrollPane {
	private static final long serialVersionUID = 1L;
	private final JTable rowHeaderTable;

	public ScrollPaneWithRowHeader(JTable mainTable, JTable rowHeaderTable) {
		super(mainTable);
		this.rowHeaderTable = rowHeaderTable;
	}

	public JTable getRowHeaderTable() {
		return rowHeaderTable;
	}

}