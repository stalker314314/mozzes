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

import java.util.Date;
import java.util.List;

import org.mozzes.application.example.common.domain.Match;
import org.mozzes.application.example.common.domain.Team;
import org.mozzes.application.example.common.service.MatchAdministration;
import org.mozzes.application.example.server.dao.MatchDAO;

import com.google.inject.Inject;

public class MatchAdministrationImpl implements MatchAdministration {

  @Inject
  private MatchDAO dao;

  @Override
  public Match save(Match match) {
    return dao.save(match);
  }

  @Override
  public void delete(Match match) {
    dao.delete(match);
  }

  @Override
  public List<Match> findAll() {
    return dao.findAll();
  }

  @Override
  public Match findById(Integer id) {
    return dao.findById(id, false);
  }

  @Override
  public List<Match> findByStartTime(Date from, Date to) {
    return dao.findByStartTime(from, to);
  }

  @Override
  public List<Match> findByTeam(Team team) {
    return dao.findByTeam(team);
  }

  @Override
  public List<Match> findByExample(Match example) {
    return dao.findByExample(example);
  }
}
