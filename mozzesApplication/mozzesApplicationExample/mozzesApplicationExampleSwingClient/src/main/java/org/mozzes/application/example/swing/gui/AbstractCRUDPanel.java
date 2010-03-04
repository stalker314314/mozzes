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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.mozzes.application.example.common.service.Administration;
import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.impl.DefaultDataModel;
import org.mozzes.swing.mgf.datarenderer.table.TableBuilder;
import org.mozzes.swing.mgf.datasource.SelectionListDataSource;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionChangedAdapter;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionChangedEvent;
import org.mozzes.swing.mgf.datasource.impl.DefaultSelectionListDataSource;
import org.mozzes.swing.mgf.datasource.impl.SelectionMode;



public abstract class AbstractCRUDPanel<T> extends JPanel {
	private static final long serialVersionUID = 1L;

	private final Administration<T> service;

	private final SelectionListDataSource<T> source;
	private final DataModel<T> model = new DefaultDataModel<T>();
	private final TableBuilder<T> table;

	private JButton btnReload = new JButton("Reload");
	private JButton btnNew = new JButton("New");
	private JButton btnEdit = new JButton("Edit");
	private JButton btnDelete = new JButton("Delete");

	private final AbstractCreateEditDialog<T> editDialog;

	public AbstractCRUDPanel(Administration<T> service, AbstractCreateEditDialog<T> editDialog) {
		this.service = service;
		this.editDialog = editDialog;
		source = new DefaultSelectionListDataSource<T>(editDialog.getEditClass());
		source.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);

		initModel(model);
		table = new TableBuilder<T>(source, model);
		
		setButtonsState();
		initLayout();
		loadData();

		setupHandlers();
	}

	protected abstract void initModel(DataModel<T> model);
	
	private void setupHandlers() {
		source.addEventListener(new SelectionChangedAdapter<T>() {
			@Override
			public void selectionChanged(SelectionListDataSource<T> source, SelectionChangedEvent<T> event) {
				setButtonsState();
			}
		});
		btnReload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reload();
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteSelectedItems();
			}
		});
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editSelectedItem();
			}
		});
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addNewItem();
			}
		});
	}

	private void setButtonsState() {
		if (source.getSelectedValues().isEmpty()) {
			btnDelete.setEnabled(false);
			btnEdit.setEnabled(false);
		} else {
			int count = source.getSelectedValues().size();
			btnDelete.setEnabled(true);
			btnEdit.setEnabled(count == 1);
		}
	}

	private void initLayout() {
		setLayout(new BorderLayout());
		add(table.getRenderComponent(), BorderLayout.CENTER);

		JPanel pnlCrudButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnlCrudButtons.add(btnNew);
		pnlCrudButtons.add(btnEdit);
		pnlCrudButtons.add(btnDelete);
		
		JPanel pnlButtons = new JPanel(new BorderLayout());
		pnlButtons.add(btnReload, BorderLayout.WEST);
		pnlButtons.add(pnlCrudButtons, BorderLayout.CENTER);
		
		add(pnlButtons, BorderLayout.SOUTH);
	}

	private void loadData() {
		source.setData(service.findAll());
	}
	
	private void reload() {
		loadData();
	}

	private void addNewItem() {
		editDialog.newObject();
		editDialog.setVisible(true);
		T t = editDialog.getObject();
		if (t != null)
			source.add(t);
	}

	private void editSelectedItem() {
		editDialog.editObject(source.getSelectedValue());
		editDialog.setVisible(true);
		T t = editDialog.getObject();
		if (t != null)
			source.set(source.getSelectedIndex(), t);
	}

	private void deleteSelectedItems() {
		List<T> selected = source.getSelectedValues();
		if (selected == null || selected.isEmpty())
			return;
		int result = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete objects?",
				"Delete confirmation", JOptionPane.YES_NO_OPTION);
		if (result != JOptionPane.YES_OPTION)
			return;
		try {
			for (T t : selected) {
				service.delete(t);
				source.remove(t);
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
