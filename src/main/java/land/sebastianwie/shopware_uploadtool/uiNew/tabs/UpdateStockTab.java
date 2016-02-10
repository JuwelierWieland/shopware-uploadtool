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

public class UpdateStockTab extends Tab {
	private static final long serialVersionUID = 8474439281302292744L;

	private Controller c;

	private JButton topMFileBtn, errorFileBtn;
	private JLabel info, topMFileLabel, errorFileLabel;
	private JTextField topMFileTxt, errorFileTxt;
	private JButton updateBtn, abortBtn;
	private StatusPanel status;

	@Override
	public boolean isCloseable() {
		return true;
	}

	@Override
	public String getTitle() {
		return "Bestände aktualisieren";
	}

	@Override
	public void lock() {
		updateBtn.setEnabled(false);
		abortBtn.setEnabled(true);
	}

	@Override
	public void unlock() {
		updateBtn.setEnabled(true);
		abortBtn.setEnabled(false);
	}

	@Override
	public JButton getCancelButton() {
		return abortBtn;
	}

	public UpdateStockTab(Controller c) {
		this.c = c;

		info = new JLabel(
				"<html>In diesem Fenster können die Bestände der Artikel in Shopware mit den Beständen aus TopM abgeglichen werden.</html>");
		topMFileLabel = new JLabel("Der Speicherort der TopM-Artikelliste im Excel-Format");
		topMFileBtn = new JButton("TopM-Artikelliste öffnen");
		topMFileTxt = new JTextField();

		errorFileLabel = new JLabel("Der Speicherort, in den fehlgeschlagene Artikelupdates gespeichert werden");
		errorFileBtn = new JButton("Fehlerdatei auswählen");
		errorFileTxt = new JTextField();

		topMFileTxt.setEditable(false);
		errorFileTxt.setEditable(false);

		updateBtn = new JButton("Bestände aktualisieren");
		abortBtn = new JButton("Update abbrechen");
		abortBtn.setEnabled(false);

		status = new StatusPanel();

		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);

		topMFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectTopMFile();
			}
		});

		errorFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectErrorFile();
			}
		});

		updateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateStock();
			}
		});

		layout.putConstraint(SpringLayout.WEST, info, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, info, 10, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, info, -10, SpringLayout.EAST, this);
		super.add(info);

		layout.putConstraint(SpringLayout.WEST, topMFileLabel, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, topMFileLabel, 20, SpringLayout.SOUTH, info);
		layout.putConstraint(SpringLayout.EAST, topMFileLabel, 0, SpringLayout.EAST, info);
		super.add(topMFileLabel);

		layout.putConstraint(SpringLayout.WEST, topMFileBtn, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, topMFileBtn, 5, SpringLayout.SOUTH, topMFileLabel);
		layout.putConstraint(SpringLayout.EAST, topMFileBtn, 250, SpringLayout.WEST, info);
		super.add(topMFileBtn);

		layout.putConstraint(SpringLayout.WEST, topMFileTxt, 10, SpringLayout.EAST, topMFileBtn);
		layout.putConstraint(SpringLayout.BASELINE, topMFileTxt, 0, SpringLayout.BASELINE, topMFileBtn);
		layout.putConstraint(SpringLayout.EAST, topMFileTxt, 0, SpringLayout.EAST, info);
		super.add(topMFileTxt);

		layout.putConstraint(SpringLayout.WEST, errorFileLabel, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.NORTH, errorFileLabel, 10, SpringLayout.SOUTH, topMFileBtn);
		layout.putConstraint(SpringLayout.EAST, errorFileLabel, 0, SpringLayout.EAST, info);
		super.add(errorFileLabel);

		layout.putConstraint(SpringLayout.NORTH, errorFileBtn, 5, SpringLayout.SOUTH, errorFileLabel);
		layout.putConstraint(SpringLayout.EAST, errorFileBtn, 0, SpringLayout.EAST, topMFileBtn);
		layout.putConstraint(SpringLayout.WEST, errorFileBtn, 0, SpringLayout.WEST, info);
		super.add(errorFileBtn);

		layout.putConstraint(SpringLayout.BASELINE, errorFileTxt, 0, SpringLayout.BASELINE, errorFileBtn);
		layout.putConstraint(SpringLayout.WEST, errorFileTxt, 10, SpringLayout.EAST, errorFileBtn);
		layout.putConstraint(SpringLayout.EAST, errorFileTxt, 0, SpringLayout.EAST, info);
		super.add(errorFileTxt);

		layout.putConstraint(SpringLayout.NORTH, updateBtn, 25, SpringLayout.SOUTH, errorFileBtn);
		layout.putConstraint(SpringLayout.WEST, updateBtn, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.EAST, updateBtn, 0, SpringLayout.EAST, info);
		super.add(updateBtn);

		layout.putConstraint(SpringLayout.NORTH, abortBtn, 5, SpringLayout.SOUTH, updateBtn);
		layout.putConstraint(SpringLayout.WEST, abortBtn, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.EAST, abortBtn, 0, SpringLayout.EAST, info);
		super.add(abortBtn);

		layout.putConstraint(SpringLayout.NORTH, status, 5, SpringLayout.SOUTH, abortBtn);
		layout.putConstraint(SpringLayout.WEST, status, 0, SpringLayout.WEST, info);
		layout.putConstraint(SpringLayout.EAST, status, 0, SpringLayout.EAST, info);
		layout.putConstraint(SpringLayout.SOUTH, status, -10, SpringLayout.SOUTH, this);
		super.add(status);
	}

	private void selectTopMFile() {
		File f = c.selectFile("Mappe1.xlsx", false, new Filetype[] { Filetype.XLSX, Filetype.XLSX_MS });
		if (f != null)
			topMFileTxt.setText(f.getAbsolutePath());
	}

	private void selectErrorFile() {
		File f = c.selectFile("errors.xlsx", true, new Filetype[] { Filetype.XLSX });
		if (f != null)
			errorFileTxt.setText(f.getAbsolutePath());
	}

	public void updateStock() {
		UpdateStockInformation info = new UpdateStockInformation();
		info.topMFile = new File(topMFileTxt.getText());
		info.errorFile = new File(errorFileTxt.getText());
		info.statusPanel = status;
		c.updateStocks(this, info);
	}

	public class UpdateStockInformation {
		public File topMFile;
		public File errorFile;
		public StatusPanel statusPanel;
	}

}
