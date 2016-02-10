package land.sebastianwie.shopware_uploadtool.uiNew.tabs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import land.sebastianwie.shopware_uploadtool.uiNew.Controller;
import land.sebastianwie.shopware_uploadtool.uiNew.Controller.Filetype;
import land.sebastianwie.shopware_uploadtool.uiNew.components.StatusPanel;

public class UploadTab extends Tab {

	private static final long serialVersionUID = 6867018521955343878L;

	private Controller c;

	private JButton importFileBtn, errorFileBtn;
	private JLabel info, importFileLabel, errorFileLabel;
	private JTextField importFileTxt, errorFileTxt;
	private JButton startBtn, updateBtn, abortBtn;
	private StatusPanel status;

	@Override
	public boolean isCloseable() {
		return true;
	}

	@Override
	public String getTitle() {
		return "Uploads";
	}

	@Override
	public void lock() {
		startBtn.setEnabled(false);
		updateBtn.setEnabled(false);
		abortBtn.setEnabled(true);
	}

	@Override
	public void unlock() {
		startBtn.setEnabled(true);
		updateBtn.setEnabled(true);
		abortBtn.setEnabled(false);
	}

	@Override
	public JButton getCancelButton() {
		return abortBtn;
	}

	public UploadTab(Controller c) {
		this.c = c;

		SpringLayout layout = new SpringLayout();
		super.setLayout(layout);

		info = new JLabel(
				"<html>In diesem Fenster können neue Artikel zu Shopware hochgeladen werden und bereits bestehende Artikel in Shopware aktualisiert werden.</html>");

		importFileBtn = new JButton("Import-Datei auswählen");
		errorFileBtn = new JButton("Fehlerdatei auswählen");

		importFileLabel = new JLabel("Wählen Sie die Datei mit den Informationen zum Hochladen aus.");
		errorFileLabel = new JLabel("Wählen Sie einen Speicherort, in dem fehlgeschlagene Uploads gespeichert werden.");

		importFileTxt = new JTextField();
		errorFileTxt = new JTextField();

		importFileTxt.setEditable(false);
		errorFileTxt.setEditable(false);

		startBtn = new JButton("Erstimport starten");
		startBtn.setToolTipText("Erstimport neuer Artikel. Artikel, die bereits in Shopware existieren, werden nicht überschrieben und in der Fehler-Datei vermerkt");
		updateBtn = new JButton("Artikelupdate starten");
		updateBtn
				.setToolTipText("Update bereits bestehender Artikel. Artikel, die noch nicht existieren, werden nicht angelegt und in der Fehler-Datei vermerkt.");
		abortBtn = new JButton("Upload abbrechen");
		abortBtn.setEnabled(false);

		status = new StatusPanel();

		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				upload();
			}
		});

		updateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});

		importFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectImportFile();
			}
		});

		errorFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectErrorFile();
			}
		});

		layout.putConstraint(SpringLayout.WEST, info, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, info, 10, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, info, -10, SpringLayout.EAST, this);
		super.add(info);

		layout.putConstraint(SpringLayout.WEST, importFileLabel, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, importFileLabel, 20, SpringLayout.SOUTH, info);
		layout.putConstraint(SpringLayout.EAST, importFileLabel, 0, SpringLayout.EAST, info);
		super.add(importFileLabel);

		layout.putConstraint(SpringLayout.WEST, importFileBtn, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, importFileBtn, 5, SpringLayout.SOUTH, importFileLabel);
		layout.putConstraint(SpringLayout.EAST, importFileBtn, 250, SpringLayout.WEST, info);
		super.add(importFileBtn);

		layout.putConstraint(SpringLayout.WEST, importFileTxt, 10, SpringLayout.EAST, importFileBtn);
		layout.putConstraint(SpringLayout.BASELINE, importFileTxt, 0, SpringLayout.BASELINE, importFileBtn);
		layout.putConstraint(SpringLayout.EAST, importFileTxt, 0, SpringLayout.EAST, info);
		super.add(importFileTxt);

		layout.putConstraint(SpringLayout.WEST, errorFileLabel, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, errorFileLabel, 10, SpringLayout.SOUTH, importFileBtn);
		layout.putConstraint(SpringLayout.EAST, errorFileLabel, 0, SpringLayout.EAST, info);
		super.add(errorFileLabel);

		layout.putConstraint(SpringLayout.WEST, errorFileBtn, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, errorFileBtn, 5, SpringLayout.SOUTH, errorFileLabel);
		layout.putConstraint(SpringLayout.EAST, errorFileBtn, 0, SpringLayout.EAST, importFileBtn);
		super.add(errorFileBtn);

		layout.putConstraint(SpringLayout.WEST, errorFileTxt, 10, SpringLayout.EAST, errorFileBtn);
		layout.putConstraint(SpringLayout.BASELINE, errorFileTxt, 0, SpringLayout.BASELINE, errorFileBtn);
		layout.putConstraint(SpringLayout.EAST, errorFileTxt, 0, SpringLayout.EAST, info);
		super.add(errorFileTxt);

		layout.putConstraint(SpringLayout.WEST, startBtn, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.EAST, startBtn, 295, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, startBtn, 25, SpringLayout.SOUTH, errorFileBtn);
		super.add(startBtn);

		layout.putConstraint(SpringLayout.EAST, updateBtn, 0, SpringLayout.EAST, info);
		layout.putConstraint(SpringLayout.WEST, updateBtn, 5, SpringLayout.EAST, startBtn);
		layout.putConstraint(SpringLayout.NORTH, updateBtn, 0, SpringLayout.NORTH, startBtn);
		super.add(updateBtn);

		layout.putConstraint(SpringLayout.NORTH, abortBtn, 5, SpringLayout.SOUTH, startBtn);
		layout.putConstraint(SpringLayout.WEST, abortBtn, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.EAST, abortBtn, 0, SpringLayout.EAST, info);
		super.add(abortBtn);

		layout.putConstraint(SpringLayout.WEST, status, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, status, 5, SpringLayout.SOUTH, abortBtn);
		layout.putConstraint(SpringLayout.EAST, status, 0, SpringLayout.EAST, info);
		layout.putConstraint(SpringLayout.SOUTH, status, -10, SpringLayout.SOUTH, this);
		super.add(status);
	}

	private void selectImportFile() {
		File f = c.selectFile("import.xlsx", false, new Filetype[] { Filetype.XLS_ALL, Filetype.XLSX, Filetype.XLSX_MS, Filetype.XLS });
		if (f != null)
			importFileTxt.setText(f.getAbsolutePath());
	}

	private void selectErrorFile() {
		File f = c.selectFile("errors.xlsx", true, new Filetype[] { Filetype.XLSX });
		if (f != null)
			errorFileTxt.setText(f.getAbsolutePath());
	}

	private void upload() {
		UploadInformation info = new UploadInformation();
		info.importFile = new File(importFileTxt.getText());
		info.errorFile = new File(errorFileTxt.getText());
		info.statusPanel = this.status;
		c.upload(this, info);
	}

	private void update() {
		UploadInformation info = new UploadInformation();
		info.importFile = new File(importFileTxt.getText());
		info.errorFile = new File(errorFileTxt.getText());
		info.statusPanel = this.status;
		c.update(this, info);
	}

	public class UploadInformation {
		public File importFile;
		public File errorFile;
		public StatusPanel statusPanel;
	}

}
