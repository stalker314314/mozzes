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
package org.mozzes.application.example.swing.gui.edit;

import java.awt.Dimension;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.mozzes.application.example.common.domain.Match;
import org.mozzes.application.example.common.domain.Result;
import org.mozzes.application.example.common.domain.Team;
import org.mozzes.application.example.common.service.MatchAdministration;
import org.mozzes.application.example.common.service.TeamAdministration;
import org.mozzes.application.example.swing.ApplicationContext;
import org.mozzes.application.example.swing.gui.AbstractCreateEditDialog;
import org.mozzes.swing.mgf.binding.Binder;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;
import org.mozzes.swing.mgf.datarenderer.combobox.ComboBoxRenderModel;



public class MatchCreateEditDialog extends AbstractCreateEditDialog<Match> {
	private static final long serialVersionUID = 1L;

	private JLabel lblHome = new JLabel("Home team:");
	private JLabel lblGuest = new JLabel("Visitor team:");
	private JLabel lblStartTime = new JLabel("Start time:");
	private JLabel lblResult = new JLabel("Result:");

	private ComboBoxRenderModel<Team> cmbHome = new ComboBoxRenderModel<Team>(Team.class);
	private ComboBoxRenderModel<Team> cmbGuest = new ComboBoxRenderModel<Team>(Team.class);

	private JXDatePicker datePicker = new JXDatePicker();
	private JTextField txtResultHome = new JTextField();
	private JTextField txtResultVisitor = new JTextField();

	public MatchCreateEditDialog() {
		super(ApplicationContext.getService(MatchAdministration.class), getEmptyMatch());

		initialize();

		setSize(new Dimension(300, 200));
		setLocationRelativeTo(null);
	}

	private static Match getEmptyMatch() {
		Match match = new Match();
		match.setResult(new Result());
		return match;
	}

	@Override
	protected void activate() {
		loadData();
	}

	private void loadData() {
		List<Team> teams = ApplicationContext.getService(TeamAdministration.class).findAll();
		cmbHome.setValues(teams, true);
		cmbGuest.setValues(teams, true);
	}

	@Override
	protected void bindFields() {
		Binder.bind(cmbHome.getComboBox(), getSource(), new PropertyField<Match, Team>(
				Team.class, "homeTeam", false));
		Binder.bind(cmbGuest.getComboBox(), getSource(), new PropertyField<Match, Team>(
				Team.class, "visitorTeam", false));
		Binder.bind(datePicker, getSource(), new PropertyField<Match, Date>(
				Date.class, "startTime", false));
		Binder.bind(txtResultHome, getSource(), new PropertyField<Match, Integer>(
				Integer.class, "result.home", false));
		Binder.bind(txtResultVisitor, getSource(), new PropertyField<Match, Integer>(
				Integer.class, "result.visitor", false));
	}

	@Override
	protected JPanel getMainPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("wrap 2", "[p!][grow, fill]"));
		panel.add(lblHome);
		panel.add(cmbHome.getRenderComponent());
		panel.add(lblGuest);
		panel.add(cmbGuest.getRenderComponent());
		panel.add(lblStartTime);
		panel.add(datePicker);
		panel.add(lblResult);
		panel.add(txtResultHome, "split");
		panel.add(new JPanel());
		panel.add(txtResultVisitor, "w 20px");

		return panel;
	}
}
