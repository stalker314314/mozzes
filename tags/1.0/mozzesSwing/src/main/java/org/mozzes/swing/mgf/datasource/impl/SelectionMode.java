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
