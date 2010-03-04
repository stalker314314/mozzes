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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private BufferedImage image;
	private JFileChooser fileChooser = new JFileChooser();

	public ImagePanel() {
		fileChooser.setFileFilter(new ImageFilter());
		setupHandlers();
	}

	private void setupHandlers() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadImage();
			}
		});
	}

	private void loadImage() {
		int result = fileChooser.showOpenDialog(this);
		if (result != JFileChooser.APPROVE_OPTION)
			return;
		File inputFile = fileChooser.getSelectedFile();

		BufferedImage read = null;
		try {
			read = ImageIO.read(inputFile);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}
		this.setImage(read);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(getImage(), 0, 0, getWidth(), getHeight(), this);

		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.black);
		g2.drawRect(0, 0, getWidth(), getHeight());
	}

	private BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		BufferedImage oldImage = this.image;
		this.image = image;
		repaint();
		firePropertyChange("image", oldImage, image);
	}

	public byte[] getImageAsByteStream() {
		if (image == null)
			return null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			return baos.toByteArray();
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

}

class ImageFilter extends FileFilter {

	@Override
	public String getDescription() {
		return "Images";
	}

	private String takeExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;

		String ext = takeExtension(f);
		String[] accepted = new String[] {
				"tiff", "tif", "jpg", "jpeg", "png", "gif" };
		for (String e : accepted) {
			if (e.equals(ext))
				return true;
		}
		return false;
	}
}