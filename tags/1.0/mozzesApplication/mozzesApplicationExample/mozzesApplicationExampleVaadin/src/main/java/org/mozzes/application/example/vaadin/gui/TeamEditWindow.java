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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;
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
	private static final Logger log = Logger.getLogger(TeamEditWindow.class);
	
	public static final String IMAGE_PATH = ".." + File.separatorChar + "images";
	public static final String IMAGE_FILE_TYPE = "png";

	private final Application application;
	private final MainWindow mainWindow;
	private final Administration<Team> administration;
	private final Team team;
	
	private TextField nameField = new TextField("Name");
	private TextField webField = new TextField("Web");
	private Embedded crestImage;
	private String crestFileName;

	TeamEditWindow(Application application, MainWindow mainWindow, String title, Team team,
			Administration<Team> administration) throws IOException {
		super(title);
		setModal(true);
		this.application = application;
		this.mainWindow = mainWindow;
		this.administration = administration;
		this.team = team;
		setTeam(team);

		FormLayout form = new FormLayout();
		form.setSizeUndefined();
		form.addComponent(nameField);
		form.addComponent(webField);
		form.addComponent(crestImage);
		form.addComponent(createImageUpload());

		Button saveButton = new Button("Save");
		saveButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 8515020970178194064L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					save();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
					showNotification("Unable to save team!", Notification.TYPE_ERROR_MESSAGE);
				}
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

	private void setTeam(Team team) throws IOException {
		nameField.setValue(team.getName());
		webField.setValue(team.getWebAddress());
		
		crestFileName = IMAGE_PATH + File.separatorChar + "t" + Thread.currentThread().hashCode()
				+ System.currentTimeMillis() + "." + IMAGE_FILE_TYPE;
		if (team.getImage() != null) {
			saveImage(team.getImage());
			crestImage = new Embedded("Crest", new FileResource(new File(crestFileName), application));
		}
		else
			crestImage = new Embedded("Crest");
		crestImage.requestRepaint();
	}

	private Upload createImageUpload() {
		ImageUploadListener uploadReceiver = new ImageUploadListener();
		Upload imageUpload = new Upload(null, uploadReceiver);
		imageUpload.addListener((Upload.SucceededListener) uploadReceiver);
		imageUpload.addListener((Upload.FailedListener) uploadReceiver);
		return imageUpload;
	}

	private void save() throws IOException {
		team.setName(String.valueOf(nameField.getValue()));
		team.setWebAddress(String.valueOf(webField.getValue()));
		team.setImage(loadImage());
		administration.save(team);
		mainWindow.removeWindow(this);
		mainWindow.reloadTeams();
	}
	
	private void saveImage(byte[] image) throws IOException {
		File file = new File(crestFileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(image);
			fos.flush();
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException ignore) {}
		}
	}
	
	private byte[] loadImage() throws IOException {
		File file = new File(crestFileName);
		if (!file.exists())
			return null;
		byte[] returnValue = new byte[(int) file.length()];
		FileInputStream fos = null;
		try {
			fos = new FileInputStream(file);
			fos.read(returnValue);
			return returnValue;
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException ignore) {}
		}
		
	}

	private class ImageUploadListener implements Upload.SucceededListener, Upload.FailedListener, Upload.Receiver {

		private static final long serialVersionUID = 7572703698105220338L;

		private File file;

		@Override
		public OutputStream receiveUpload(String filename, String MIMEType) { 
			if (MIMEType.indexOf("image") == -1) {
				showNotification("Invalid image type, only " + IMAGE_FILE_TYPE + " images are allowed!", Notification.TYPE_ERROR_MESSAGE);
				return null;
			}
			
			if (!MIMEType.substring(MIMEType.indexOf('/') + 1).equalsIgnoreCase(IMAGE_FILE_TYPE)) {
				showNotification("Invalid image type, only " + IMAGE_FILE_TYPE + " images are allowed!", Notification.TYPE_ERROR_MESSAGE);
				return null;
			}

			file = new File(crestFileName);
			FileOutputStream fos = null; // Output stream to write to

			try {
				file.createNewFile();
				fos = new FileOutputStream(file);
			} catch (IOException e) {
				// Error while opening the file. Not reported here.
				log.error("Unable to open team crest file", e);
				return null;
			}
			return fos; // Return the output stream to write to
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			showNotification("File " + event.getFilename() + " of type '" + event.getMIMEType() + "' uploaded.",
					Notification.TYPE_HUMANIZED_MESSAGE);
			crestImage.setSource((new FileResource(file, application)));
			crestImage.requestRepaint();
		}

		@Override
		public void uploadFailed(FailedEvent event) {
			showNotification("Uploading " + event.getFilename() + " of type '" + event.getMIMEType() + "' failed.",
					Notification.TYPE_ERROR_MESSAGE);
		}
	}

}
