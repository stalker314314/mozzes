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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import org.mozzes.swing.mgf.binding.AbstractPropagator;
import org.mozzes.swing.mgf.binding.BindingHandler;
import org.mozzes.utils.reflection.ReflectionUtils;


public class TextFieldBinder<T> extends BindingHandler<T, String, JTextField> {
	private final ToSourceBinder toSourceBinder = new ToSourceBinder();
	private FocusListener updateOnLostFocus = new UpdateOnLostFocus();

	public TextFieldBinder() {
		super(JTextField.class);
		setToSourcePropagator(toSourceBinder);
	}

	private void alignRightIfNumber() {
		Class<?> fieldType = getField().getFieldType();
		fieldType = fieldType.isPrimitive() ? ReflectionUtils.resolvePrimitiveType(fieldType) : fieldType;
		if (Number.class.isAssignableFrom(fieldType))
			getComponent().setHorizontalAlignment(SwingConstants.RIGHT);
	}

	@Override
	protected void bind() {
		getComponent().getDocument().addUndoableEditListener(toSourceBinder);
		alignRightIfNumber();
		getComponent().addFocusListener(updateOnLostFocus);
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

	private class UpdateOnLostFocus implements FocusListener {
		@Override
		public void focusGained(FocusEvent e) {
			if (!(Number.class.isAssignableFrom(
					ReflectionUtils.resolvePrimitiveType(getField().getFieldType()))))
				return;
			getComponent().selectAll();
		}

		@Override
		public void focusLost(FocusEvent e) {
			promoteChangeFromSource();
			triggerValidation();
		}
	}
}
