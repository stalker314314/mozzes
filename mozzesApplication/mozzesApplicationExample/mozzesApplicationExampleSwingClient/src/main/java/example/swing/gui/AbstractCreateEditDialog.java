package example.swing.gui;

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

import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.impl.BeanCopyDataSource;
import org.mozzes.validation.ValidationUtils;


import example.common.service.Administration;
import example.swing.ExampleSwingApplication;

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
