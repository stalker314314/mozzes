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

import org.mozzes.swing.component.AmountFormatter;

/**
 * Editor za celije tabele koji omogucava samo unos 
 * pozitivnog broja ili nule
 * @author vaso
 */
public class PositiveNumberCellEditor extends NumberValidationCellEditor {
	
	/**
	 * Construktor {@link PositiveNumberCellEditor} objekta
	 * @param amountFormatter {@link AmountFormatter}
	 */
	public PositiveNumberCellEditor(final AmountFormatter amountFormatter) {
		super(amountFormatter);
	}
	
	/**
	 * Construktor
	 * @param cellStrLength Max length of the String that will be aceepted as input.
	 * @author draganm
	 */
	public PositiveNumberCellEditor(final AmountFormatter amountFormatter, int cellStrLength) {
		super(amountFormatter, cellStrLength);
	}

	private static final long serialVersionUID = 8401333504018579789L;
	@Override
	protected boolean isNumberValid(Number n) {
		return n != null && n.doubleValue() >= 0d;
	}
}
