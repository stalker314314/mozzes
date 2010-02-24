package example.swing.gui.main;

import java.util.Date;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;


import example.common.domain.Match;
import example.common.domain.Result;
import example.common.domain.Team;
import example.swing.Server;
import example.swing.gui.AbstractCRUDPanel;
import example.swing.gui.edit.MatchCreateEditDialog;

public class MatchAdministrationPanel extends AbstractCRUDPanel<Match> {
	private static final long serialVersionUID = 1L;

	public MatchAdministrationPanel() {
		super(Server.getMatchAdministrationService(), new MatchCreateEditDialog());
	}

	@Override
	protected void initModel(DataModel<Match> model) {
		model.addField(new PropertyField<Match, Team>(Team.class, "homeTeam", "Domacin"));
		model.addField(new PropertyField<Match, Team>(Team.class, "visitorTeam", "Gost"));
		model.addField(new PropertyField<Match, Date>(Date.class, "startTime", "Vreme pocetka"));
		model.addField(new PropertyField<Match, Result>(Result.class, "result", "Rezultat"));
	}

}
