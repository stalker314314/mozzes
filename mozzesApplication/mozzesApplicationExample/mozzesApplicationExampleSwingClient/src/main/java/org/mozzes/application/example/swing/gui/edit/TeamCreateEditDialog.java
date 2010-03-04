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
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.mozzes.application.example.common.domain.Team;
import org.mozzes.application.example.common.service.TeamAdministration;
import org.mozzes.application.example.swing.ApplicationContext;
import org.mozzes.application.example.swing.gui.AbstractCreateEditDialog;
import org.mozzes.application.example.swing.gui.ImagePanel;
import org.mozzes.swing.mgf.binding.Binder;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.DataSourceEventListener;

import net.miginfocom.swing.MigLayout;



public class TeamCreateEditDialog extends AbstractCreateEditDialog<Team> {
	private static final long serialVersionUID = 1L;

	private final ToSource toSource = new ToSource();
	private final FromSource fromSource = new FromSource();

	private JLabel lblName = new JLabel("Naziv:");
	private JLabel lblWebSite = new JLabel("Web adresa:");
	private JLabel lblCrest = new JLabel("Grb: ");

	private JTextField txtName = new JTextField();
	private JTextField txtWebSite = new JTextField();
	private ImagePanel imagePanel = new ImagePanel();

	public TeamCreateEditDialog() {
		super(ApplicationContext.getService(TeamAdministration.class), new Team());
		initialize();
		setSize(new Dimension(640, 480));
		setLocationRelativeTo(null);
		configureImagePanel();
	}

	private void configureImagePanel() {
		imagePanel.setPreferredSize(new Dimension(0, 10000));
		imagePanel.addPropertyChangeListener("image", toSource);
		getSource().addEventListener(fromSource);
	}

	@Override
	protected void bindFields() {
		Binder.bind(txtName, getSource(), new PropertyField<Team, String>(
				String.class, "name", false));
		Binder.bind(txtWebSite, getSource(), new PropertyField<Team, String>(
				String.class, "webAddress", false));
	}

	@Override
	protected JPanel getMainPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("wrap 2", "[p!][grow, fill]"));
		panel.add(lblName);
		panel.add(txtName);
		panel.add(lblWebSite);
		panel.add(txtWebSite);
		panel.add(lblCrest, "aligny top");
		panel.add(imagePanel, "growy, growx");
		return panel;
	}

	private class ToSource implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			getSource().getData().setImage(imagePanel.getImageAsByteStream());
		}
	}

	private class FromSource implements DataSourceEventListener<Team> {
		@Override
		public void handleDataSourceEvent(DataSource<Team> source, DataSourceEvent<Team> event) {
			try {
				if (source.getData().getImage() == null) {
					imagePanel.setImage(null);
					return;
				}
				ByteArrayInputStream bais = new ByteArrayInputStream(source.getData().getImage());
				BufferedImage read;
				read = ImageIO.read(bais);
				imagePanel.setImage(read);
			} catch (IOException e) {
				throw new IllegalStateException(e.getMessage());
			}
		}
	}
}
