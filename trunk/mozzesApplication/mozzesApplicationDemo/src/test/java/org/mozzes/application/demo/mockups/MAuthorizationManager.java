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
