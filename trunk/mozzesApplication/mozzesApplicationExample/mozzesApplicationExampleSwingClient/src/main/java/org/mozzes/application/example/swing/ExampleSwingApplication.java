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
package org.mozzes.application.example.swing;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.mozzes.application.example.swing.gui.MainFrame;
import org.mozzes.swing.mgf.localization.Localization;



public class ExampleSwingApplication {
	private static final Dimension WINDOW_SIZE = new Dimension(800, 600);
	private static JFrame applicationMainFrame;

	public static void main(String[] args) {
		// ExampleServer.main(null);
		org.mozzes.swing.mgf.localization.Localization.getValue(
				org.mozzes.swing.mgf.localization.LocalizationKey.INVALID_NUMBER_FORMAT_MESSAGE);
		Localization.initialize("sr");
		setAmadeusLookAndFeel();
		showMainFrame();
	}

	private static void showMainFrame() {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				applicationMainFrame = new MainFrame();
				applicationMainFrame.setSize(WINDOW_SIZE);
				applicationMainFrame.setLocationRelativeTo(null);

				applicationMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				applicationMainFrame.setVisible(true);
			}
		});
	}

	public static void setAmadeusLookAndFeel() {
		try {
			// In Java version 7 Nimbus will move to plaf.nimbus
			// That's way we are loading it like this (and for falling back to default)
			for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(laf.getName())) {
					UIManager.setLookAndFeel(laf.getClassName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public static JFrame getApplicationMainFrame() {
		return applicationMainFrame;
	}
}
