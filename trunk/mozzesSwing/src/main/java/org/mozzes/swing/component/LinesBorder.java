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
/* (swing1.1) */
package org.mozzes.swing.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;

public class LinesBorder extends AbstractBorder implements SwingConstants {

	private static final long serialVersionUID = 1L;

	protected int northThickness;
	protected int southThickness;
	protected int eastThickness;
	protected int westThickness;
	protected Color northColor;
	protected Color southColor;
	protected Color eastColor;
	protected Color westColor;

	public LinesBorder() {
		
	}
	
	public LinesBorder(Color color) {
		this(color, 1);
	}

	public LinesBorder(Color color, int thickness) {
		setColor(color);
		setThickness(thickness);
	}

	public LinesBorder(Color color, Insets insets) {
		setColor(color);
		setThickness(insets);
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Color oldColor = g.getColor();

		g.setColor(northColor);
		for (int i = 0; i < northThickness; i++) {
			g.drawLine(x, y + i, x + width - 1, y + i);
		}
		g.setColor(southColor);
		for (int i = 0; i < southThickness; i++) {
			g.drawLine(x, y + height - i - 1, x + width - 1, y + height - i - 1);
		}
		g.setColor(westColor);
		for (int i = 0; i < westThickness; i++) {
			g.drawLine(x + i, y, x + i, y + height - 1);
		}
		g.setColor(eastColor);
		for (int i = 0; i < eastThickness; i++) {
			g.drawLine(x + width - i - 1, y, x + width - i - 1, y + height - 1);
		}

		g.setColor(oldColor);
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(northThickness, westThickness, southThickness, eastThickness);
	}

	@Override
	public Insets getBorderInsets(Component c, Insets insets) {
		return new Insets(northThickness, westThickness, southThickness, eastThickness);
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}

	public void setColor(Color c) {
		northColor = c;
		southColor = c;
		eastColor = c;
		westColor = c;
	}

	public void setColor(Color c, int direction) {
		switch (direction) {
		case NORTH:
			northColor = c;
			break;
		case SOUTH:
			southColor = c;
			break;
		case EAST:
			eastColor = c;
			break;
		case WEST:
			westColor = c;
			break;
		default:
		}
	}
	
	public Color getColor(int direction) {
		switch (direction) {
		case NORTH:
			return northColor;
		case SOUTH:
			return southColor;
		case EAST:
			return eastColor;
		case WEST:
			return westColor;
		default:
			return null;
		}
	}

	public void setThickness(int n) {
		northThickness = n;
		southThickness = n;
		eastThickness = n;
		westThickness = n;
	}

	public void setThickness(Insets insets) {
		northThickness = insets.top;
		southThickness = insets.bottom;
		eastThickness = insets.right;
		westThickness = insets.left;
	}

	public void setThickness(int n, int direction) {
		switch (direction) {
		case NORTH:
			northThickness = n;
			break;
		case SOUTH:
			southThickness = n;
			break;
		case EAST:
			eastThickness = n;
			break;
		case WEST:
			westThickness = n;
			break;
		default:
		}
	}

	public void append(LinesBorder b, boolean isReplace) {
		if (isReplace) {
			northThickness = b.northThickness;
			southThickness = b.southThickness;
			eastThickness = b.eastThickness;
			westThickness = b.westThickness;
		} else {
			northThickness = Math.max(northThickness, b.northThickness);
			southThickness = Math.max(southThickness, b.southThickness);
			eastThickness = Math.max(eastThickness, b.eastThickness);
			westThickness = Math.max(westThickness, b.westThickness);
		}
	}

	public void append(Insets insets, boolean isReplace) {
		if (isReplace) {
			northThickness = insets.top;
			southThickness = insets.bottom;
			eastThickness = insets.right;
			westThickness = insets.left;
		} else {
			northThickness = Math.max(northThickness, insets.top);
			southThickness = Math.max(southThickness, insets.bottom);
			eastThickness = Math.max(eastThickness, insets.right);
			westThickness = Math.max(westThickness, insets.left);
		}
	}

}
