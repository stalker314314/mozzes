package org.mozzes.swing.mgf.datasource.impl;

import java.util.List;

import javax.swing.ListSelectionModel;

import org.mozzes.swing.mgf.datasource.SelectionListDataSource;


/**
 * Represents the selection mode for the {@link SelectionListDataSource}
 * 
 * @author milos
 */
public enum SelectionMode {
	/**
	 * Only one object can be selected
	 */
	SINGLE_SELECTION,
	/**
	 * More than one object can be selected
	 */
	MULTIPLE_SELECTION;

	void filterSelection(List<Integer> selection) {
		if (selection == null)
			return;
		switch (this) {
		case SINGLE_SELECTION:
			singleSelectionFilter(selection);
			break;
		case MULTIPLE_SELECTION:
			break;
		default:
			break;
		}
	}

	private void singleSelectionFilter(List<Integer> selection) {
		if (selection.isEmpty())
			return;
		Integer first = selection.get(0);
		selection.clear();
		selection.add(first);
	}

	public int getSwingSelectionMode() {
		int selectionMode;
		switch (this) {
		case SINGLE_SELECTION:
			selectionMode = ListSelectionModel.SINGLE_SELECTION;
			break;
		case MULTIPLE_SELECTION:
			selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
			break;
		default:
			selectionMode = ListSelectionModel.SINGLE_SELECTION;
			break;
		}
		return selectionMode;
	}
}
