package org.mozzes.swing.mgf.component.table.helpers;

import static javax.swing.JComponent.*;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JTable;
import javax.swing.KeyStroke;

/**
 * Klasa koja upravlja fokusom u okviru tabele na taj nacin da se samo krece kroz editabilna polja na formi. <br>
 * TODO [MILOS] Pepisati citavu klasu, ima nekonsistentnosti<br>
 * TODO [Vaso] Doraditi tako da moze da se definise da li se zeli samo kroz editabilna ili kroz sva i da se definise
 * smer kretanja nakon enter dugmeta (horizontalno ili vertikalno)
 * 
 * @author vaso
 */
public class TableCellFocusManager {

	private final JTable table;

	public TableCellFocusManager(JTable table) {
		this.table = table;
		init();
	}

	private void init() {

		Action handleEnter = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {

				int originalRowIndex = TableCellFocusManager.this.table.getSelectedRow();
				int originalColumnIndex = TableCellFocusManager.this.table.getSelectedColumn() + 1;

				if (TableCellFocusManager.this.table.getCellEditor() != null) {
					TableCellFocusManager.this.table.getCellEditor().stopCellEditing();
				}

				if (isAllNonEditable()) {
					// NOTE: Check if this should be done at all
					TableCellFocusManager.this.table.changeSelection(originalRowIndex, originalColumnIndex, false,
							false);
					return;
				}

				boolean firstRowCatched = false;
				boolean changed = false;

				int i = originalRowIndex;
				int j = originalColumnIndex;

				while (true) {
					if (i == -1)
						break;

					if (i > TableCellFocusManager.this.table.getRowCount() - 1) {
						i = 0;
						firstRowCatched = true;
					}
					while (true) {
						if ((j > TableCellFocusManager.this.table.getColumnCount() - 1)
								|| isStartCellCatched(firstRowCatched, i, j, originalRowIndex, originalColumnIndex)) {
							j = 0;
							break;
						}
						if (TableCellFocusManager.this.table.isCellEditable(i, j)) {
							final int row = i;
							final int column = j;
							TableCellFocusManager.this.table.changeSelection(row, column, false, false);
							changed = true;
							break;
						}
						j++;
					}
					if (changed || isStartCellCatched(firstRowCatched, i, j, originalRowIndex, originalColumnIndex))
						break;
					i++;
				}
			}
		};

		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
		InputMap map = table.getInputMap(WHEN_FOCUSED);
		map.put(enter, "handleEnter");
		map.put(tab, "handleTab");
		table.getActionMap().put("handleEnter", handleEnter);
		table.getActionMap().put("handleTab", handleEnter);
	}

	/**
	 * Da li je dostignuta polazna celija
	 * 
	 * @param currentRowIndex - index trenutnog reda
	 * @param currentColumnIndex - index trenutne kolone
	 */
	private boolean isStartCellCatched(boolean firstRowCatched, int currentRowIndex, int currentColumnIndex,
			int currentSelectedRowIndex, int currentSelectedColumnIndex) {
		return firstRowCatched && currentRowIndex == currentSelectedRowIndex
				&& currentColumnIndex == currentSelectedColumnIndex;
	}

	/**
	 * Proverava da li su sve celije u tabeli needitabilne
	 * 
	 * @return - true ako jesu, false u suprotnom
	 */
	private boolean isAllNonEditable() {
		for (int i = 0; i < table.getRowCount(); i++)
			for (int j = 0; j < table.getColumnCount(); j++)
				if (table.isCellEditable(i, j))
					return false;
		return true;
	}
}
