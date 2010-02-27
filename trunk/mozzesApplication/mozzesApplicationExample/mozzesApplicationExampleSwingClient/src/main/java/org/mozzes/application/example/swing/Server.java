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
package org.mozzes.application.example.swing;

import java.lang.reflect.UndeclaredThrowableException;

import javax.swing.JOptionPane;

import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.example.common.service.MatchAdministration;
import org.mozzes.application.example.common.service.TeamAdministration;
import org.mozzes.application.remoting.client.RemoteClientConfiguration;



public class Server {
	private static final String HOST = "localhost";
	private static final int PORT = 1234;

	private static Server instance;

	private final MozzesClient client;

	private Server() {
		MozzesClient mozzesClient = null;
		try {
			mozzesClient = new MozzesClient(new RemoteClientConfiguration(HOST, PORT));
		} catch (UndeclaredThrowableException e) {
			JOptionPane.showMessageDialog(ExampleSwingApplication.getApplicationMainFrame(),
					"Server nije pokrenut!");
			System.exit(1);
		}
		client = mozzesClient;
	}

	private static Server getInstance() {
		if (instance == null)
			instance = new Server();
		return instance;
	}

	public static TeamAdministration getTeamAdministrationService() {
		return getInstance().client.getService(TeamAdministration.class);
	}

	public static MatchAdministration getMatchAdministrationService() {
		return getInstance().client.getService(MatchAdministration.class);
	}
}
