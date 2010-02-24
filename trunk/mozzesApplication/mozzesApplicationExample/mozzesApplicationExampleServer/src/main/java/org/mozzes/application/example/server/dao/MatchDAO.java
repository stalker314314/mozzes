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
