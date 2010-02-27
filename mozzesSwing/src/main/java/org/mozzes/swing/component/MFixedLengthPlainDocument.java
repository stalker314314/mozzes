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
