package example.swing.gui.main;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;

import example.common.domain.Team;
import example.swing.Server;
import example.swing.gui.AbstractCRUDPanel;
import example.swing.gui.edit.TeamCreateEditDialog;

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
