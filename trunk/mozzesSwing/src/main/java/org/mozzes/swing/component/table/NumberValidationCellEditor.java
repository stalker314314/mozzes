package org.mozzes.swing.component.table;

import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.mozzes.swing.component.AmountFormatter;


/**
 * [FIN-77] Cell editor koji vrshi validaciju da li su u pitanju brojevi (u
 * zavisnosti od klase u table modelu) i da li su validni sto se odredjuje na
 * osnovu poziva metode isNumberValid() koja se mozhe preklopiti. Ukoliko unos
 * nije validan sprechava se prestanak editovanja cjelije i uokviruje se
 * odgovarajucjom bojom.
 * 
 * @author nenadl
 * 
 */
public class NumberValidationCellEditor extends GenericEditor {

	/**
	 * Svuid.
	 */
	private static final long serialVersionUID = -7584667054021231532L;
	
	/**
	 * Instanca {@link AmountFormatter} objekta
	 */
	private AmountFormatter amountFormatter;
	
	/**
	 * Konstruktor {@link NumberValidationCellEditor} objekta
	 * @param amountFormatter {@link AmountFormatter}
	 */
	public NumberValidationCellEditor(final AmountFormatter amountFormatter) {
		init(amountFormatter);
	}
	
	/**
	 * Construktor of {@link NumberValidationCellEditor} object
	 * @param cellStrLength Max length of the String that will be aceepted as input.
	 * @author draganm
	 */
	public NumberValidationCellEditor(final AmountFormatter amountFormatter, int cellStrLength) {
		super(cellStrLength);
		init(amountFormatter);
	}
	
	private void init(final AmountFormatter amountFormatter) {
		if(amountFormatter == null)
			throw new IllegalArgumentException("Prosledjeni DecimalFormat objekat ne sme biti null.");
		this.amountFormatter = amountFormatter;
		((JTextField)getComponent()).setHorizontalAlignment(SwingConstants.RIGHT);
	}
	
	@Override
	public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected,
			final int rowIndex, final int columnIndex) {
		
		JTextField textField = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, rowIndex, columnIndex);
		textField.selectAll();
		
		return textField;
	}

	/**
	 * Zavrshava editovanje cjelije. Ukoliko unos nije validan, ne dozvoljava se zavrshetak editovanja.
	 */
	@Override
	public boolean stopCellEditing() {
		return stopCellEditing(isInputValid());
	}

	/**
	 * Zavrsava editovanje celije na osnovu validacije unosa koja se ogleda u parametru inputValid
	 */
	protected boolean stopCellEditing(boolean inputValid) {
		if (!inputValid) {
			processCellErrorValue();
			return false;
		} 
		else {
			processCellOkValue();
			super.stopCellEditing();
			return true;
		}
	}

	/**
	 * Metoda koja se zove posle uspesne validaicje
	 * pre poziva super.stopCellEditing()
	 * Tu je ostavljena mogucnost za custom intervencije
	 * tako sto se ova metoda overriduje
	 */
	protected void processCellOkValue() {
		
	}

	/**
	 * Vrsi obradu celije koja sadrzi nevalidan unos. 
	 * Moze da se overriduje tako da se dozvoli custom intervencija.
	 */
	protected void processCellErrorValue() {
		((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
	}
	
	/**
	 * vrsi validaciju inputa
	 */
	protected boolean isInputValid() {
		boolean inputValid = true;

		String s = (String) delegate.getCellEditorValue();

		if (!s.equals("")) {
			try {
				value = constructor.newInstance(new Object[] { s });
				Number n = (Number) value;
				if (!isNumberValid(n)) {
					inputValid = false;
				}
			} catch (InstantiationException e) {
				inputValid = false;
			} catch (IllegalAccessException e) {
				inputValid = false;
			} catch (InvocationTargetException e) {
				inputValid = false;
			}
		} else {
			value = null;
		}
		return inputValid;
	}

	/**
	 * Metoda koja proverava da li je unos u cjeliju validan. Treba je preklopiti
	 * u nasledjenoj klasi.
	 * @param n - broj koji je unesen u cjeliju.
	 * @return boolean
	 */
	protected boolean isNumberValid(Number n) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jdesktop.swingx.JXTable.GenericEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return this.amountFormatter.format(super.getCellEditorValue());
	}
}
