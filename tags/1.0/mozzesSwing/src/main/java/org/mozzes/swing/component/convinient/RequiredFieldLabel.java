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
package org.mozzes.swing.component.convinient;

import javax.swing.JLabel;

/**
 * labela koju treba koristiti kada oznacavamo da je neko polje/podatak obavezno...
 * 
 * @author nikolad
 * 
 */
public class RequiredFieldLabel extends JLabel {

	private static final long serialVersionUID = -8811770108081120103L;

	private static final String REQUIRED = "*";

	public RequiredFieldLabel(JLabel label) {
		super(label.getText());
	}

	public RequiredFieldLabel(String text) {
		super(text);
	}

	@Override
	public void setText(String text) {
		super.setText(text.trim() + REQUIRED);
	}
}
