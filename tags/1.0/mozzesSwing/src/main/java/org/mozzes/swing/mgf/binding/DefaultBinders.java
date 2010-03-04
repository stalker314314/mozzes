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
package org.mozzes.swing.mgf.binding;

import java.util.ArrayList;
import java.util.List;

import org.mozzes.swing.mgf.binding.binders.CheckBoxBinder;
import org.mozzes.swing.mgf.binding.binders.ComboBoxBinder;
import org.mozzes.swing.mgf.binding.binders.JXDatePickerBinder;
import org.mozzes.swing.mgf.binding.binders.LabelBinder;
import org.mozzes.swing.mgf.binding.binders.TextAreaBinder;
import org.mozzes.swing.mgf.binding.binders.TextFieldBinder;


/**
 * Contains the default binders set by MGF Framework
 * 
 * @author milos
 */
class DefaultBinders {
	/**
	 * @return List of default binders to be set by MGF Framework
	 */
	// public static List<Class<? extends BindingHandler<?, ?, ?>> getDefaultBinders() {
	public static List<Class<?>> getDefaultBinders() {
		List<Class<?>> binders = new ArrayList<Class<?>>();

		binders.add(TextFieldBinder.class);
		binders.add(LabelBinder.class);
		binders.add(TextAreaBinder.class);
		binders.add(CheckBoxBinder.class);
		binders.add(ComboBoxBinder.class);
		binders.add(JXDatePickerBinder.class);

		return binders;
	}
}
