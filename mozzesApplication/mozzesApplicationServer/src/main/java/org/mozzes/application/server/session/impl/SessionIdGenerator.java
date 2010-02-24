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