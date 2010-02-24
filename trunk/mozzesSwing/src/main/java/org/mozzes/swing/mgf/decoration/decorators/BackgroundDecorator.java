package org.mozzes.swing.mgf.decoration.decorators;

import java.awt.Color;

import javax.swing.JComponent;

import org.mozzes.swing.mgf.decoration.Decorator;
import org.mozzes.swing.mgf.decoration.StateRestoreFunction;


public class BackgroundDecorator extends Decorator {
	private Color background;

	public BackgroundDecorator(Color background) {
		this.background = background;
	}

	@Override
	protected void decorateComponent(JComponent component) {
		component.setBackground(background);
	}

	@Override
	protected StateRestoreFunction getStateRestoreFunction(final JComponent component) {
		final Color color = component.getBackground();
		return new StateRestoreFunction() {
			@Override
			public void restoreState(JComponent component) {
				component.setBackground(color);
	}
		};
	}

	public void setBackground(Color background) {
		this.background = background;
		refresh();
	}

	public Color getBackground() {
		return background;
	}
	}
