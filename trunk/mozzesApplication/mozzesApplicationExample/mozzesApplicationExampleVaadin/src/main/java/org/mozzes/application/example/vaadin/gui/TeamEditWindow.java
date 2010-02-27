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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.mozzes.application.example.common.domain.Team;
import org.mozzes.application.example.common.service.Administration;

import com.vaadin.Application;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.SucceededEvent;

public class TeamEditWindow extends Window {

	private static final long serialVersionUID = 3432589583281572169L;

	private final Application application;
	private final MainWindow mainWindow;
	private final Administration<Team> administration;
	private FormLayout form;

	private Team team;
	private Upload imageUpload;
	private TextField nameField = new TextField("Name");
	private TextField webField = new TextField("Web");
	private Embedded crestImage;
	private ImageUploadListener uploadReceiver;

	TeamEditWindow(Application application, MainWindow mainWindow, String title, Team team,
			Administration<Team> administration) {
		super(title);
		setModal(true);
		this.application = application;
		this.mainWindow = mainWindow;
		this.administration = administration;
		setTeam(team);
		setImageUpload(team);

		form = new FormLayout();
		form.setSizeUndefined();
		form.addComponent(nameField);
		form.addComponent(webField);
		form.addComponent(crestImage);
		form.addComponent(imageUpload);

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
		layout.addComponent(form);
		layout.addComponent(actions);
		setContent(layout);
	}

	private void setTeam(Team team) {
		this.team = team;
		nameField.setValue(team.getName());
		webField.setValue(team.getWebAddress());
		if (team.getCrestImage() != null)
			crestImage = new Embedded("Crest", new FileResource(new File(team.getCrestImage()), application));
		else
			crestImage = new Embedded("Crest");
		crestImage.requestRepaint();
	}

	private void setImageUpload(Team team) {
		uploadReceiver = new ImageUploadListener(team);
		imageUpload = new Upload("Upload crest image", uploadReceiver);
		imageUpload.setButtonCaption("Upload");
		imageUpload.addListener((Upload.SucceededListener) uploadReceiver);
		imageUpload.addListener((Upload.FailedListener) uploadReceiver);
	}

	private void save() {
		team.setName(String.valueOf(nameField.getValue()));
		team.setWebAddress(String.valueOf(webField.getValue()));
		if (uploadReceiver.file != null)
			team.setCrestImage(uploadReceiver.file.getAbsolutePath());
		administration.save(team);
		mainWindow.removeWindow(this);
		mainWindow.reloadTeams();
	}

	private static final String IMAGE_PATH = "c:" + File.separatorChar + "images" + File.separatorChar;

	private class ImageUploadListener implements Upload.SucceededListener, Upload.FailedListener, Upload.Receiver {

		private static final long serialVersionUID = 7572703698105220338L;

		private final Team team;
		private File file;

		public ImageUploadListener(Team team) {
			this.team = team;
		}

		@Override
		public OutputStream receiveUpload(String filename, String MIMEType) {
			if (MIMEType.indexOf("image") == -1)
				return null;

			FileOutputStream fos = null; // Output stream to write to
			if (team.getCrestImage() != null)
				file = new File(team.getCrestImage());
			else
				file = new File(generateImageFileName() + "." + MIMEType.substring(MIMEType.indexOf('/') + 1));
			try {
				// Open the file for writing.
				fos = new FileOutputStream(file);
			} catch (final java.io.FileNotFoundException e) {
				// Error while opening the file. Not reported here.
				e.printStackTrace();
				return null;
			}
			return fos; // Return the output stream to write to
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			showNotification("File " + event.getFilename() + " of type '" + event.getMIMEType() + "' uploaded.");
			form.removeComponent(crestImage);
			crestImage = new Embedded("Crest", (new FileResource(file, application)));
			crestImage.requestRepaint();
			form.addComponent(crestImage);
		}

		@Override
		public void uploadFailed(FailedEvent event) {
			showNotification("Uploading " + event.getFilename() + " of type '" + event.getMIMEType() + "' failed.",
					Notification.TYPE_ERROR_MESSAGE);
		}

		private String generateImageFileName() {
			return IMAGE_PATH + "t" + hashCode() + System.currentTimeMillis();
		}
	}

}
