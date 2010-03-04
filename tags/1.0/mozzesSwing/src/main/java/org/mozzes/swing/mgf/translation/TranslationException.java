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
 * This exception is thrown by {@link Translator translators} when translation cannot be done for some reason
 * 
 * @author milos
 */
public class TranslationException extends Exception {
	private static final long serialVersionUID = 15L;

	/**
	 * @param message Reason of failure(the message that will be shown to the user through component decoration)F
	 */
	public TranslationException(String message) {
		super(message);
	}

	/**
	 * @param message Reason of failure(the message that will be shown to the user through component decoration)
	 * @param cause {@link Exception} that happened while translating a value
	 */
	public TranslationException(String message, Throwable cause) {
		super(message, cause);
	}
}