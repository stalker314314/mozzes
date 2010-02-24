package org.mozzes.swing.mgf.decoration.decorators;

import javax.swing.JComponent;
import javax.swing.Popup;

import org.mozzes.swing.mgf.decoration.StateRestoreFunction;
import org.mozzes.swing.mgf.helpers.PopupMessages;


public class PopupMessageDecorator extends MessageDecorator {
	private Popup lastPopupForRestoration = null;

	public PopupMessageDecorator() {
	}

	public PopupMessageDecorator(String... messages) {
		setMessages(messages);
	}

	@Override
	protected void decorateComponent(JComponent component) {
		lastPopupForRestoration.show();
		lastPopupForRestoration = null;
	}

	@Override
	protected StateRestoreFunction getStateRestoreFunction(final JComponent component) {
		return new StateRestoreFunction() {
			private final Popup popup;
			{
				popup = PopupMessages.getPopupMessage(component, getMessages());
				lastPopupForRestoration = popup;
	}

	@Override
			public void restoreState(JComponent component) {
				popup.hide();
	}
		};
	}
		}
