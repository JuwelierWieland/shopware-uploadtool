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

public class DeleteTab extends Tab {
	private static final long serialVersionUID = 2546863158385046237L;

	private Controller c;

	private JButton deleteFileBtn, errorFileBtn;
	private JLabel info, deleteFileLabel, errorFileLabel;
	private JTextField deleteFileTxt, errorFileTxt;
	private JButton deleteBtn, abortBtn;
	private StatusPanel status;

	@Override
	public boolean isCloseable() {
		return true;
	}

	@Override
	public String getTitle() {
		return "Artikel löschen";
	}

	@Override
	public void lock() {
		deleteBtn.setEnabled(false);
		abortBtn.setEnabled(true);
	}

	@Override
	public void unlock() {
		deleteBtn.setEnabled(true);
		abortBtn.setEnabled(false);
	}

	@Override
	public JButton getCancelButton() {
		return abortBtn;
	}

	public DeleteTab(Controller c) {
		this.c = c;

		info = new JLabel(
				"<html>In diesem Fenster können einzelne Artikel aus Shopware gelöscht werden. Diese Artikel werden dauerhaft gelöscht und können nicht wiederhergestellt werden.</html>");
		deleteFileLabel = new JLabel("Die Liste mit Artikelnummern/IDs der zu löschenden Artikel");
		deleteFileBtn = new JButton("Artikelliste öffnen");
		deleteFileTxt = new JTextField();
		deleteFileTxt.setEditable(false);

		errorFileLabel = new JLabel("Der Speicherort, in den fehlgeschlagene Löschvorgänge gespeichert werden");
		errorFileBtn = new JButton("Fehlerdatei auswählen");
		errorFileTxt = new JTextField();
		errorFileTxt.setEditable(false);

		deleteBtn = new JButton("Artikel permanent löschen");
		abortBtn = new JButton("Löschen abbrechen");
		abortBtn.setEnabled(false);

		status = new StatusPanel();

		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);

		deleteFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectDeleteFile();
			}
		});

		errorFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectErrorFile();
			}
		});

		deleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});

		layout.putConstraint(SpringLayout.WEST, info, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, info, 10, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, info, -10, SpringLayout.EAST, this);
		super.add(info);

		layout.putConstraint(SpringLayout.WEST, deleteFileLabel, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, deleteFileLabel, 20, SpringLayout.SOUTH, info);
		layout.putConstraint(SpringLayout.EAST, deleteFileLabel, 0, SpringLayout.EAST, info);
		super.add(deleteFileLabel);

		layout.putConstraint(SpringLayout.WEST, deleteFileBtn, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, deleteFileBtn, 5, SpringLayout.SOUTH, deleteFileLabel);
		layout.putConstraint(SpringLayout.EAST, deleteFileBtn, 250, SpringLayout.WEST, info);
		super.add(deleteFileBtn);

		layout.putConstraint(SpringLayout.WEST, deleteFileTxt, 10, SpringLayout.EAST, deleteFileBtn);
		layout.putConstraint(SpringLayout.BASELINE, deleteFileTxt, 0, SpringLayout.BASELINE, deleteFileBtn);
		layout.putConstraint(SpringLayout.EAST, deleteFileTxt, 0, SpringLayout.EAST, info);
		super.add(deleteFileTxt);

		layout.putConstraint(SpringLayout.WEST, errorFileLabel, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, errorFileLabel, 10, SpringLayout.SOUTH, deleteFileBtn);
		layout.putConstraint(SpringLayout.EAST, errorFileLabel, 0, SpringLayout.EAST, info);
		super.add(errorFileLabel);

		layout.putConstraint(SpringLayout.NORTH, errorFileBtn, 5, SpringLayout.SOUTH, errorFileLabel);
		layout.putConstraint(SpringLayout.EAST, errorFileBtn, 0, SpringLayout.EAST, deleteFileBtn);
		layout.putConstraint(SpringLayout.WEST, errorFileBtn, 0, SpringLayout.WEST, info);
		super.add(errorFileBtn);

		layout.putConstraint(SpringLayout.BASELINE, errorFileTxt, 0, SpringLayout.BASELINE, errorFileBtn);
		layout.putConstraint(SpringLayout.WEST, errorFileTxt, 10, SpringLayout.EAST, errorFileBtn);
		layout.putConstraint(SpringLayout.EAST, errorFileTxt, 0, SpringLayout.EAST, info);
		super.add(errorFileTxt);

		layout.putConstraint(SpringLayout.NORTH, deleteBtn, 25, SpringLayout.SOUTH, errorFileBtn);
		layout.putConstraint(SpringLayout.WEST, deleteBtn, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.EAST, deleteBtn, 0, SpringLayout.EAST, info);
		super.add(deleteBtn);

		layout.putConstraint(SpringLayout.NORTH, abortBtn, 5, SpringLayout.SOUTH, deleteBtn);
		layout.putConstraint(SpringLayout.WEST, abortBtn, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.EAST, abortBtn, 0, SpringLayout.EAST, info);
		super.add(abortBtn);

		layout.putConstraint(SpringLayout.NORTH, status, 5, SpringLayout.SOUTH, abortBtn);
		layout.putConstraint(SpringLayout.WEST, status, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.EAST, status, 0, SpringLayout.EAST, info);
		layout.putConstraint(SpringLayout.SOUTH, status, -10, SpringLayout.SOUTH, this);
		super.add(status);
	}

	private void selectDeleteFile() {
		File f = c.selectFile("todelete.xlsx", false, new Filetype[] { Filetype.XLS_ALL, Filetype.XLSX,
				Filetype.XLSX_MS, Filetype.XLS });
		if (f != null)
			deleteFileTxt.setText(f.getAbsolutePath());
	}

	private void selectErrorFile() {
		File f = c.selectFile("errors.xlsx", true, new Filetype[] { Filetype.XLSX });
		if (f != null)
			errorFileTxt.setText(f.getAbsolutePath());
	}

	private void delete() {
		DeleteInformation info = new DeleteInformation();
		info.toDelete = new File(deleteFileTxt.getText());
		info.errors = new File(errorFileTxt.getText());
		info.statusPanel = status;
		c.delete(this, info);
	}

	public class DeleteInformation {
		public File toDelete;
		public File errors;
		public StatusPanel statusPanel;
	}

}
