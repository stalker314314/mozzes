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
package org.mozzes.application.example.rest.jersey;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.example.common.domain.Team;
import org.mozzes.application.example.common.service.TeamAdministration;
import org.mozzes.rest.jersey.guice.MozzesGuiceFactory;

import com.google.inject.Inject;
import com.sun.jersey.api.NotFoundException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

@Path("team/{teamId}/")
@MozzesGuiceFactory
/**
 * Rest service for getting information about one team
 * @author stalker
 */
public class RestOneTeamService {

  @Inject
  private MozzesClient mozzesClient;

  @GET
  @Produces({ MediaType.TEXT_XML })
  public String getTeam(@PathParam("teamId") String teamId) {
    Integer id;
    try {
      id = Integer.parseInt(teamId);
    } catch (NumberFormatException e) {
      throw new NotFoundException("Team " + teamId + " not found");
    }
    List<Team> teams = mozzesClient.getService(TeamAdministration.class).findAll();
    XStream xstream = new XStream(new DomDriver());
    xstream.alias("team", Team.class);
    for (Team team : teams) {
      if (team.getId().equals(id)) {
        return xstream.toXML(team);
      }
    }
    throw new NotFoundException("Team " + teamId + " not found");
  }
}