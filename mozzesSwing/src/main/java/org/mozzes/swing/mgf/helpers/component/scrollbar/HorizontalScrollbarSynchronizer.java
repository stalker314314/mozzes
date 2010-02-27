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
package org.mozzes.swing.mgf.helpers.component.scrollbar;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * Klasa koja sinhronizuje pomeranje vertikalnog scrollbar-a, kod zadatih JScrollPane-ova, ovim se zapravo sinhronizuje
 * sadrzaj koji se prikazuje u tim JScrollPane-ovima, npr. redovi u tabeli. Kako se pomera vertikalni scrollbar jednog
 * JScrollPane-a tako se automatski pomera i vertikalni scrollbar ostalih JScrollPane-ova, tj. njihov sadrzaj.
 * 
 * @author adnan
 * 
 */
public class HorizontalScrollbarSynchronizer implements AdjustmentListener {

	private final JScrollBar scrollBar1;
	private final JScrollBar scrollBar2;

	private HorizontalScrollbarSynchronizer(JScrollPane scrollPane1, JScrollPane scrollPane2) {
		scrollBar1 = scrollPane1.getHorizontalScrollBar();
		scrollBar2 = scrollPane2.getHorizontalScrollBar();
		//
		scrollBar1.addAdjustmentListener(this);
		scrollBar2.addAdjustmentListener(this);
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		if (e.getSource() == scrollBar1) {
			scrollBar2.setValue(scrollBar1.getValue());
		} else {
			scrollBar1.setValue(scrollBar2.getValue());
		}
	}

	/**
	 * Za zadate JScrollPane-ove, instalira sinhronizaciju pomeranja vertikalnog scrollbar-a, cime se zapravo
	 * sinhronizuje sadrzaj koji se prikazuje u tim JScrollPane-ovima, npr. redovi u tabeli. Kako se pomera vertikalni
	 * scrollbar jednog JScrollPane-a tako se automatski pomera i vertikalni scrollbar ostalih JScrollPane-ova, tj.
	 * njihov sadrzaj. Sinhronizacija ce postojati sve dok postoje zadati JScrollPane-ovi, prakticno dok postoji forma.
	 * 
	 * @param jScrollPaneList - lista JScrollPane-ova kod kojih treba sinhronizovati vertikalno pomeranje sadrzaja
	 */
	public static void install(JScrollPane... jScrollPaneList) {
		for (int i = 0; i < jScrollPaneList.length; i++) {
			for (int j = i + 1; j < jScrollPaneList.length; j++) {
				new HorizontalScrollbarSynchronizer(jScrollPaneList[i], jScrollPaneList[j]);
			}
		}
	}

}
