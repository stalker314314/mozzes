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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import org.mozzes.swing.mgf.binding.AbstractPropagator;
import org.mozzes.swing.mgf.binding.BindingHandler;


public class ComboBoxBinder<BeanType, ValueType> extends BindingHandler<BeanType, ValueType, JComboBox> {
	private final ToSourceBinder toSourceBinder = new ToSourceBinder();

	public ComboBoxBinder() {
		super(JComboBox.class);
		setToSourcePropagator(toSourceBinder);
	}

	@Override
	protected void bind() {
		getComponent().addItemListener(toSourceBinder);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected ValueType getComponentValue() {
		return (ValueType) getComponent().getSelectedItem();
	}

	@Override
	protected void setComponentValue(ValueType value) {
		getComponent().setSelectedItem(value);
	}

	@Override
	protected void setEditable(boolean editable) {
		getComponent().setEnabled(editable);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<ValueType> getComponentValueType() {
		return (Class<ValueType>) getField().getFieldType();
	}

	private class ToSourceBinder extends AbstractPropagator implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (!isEnabled())
				return;
			this.disable();
			promoteChangeToSource();
			this.enable();
			
			triggerValidation();
		}
	}
}
