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

import java.util.Date;

import org.mozzes.application.example.common.domain.Match;
import org.mozzes.application.example.common.domain.Result;
import org.mozzes.application.example.common.domain.Team;
import org.mozzes.application.example.common.service.MatchAdministration;
import org.mozzes.application.example.swing.ApplicationContext;
import org.mozzes.application.example.swing.gui.AbstractCRUDPanel;
import org.mozzes.application.example.swing.gui.edit.MatchCreateEditDialog;
import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;



public class MatchAdministrationPanel extends AbstractCRUDPanel<Match> {
	private static final long serialVersionUID = 1L;

	public MatchAdministrationPanel() {
		super(ApplicationContext.getService(MatchAdministration.class), new MatchCreateEditDialog());
	}

	@Override
	protected void initModel(DataModel<Match> model) {
		model.addField(new PropertyField<Match, Team>(Team.class, "homeTeam", "Home team"));
		model.addField(new PropertyField<Match, Team>(Team.class, "visitorTeam", "Visitor team"));
		model.addField(new PropertyField<Match, Date>(Date.class, "startTime", "Start time"));
		model.addField(new PropertyField<Match, Result>(Result.class, "result", "Result"));
	}

}
