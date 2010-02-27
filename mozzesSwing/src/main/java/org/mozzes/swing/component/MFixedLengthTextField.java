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