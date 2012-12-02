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
package org.mozzes.application.example.server;

import org.mozzes.application.example.common.ExampleConstants;
import org.mozzes.application.example.common.service.MatchAdministration;
import org.mozzes.application.example.common.service.TeamAdministration;
import org.mozzes.application.example.server.service.MatchAdministrationImpl;
import org.mozzes.application.example.server.service.TeamAdministrationImpl;
import org.mozzes.application.hibernate.HibernateConfigurationType;
import org.mozzes.application.hibernate.HibernatePlugin;
import org.mozzes.application.remoting.server.RemotingPlugin;
import org.mozzes.application.server.MozzesServer;
import org.mozzes.application.server.MozzesServerConfiguration;
import org.mozzes.rest.jersey.RestJerseyModule;

public class ExampleServer {

  public MozzesServerConfiguration createServerConfiguration() {
    MozzesServerConfiguration cfg = new MozzesServerConfiguration();
    cfg.addApplicationModule(new HibernatePlugin(HibernateConfigurationType.ANNOTATION));
    cfg.addApplicationModule(new RemotingPlugin(ExampleConstants.PORT));
    cfg.addApplicationModule(new RestJerseyModule("http://localhost:8000/",
        "org.mozzes.application.example.rest.jersey"));

    cfg.addService(MatchAdministration.class, MatchAdministrationImpl.class);
    cfg.addService(TeamAdministration.class, TeamAdministrationImpl.class);
    return cfg;
  }

  private void start() {
    MozzesServer server = new MozzesServer(createServerConfiguration());
    server.start();
  }

  public static void main(String[] args) {
    new ExampleServer().start();
  }

}
