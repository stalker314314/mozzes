package example.server.service;

import java.util.List;

import com.google.inject.Inject;

import example.common.domain.Team;
import example.common.service.TeamAdministration;
import example.server.dao.TeamDAO;

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
