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
import org.mozzes.application.example.swing.Server;
import org.mozzes.application.example.swing.gui.AbstractCreateEditDialog;
import org.mozzes.swing.mgf.binding.Binder;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;
import org.mozzes.swing.mgf.datarenderer.combobox.ComboBoxRenderModel;



public class MatchCreateEditDialog extends AbstractCreateEditDialog<Match> {
	private static final long serialVersionUID = 1L;

	private JLabel lblHome = new JLabel("Domacin:");
	private JLabel lblGuest = new JLabel("Gost:");
	private JLabel lblStartTime = new JLabel("Vreme pocetka:");
	private JLabel lblResult = new JLabel("Rezultat:");

	private ComboBoxRenderModel<Team> cmbHome = new ComboBoxRenderModel<Team>(Team.class);
	private ComboBoxRenderModel<Team> cmbGuest = new ComboBoxRenderModel<Team>(Team.class);

	private JXDatePicker datePicker = new JXDatePicker();
	private JTextField txtResultHome = new JTextField();
	private JTextField txtResultVisitor = new JTextField();

	public MatchCreateEditDialog() {
		super(Server.getMatchAdministrationService(), getEmptyMatch());

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
		List<Team> teams = Server.getTeamAdministrationService().findAll();
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
