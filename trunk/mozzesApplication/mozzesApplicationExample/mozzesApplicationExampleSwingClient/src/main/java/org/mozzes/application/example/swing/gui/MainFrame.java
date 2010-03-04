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
package org.mozzes.application.example.swing.gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.mozzes.application.example.swing.gui.main.MatchAdministrationPanel;
import org.mozzes.application.example.swing.gui.main.TeamAdministrationPanel;


public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabs = new JTabbedPane();

	public MainFrame() {
		initTabs();
		setContentPane(tabs);
	}

	private void initTabs() {
		TeamAdministrationPanel teamAdministration = new TeamAdministrationPanel();
		tabs.addTab("Teams", teamAdministration);

		MatchAdministrationPanel matchAdministration = new MatchAdministrationPanel();
		tabs.addTab("Matches", matchAdministration);
	}

}
