package org.mozzes.application.example.common.service;

import java.util.Date;
import java.util.List;

import org.mozzes.application.example.common.domain.Match;
import org.mozzes.application.example.common.domain.Team;


public interface MatchAdministration extends Administration<Match> {
	
	public List<Match> findByStartTime(Date from, Date to);
	
	public List<Match> findByTeam(Team team);

}
