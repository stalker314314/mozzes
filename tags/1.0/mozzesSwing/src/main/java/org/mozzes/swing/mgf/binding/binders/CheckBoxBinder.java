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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import org.mozzes.swing.mgf.binding.AbstractPropagator;
import org.mozzes.swing.mgf.binding.BindingHandler;
import org.mozzes.utils.reflection.ReflectionUtils;


public class CheckBoxBinder<T> extends BindingHandler<T, Boolean, JCheckBox> {
	private final ToSourceBinder toSourceBinder = new ToSourceBinder();

	public CheckBoxBinder() {
		super(JCheckBox.class);
		setToSourcePropagator(toSourceBinder);
	}

	@Override
	protected void bind() {
		getComponent().addActionListener(toSourceBinder);
	}

	@Override
	protected Boolean getComponentValue() {
		return getComponent().isSelected();
	}

	@Override
	protected void setComponentValue(Boolean value) {
		Boolean nullResolved = ReflectionUtils.resolvePrimitiveNull(boolean.class, value);
		getComponent().setSelected(nullResolved);
	}

	@Override
	protected void setEditable(boolean editable) {
		getComponent().setEnabled(editable);
	}

	@Override
	public Class<Boolean> getComponentValueType() {
		return Boolean.class;
	}

	private class ToSourceBinder extends AbstractPropagator implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isEnabled())
				return;
			promoteChangeToSource();
			triggerValidation();
		}

	}
}
