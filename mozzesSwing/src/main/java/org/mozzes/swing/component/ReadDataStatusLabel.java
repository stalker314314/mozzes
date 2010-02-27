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
import java.awt.Font;

import javax.swing.JLabel;


/**
 * [FINIMPL] [FIN-59]
 * Labela za prikazivanje broja prochitanih redova.
 * 
 * @author nenadl.
 *
 */
public class ReadDataStatusLabel extends JLabel {

	/**
	 * Svuid.
	 */
	private static final long serialVersionUID = -5685755979290588127L;
	
	/**
	 * Tekst: 'Status'.
	 */
	private String status;
	
	/**
	 * Poruka koja se ispisuje ako je broj prochitanih redova veci od 0.
	 */
	private String dataRead;

	/**
	 * Poruka koja se ispisuje ako je broj prochitanih redova 0.
	 */
	private String noData;

	/**
	 * Param konstruktor.
	 */
	public ReadDataStatusLabel(String status, String dataRead, String noData) {
		
		if(status == null || dataRead == null || noData == null) {
			throw new IllegalArgumentException("Statusne poruke ne mogu biti null.");
		}
		
		this.status = status;
		this.dataRead = dataRead;
		this.noData = noData;

		setNumberOfRows(null);
	}
	
	/**
	 * Postavlja tekst labele na osnovu broja prochitanih redova.
	 * @param rows - broj prochitanih redova.
	 */
	public void setNumberOfRows(Integer rows) {
		if (rows == null) {
			setFont(getFont().deriveFont(Font.PLAIN));
			setForeground(Color.black);
			setText(status + ":");
		}else if (rows > 0) {
			setFont(getFont().deriveFont(Font.PLAIN));
			setForeground(Color.black);
			setText(status + ": " + dataRead + " " + rows + ".");
		} else {
			setFont(getFont().deriveFont(Font.BOLD));
			setForeground(new Color(150, 0, 0));
			setText(status + ": " + noData + ".");
		}
	}
}
