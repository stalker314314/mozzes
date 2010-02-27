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

/**
 * Allows applications to replace default {@link Translator translators} with custom ones, or provide their own
 * Translators for custom types
 * 
 * @author milos
 */
public class TranslatorConfiguration {
	private TranslatorConfiguration() {
	}

	/**
	 * Contributes a {@link Translator} to framework
	 * 
	 * @param translator Translator that you want to contribute to framework
	 * 
	 * @throws IllegalArgumentException if the passed translator translates between the same types of objects as some
	 *             other translator
	 */
	public static <From, To> void addTranslator(Translator<From, To> translator) {
		TranslatorRepository.addTranslator(translator, false);
	}

	/**
	 * Contributes a {@link Translator} to framework, if the passed translator translates between the same types of
	 * objects as some other contributed(or default) translator it overrides the old one.
	 * 
	 * @param translator Translator that you want to contribute to framework
	 */
	public static <From, To> void overrideTranslator(Translator<From, To> translator) {
		TranslatorRepository.addTranslator(translator, true);
	}

}
