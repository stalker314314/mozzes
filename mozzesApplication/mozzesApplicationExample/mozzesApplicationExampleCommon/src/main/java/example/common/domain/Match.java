package example.common.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Match implements Serializable {

    @Id
    @GeneratedValue
	private Integer id;
	private Date startTime;
	private Team homeTeam;
	private Team visitorTeam;
	private Result result;

	public Match() {
	}
	
	public Match(Date startTime, Team homeTeam, Team visitorTeam, Result result) {
		setStartTime(startTime);
		setHomeTeam(homeTeam);
		setVisitorTeam(visitorTeam);
		setResult(result);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Team getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}

	public Team getVisitorTeam() {
		return visitorTeam;
	}

	public void setVisitorTeam(Team visitorTeam) {
		this.visitorTeam = visitorTeam;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
	
	@Override
	public String toString() {
		return df.format(startTime) + " " + homeTeam + " - " + visitorTeam + (result != null ? " " + result : "");
	}

	@Override
	public int hashCode() {
		return (id == null) ? 0 : id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Match other = (Match) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	private static final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm");
	private static final long serialVersionUID = 199636028893450246L;
}
