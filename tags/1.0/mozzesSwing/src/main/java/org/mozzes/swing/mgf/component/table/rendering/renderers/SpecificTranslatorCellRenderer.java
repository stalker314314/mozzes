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
package org.mozzes.swing.mgf.component.table.rendering.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.mozzes.swing.mgf.translation.TranslationException;
import org.mozzes.swing.mgf.translation.Translator;


public class SpecificTranslatorCellRenderer<T> extends DefaultCellRenderer {
	private final Translator<T, String> translator;

	public SpecificTranslatorCellRenderer(Translator<T, String> translator) {
		if (translator == null)
			throw new IllegalArgumentException("Translator must not be null!");
		this.translator = translator;
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

		if (!translator.getFromClass().isAssignableFrom(valueClass))
			throw new IllegalStateException(String.format(
					"Provided translator (%s to String) is invalid!\n" +
					"Translator from %s to String must be provided!\n Column: %d",
					translator.getFromClass().getSimpleName(),
					valueClass.getSimpleName(), column));

		JLabel label = (JLabel) component;
		try {
			String text = translator.translateTo((T) value);
			label.setText(text);
		} catch (TranslationException e) {
			String message = "Exception occured while translating value!\n" +
					"{value: %s; row: %d; column: %d;}";

			throw new IllegalStateException(String.format(message, value, row, column), e);
		}

		return component;
	}
}
