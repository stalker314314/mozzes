package example.vaadin.gui;

import java.util.List;

import org.mozzes.application.common.client.MozzesClient;

import com.google.inject.Inject;
import com.google.inject.servlet.SessionScoped;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import example.common.domain.Match;
import example.common.domain.Team;
import example.common.service.MatchAdministration;
import example.common.service.TeamAdministration;

@SessionScoped
public class MainWindow extends Window {

	private static final long serialVersionUID = -1629403228064679292L;

	private final MozzesClient client;
	private BeanItemContainer<Team> teamSource = new BeanItemContainer<Team>(Team.class);
	private BeanItemContainer<Match> matchSource = new BeanItemContainer<Match>(Match.class);
	
	@Inject
	public MainWindow(MozzesClient client) {
		super("Hello!");
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

		Panel teamPanel = new Panel("Timovi");
		teamPanel.setContent(teamLayout);
		teamPanel.setSizeFull();
		
		VerticalLayout matchLayout = new VerticalLayout();
		Table matchTable = createMatchTable();
		matchLayout.addComponent(matchTable);
		matchLayout.addComponent(createMatchButtonPanel());
		matchLayout.setExpandRatio(matchTable, 1f);
		matchLayout.setSizeFull();

		Panel matchPanel = new Panel("Mečevi");
		matchPanel.setContent(matchLayout);
		matchPanel.setSizeFull();

		HorizontalLayout mainLayout = new HorizontalLayout();
		mainLayout.addComponent(teamPanel);
		mainLayout.addComponent(matchPanel);
		mainLayout.setSizeFull();

		Panel mainPanel = new Panel("Primer Vaadin klijenta za Mozzes server");
		mainPanel.setStyleName(STYLE_LIGHT);
		mainPanel.setContent(mainLayout);
		mainPanel.setSizeFull();

		setContent(mainPanel);
	}

	private Table createTeamTable() {
		Table teamTable = new Table(null, teamSource);
		teamTable.setVisibleColumns(new Object[]{"name", "image", "webAddress"});
		teamTable.setColumnHeaders(new String[]{"Naziv", "Grb", "Web"});
		teamTable.setSizeFull();
		return teamTable;
	}

	private GridLayout createTeamButtonPanel() {
		Button refreshTeamsButton = new Button("Osveži timove");
		refreshTeamsButton.setIcon(new ThemeResource("icons/32/reload.png"));
		refreshTeamsButton.addListener(new ClickListener() {
			private static final long serialVersionUID = -4440833664676514934L;
			@Override
			public void buttonClick(ClickEvent event) {
				reloadTeams();
			}
		});
		
		Button newTeamButton = new Button("Kreiraj tim");
		newTeamButton.setIcon(new ThemeResource("icons/32/document-add.png"));
		newTeamButton.addListener(new ClickListener() {
			private static final long serialVersionUID = -5792165786350656817L;
			@Override
			public void buttonClick(ClickEvent event) {
				newTeam(new Team());
			}
		});
		
		Button editTeamButton = new Button("Izmeni tim");
		editTeamButton.setIcon(new ThemeResource("icons/32/document-edit.png"));
		Button deleteTeamButton = new Button("Obriši tim");
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
		Table matchTable = new Table(null, matchSource);
		matchTable.setVisibleColumns(new Object[]{"startTime", "homeTeam", "visitorTeam", "result"});
		matchTable.setColumnHeaders(new String[]{"Vreme", "Domaćin", "Gost", "Rezultat"});
		matchTable.setSizeFull();
		return matchTable;
	}

	private GridLayout createMatchButtonPanel() {
		Button refreshMatchesButton = new Button("Osveži mečeve");
		refreshMatchesButton.setIcon(new ThemeResource("icons/32/reload.png"));
		refreshMatchesButton.addListener(new ClickListener() {
			private static final long serialVersionUID = -4440833664676514934L;
			@Override
			public void buttonClick(ClickEvent event) {
				reloadMatches();
			}
		});
		
		Button newMatchButton = new Button("Kreiraj meč");
		newMatchButton.setIcon(new ThemeResource("icons/32/document-add.png"));
		
		Button editMatchButton = new Button("Izmeni meč");
		editMatchButton.setIcon(new ThemeResource("icons/32/document-edit.png"));
		Button deleteMatchButton = new Button("Obriši meč");
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

	private void reloadMatches() {
		matchSource.removeAllItems();
		for (Match match : getMatches()) 
			matchSource.addBean(match);
	}

	private void reloadTeams() {
		teamSource.removeAllItems();
		for (Team team: getTeams()) 
			teamSource.addBean(team);
	}
	

	private List<Team> getTeams() {
		return client.getService(TeamAdministration.class).findAll();
	}
	
	private List<Match> getMatches() {
		return client.getService(MatchAdministration.class).findAll();
	}
	
	private void newTeam(Team team) {
		Form teamForm = new Form();
		teamForm.setItemDataSource(new BeanItem<Team>(team));
		
		
		GridLayout layout = new GridLayout(1, 1);
		layout.addComponent(teamForm);
		addWindow(new Window("Kreiranje tima", layout));
	}
}
