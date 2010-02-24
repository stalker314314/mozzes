package org.mozzes.swing.component.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.mozzes.swing.component.MFixedLengthTextField;


/**
 * Default swing editor za cjelije tabele. Ubachen posto je bio zasticjen u
 * okviru JTable-a.
 */
@SuppressWarnings("unchecked")
public class GenericEditor extends DefaultCellEditor {

	private static final long serialVersionUID = -3166392633386216530L;

	Class[] argTypes = new Class[] { String.class };
	java.lang.reflect.Constructor constructor;
	Object value;

	public GenericEditor() {
		super(new JTextField());
		getComponent().setName("Table.editor");
	}

	/**
	 * Constructor
	 * 
	 * @param length Max length of the String that will be aceepted as input.
	 */
	public GenericEditor(int length) {
		super(new MFixedLengthTextField(length));
		getComponent().setName("Table.editor");
	}

	@Override
	public boolean stopCellEditing() {
		String s = (String) super.getCellEditorValue();
		// Here we are dealing with the case where a user
		// has deleted the string value in a cell, possibly
		// after a failed validation. Return null, so that
		// they have the option to replace the value with
		// null or use escape to restore the original.
		// For Strings, return "" for backward compatibility.
		if ("".equals(s)) {
			if (constructor.getDeclaringClass() == String.class) {
				value = s;
			}
			super.stopCellEditing();
		}

		try {
			value = constructor.newInstance(new Object[] { s });
		} catch (Exception e) {
			((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
			return false;
		}
		return super.stopCellEditing();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.value = null;
		((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
		try {
			Class type = table.getColumnClass(column);
			// Since our obligation is to produce a value which is
			// assignable for the required type it is OK to use the
			// String constructor for columns which are declared
			// to contain Objects. A String is an Object.
			if (type == Object.class) {
				type = String.class;
			}
			constructor = type.getConstructor(argTypes);
		} catch (NoSuchMethodException e) {
			return null;
		}
		return super.getTableCellEditorComponent(table, value, isSelected, row,
				column);
	}

	@Override
	public Object getCellEditorValue() {
		return value;
	}
}

