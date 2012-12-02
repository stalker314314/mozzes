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

import java.util.Date;
import java.util.List;

import org.mozzes.application.example.common.domain.Match;
import org.mozzes.application.example.common.domain.Team;
import org.mozzes.application.example.common.service.Administration;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class MatchEditWindow extends Window {

  private static final long serialVersionUID = 3432589583281572169L;

  private final MainWindow mainWindow;
  private final Administration<Match> administration;
  private final Match match;

  private DateField startTime = new DateField("Start time");
  private ComboBox homeTeamCombo = new ComboBox("Home team");
  private ComboBox visitorTeamCombo = new ComboBox("Visitor team");

  MatchEditWindow(MainWindow mainWindow, String title, Match match, Administration<Match> administration,
      List<Team> teams) {
    super(title);
    setModal(true);
    this.mainWindow = mainWindow;
    this.administration = administration;
    this.match = match;

    startTime.setResolution(DateField.RESOLUTION_MIN);
    homeTeamCombo.setNewItemsAllowed(false);
    homeTeamCombo.setNullSelectionAllowed(false);
    homeTeamCombo.addItem("");
    visitorTeamCombo.setNewItemsAllowed(false);
    visitorTeamCombo.setNullSelectionAllowed(false);
    visitorTeamCombo.addItem("");
    for (Team team : teams) {
      homeTeamCombo.addItem(team);
      visitorTeamCombo.addItem(team);
    }

    setMatch(match);

    FormLayout form = new FormLayout();
    form.setSizeUndefined();
    form.addComponent(startTime);
    form.addComponent(homeTeamCombo);
    form.addComponent(visitorTeamCombo);

    Button saveButton = new Button("Save");
    saveButton.addListener(new ClickListener() {
      private static final long serialVersionUID = 8515020970178194064L;

      @Override
      public void buttonClick(ClickEvent event) {
        save();
      }
    });

    HorizontalLayout actions = new HorizontalLayout();
    actions.addComponent(saveButton);
    actions.setComponentAlignment(saveButton, Alignment.MIDDLE_CENTER);

    Layout layout = new VerticalLayout();
    layout.setStyleName(STYLE_LIGHT);
    layout.setSizeUndefined();
    layout.setMargin(true);
    layout.addComponent(form);
    layout.addComponent(actions);
    setContent(layout);
  }

  private void setMatch(Match match) {
    startTime.setValue(match.getStartTime());
    homeTeamCombo.setValue(match.getHomeTeam());
    visitorTeamCombo.setValue(match.getVisitorTeam());
  }

  private void save() {
    match.setStartTime((Date) startTime.getValue());
    match.setHomeTeam((Team) homeTeamCombo.getValue());
    match.setVisitorTeam((Team) visitorTeamCombo.getValue());
    administration.save(match);
    mainWindow.removeWindow(this);
    mainWindow.reloadMatches();
  }
}
