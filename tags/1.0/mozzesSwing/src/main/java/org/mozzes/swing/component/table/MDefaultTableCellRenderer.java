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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import org.mozzes.swing.utils.CodeUtils;


/**
 * Osnovni cell renderer za MTable. Svaki custom cell renderer za ovaj tip tabele mora da extenduje ovaj. On vodi racuna
 * o zebrastom izgledu tabele i renderovanju prve kolone u zavisnosti da li je to redni broj ili ne.
 * 
 * @author neda
 * 
 */
public class MDefaultTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	// da li je prva kolona redni broj
	private boolean orderColumn = false;
	private final Color alternateRowColor;

	/**
	 * Kreiranje renderera.
	 * 
	 * @param orderColumn da li postoji prva kolona kao redni broj
	 */
	public MDefaultTableCellRenderer(Boolean orderColumn) {
		super();
		if (orderColumn != null) {
			this.orderColumn = orderColumn;
		}
		alternateRowColor = (Color) UIManager.get("Table.alternateRowColor");
	}

	/**
	 * Returns the default table cell renderer.
	 * 
	 * @param table the <code>JTable</code>
	 * @param aValue the value to assign to the cell at <code>[row, column]</code>
	 * @param isSelected true if cell is selected
	 * @param hasFocus true if cell has focus
	 * @param row the row of the cell to render
	 * @param column the column of the cell to render
	 * @return the default table cell renderer
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object aValue, boolean isSelected, boolean hasFocus,
			int row, int column) {

		super.getTableCellRendererComponent(table, aValue, isSelected, hasFocus, row, column);
		
		Object value = prepareValue(aValue, row, column);

		if (isSelected) {
			// red je selektovan.
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			// podesavanje boje pozadine za parne i neparne redove.
			if(row % 2 == 0) {
				setBackground(alternateRowColor);
			} else {
				setBackground(table.getBackground());
			}
			setForeground(table.getForeground());
		}
        if (aValue == null) {
        	setText("/");
        	setHorizontalAlignment(CENTER);
        	return this;
        }
		// omogucavanje da renderer za odredjeni tip izvrsi nesto specificno.
		doTypeSpecific(table, value, isSelected, hasFocus, row, column);

		// [FIN-77] [nenadl] String koji se prikazuje u cjeliji.
		String displayValue = "";

		if (value instanceof Number) {
			displayValue = numberTypeSpecifics((Number) value);
		} else if (value instanceof Date) {
			displayValue = dateTypeSpecifics((Date) value);
		} else {

			setHorizontalAlignment(LEFT);

			// [FIN-77] [nenadl] Prikazuje se string reprezentacija objekta ako nije broj.
			if (value != null) {
				displayValue = value.toString();
			}
		}

		setText(displayValue);
		// omogucavanje da korisnik uradi nesto specificno.
		doCustom(table, value, isSelected, hasFocus, row, column);

		return this;
	}

	private Object prepareValue(Object value, int row, int column) {
		// postavljanje vrednosti ukoliko je prva kolona i ona predstavlja redni broj

		Object newValue = null;

		if (orderColumn && column == 0) {
			newValue = Integer.valueOf(row + 1);
			setHorizontalAlignment(RIGHT);
		} else {
			newValue = value;
		}

		return newValue;
	}
	
	private String numberTypeSpecifics(Number value) {
		String displayValue;
		setHorizontalAlignment(RIGHT);

		// [FIN-77] [nenadl] Zaokruzhivanje na dve decimale ako je realna vrednost.
		if (value instanceof Double || value instanceof Float) {
			displayValue = CodeUtils.format(value, 2, " ");
		} else {
			displayValue = CodeUtils.format(value, 0, " ");
		}
		return displayValue;
	}
	
	private String dateTypeSpecifics(Date value) {
		String displayValue = value.toString();
		
		displayValue = new SimpleDateFormat("dd.MM.yyyy").format(value);
		setHorizontalAlignment(RIGHT);
		
		return displayValue;
	}

	/**
	 * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
	 * 
	 * @since 1.5
	 */
	@Override
	public void invalidate() {
	}

	/**
	 * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
	 */
	@Override
	public void validate() {
	}

	/**
	 * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
	 */
	@Override
	public void revalidate() {
	}

	/**
	 * Metoda koje nasledjuju nadklase za definisanje odredjenog ponasanja za neki tip. Npr Double, Long, String i sl.
	 * 
	 * @param table tabela.
	 * @param value vrednost.
	 * @param isSelected true red je selektovan, suprotno false.
	 * @param hasFocus true red ima focus, suprotno false.
	 * @param row indeks reda.
	 * @param column indeks kolone.
	 */
	protected void doTypeSpecific(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

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
