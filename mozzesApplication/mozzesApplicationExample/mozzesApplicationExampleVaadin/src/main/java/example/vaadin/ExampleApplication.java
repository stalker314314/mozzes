package example.vaadin;

import com.google.inject.Inject;
import com.vaadin.Application;

import example.vaadin.gui.MainWindow;

public class ExampleApplication extends Application {

	private static final long serialVersionUID = -8899157770148945072L;
	
	@Inject
	private MainWindow mainWindow;

	@Override
	public void init() {
		setMainWindow(mainWindow);
	}

}
