package org.mozzes.swing.mgf.decoration.decorators;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import org.mozzes.swing.mgf.decoration.Decorator;
import org.mozzes.swing.mgf.decoration.StateRestoreFunction;


public class BorderDecorator extends Decorator {
	private Border border;
	private final boolean useCompositeBorder;

	public BorderDecorator(Border border) {
		this(border, false);
	}

	public BorderDecorator(Border border, boolean useCompositeBorder) {
		this.border = border;
		this.useCompositeBorder = useCompositeBorder;
	}

	@Override
	protected void decorateComponent(JComponent component) {
		if (useCompositeBorder) {
			CompoundBorder compund = new CompoundBorder(border, component.getBorder());
			component.setBorder(compund);
		} else {
			component.setBorder(border);
	}
	}

	@Override
	protected StateRestoreFunction getStateRestoreFunction(final JComponent component) {
		final Border border = component.getBorder();
		return new StateRestoreFunction() {
			@Override
			public void restoreState(JComponent component) {
				component.setBorder(border);
	}
		};
	}

	public void setBorder(Border border) {
		this.border = border;
		refresh();
	}

	public Border getBorder() {
		return border;
	}

	}
