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
package org.mozzes.application.example.server.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.mozzes.application.example.common.domain.Match;
import org.mozzes.application.example.common.domain.Team;
import org.mozzes.application.hibernate.dao.GenericHibernateDAO;

public class MatchDAO extends GenericHibernateDAO<Match, Integer> {

  public List<Match> findByStartTime(Date from, Date to) {
    return findByCriteria(Restrictions.between("startTime", from, to));
  }

  public List<Match> findByTeam(Team team) {
    return findByCriteria(Restrictions.or(Restrictions.eq("homeTeam", team), Restrictions.eq("visitorTeam", team)));
  }
}
