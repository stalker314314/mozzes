package org.mozzes.swing.mgf.decoration.decorators;

import java.awt.Color;

import javax.swing.JComponent;

import org.mozzes.swing.mgf.decoration.Decorator;
import org.mozzes.swing.mgf.decoration.StateRestoreFunction;


public class ForegroundDecorator extends Decorator {
	private Color foreground;

	public ForegroundDecorator(Color foreground) {
		this.foreground = foreground;
	}

	@Override
	protected void decorateComponent(JComponent component) {
		component.setForeground(foreground);
	}

	@Override
	protected StateRestoreFunction getStateRestoreFunction(final JComponent component) {
		final Color color = component.getForeground();
		return new StateRestoreFunction() {
			@Override
			public void restoreState(JComponent component) {
				component.setForeground(color);
	}
		};
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
		refresh();
	}

	public Color getForeground() {
		return foreground;
	}
	}
