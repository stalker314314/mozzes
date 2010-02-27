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
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.mozzes.application.example.common.service.Administration;
import org.mozzes.application.example.swing.ExampleSwingApplication;
import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.impl.BeanCopyDataSource;
import org.mozzes.validation.ValidationUtils;



public abstract class AbstractCreateEditDialog<T> extends JDialog {
	private static final long serialVersionUID = 1L;

	private final Administration<T> service;
	private final T emptyInstance;

	private boolean cancel = true;
	private final BeanDataSource<T> source;

	private JButton btnSave = new JButton("Save");
	private JButton btnCancel = new JButton("Cancel");

	public AbstractCreateEditDialog(Administration<T> service, T emptyInstance) {
		super(getFrame());
		this.emptyInstance = emptyInstance;
		this.service = service;
		source = new BeanCopyDataSource<T>(getEditClass());
		setModal(true);
	}

	protected void initialize() {
		bindFields();
		setupHandlers();
		initLayout();
		loadData();
		activate();
	}

	protected void activate() {
	}

	protected abstract void bindFields();

	protected abstract JPanel getMainPanel();

	protected BeanDataSource<T> getSource() {
		return source;
	}

	private void setupHandlers() {
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				cancel = true;
			}
		});
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				activate();
			}
		});
	}

	private static Frame getFrame() {
		return ExampleSwingApplication.getApplicationMainFrame();
	}

	private void initLayout() {
		setLayout(new BorderLayout());

		JPanel central = getMainPanel();
		add(central, BorderLayout.CENTER);

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttons.add(btnSave);
		buttons.add(btnCancel);

		add(buttons, BorderLayout.SOUTH);
	}

	private void loadData() {
		if (getSource().getData() == null)
			getSource().setData(emptyInstance);
	}

	private void cancel() {
		cancel = true;
		setVisible(false);
	}

	private void save() {
		if (!ValidationUtils.isValid(getSource().getData())) {
			JOptionPane.showMessageDialog(this, "Object not valid!");
			return;
		}

		T saved = service.save(getSource().getData());
		getSource().setData(saved);
		cancel = false;
		setVisible(false);
	}

	public void newObject() {
		editObject(emptyInstance);
	}

	public void editObject(T employee) {
		cancel = true;
		if (employee == null)
			throw new IllegalArgumentException("Employee cannot be null!");
		getSource().setData(employee);
	}

	public T getObject() {
		if (cancel)
			return null;
		return getSource().getData();
	}

	@SuppressWarnings("unchecked")
	public Class<T> getEditClass() {
		return (Class<T>) emptyInstance.getClass();
	}
}
