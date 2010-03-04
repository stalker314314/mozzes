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
import org.mozzes.application.example.common.ExampleConstants;
import org.mozzes.application.remoting.client.RemoteClientConfiguration;

public class ApplicationContext {

	private static ApplicationContext instance;

	private final MozzesClient client;

	private ApplicationContext() {
		client = new MozzesClient(new RemoteClientConfiguration(ExampleConstants.HOST, ExampleConstants.PORT));
	}

	private static ApplicationContext getInstance() {
		if (instance == null) {
			try {
				instance = new ApplicationContext();
			} catch (UndeclaredThrowableException e) {
				JOptionPane.showMessageDialog(ExampleSwingClient.getApplicationMainFrame(),
						"Server is not starated!");
				System.exit(1);
			}
		}
		return instance;
	}
	
	public static <T> T getService(Class<T> serviceInterface) {
		return getInstance().client.getService(serviceInterface);
	}
}
