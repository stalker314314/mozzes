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
package org.mozzes.application.demo.mockups.services.basic;

import java.io.*;

import org.mozzes.application.common.transaction.*;


public interface ServiceThatThrowsExceptions {

	/**
	 * This service method is throwing checked exception so we can test if exception propagation to the client is
	 * working
	 */
	String serviceWhichThrowsException() throws IOException;

	/**
	 * This service method is throwing runtime exception so we can test if exception propagation to the client is
	 * working
	 */
	String serviceWhichThrowsRuntimeException();

	/**
	 * This service method is throwing exception which type is annotated with {@link TransactionIgnored} annotation so
	 * the transaction in which this call occurred will not be rollbacked.
	 */
	void serviceWhichThrowsIgnoredException();
}
