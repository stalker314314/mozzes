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
public class VerticalScrollbarSynchronizer implements AdjustmentListener {

	private final JScrollBar scrollBar1;
	private final JScrollBar scrollBar2;

	private VerticalScrollbarSynchronizer(JScrollPane scrollPane1, JScrollPane scrollPane2) {
		scrollBar1 = scrollPane1.getVerticalScrollBar();
		scrollBar2 = scrollPane2.getVerticalScrollBar();
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
				new VerticalScrollbarSynchronizer(jScrollPaneList[i], jScrollPaneList[j]);
			}
		}
	}

}
