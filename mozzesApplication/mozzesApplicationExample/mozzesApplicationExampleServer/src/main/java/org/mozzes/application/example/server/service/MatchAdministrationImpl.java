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
