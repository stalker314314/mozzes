package org.mozzes.application.example.swing.gui.main;

import org.mozzes.application.example.common.domain.Team;
import org.mozzes.application.example.swing.Server;
import org.mozzes.application.example.swing.gui.AbstractCRUDPanel;
import org.mozzes.application.example.swing.gui.edit.TeamCreateEditDialog;
import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;


public class TeamAdministrationPanel extends AbstractCRUDPanel<Team> {
	private static final long serialVersionUID = 1L;

	public TeamAdministrationPanel() {
		super(Server.getTeamAdministrationService(), new TeamCreateEditDialog());
	}

	@Override
	protected void initModel(DataModel<Team> model) {
		model.addField(new PropertyField<Team, String>(String.class, "name", "Naziv"));
		model.addField(new PropertyField<Team, String>(String.class, "webAddress", "Web adresa"));
	}
}
