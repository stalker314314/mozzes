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
package org.mozzes.application.server.session.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class responsible for generating sessionIds for new sessions
 */
class SessionIdGenerator {

	private static final Logger logger = LoggerFactory.getLogger(SessionIdGenerator.class);

	private final Random random = new SecureRandom();

	private final MessageDigest digest = MessageDigest.getInstance("MD5");

	SessionIdGenerator() throws NoSuchAlgorithmException {
		logger.trace("session idGenerator instantiated");
	}

	/**
	 * @return generated sessionId
	 */
	synchronized String generateSessionId() {
		
		byte bytes[] = new byte[16];
		random.nextBytes(bytes);
		bytes = digest.digest(bytes);
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			byte b1 = (byte) ((bytes[i] & 0xf0) >> 4);
			byte b2 = (byte) (bytes[i] & 0xf);
			if (b1 < 10)
				result.append((char) (48 + b1));
			else
				result.append((char) (65 + (b1 - 10)));
			if (b2 < 10)
				result.append((char) (48 + b2));
			else
				result.append((char) (65 + (b2 - 10)));
		}
		logger.trace("session idGenerator generated new id "+result.toString());
		return result.toString();
	}
}