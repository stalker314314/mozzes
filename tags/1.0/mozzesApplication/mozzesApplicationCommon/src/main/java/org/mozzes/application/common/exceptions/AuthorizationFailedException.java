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
package org.mozzes.application.common.exceptions;

/**
 * The Class AuthorizationFailedException. This exception occurs when user tries to login unsuccessfully.
 */
public class AuthorizationFailedException extends MozzesException {

	private static final long serialVersionUID = 5234925841194197427L;

	public AuthorizationFailedException() {
		super();
	}

	public AuthorizationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthorizationFailedException(String message) {
		super(message);
	}

	public AuthorizationFailedException(Throwable cause) {
		super(cause);
	}
}
