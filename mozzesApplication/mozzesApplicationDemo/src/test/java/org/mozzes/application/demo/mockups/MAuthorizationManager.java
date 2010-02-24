package org.mozzes.application.demo.mockups;

import java.util.ArrayList;

import org.mozzes.application.common.exceptions.AuthorizationFailedException;
import org.mozzes.application.plugin.authorization.AuthorizationManager;


/**
 * This is the mockup authorization manager used only in the testing. It allows only credentials specified in the
 * validCredentials list.
 * 
 * @author vita
 */
public class MAuthorizationManager implements AuthorizationManager {

	/**
	 * list of valid credentials. every element is the array of 2 elements:username and password
	 */
	private static ArrayList<String[]> validCredentials = new ArrayList<String[]>();

	public static boolean isUserLogged = false;

	/**
	 * @see AuthorizationManager#authorize(java.lang.String, java.lang.String)
	 */
	@Override
	public long authorize(String username, String password) throws AuthorizationFailedException {
		if (!isValid(username, password))
			throw new AuthorizationFailedException();

		
		isUserLogged = true;
		return Long.MAX_VALUE;
	}

	/**
	 * Testing if the username/password combination is present in the validCredential list.
	 */
	private boolean isValid(String username, String password) {
		for (String[] credentials : validCredentials)
			if (credentials[0].equals(username) && credentials[1].equals(password))
				return true;

		return false;
	}

	/**
	 * Add username/password combination to the valid credential list
	 */
	public static void addValidCredentials(String username, String password) {
		validCredentials.add(new String[] { username, password });
	}
}
