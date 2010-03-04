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
package org.mozzes.application.demo.mockups;

import org.mozzes.application.common.transaction.*;

/**
 * When this exception occurs in some service method execution it will NOT cause transaction rollback because it's
 * annotated with {@link TransactionIgnored}
 * 
 * @author vita
 */
@TransactionIgnored
public class MIgnoredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MIgnoredException() {
		super();
	}

	public MIgnoredException(String message, Throwable cause) {
		super(message, cause);
	}

	public MIgnoredException(String message) {
		super(message);
	}

	public MIgnoredException(Throwable cause) {
		super(cause);
	}
}