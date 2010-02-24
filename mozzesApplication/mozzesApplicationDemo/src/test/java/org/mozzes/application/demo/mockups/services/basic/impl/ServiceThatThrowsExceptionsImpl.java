package org.mozzes.application.demo.mockups.services.basic.impl;

import java.io.*;

import org.mozzes.application.demo.mockups.*;
import org.mozzes.application.demo.mockups.services.basic.*;


public class ServiceThatThrowsExceptionsImpl implements ServiceThatThrowsExceptions {

	/**
	 * @see ServiceThatThrowsExceptions#serviceWhichThrowsException()
	 */
	@Override
	public String serviceWhichThrowsException() throws IOException {
		throw new IOException("example exception");
	}

	/**
	 * @see ServiceThatThrowsExceptions#serviceWhichThrowsRuntimeException()
	 */
	@Override
	public String serviceWhichThrowsRuntimeException() {
		throw new NumberFormatException("example exception");
	}

	/**
	 * @see ServiceThatThrowsExceptions#serviceWhichThrowsIgnoredException()
	 */
	@Override
	public void serviceWhichThrowsIgnoredException() {
		throw new MIgnoredException();
	}
}