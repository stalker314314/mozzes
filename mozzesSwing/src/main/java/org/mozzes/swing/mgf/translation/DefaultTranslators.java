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
package org.mozzes.swing.mgf.translation;

import java.util.ArrayList;
import java.util.List;

import org.mozzes.swing.mgf.translation.translators.DateToString;
import org.mozzes.swing.mgf.translation.translators.DoubleToString;
import org.mozzes.swing.mgf.translation.translators.IntegerToString;
import org.mozzes.swing.mgf.translation.translators.TimestampToString;


/**
 * Contains the default translators set by MGF Framework
 * 
 * @author milos
 */
public class DefaultTranslators {
	private DefaultTranslators() {
	}

	/**
	 * @return List of default translators to be set by MGF Framework
	 */
	public static List<Translator<?, ?>> getDefaultTranslators() {
		List<Translator<?, ?>> translators = new ArrayList<Translator<?, ?>>();

		translators.add(new IntegerToString());
		translators.add(new DoubleToString());
		translators.add(new DateToString());
		translators.add(new TimestampToString());

		return translators;
	}
}
