package org.mozzes.application.example.rest.jersey;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.example.common.domain.Team;
import org.mozzes.application.example.common.service.TeamAdministration;
import org.mozzes.rest.jersey.guice.MozzesGuiceFactory;

import com.google.inject.Inject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

@Path("/teams")
@MozzesGuiceFactory
/**
 * Rest service for getting information about all teams
 * @author stalker
 */
public class RestTeamsService {

	@Inject
	private MozzesClient mozzesClient;

	public RestTeamsService() {
	}

	@GET
	@Produces( { MediaType.TEXT_XML })
	public String getXml() {
		List<Team> teams = mozzesClient.getService(TeamAdministration.class).findAll();
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("team", Team.class);
		return xstream.toXML(teams);
	}
}