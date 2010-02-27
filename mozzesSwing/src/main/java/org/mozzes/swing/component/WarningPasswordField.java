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
package org.mozzes.swing.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPasswordField;
import javax.swing.text.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component that adds icon to the {@link JPasswordField} for warning the user that CAPS LOCK are on.
 *
 * @author kokan
 */
public class WarningPasswordField extends JPasswordField {
	
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(WarningPasswordField.class);
	
	private final int verticalInsets = 5;

	private final int rightOffset = 4;

	private BufferedImage warningImage = null;

	private boolean paintWarn = false;

	public WarningPasswordField(Document doc, String txt, int columns) {
	    super(doc, txt, columns);
	    init();
    }

	public WarningPasswordField(int columns) {
	    super(columns);
	    init();
    }

	public WarningPasswordField(String text, int columns) {
	    super(text, columns);
	    init();
    }

	public WarningPasswordField(String text) {
	    super(text);
	    init();
    }

	public WarningPasswordField() {
		init();
	}

	private void init() {
	    try {
			InputStream is = ClassLoader.getSystemResourceAsStream("icons/warning.png");
			if (is != null) {
				warningImage = ImageIO.read(is);
				addFocusListener(new FocusListener() {
					
					@Override
					public void focusLost(FocusEvent e) {
						paintWarn = false;
						repaint();
					}
					
					@Override
					public void focusGained(FocusEvent e) {
						checkAndRepaint();
					}
				});
				
				addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent p_e) {
						checkAndRepaint();
					}
				});
			} else
				logger.warn("warning.png image not loaded");
			
		}catch (IOException e) {
			logger.warn("warning.png image not loaded",e);
		}
    }

	private void checkAndRepaint() {
		try {
			paintWarn = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
		}catch (UnsupportedOperationException ignore) {
			/* workaround sun java bug 5100701 when using XToolkit */
		}
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		/* image is not loaded, behave like a normal password field */
		if (warningImage == null)
			return;

		if (paintWarn) {
			int width = this.getWidth();
			int height = this.getHeight();
			int iconSize = height - 2 * verticalInsets;
			int dx1 = width - (iconSize + rightOffset);
			int dy1 = verticalInsets;
			
			/*
			 * fill white rectangle where image will be, so password stars are not visible if image is transparent
			 */
			g.setColor(Color.WHITE);
			g.fillRect(dx1, dy1, iconSize, iconSize);

			/* draw warning image */
			g.drawImage(warningImage, dx1, dy1, dx1 + iconSize, dy1 + iconSize, 0, 0, warningImage.getWidth(), warningImage.getHeight(), null);
		}
	}
}
