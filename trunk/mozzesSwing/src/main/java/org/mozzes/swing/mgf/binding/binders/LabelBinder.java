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

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.mozzes.swing.mgf.binding.BindingHandler;
import org.mozzes.utils.reflection.ReflectionUtils;


public class LabelBinder<T> extends BindingHandler<T, String, JLabel> {
	public LabelBinder() {
		super(JLabel.class);
	}

	@Override
	protected void bind() {
		alignRightIfNumber();
	}

	private void alignRightIfNumber() {
		Class<?> fieldType = getField().getFieldType();
		fieldType = fieldType.isPrimitive() ? ReflectionUtils.resolvePrimitiveType(fieldType) : fieldType;
		if (Number.class.isAssignableFrom(fieldType))
			getComponent().setHorizontalAlignment(SwingConstants.RIGHT);
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
	}

	@Override
	public Class<String> getComponentValueType() {
		return String.class;
	}
}
