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
package org.mozzes.application.example.vaadin.gui;

import java.util.List;

import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.example.common.domain.Match;
import org.mozzes.application.example.common.domain.Team;
import org.mozzes.application.example.common.service.MatchAdministration;
import org.mozzes.application.example.common.service.TeamAdministration;

import com.google.inject.Inject;
import com.google.inject.servlet.SessionScoped;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SessionScoped
public class MainWindow extends Window {

	private static final String TITLE = "Mozzes example - Vaadin web client";

	private static final long serialVersionUID = -1629403228064679292L;

	private final MozzesClient client;
	private BeanItemContainer<Team> teamSource = new BeanItemContainer<Team>(Team.class);
	private BeanItemContainer<Match> matchSource = new BeanItemContainer<Match>(Match.class);

	private Table teamTable;
	private Table matchTable;

	@Inject
	public MainWindow(MozzesClient client) {
		super(TITLE);
		this.client = client;
		setTheme("runo");
		initGui();
		reloadData();
	}

	private void initGui() {
		VerticalLayout teamLayout = new VerticalLayout();
		Table teamTable = createTeamTable();
		teamLayout.addComponent(teamTable);
		teamLayout.addComponent(createTeamButtonPanel());
		teamLayout.setExpandRatio(teamTable, 1f);
		teamLayout.setSizeFull();

		Panel teamPanel = new Panel("Teams");
		teamPanel.setContent(teamLayout);
		teamPanel.setSizeFull();

		VerticalLayout matchLayout = new VerticalLayout();
		Table matchTable = createMatchTable();
		matchLayout.addComponent(matchTable);
		matchLayout.addComponent(createMatchButtonPanel());
		matchLayout.setExpandRatio(matchTable, 1f);
		matchLayout.setSizeFull();

		Panel matchPanel = new Panel("Matches");
		matchPanel.setContent(matchLayout);
		matchPanel.setSizeFull();

		HorizontalLayout mainLayout = new HorizontalLayout();
		mainLayout.addComponent(teamPanel);
		mainLayout.addComponent(matchPanel);
		mainLayout.setSizeFull();

		Panel mainPanel = new Panel(TITLE);
		mainPanel.setStyleName(STYLE_LIGHT);
		mainPanel.setContent(mainLayout);
		mainPanel.setSizeFull();

		setContent(mainPanel);
	}

	private Table createTeamTable() {
		teamTable = new Table(null, teamSource);
		teamTable.setVisibleColumns(new Object[] { "name", "crestImage", "webAddress" });
		teamTable.setColumnHeaders(new String[] { "Name", "Crest", "Web" });
		teamTable.setSelectable(true);
		teamTable.setSizeFull();
		return teamTable;
	}

	private GridLayout createTeamButtonPanel() {
		Button refreshTeamsButton = new Button("Reload teams");
		refreshTeamsButton.setIcon(new ThemeResource("icons/32/reload.png"));
		refreshTeamsButton.addListener(new ClickListener() {
			private static final long serialVersionUID = -4440833664676514934L;

			@Override
			public void buttonClick(ClickEvent event) {
				reloadTeams();
			}
		});

		Button newTeamButton = new Button("New team");
		newTeamButton.setIcon(new ThemeResource("icons/32/document-add.png"));
		newTeamButton.addListener(new ClickListener() {
			private static final long serialVersionUID = -5792165786350656817L;

			@Override
			public void buttonClick(ClickEvent event) {
				addWindow(new TeamEditWindow(getApplication(), MainWindow.this, "New team", new Team(), client
						.getService(TeamAdministration.class)));
			}
		});

		Button editTeamButton = new Button("Edit team");
		editTeamButton.setIcon(new ThemeResource("icons/32/document-edit.png"));
		editTeamButton.addListener(new ClickListener() {
			private static final long serialVersionUID = -5792165786350656817L;

			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(ClickEvent event) {
				Object selected = teamTable.getValue();
				if (selected != null) {
					Team selectedTeam = ((BeanItem<Team>) teamTable.getItem(selected)).getBean();
					addWindow(new TeamEditWindow(getApplication(), MainWindow.this, "Edit team", selectedTeam, client
							.getService(TeamAdministration.class)));
					;
				} else
					showNotification("No team selected!");
			}
		});

		Button deleteTeamButton = new Button("Delete team");
		deleteTeamButton.setIcon(new ThemeResource("icons/32/document-delete.png"));

		GridLayout teamButtonPanel = new GridLayout(4, 1);
		teamButtonPanel.setHeight(40, UNITS_PIXELS);
		teamButtonPanel.setWidth(100, UNITS_PERCENTAGE);
		teamButtonPanel.addComponent(refreshTeamsButton);
		teamButtonPanel.addComponent(newTeamButton);
		teamButtonPanel.addComponent(editTeamButton);
		teamButtonPanel.addComponent(deleteTeamButton);
		teamButtonPanel.setComponentAlignment(refreshTeamsButton, Alignment.MIDDLE_LEFT);
		teamButtonPanel.setComponentAlignment(newTeamButton, Alignment.MIDDLE_RIGHT);
		teamButtonPanel.setComponentAlignment(editTeamButton, Alignment.MIDDLE_CENTER);
		teamButtonPanel.setComponentAlignment(deleteTeamButton, Alignment.MIDDLE_LEFT);
		return teamButtonPanel;
	}

