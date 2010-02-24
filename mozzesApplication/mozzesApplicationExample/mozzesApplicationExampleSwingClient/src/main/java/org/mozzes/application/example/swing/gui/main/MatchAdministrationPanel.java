package org.mozzes.application.example.swing.gui.main;

import java.util.Date;

import org.mozzes.application.example.common.domain.Match;
import org.mozzes.application.example.common.domain.Result;
import org.mozzes.application.example.common.domain.Team;
import org.mozzes.application.example.swing.Server;
import org.mozzes.application.example.swing.gui.AbstractCRUDPanel;
import org.mozzes.application.example.swing.gui.edit.MatchCreateEditDialog;
import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;



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
