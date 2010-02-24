package org.mozzes.application.example.server;

import java.util.Date;

import org.apache.log4j.Logger;
import org.mozzes.application.example.common.domain.Match;
import org.mozzes.application.example.common.domain.Result;
import org.mozzes.application.example.common.domain.Team;
import org.mozzes.application.example.common.service.MatchAdministration;
import org.mozzes.application.example.common.service.TeamAdministration;
import org.mozzes.application.example.server.service.MatchAdministrationImpl;
import org.mozzes.application.example.server.service.TeamAdministrationImpl;
import org.mozzes.application.hibernate.HibernateConfigurationType;
import org.mozzes.application.hibernate.HibernatePlugin;
import org.mozzes.application.remoting.server.RemotingPlugin;
import org.mozzes.application.server.MozzesServer;
import org.mozzes.application.server.MozzesServerConfiguration;



public class ExampleServer {
	private static final int PORT = 1234;

	public MozzesServerConfiguration createServerConfiguration() {
		MozzesServerConfiguration cfg = new MozzesServerConfiguration();
		cfg.addApplicationModule(new HibernatePlugin(HibernateConfigurationType.ANNOTATION));
		cfg.addApplicationModule(new RemotingPlugin(PORT));

		cfg.addService(MatchAdministration.class, MatchAdministrationImpl.class);
		cfg.addService(TeamAdministration.class, TeamAdministrationImpl.class);
		return cfg;
	}

	private void start() {
		MozzesServer server = new MozzesServer(createServerConfiguration());
		server.start();

		Team t1 = new Team();
		t1.setName("Crvena zvezda");
		Team t2 = new Team();
		t2.setName("Partizan");

		t1 = server.getLocalClient().getService(TeamAdministration.class).save(t1);
		t2 = server.getLocalClient().getService(TeamAdministration.class).save(t2);
		log.info(server.getLocalClient().getService(TeamAdministration.class).findAll());

		server.getLocalClient().getService(MatchAdministration.class).save(
				new Match(new Date(), t1, t2, new Result(2, 1)));
		log.info(server.getLocalClient().getService(MatchAdministration.class).findAll());

	}

	private static final Logger log = Logger.getLogger(ExampleServer.class);

	public static void main(String[] args) {
		new ExampleServer().start();
	}

}