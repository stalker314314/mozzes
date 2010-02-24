package org.mozzes.swing.mgf.component.table.editing;

import javax.swing.CellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Provides methods for setting up cell editors
 * 
 * @author milos
 */
public class CellEditingFacility {
	private final JTable attachedTable;

	/**
	 * @param table Setups table to work with the facility
	 */
	public CellEditingFacility(JTable table) {
		this.attachedTable = table;
	}

	/**
	 * @return Table attached to this facility instance
	 */
	public JTable getAttachedTable() {
		return attachedTable;
	}

	/**
	 * Sets a {@link CellEditor} for a column
	 * 
	 * @param column Column for which to set the editor
	 * @param editor Editor that should be set
	 */
	public void setColumnEditor(int column, TableCellEditor editor) {
		attachedTable.getColumnModel().getColumn(column).setCellEditor(editor);
	}

	/**
	 * Sets a {@link CellEditor} for a specific class
	 * 
	 * @param clazz Class for which to set the editor
	 * @param editor Editor that should be set
	 */
	public void setClassEditor(Class<?> clazz, TableCellEditor editor) {
		attachedTable.setDefaultEditor(clazz, editor);
	}
}
