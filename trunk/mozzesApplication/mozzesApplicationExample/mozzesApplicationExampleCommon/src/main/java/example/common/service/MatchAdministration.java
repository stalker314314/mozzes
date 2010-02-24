package example.common.service;

import java.util.Date;
import java.util.List;

import example.common.domain.Match;
import example.common.domain.Team;

public interface MatchAdministration extends Administration<Match> {
	
	public List<Match> findByStartTime(Date from, Date to);
	
	public List<Match> findByTeam(Team team);

}
