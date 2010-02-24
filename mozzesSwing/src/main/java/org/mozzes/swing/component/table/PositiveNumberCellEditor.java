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