	private Table createMatchTable() {
		matchTable = new Table(null, matchSource);
		matchTable.setVisibleColumns(new Object[] { "startTime", "homeTeam", "visitorTeam", "result" });
		matchTable.setColumnHeaders(new String[] { "Start time", "Home team", "Visitor team", "Result" });
		matchTable.setSizeFull();
		return matchTable;
	}

	private GridLayout createMatchButtonPanel() {
		Button refreshMatchesButton = new Button("Reload matches");
		refreshMatchesButton.setIcon(new ThemeResource("icons/32/reload.png"));
		refreshMatchesButton.addListener(new ClickListener() {
			private static final long serialVersionUID = -4440833664676514934L;

			@Override
			public void buttonClick(ClickEvent event) {
				reloadMatches();
			}
		});

		Button newMatchButton = new Button("New match");
		newMatchButton.setIcon(new ThemeResource("icons/32/document-add.png"));

		Button editMatchButton = new Button("Edit match");
		editMatchButton.setIcon(new ThemeResource("icons/32/document-edit.png"));
		Button deleteMatchButton = new Button("Delete match");
		deleteMatchButton.setIcon(new ThemeResource("icons/32/document-delete.png"));

		GridLayout matchButtonPanel = new GridLayout(4, 1);
		matchButtonPanel.setHeight(40, UNITS_PIXELS);
		matchButtonPanel.setWidth(100, UNITS_PERCENTAGE);
		matchButtonPanel.addComponent(refreshMatchesButton);
		matchButtonPanel.addComponent(newMatchButton);
		matchButtonPanel.addComponent(editMatchButton);
		matchButtonPanel.addComponent(deleteMatchButton);
		matchButtonPanel.setComponentAlignment(refreshMatchesButton, Alignment.MIDDLE_LEFT);
		matchButtonPanel.setComponentAlignment(newMatchButton, Alignment.MIDDLE_RIGHT);
		matchButtonPanel.setComponentAlignment(editMatchButton, Alignment.MIDDLE_CENTER);
		matchButtonPanel.setComponentAlignment(deleteMatchButton, Alignment.MIDDLE_LEFT);
		return matchButtonPanel;
	}

	private void reloadData() {
		reloadTeams();
		reloadMatches();
	}

	void reloadMatches() {
		matchSource.removeAllItems();
		for (Match match : getMatches())
			matchSource.addBean(match);
	}

	void reloadTeams() {
		teamSource.removeAllItems();
		for (Team team : getTeams())
			teamSource.addBean(team);
	}

	private List<Team> getTeams() {
		return client.getService(TeamAdministration.class).findAll();
	}

	private List<Match> getMatches() {
		return client.getService(MatchAdministration.class).findAll();
	}

}
