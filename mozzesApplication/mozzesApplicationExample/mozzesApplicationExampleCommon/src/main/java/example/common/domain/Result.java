package example.common.domain;

import java.io.Serializable;

public class Result implements Serializable {

	private Integer home;
	private Integer visitor;
	
	public Result() {
	}
	
	public Result(Integer home, Integer visitor) {
		setHome(home);
		setVisitor(visitor);
	}

	public Integer getHome() {
		return home;
	}

	public void setHome(Integer home) {
		this.home = home;
	}

	public Integer getVisitor() {
		return visitor;
	}

	public void setVisitor(Integer visitor) {
		this.visitor = visitor;
	}

	@Override
	public String toString() {
		return home + ":" + visitor;
	}

	private static final long serialVersionUID = -1919160039204696317L;
}
