package example.server.service;

import java.util.Date;
import java.util.List;

import com.google.inject.Inject;

import example.common.domain.Match;
import example.common.domain.Team;
import example.common.service.MatchAdministration;
import example.server.dao.MatchDAO;

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
