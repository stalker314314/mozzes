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
package org.mozzes.application.example.server.service;

import java.util.List;

import org.mozzes.application.example.common.domain.Team;
import org.mozzes.application.example.common.service.TeamAdministration;
import org.mozzes.application.example.server.dao.TeamDAO;

import com.google.inject.Inject;

public class TeamAdministrationImpl implements TeamAdministration {

  @Inject
  private TeamDAO dao;

  @Override
  public Team save(Team team) {
    return dao.save(team);
  }

  @Override
  public void delete(Team team) {
    dao.delete(team);
  }

  @Override
  public List<Team> findAll() {
    return dao.findAll();
  }

  @Override
  public Team findById(Integer id) {
    return dao.findById(id, false);
  }

  @Override
  public List<Team> findByExample(Team example) {
    return dao.findByExample(example);
  }

}
