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
