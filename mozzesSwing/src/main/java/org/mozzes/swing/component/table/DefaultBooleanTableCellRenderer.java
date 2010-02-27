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
package org.mozzes.swing.component.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * [FINIMPL] [FIN-59] Renderer za celiju u tabeli koja prikazuje check box.
 * 
 * @author nenadl
 */
public class DefaultBooleanTableCellRenderer extends JCheckBox implements TableCellRenderer {

	/**
	 * Svuid.
	 */
	private static final long serialVersionUID = 5575496953821939869L;


	/**
	 * Boja za farbanje pozadine reda tabele kada je on selektovan.
	 */
	private static final Color BACKGROUND_COLOR_BLUE = new Color(57, 105, 138);

	/**
	 * Default konstruktor.
	 */
	public DefaultBooleanTableCellRenderer() {
		super();
		setOpaque(true);
	}

	/**
	 * Vracja komponentu koju treba iscrtati u cjeliji.
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		
		LookAndFeel laf= UIManager.getLookAndFeel();
		
		Color alternateColor = (Color) laf.getDefaults().get("Table.alternateRowColor");
		
		if (isSelected) {
			setBackground(BACKGROUND_COLOR_BLUE);
		} else {
			// Belo-siva pozadina naizmenicno
			if (row % 2 == 0) {
				setBackground(alternateColor);
			} else {
				setBackground(Color.white);
			}
		}

		if (value == null) {
			return null;
		}

		Boolean bool = (Boolean) value;

		this.setSelected(bool.booleanValue());
		this.setHorizontalAlignment(SwingConstants.CENTER);

		// omogucavanje da korisnik uradi nesto specificno.
		doCustom(table, value, isSelected, hasFocus, row, column);
		
		return this;
	
	}
	
	/**
	 * Mogucnost kastom podesavnja.
	 * 
	 * @param table tabela.
	 * @param value vrednost.
	 * @param isSelected true red je selektovan, suprotno false.
	 * @param hasFocus true red ima fokus, suprotno false.
	 * @param row indeks reda.
	 * @param column indeks kolone.
	 */
	protected void doCustom(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

	}
}
