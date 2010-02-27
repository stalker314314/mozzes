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
 * Defines interface for the translation between <b>From</b> and <b>To</b>
 * 
 * @author milos
 * 
 * @param <From> Type of an object that will be translated to <b>To</b>
 * @param <To> Type of the object to which an object of type <b>From</b> will be translated to
 */
public interface Translator<From, To> {

	/**
	 * Translates <b>From</b> <i>object</i> to <b>To</b> object
	 * 
	 * @param object Object to be translated
	 * @return Translated object of type <b>To</b>
	 * @throws TranslationException If the specified object cannot be translated for some reason
	 */
	To translateTo(From object) throws TranslationException;

	/**
	 * Translates <b>To</b> <i>object</i> to <b>From</b> object
	 * 
	 * @param object Object to be translated
	 * @return Translated object of type <b>From</b>
	 * @throws TranslationException If the specified object cannot be translated for some reason
	 */
	From translateFrom(To object) throws TranslationException;

	/**
	 * @return Class of the <b>From</b>
	 */
	Class<From> getFromClass();

	/**
	 * @return Class of the <b>To</b>
	 */
	Class<To> getToClass();

	/**
	 * @return Translator translating from class <b>To</b> to class <b>From</b>
	 */
	Translator<To, From> inverse();
}
