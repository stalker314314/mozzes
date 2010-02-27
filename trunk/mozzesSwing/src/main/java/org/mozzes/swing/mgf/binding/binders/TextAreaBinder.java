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
package org.mozzes.swing.mgf.binding.binders;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import org.mozzes.swing.mgf.binding.AbstractPropagator;
import org.mozzes.swing.mgf.binding.BindingHandler;


public class TextAreaBinder<T> extends BindingHandler<T, String, JTextArea> {
	private final ToSourceBinder toSourceBinder = new ToSourceBinder();
	private ValidationFocusListener updateAndValidateOnLostFocus = new ValidationFocusListener();

	public TextAreaBinder() {
		super(JTextArea.class);
		setToSourcePropagator(toSourceBinder);
	}

	@Override
	protected void bind() {
		getComponent().addFocusListener(updateAndValidateOnLostFocus);
		getComponent().getDocument().addUndoableEditListener(toSourceBinder);
	}

	@Override
	protected String getComponentValue() {
		return getComponent().getText();
	}

	@Override
	protected void setComponentValue(String value) {
		getComponent().setText(value);
	}

	@Override
	protected void setEditable(boolean editable) {
		getComponent().setEditable(editable);
	}

	@Override
	public Class<String> getComponentValueType() {
		return String.class;
	}

	private class ToSourceBinder extends AbstractPropagator implements UndoableEditListener {
		@Override
		public void undoableEditHappened(UndoableEditEvent e) {
			if (!isEnabled())
				return;
			promoteChangeToSource();
		}
	}

	private class ValidationFocusListener extends FocusAdapter {
		@Override
		public void focusLost(FocusEvent e) {
			promoteChangeFromSource();
			triggerValidation();
}
	}
}
