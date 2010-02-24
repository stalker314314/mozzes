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
