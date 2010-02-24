package example.swing;

import java.lang.reflect.UndeclaredThrowableException;

import javax.swing.JOptionPane;

import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.remoting.client.RemoteClientConfiguration;


import example.common.service.MatchAdministration;
import example.common.service.TeamAdministration;

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
