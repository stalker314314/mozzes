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
package org.mozzes.application.server.mockups;

import org.mozzes.application.common.exceptions.AuthorizationFailedException;
import org.mozzes.application.plugin.authorization.AuthorizationManager;


public class MockUpAuthorizationService implements AuthorizationManager {

	private boolean authorize = true;

	public void setAuthorize(boolean authorize) {
		this.authorize = authorize;
	}

	@Override
	public long authorize(String username, String password) throws AuthorizationFailedException {
		if (authorize == false)
			throw new AuthorizationFailedException();
		
		return Long.MAX_VALUE;
	}
}
