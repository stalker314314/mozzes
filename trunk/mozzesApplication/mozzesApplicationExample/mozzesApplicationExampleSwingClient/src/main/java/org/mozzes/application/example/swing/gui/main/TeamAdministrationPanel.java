/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
