package org.mozzes.swing.component;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * PlainDocument customized class to be able 
 * to creates document with the given max length.
 * 
 * @author draganm
 *
 */
public class MFixedLengthPlainDocument extends PlainDocument {

	private static final long serialVersionUID = 3020104530605193736L;
	
	
	private int maxLength;

	/**
	 * Constructor
	 * Creates a new document with the given max length
	 */
	public MFixedLengthPlainDocument(int maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * If this insertion would exceed the maximum document length, we "beep" and do
	 * nothing else. Otherwise, super.insertString() is called.
	 */
	@Override
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		if (getLength() + str.length() > maxLength) {
			Toolkit.getDefaultToolkit().beep();
		} else {
			super.insertString(offset, str, a);
		}
	}

}
