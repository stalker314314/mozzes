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
