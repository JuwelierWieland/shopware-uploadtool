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

public class DownloadTab extends Tab {
	private static final long serialVersionUID = -6687402840237090989L;

	private Controller c;

	private JButton sourceFileBtn, outputFileBtn;
	private JLabel info, sourceFileLabel, outputFileLabel;
	private JTextField sourceFileTxt, outputFileTxt;
	private JButton downloadBtn, downloadAllBtn, abortBtn;
	private StatusPanel status;

	@Override
	public boolean isCloseable() {
		return true;
	}

	@Override
	public String getTitle() {
		return "Downloads";
	}

	@Override
	public void lock() {
		downloadBtn.setEnabled(false);
		downloadAllBtn.setEnabled(false);
		abortBtn.setEnabled(true);
	}

	@Override
	public void unlock() {
		downloadBtn.setEnabled(true);
		downloadAllBtn.setEnabled(true);
		abortBtn.setEnabled(false);
	}

	@Override
	public JButton getCancelButton() {
		return abortBtn;
	}

	public DownloadTab(Controller c) {
		this.c = c;

		SpringLayout layout = new SpringLayout();
		super.setLayout(layout);

		info = new JLabel(
				"<html>In diesem Fenster können die Informationen der Artikel, die sich auf Shopware befinden, heruntergeladen werden.</html>");

		sourceFileBtn = new JButton("Quelldatei öffnen");
		outputFileBtn = new JButton("Ausgabe-Datei auswählen");

		sourceFileLabel = new JLabel("Eine Datei mit bestimmten Artikeln, deren Informationen Sie herunterladen möchten.");
		outputFileLabel = new JLabel("Speicherort, wo die heruntergeladenen Artikel gespeichert werden sollen.");

		sourceFileTxt = new JTextField();
		outputFileTxt = new JTextField();
		sourceFileTxt.setEditable(false);
		outputFileTxt.setEditable(false);

		downloadBtn = new JButton("Einzelne Artikel herunterladen");
		downloadBtn
				.setToolTipText("Alle Artikel, die in der Quelldatei angegeben sind, werden heruntergeladen. Falls ein Artikel auf Shopware nicht existiert, wird das in der Fehler-Datei vermerkt.");
		downloadAllBtn = new JButton("Alle Artikel herunterladen");
		downloadAllBtn.setToolTipText("Alle Artikel, die sich auf Shopware befinden, herunterladen. Die Quelldatei wird ignoriert.");
		abortBtn = new JButton("Download abbrechen");
		abortBtn.setEnabled(false);

		status = new StatusPanel();

		downloadBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// upload();
				download();
			}
		});

		downloadAllBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// update();
				downloadAll();
			}
		});

		sourceFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectSourceFile();
			}
		});

		outputFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectOutputFile();
			}
		});

		layout.putConstraint(SpringLayout.WEST, info, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, info, 10, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, info, -10, SpringLayout.EAST, this);
		super.add(info);

		layout.putConstraint(SpringLayout.WEST, sourceFileLabel, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, sourceFileLabel, 20, SpringLayout.SOUTH, info);
		layout.putConstraint(SpringLayout.EAST, sourceFileLabel, 0, SpringLayout.EAST, info);
		super.add(sourceFileLabel);

		layout.putConstraint(SpringLayout.WEST, sourceFileBtn, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, sourceFileBtn, 5, SpringLayout.SOUTH, sourceFileLabel);
		layout.putConstraint(SpringLayout.EAST, sourceFileBtn, 250, SpringLayout.WEST, info);
		super.add(sourceFileBtn);

		layout.putConstraint(SpringLayout.WEST, sourceFileTxt, 10, SpringLayout.EAST, sourceFileBtn);
		layout.putConstraint(SpringLayout.BASELINE, sourceFileTxt, 0, SpringLayout.BASELINE, sourceFileBtn);
		layout.putConstraint(SpringLayout.EAST, sourceFileTxt, 0, SpringLayout.EAST, info);
		super.add(sourceFileTxt);

		layout.putConstraint(SpringLayout.WEST, outputFileLabel, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, outputFileLabel, 10, SpringLayout.SOUTH, sourceFileBtn);
		layout.putConstraint(SpringLayout.EAST, outputFileLabel, 0, SpringLayout.EAST, info);
		super.add(outputFileLabel);

		layout.putConstraint(SpringLayout.WEST, outputFileBtn, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, outputFileBtn, 5, SpringLayout.SOUTH, outputFileLabel);
		layout.putConstraint(SpringLayout.EAST, outputFileBtn, 0, SpringLayout.EAST, sourceFileBtn);
		super.add(outputFileBtn);

		layout.putConstraint(SpringLayout.WEST, outputFileTxt, 10, SpringLayout.EAST, outputFileBtn);
		layout.putConstraint(SpringLayout.BASELINE, outputFileTxt, 0, SpringLayout.BASELINE, outputFileBtn);
		layout.putConstraint(SpringLayout.EAST, outputFileTxt, 0, SpringLayout.EAST, info);
		super.add(outputFileTxt);

		layout.putConstraint(SpringLayout.WEST, downloadBtn, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.EAST, downloadBtn, 295, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, downloadBtn, 25, SpringLayout.SOUTH, outputFileBtn);
		super.add(downloadBtn);

		layout.putConstraint(SpringLayout.EAST, downloadAllBtn, 0, SpringLayout.EAST, info);
		layout.putConstraint(SpringLayout.WEST, downloadAllBtn, 5, SpringLayout.EAST, downloadBtn);
		layout.putConstraint(SpringLayout.NORTH, downloadAllBtn, 0, SpringLayout.NORTH, downloadBtn);
		super.add(downloadAllBtn);

		layout.putConstraint(SpringLayout.NORTH, abortBtn, 5, SpringLayout.SOUTH, downloadBtn);
		layout.putConstraint(SpringLayout.WEST, abortBtn, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.EAST, abortBtn, 0, SpringLayout.EAST, info);
		super.add(abortBtn);

		layout.putConstraint(SpringLayout.WEST, status, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, status, 5, SpringLayout.SOUTH, abortBtn);
		layout.putConstraint(SpringLayout.EAST, status, 0, SpringLayout.EAST, info);
		layout.putConstraint(SpringLayout.SOUTH, status, -10, SpringLayout.SOUTH, this);
		super.add(status);
	}

	private void selectSourceFile() {
		File f = c.selectFile("source.xlsx", false, new Filetype[] { Filetype.XLS_ALL, Filetype.XLSX, Filetype.XLSX_MS, Filetype.XLS });
		if (f != null)
			sourceFileTxt.setText(f.getAbsolutePath());
	}

	private void selectOutputFile() {
		File f = c.selectFile("output.xlsx", true, new Filetype[] { Filetype.XLSX });
		if (f != null)
			outputFileTxt.setText(f.getAbsolutePath());
	}

	private void download() {
		DownloadInformation info = new DownloadInformation();
		info.sourceFile = new File(sourceFileTxt.getText());
		info.destFile = new File(outputFileTxt.getText());
		info.statusPanel = status;
		c.download(this, info);
	}

	private void downloadAll() {
		DownloadInformation info = new DownloadInformation();
		info.sourceFile = new File(sourceFileTxt.getText());
		info.destFile = new File(outputFileTxt.getText());
		info.statusPanel = status;
		c.downloadAll(this, info);
	}

	public class DownloadInformation {
		public File sourceFile;
		public File destFile;
		public StatusPanel statusPanel;
	}

}
