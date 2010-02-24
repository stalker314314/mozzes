package org.mozzes.swing.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class FileChooser extends JFileChooser {

	private static final long serialVersionUID = 1L;

	/**
	 * Podrazumevani kljuc pod kojim se u java preferences, cuva putanja do direktorijuma u kome se poslednji put snimao
	 * .xls fajl.
	 */
	private static final String DEFAULT_PREVIOUS_SAVE_DIRECTORY_KEY = "defaultPreviousSaveDirectoryKey";

	/**
	 * Kljuc pod kojim se u java preferences, cuva putanja do direktorijuma u kome se poslednji put snimao .xls fajl.
	 */
	private String previousSaveDirectoryKey;

	/**
	 * Putanja do direktorijuma u kome se poslednji put snimao .xls fajl.
	 */
	private File previousSaveDirectory;

	public static final String FILE_NAME = "<file-name/>";

	private String message;
	private String title;
	private String yesOption;
	private String noOption;

	private String[] fileExtensions;

	public FileChooser() {
		this(DEFAULT_PREVIOUS_SAVE_DIRECTORY_KEY);
	}

	public FileChooser(String previousSaveDirectoryKey) {
		super();
		//
		this.previousSaveDirectoryKey = previousSaveDirectoryKey;
		previousSaveDirectory = new File(Preferences.userRoot().get(previousSaveDirectoryKey,
				System.getProperty("user.home")));
		setCurrentDirectory(previousSaveDirectory);
		//
		message = "This folder already contanis a file named:\n" + FILE_NAME + "\nDo you want replace it ?";
		title = "Confirm File Replace";
		yesOption = "Yes";
		noOption = "No";
	}

	@Override
	public void approveSelection() {
		if (isSaveDialog() && isSelectedFileExists() && notOverwrite()) {
			return;
		}
		setAndSavePreviousDirectory();
		super.approveSelection();
	}

	private boolean isSaveDialog() {
		return getDialogType() == JFileChooser.SAVE_DIALOG;
	}

	private boolean isSelectedFileExists() {
		return getSelectedFile().exists();
	}

	@Override
	public File getSelectedFile() {
		File selectedFile = super.getSelectedFile();
		if (selectedFile != null) {
			final String pathToFile = selectedFile.getAbsolutePath();
			if (noExtension(pathToFile)) {
				selectedFile = new File(pathToFile + "." + fileExtensions[0]);
				setSelectedFile(selectedFile);
			}
			return selectedFile;
		}
		return null;
	}

	private boolean noExtension(String pathToFile) {
		if (fileExtensions == null) {
			return false;
		}
		for (int i = 0; i < fileExtensions.length; i++) {
			if (pathToFile.endsWith(fileExtensions[i])) {
				return false;
			}
		}
		return true;
	}

	private boolean notOverwrite() {
		final String[] options = new String[] { yesOption, noOption };
		return JOptionPane.showOptionDialog(getTopLevelAncestor(), getMessageWithFileName(), title,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]) == 1;
	}

	private String getMessageWithFileName() {
		final int firstPartIndex = message.indexOf(FILE_NAME);
		final int secondPartndex = firstPartIndex + FILE_NAME.length();
		return message.substring(0, firstPartIndex) + getSelectedFile().getAbsolutePath()
				+ message.substring(secondPartndex);
	}

	/**
	 * Postavlja i snima direktorijum u kome se poslednji put snimao .xls fajl.
	 * 
	 */
	private void setAndSavePreviousDirectory() {
		File selectedFile = super.getSelectedFile();
		if (selectedFile != null) {
			this.previousSaveDirectory = selectedFile.getParentFile();
			Preferences.userRoot().put(previousSaveDirectoryKey, previousSaveDirectory.getPath());
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYesOption() {
		return yesOption;
	}

	public void setYesOption(String yesOption) {
		this.yesOption = yesOption;
	}

	public String getNoOption() {
		return noOption;
	}

	public void setNoOption(String noOption) {
		this.noOption = noOption;
	}

	public String[] getFileExtensions() {
		return fileExtensions;
	}

	public void setFileExtensions(String... fileExtensions) {
		this.fileExtensions = fileExtensions;
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		//
		JFrame jFrame = new JFrame();
		//
		JButton jButton = new JButton("Snimi");
		jButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Zamena fajla");
				fileChooser.setMessage("Vec postoji fajl sa imenom koje ste zadali:\n" + FILE_NAME
						+ "\nDa li zelite da ga \"pregazite\".");
				fileChooser.setYesOption("Da");
				fileChooser.setNoOption("Ne");
				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					if (file != null) {
						System.out.println(file.getAbsolutePath());
					}
				}
			}
		});
		//
		jFrame.getContentPane().add(jButton);
		//
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.pack();
		jFrame.setVisible(true);
	}
}
