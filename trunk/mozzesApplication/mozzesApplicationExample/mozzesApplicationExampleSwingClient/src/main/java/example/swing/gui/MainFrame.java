package example.swing.gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import example.swing.gui.main.MatchAdministrationPanel;
import example.swing.gui.main.TeamAdministrationPanel;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabs = new JTabbedPane();

	public MainFrame() {
		initTabs();
		setContentPane(tabs);
	}

	private void initTabs() {
		TeamAdministrationPanel teamAdministration = new TeamAdministrationPanel();
		tabs.addTab("Timovi", teamAdministration);

		MatchAdministrationPanel matchAdministration = new MatchAdministrationPanel();
		tabs.addTab("Mecevi", matchAdministration);
	}

}
