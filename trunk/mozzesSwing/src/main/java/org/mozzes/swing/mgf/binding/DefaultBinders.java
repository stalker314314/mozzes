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
