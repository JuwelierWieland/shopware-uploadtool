package land.sebastianwie.shopware_uploadtool.ui;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import land.sebastianwie.shopware_uploadtool.excel.article.ExcelCreator;
import land.sebastianwie.shopware_uploadtool.transfer.ArticleProcessor.ProcessMode;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = -80880855269228038L;
	private JTextField importFile;
	private JTextField errorFile;
	private JTextField imageFile;
	private JButton btnImport;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JButton btnDownload;
	private JButton btnDownloadAll;
	private JButton btnCancel;
	private JPanel statusPanel;

	@SuppressWarnings("unused")
	private MainApplication parent;
	private MainPanel ref = this;

	private final JFileChooser fc = new JFileChooser();
	private final JFileChooser imagefc = new JFileChooser();
	private JLabel statusLabel;
	private JTextField txtStatus;
	private JLabel lblFortschritt;
	private JTextField progressField;
	private JProgressBar progressBar;
	private JLabel lblGesamtArtikel;
	private JTextField artikelGesamt;
	private JLabel lblArtikelFertig;
	private JTextField artikelFertig;
	private JTextField verstricheneZeit;
	private JLabel lblVerbleibendeZeit;
	private JTextField verbleibendeZeit;

	/**
	 * Create the panel.
	 */
	public MainPanel(MainApplication parent) {
		this.parent = parent;
		fc.setFileFilter(new FileNameExtensionFilter("Office OpenXML Worksheet", "xlsx"));
		fc.setAcceptAllFileFilterUsed(false);
		imagefc.setFileFilter(new FileNameExtensionFilter("Zip-Archiv", "zip"));
		fc.setAcceptAllFileFilterUsed(false);

		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);

		JButton btnImportFile = new JButton("Import-Datei auswählen");
		springLayout.putConstraint(SpringLayout.NORTH, btnImportFile, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, btnImportFile, 10, SpringLayout.WEST, this);
		//springLayout.putConstraint(SpringLayout.SOUTH, btnImportFile, 35, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, btnImportFile, -370, SpringLayout.EAST, this);
		btnImportFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fc.setSelectedFile(new File(""));
				int status = fc.showOpenDialog(parent.frame);
				if (status == JFileChooser.APPROVE_OPTION) {
					importFile.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		add(btnImportFile);

		importFile = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, importFile, 6, SpringLayout.EAST, btnImportFile);
		springLayout.putConstraint(SpringLayout.EAST, importFile, -10, SpringLayout.EAST, this);
		add(importFile);
		importFile.setColumns(10);

		JButton btnErrorFile = new JButton("Ausgabe-Datei auswählen");
		springLayout.putConstraint(SpringLayout.NORTH, btnErrorFile, 6, SpringLayout.SOUTH, btnImportFile);
		springLayout.putConstraint(SpringLayout.WEST, btnErrorFile, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, btnErrorFile, -370, SpringLayout.EAST, this);
		btnErrorFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fc.setSelectedFile(new File(""));
				int status = fc.showSaveDialog(parent.frame);
				if (status == JFileChooser.APPROVE_OPTION) {
					errorFile.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		add(btnErrorFile);

		errorFile = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, errorFile, 6, SpringLayout.EAST, btnErrorFile);
		springLayout.putConstraint(SpringLayout.EAST, errorFile, -10, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, importFile, -12, SpringLayout.NORTH, errorFile);
		add(errorFile);
		errorFile.setColumns(10);

		JButton btnImages = new JButton("Bilder-Datei auswählen");
		springLayout.putConstraint(SpringLayout.WEST, btnImages, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, btnErrorFile, -6, SpringLayout.NORTH, btnImages);
		springLayout.putConstraint(SpringLayout.NORTH, btnImages, 72, SpringLayout.NORTH, this);
		btnImages.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int status = imagefc.showOpenDialog(parent.frame);
				if (status == JFileChooser.APPROVE_OPTION) {
					imageFile.setText(imagefc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		btnImages.setEnabled(false);
		add(btnImages);

		imageFile = new JTextField();
		springLayout.putConstraint(SpringLayout.EAST, btnImages, -6, SpringLayout.WEST, imageFile);
		springLayout.putConstraint(SpringLayout.SOUTH, errorFile, -12, SpringLayout.NORTH, imageFile);
		springLayout.putConstraint(SpringLayout.NORTH, imageFile, 75, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, imageFile, 0, SpringLayout.WEST, importFile);
		springLayout.putConstraint(SpringLayout.EAST, imageFile, -10, SpringLayout.EAST, this);
		imageFile.setEnabled(false);
		add(imageFile);
		imageFile.setColumns(10);

		btnImport = new JButton("Artikel Import starten");
		springLayout.putConstraint(SpringLayout.NORTH, btnImport, 10, SpringLayout.SOUTH, btnImages);
		springLayout.putConstraint(SpringLayout.EAST, btnImport, 317, SpringLayout.WEST, this);
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (importFile.getText().trim().length() > 0 && errorFile.getText().trim().length() > 0)
					parent.performUpload(ProcessMode.UPLOAD, importFile.getText().trim(), errorFile.getText().trim(),
							imageFile.getText().trim(), ref);
			}
		});
		springLayout.putConstraint(SpringLayout.WEST, btnImport, 10, SpringLayout.WEST, this);
		add(btnImport);

		btnUpdate = new JButton("Artikel Update starten");
		springLayout.putConstraint(SpringLayout.NORTH, btnUpdate, 0, SpringLayout.NORTH, btnImport);
		springLayout.putConstraint(SpringLayout.WEST, btnUpdate, 6, SpringLayout.EAST, btnImport);
		springLayout.putConstraint(SpringLayout.EAST, btnUpdate, -10, SpringLayout.EAST, this);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (importFile.getText().trim().length() > 0 && errorFile.getText().trim().length() > 0)
					parent.performUpload(ProcessMode.UPDATE, importFile.getText().trim(), errorFile.getText().trim(),
							imageFile.getText().trim(), ref);
			}
		});
		add(btnUpdate);

		JButton btnTemplate = new JButton("Import-Template erstellen");
		springLayout.putConstraint(SpringLayout.NORTH, btnTemplate, 5, SpringLayout.SOUTH, btnImport);
		springLayout.putConstraint(SpringLayout.EAST, btnTemplate, 317, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.WEST, btnTemplate, 10, SpringLayout.WEST, this);
		btnTemplate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fc.setSelectedFile(new File("template.xlsx"));
				int status = fc.showSaveDialog(parent.frame);
				if (status == JFileChooser.APPROVE_OPTION) {
					ExcelCreator ec = new ExcelCreator();
					ec.saveAs(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		add(btnTemplate);

		btnDelete = new JButton("Artikel löschen");
		springLayout.putConstraint(SpringLayout.NORTH, btnDelete, 5, SpringLayout.SOUTH, btnUpdate);
		springLayout.putConstraint(SpringLayout.WEST, btnDelete, 6, SpringLayout.EAST, btnTemplate);
		springLayout.putConstraint(SpringLayout.EAST, btnDelete, -10, SpringLayout.EAST, this);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (importFile.getText().trim().length() > 0 && errorFile.getText().trim().length() > 0)
					if (JOptionPane.showConfirmDialog(parent.frame,
							"Sind Sie sich sicher, dass Sie die Artikel löschen möchten?\nDie Artikel werden dabei unwiederruflich gelöscht.\nEs wird empfohlen, vorher die Artikeldetails herunterzuladen und als Backup aufzubewahren.", "Bestätigen",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
						parent.performUpload(ProcessMode.DELETE, importFile.getText().trim(), errorFile.getText()
								.trim(), imageFile.getText().trim(), ref);
			}
		});
		add(btnDelete);
		
		btnDownload = new JButton("Artikeldetails herunterladen");
		springLayout.putConstraint(SpringLayout.NORTH, btnDownload, 5, SpringLayout.SOUTH, btnTemplate);
		springLayout.putConstraint(SpringLayout.EAST, btnDownload, 317, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.WEST, btnDownload, 10, SpringLayout.WEST, this);
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (importFile.getText().trim().length() > 0 && errorFile.getText().trim().length() > 0)
					parent.performUpload(ProcessMode.DOWNLOAD, importFile.getText().trim(), errorFile.getText().trim(),
							imageFile.getText().trim(), ref);
			}
		});
		add(btnDownload);
		
		btnDownloadAll = new JButton("Alle Artikel herunterladen");
		springLayout.putConstraint(SpringLayout.NORTH, btnDownloadAll, 0, SpringLayout.NORTH, btnDownload);
		springLayout.putConstraint(SpringLayout.WEST, btnDownloadAll, 6, SpringLayout.EAST, btnDownload);
		springLayout.putConstraint(SpringLayout.EAST, btnDownloadAll, -10, SpringLayout.EAST, this);
		btnDownloadAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (errorFile.getText().trim().length() > 0)
					parent.performUpload(ProcessMode.DOWNLOAD_ALL, importFile.getText().trim(), errorFile.getText().trim(),
							imageFile.getText().trim(), ref);
			}
		});
		add(btnDownloadAll);

		statusPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, statusPanel, 10, SpringLayout.SOUTH, btnDownload);
		springLayout.putConstraint(SpringLayout.WEST, statusPanel, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, statusPanel, -10, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, statusPanel, -10, SpringLayout.EAST, this);
		statusPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		add(statusPanel);
		SpringLayout sl_statusPanel = new SpringLayout();
		statusPanel.setLayout(sl_statusPanel);

		statusLabel = new JLabel("Status:");
		sl_statusPanel.putConstraint(SpringLayout.NORTH, statusLabel, 10, SpringLayout.NORTH, statusPanel);
		sl_statusPanel.putConstraint(SpringLayout.WEST, statusLabel, 10, SpringLayout.WEST, statusPanel);
		statusPanel.add(statusLabel);

		txtStatus = new JTextField();
		sl_statusPanel.putConstraint(SpringLayout.NORTH, txtStatus, -2, SpringLayout.NORTH, statusLabel);
		sl_statusPanel.putConstraint(SpringLayout.WEST, txtStatus, 75, SpringLayout.EAST, statusLabel);
		sl_statusPanel.putConstraint(SpringLayout.EAST, txtStatus, -11, SpringLayout.EAST, statusPanel);
		txtStatus.setEditable(false);
		txtStatus.setText("noch nicht gestartet");
		statusPanel.add(txtStatus);
		txtStatus.setColumns(10);

		progressBar = new JProgressBar();
		sl_statusPanel.putConstraint(SpringLayout.NORTH, progressBar, 9, SpringLayout.SOUTH, txtStatus);
		sl_statusPanel.putConstraint(SpringLayout.WEST, progressBar, 12, SpringLayout.WEST, statusPanel);
		sl_statusPanel.putConstraint(SpringLayout.EAST, progressBar, 0, SpringLayout.EAST, txtStatus);
		statusPanel.add(progressBar);

		lblFortschritt = new JLabel("Fortschritt");
		sl_statusPanel.putConstraint(SpringLayout.NORTH, lblFortschritt, 17, SpringLayout.SOUTH, progressBar);
		sl_statusPanel.putConstraint(SpringLayout.WEST, lblFortschritt, 0, SpringLayout.WEST, statusLabel);
		statusPanel.add(lblFortschritt);

		progressField = new JTextField();
		sl_statusPanel.putConstraint(SpringLayout.NORTH, progressField, -2, SpringLayout.NORTH, lblFortschritt);
		sl_statusPanel.putConstraint(SpringLayout.WEST, progressField, 0, SpringLayout.WEST, txtStatus);
		sl_statusPanel.putConstraint(SpringLayout.EAST, progressField, 0, SpringLayout.EAST, txtStatus);
		progressField.setEditable(false);
		progressField.setText("0 %");
		statusPanel.add(progressField);
		progressField.setColumns(10);

		lblGesamtArtikel = new JLabel("Gesamt Artikel");
		sl_statusPanel.putConstraint(SpringLayout.NORTH, lblGesamtArtikel, 6, SpringLayout.SOUTH, lblFortschritt);
		sl_statusPanel.putConstraint(SpringLayout.WEST, lblGesamtArtikel, 0, SpringLayout.WEST, statusLabel);
		statusPanel.add(lblGesamtArtikel);

		artikelGesamt = new JTextField();
		sl_statusPanel.putConstraint(SpringLayout.NORTH, artikelGesamt, -2, SpringLayout.NORTH, lblGesamtArtikel);
		sl_statusPanel.putConstraint(SpringLayout.WEST, artikelGesamt, 0, SpringLayout.WEST, txtStatus);
		sl_statusPanel.putConstraint(SpringLayout.EAST, artikelGesamt, 0, SpringLayout.EAST, txtStatus);
		artikelGesamt.setEditable(false);
		artikelGesamt.setText("0");
		statusPanel.add(artikelGesamt);
		artikelGesamt.setColumns(10);

		lblArtikelFertig = new JLabel("Artikel fertig");
		sl_statusPanel.putConstraint(SpringLayout.NORTH, lblArtikelFertig, 6, SpringLayout.SOUTH, lblGesamtArtikel);
		sl_statusPanel.putConstraint(SpringLayout.WEST, lblArtikelFertig, 0, SpringLayout.WEST, statusLabel);
		statusPanel.add(lblArtikelFertig);

		artikelFertig = new JTextField();
		sl_statusPanel.putConstraint(SpringLayout.NORTH, artikelFertig, -2, SpringLayout.NORTH, lblArtikelFertig);
		sl_statusPanel.putConstraint(SpringLayout.WEST, artikelFertig, 0, SpringLayout.WEST, txtStatus);
		sl_statusPanel.putConstraint(SpringLayout.EAST, artikelFertig, 0, SpringLayout.EAST, txtStatus);
		artikelFertig.setEditable(false);
		artikelFertig.setText("0");
		statusPanel.add(artikelFertig);
		artikelFertig.setColumns(10);

		JLabel lblVerstricheneZeit = new JLabel("Verstrichene Zeit");
		sl_statusPanel.putConstraint(SpringLayout.NORTH, lblVerstricheneZeit, 6, SpringLayout.SOUTH, lblArtikelFertig);
		sl_statusPanel.putConstraint(SpringLayout.WEST, lblVerstricheneZeit, 10, SpringLayout.WEST, statusPanel);
		statusPanel.add(lblVerstricheneZeit);

		verstricheneZeit = new JTextField();
		sl_statusPanel.putConstraint(SpringLayout.NORTH, verstricheneZeit, -2, SpringLayout.NORTH, lblVerstricheneZeit);
		sl_statusPanel.putConstraint(SpringLayout.WEST, verstricheneZeit, 41, SpringLayout.WEST, txtStatus);
		sl_statusPanel.putConstraint(SpringLayout.EAST, verstricheneZeit, -11, SpringLayout.EAST, statusPanel);
		verstricheneZeit.setEditable(false);
		verstricheneZeit.setText("0:00");
		statusPanel.add(verstricheneZeit);
		verstricheneZeit.setColumns(10);

		lblVerbleibendeZeit = new JLabel("Verbleibende Zeit");
		sl_statusPanel.putConstraint(SpringLayout.NORTH, lblVerbleibendeZeit, 6, SpringLayout.SOUTH,
				lblVerstricheneZeit);
		sl_statusPanel.putConstraint(SpringLayout.WEST, lblVerbleibendeZeit, 0, SpringLayout.WEST, statusLabel);
		statusPanel.add(lblVerbleibendeZeit);

		verbleibendeZeit = new JTextField();
		sl_statusPanel.putConstraint(SpringLayout.NORTH, verbleibendeZeit, -2, SpringLayout.NORTH, lblVerbleibendeZeit);
		sl_statusPanel.putConstraint(SpringLayout.WEST, verbleibendeZeit, 0, SpringLayout.WEST, verstricheneZeit);
		sl_statusPanel.putConstraint(SpringLayout.EAST, verbleibendeZeit, 0, SpringLayout.EAST, txtStatus);
		verbleibendeZeit.setEditable(false);
		verbleibendeZeit.setText("0:00");
		statusPanel.add(verbleibendeZeit);
		verbleibendeZeit.setColumns(10);

		btnCancel = new JButton("Abbruch");
		btnCancel.setEnabled(false);
		sl_statusPanel.putConstraint(SpringLayout.WEST, btnCancel, 0, SpringLayout.WEST, statusLabel);
		sl_statusPanel.putConstraint(SpringLayout.SOUTH, btnCancel, -9, SpringLayout.SOUTH, statusPanel);
		sl_statusPanel.putConstraint(SpringLayout.EAST, btnCancel, 0, SpringLayout.EAST, txtStatus);
		statusPanel.add(btnCancel);

	}

	public void setStatus(String status) {
		txtStatus.setText(status);
	}

	public void setProgress(int progress) {
		progressBar.setValue(progress);
		progressField.setText(progress + "%");
	}

	public void setArtikelGesamt(int artikelGesamt) {
		this.artikelGesamt.setText(artikelGesamt + " Artikel");
	}

	public void setArtikelFertig(int fertig, int fehler) {
		artikelFertig.setText(fertig + " Artikel fertig, davon " + fehler + " gescheitert (Siehe Ausgabe-Datei)");
	}

	public void setVerstricheneZeit(long millis) {
		verstricheneZeit.setText(MainApplication.formatTime(millis));
	}

	public void setVerbleibendeZeit(long millis) {
		verbleibendeZeit.setText(MainApplication.formatTime(millis));
	}

	public JButton getCancelButton() {
		return btnCancel;
	}

	public void lock() {
		btnImport.setEnabled(false);
		btnUpdate.setEnabled(false);
		btnDelete.setEnabled(false);
		btnDownload.setEnabled(false);
		btnDownloadAll.setEnabled(false);
	}

	public void unlock() {
		btnImport.setEnabled(true);
		btnUpdate.setEnabled(true);
		btnDelete.setEnabled(true);
		btnDownload.setEnabled(true);
		btnDownloadAll.setEnabled(true);
	}

}
