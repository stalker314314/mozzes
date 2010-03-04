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
package org.mozzes.swing.component.convinient;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author nikolad
 *
 */
public class SingleSelectionListPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7457183949150370837L;
	private JLabel titleLabel;
	private String title;
	private JList list;
	
	private Object previouslySelected;
	private Object currSelected;
	
	/**
	 * lista akcija koje treba da se izvrsavaju u okviru ListSelectionListener-a
	 */
	private ArrayList<ListSelectionAction> actions = new ArrayList<ListSelectionAction>();
	
	/**
	 *<p>
	 * @param title - tekst labele koja stoji iznad liste[proslediti prevod a ne kljuc]
	 * @param elements Lista elemenata
	 * sugerise koja je vrednost izabrana [proslediti prevod a ne kljuc] </p>
	 * nije hendlovano prosledjivanje null vrednosti!
	 */
	public SingleSelectionListPanel(String title, List<Object> elements){
		super();
		this.title = title;
		init();
		addElementz(elements);
		
	}
	/**
	 *<p>
	 * @param title - tekst labele koja stoji iznad liste[proslediti prevod a ne kljuc]
	 *  
	 * koja je vrednost izabrana [proslediti prevod a ne kljuc] </p>
	 * nije hendlovano prosledjivanje null vrednosti!
	 */
	public SingleSelectionListPanel(String title){
		super();
		this.title = title;
		init();
	}
	
	
	/**
	 * bagerisanje
	 */
	private void init(){
		createComponents();
		doLayoutStuff();
	}

	/**
	 * kreiranje komponenti
	 */
	private void createComponents() {
		titleLabel = new JLabel(title); 
		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting())
				 return;
				previouslySelected = currSelected;			 
				currSelected = list.getSelectedValue();
				executeActions();
			}
			
		});
	}
	
	/**
	 * zmajski mig layout
	 */
	private void doLayoutStuff(){
		setLayout(new MigLayout("fill"));
		add(titleLabel, "wrap 3");
		JScrollPane scrollPane = new JScrollPane(list); 
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
		add(scrollPane, "width 100:100:, height 150:150:, grow, wrap 3");
	}
	
	
	/**
	 * kreiramo model i dodeljujemo ga listi
	 */
	private void addElementz(List<?> elements){
		if(elements == null)
			return;
		DefaultListModel model = new DefaultListModel();
		for (Object item : elements) {
			model.addElement(item);
		}
		list.setModel(model);
	}
	
	
	/**
	 * @param elements Elementi koji ce biti prikazani u listi
	 */
	public void setElemenents(List<?> elements){
		addElementz(elements);
	}
	
	/** 
	 * @return Izabrane vrednost iz liste
	 */
	public Object getSelectedValue(){
		return list.getSelectedValue();
	}
	
	
	/**
	 * Lista je dostupna svima
	 * @return Lista
	 */
	public JList getList(){
		return list;
	}
	
	/**
	 * Postavlja selekciju u listi na prethodno selectovanu stavku
	 */
	public void setPreviouslySelected() {
		if (hasPreviouslySelected())
			this.getList().setSelectedValue(previouslySelected, true);
		else 
			this.getList().clearSelection();
		
	}
	
	/**
	 * @return <code>true</code> ako postoji stavka koja je prethodno selektovana
	 */
	public boolean hasPreviouslySelected(){
		return !(this.previouslySelected == null);
	}
		
	/**
	 * dodaje akciju koja ce se izvrsavati na promenu selektovane stavke
	 * @param action Akcija za dodavanje
	 */
	public void addActionOnValueChanged(ListSelectionAction action) {
		this.actions.add(action);
	}
	
	/**
	 * redom izvrsava dodate akcije iz liste dodatih
	 */
	private void  executeActions(){		
		for (ListSelectionAction action: actions)
		if (action != null)
			action.valueChanged();
	}
	
}
