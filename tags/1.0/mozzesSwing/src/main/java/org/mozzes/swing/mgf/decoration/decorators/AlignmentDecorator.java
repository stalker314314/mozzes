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

import javax.swing.JComponent;

import org.mozzes.swing.mgf.decoration.Decorator;
import org.mozzes.swing.mgf.decoration.StateRestoreFunction;
import org.mozzes.utils.reflection.ReflectionUtils;
import org.mozzes.utils.reflection.ReflectiveMethod;


public class AlignmentDecorator extends Decorator {
	private int alignment;

	public AlignmentDecorator(int alignment) {
		this.alignment = alignment;
	}

	@Override
	protected void decorateComponent(JComponent component) {
		applyAlignment(component, alignment);

	}

	private static void applyAlignment(JComponent component, int alignment) {
		try {
			ReflectiveMethod<JComponent, Void> method =
					ReflectionUtils.getMethod(component.getClass(),
					"setHorizontalAlignment", int.class);
			method.invoke(component, alignment);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(
					"This decorator can only be attached to a component that has " +
					"\"setHorizontalAlignment\" method, " +
					"you are trying to attach it to "
					+ component.getClass().getSimpleName());
	}
	}

	private static int getAlignment(JComponent component) {
		try {
			ReflectiveMethod<JComponent, Integer> method =
					ReflectionUtils.getMethod(component.getClass(), int.class,
					"getHorizontalAlignment");
			return method.invoke(component);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(
					"This decorator can only be attached to a component that has " +
					"\"setHorizontalAlignment\" method, " +
					"you are trying to attach it to "
					+ component.getClass().getSimpleName());
	}
	}

	@Override
	protected StateRestoreFunction getStateRestoreFunction(final JComponent component) {
		final int alignment = getAlignment(component);
		return new StateRestoreFunction() {
		@Override
			public void restoreState(JComponent component) {
				applyAlignment(component, alignment);
		}
		};
	}

	public void setAlignment(int alignment) {
		this.alignment = alignment;
		refresh();
}

	public int getAlignment() {
		return alignment;
	}
}
