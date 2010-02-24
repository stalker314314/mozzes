package org.mozzes.swing.component;

import javax.swing.JTextField;

/**
 * Customized JTextField to create a JTextField with 
 * restricting the number of characters to be entered into it
 * 
 * @author draganm
 *
 */
public class MFixedLengthTextField extends JTextField {

	private static final long serialVersionUID = 3488412023279796730L;

	/**
	 * Constructor
	 * 
	 * @param length Max length of the String that will be aceepted.
	 */
	public MFixedLengthTextField(int length) {
		this(null, length);
	}

	/**
	 * @param length Max length of the String that will be aceepted.
	 */
	public MFixedLengthTextField(String text, int length) {
		super(new MFixedLengthPlainDocument(length), text, length);
	}
}