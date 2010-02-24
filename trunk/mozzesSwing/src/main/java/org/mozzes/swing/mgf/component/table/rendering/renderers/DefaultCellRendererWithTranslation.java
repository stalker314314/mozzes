package org.mozzes.swing.mgf.component.table.rendering.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.mozzes.swing.mgf.translation.TranslationException;
import org.mozzes.swing.mgf.translation.Translator;
import org.mozzes.swing.mgf.translation.TranslatorRepository;


public class DefaultCellRendererWithTranslation extends DefaultCellRenderer {

	public DefaultCellRendererWithTranslation() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component component;

		component = super.getTableCellRendererComponent(
				table, value, isSelected, hasFocus, row, column);

		if (!(component instanceof JLabel) || value instanceof String)
			return component;

		Class<?> valueClass = value == null ?
				table.getModel().getColumnClass(column) :
				value.getClass();

		final Translator<Object, String> translator = TranslatorRepository
				.getDefault((Class<Object>) valueClass, String.class);

		if (translator == null)
			return component;

		JLabel label = (JLabel) component;
		try {
			label.setText(translator.translateTo(value));
		} catch (TranslationException ignore) {
		}

		return component;
	}
}
