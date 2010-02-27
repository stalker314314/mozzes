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
import java.util.Date;

import org.jdesktop.swingx.JXDatePicker;
import org.mozzes.swing.mgf.binding.AbstractPropagator;
import org.mozzes.swing.mgf.binding.BindingHandler;


public class JXDatePickerBinder<T> extends BindingHandler<T, Date, JXDatePicker> {
	private final ToSourceBinder toSourceBinder = new ToSourceBinder();

	public JXDatePickerBinder() {
		super(JXDatePicker.class);
	}

	@Override
	protected void bind() {
		getComponent().addActionListener(toSourceBinder);
	}

	@Override
	protected Date getComponentValue() {
		return getComponent().getDate();
	}

	@Override
	public Class<Date> getComponentValueType() {
		return Date.class;
	}

	@Override
	protected void setComponentValue(Date value) {
		getComponent().setDate(value);
	}

	@Override
	protected void setEditable(boolean editable) {
		boolean editorEditable = getComponent().getEditor().isEditable();
		getComponent().setEditable(editable);
		if(editable)
			getComponent().getEditor().setEditable(editorEditable);
		// getComponent().setEnabled(editable);
	}

	private class ToSourceBinder extends AbstractPropagator implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isEnabled())
				return;
			promoteChangeToSource();
		}
	}
}
