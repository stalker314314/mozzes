package org.mozzes.swing.mgf.decoration.decorators;

import java.awt.Font;

import javax.swing.JComponent;

import org.mozzes.swing.mgf.decoration.Decorator;
import org.mozzes.swing.mgf.decoration.StateRestoreFunction;


public class FontDecorator extends Decorator {
	private Font font;

	public FontDecorator(Font font) {
		this.font = font;
	}

	@Override
	protected void decorateComponent(JComponent component) {
		component.setFont(font);
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

	public void setFont(Font font) {
		this.font = font;
		refresh();
	}

	public Font getFont() {
		return font;
	}

}
