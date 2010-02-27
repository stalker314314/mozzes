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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;



/**
 * Helper class to add context menu to JTable rows
 * 
 * @author milos
 */
public class ContextMenuTrigger extends MouseAdapter {

	private final JPopupMenu popupMenu;
	private JTable table;
	private final List<PopupEventListener> listeners = new ArrayList<PopupEventListener>();

	/**
	 * Creates an instance which will open passed menu on context trigger(right click)
	 * 
	 * @param menu Context menu to be shown
	 */
	public ContextMenuTrigger(JPopupMenu menu) {
		popupMenu = menu;
		popupMenu.addPopupMenuListener(new PopupMenuAdapter());
	}

	@Override
	public final void mousePressed(MouseEvent e) {
		if (!e.isPopupTrigger())
			return;

		selectRow(e);
	}

	@Override
	public final void mouseReleased(MouseEvent e) {
		if (!e.isPopupTrigger())
			return;

		selectRow(e);

		showPopup(e);
	}

	private void initTable(MouseEvent e) {
		if (table != null)
			return;
		if (e.getComponent() instanceof JTable) {
			table = (JTable) e.getComponent();
		} else {
			throw new ClassCastException(
					"This adapter can be added to JTable and its sublcasses only!");
		}
	}

	private void selectRow(MouseEvent e) {
		initTable(e);

		int row = table.rowAtPoint(e.getPoint());
		if (row == -1)
			return;

		table.setRowSelectionInterval(row, row);
	}

	private void showPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	private class PopupMenuAdapter implements PopupMenuListener {
		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			for (PopupEventListener listener : listeners) {
				listener.showing();
			}
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			for (PopupEventListener listener : listeners) {
				listener.hiding();
			}
		}

		@Override
		public void popupMenuCanceled(PopupMenuEvent e) {
			for (PopupEventListener listener : listeners) {
				listener.canceled();
			}
		}
	}

	public void addEventListener(PopupEventListener listener) {
		listeners.add(listener);
	}

	public void removeEventListener(PopupEventListener listener) {
		listeners.remove(listener);
	}

}