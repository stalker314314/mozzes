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
