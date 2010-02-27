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
package org.mozzes.swing.mgf.decoration.decorators;

import java.awt.Font;

import javax.swing.JComponent;

import org.mozzes.swing.mgf.decoration.Decorator;
import org.mozzes.swing.mgf.decoration.StateRestoreFunction;


public class FontStyleDecorator extends Decorator {
	private int style;

	public FontStyleDecorator(int style) {
		this.style = style;
	}

	@Override
	protected void decorateComponent(JComponent component) {
		component.setFont(component.getFont().deriveFont(style));
	}

	@Override
	protected StateRestoreFunction getStateRestoreFunction(final JComponent component) {
		final Font font = component.getFont();
		return new StateRestoreFunction() {
			@Override
			public void restoreState(JComponent component) {
				component.setFont(font);
			}
		};
	}

	public void setStyle(int style) {
		this.style = style;
		refresh();
	}

	public int getStyle() {
		return style;
	}

}
