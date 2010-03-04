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
