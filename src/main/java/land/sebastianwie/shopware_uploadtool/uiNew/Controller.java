package land.sebastianwie.shopware_uploadtool.uiNew;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import land.sebastianwie.shopware_uploadtool.api.ApiConnector;
import land.sebastianwie.shopware_uploadtool.excel.article.ExcelCreator;
import land.sebastianwie.shopware_uploadtool.excel.article.ExcelReader;
import land.sebastianwie.shopware_uploadtool.topm.TopmArticleList;
import land.sebastianwie.shopware_uploadtool.topm.TopmExcelList;
import land.sebastianwie.shopware_uploadtool.transfer.ArticleProcessor;
import land.sebastianwie.shopware_uploadtool.transfer.ArticleStockProcessor;
import land.sebastianwie.shopware_uploadtool.transfer.ProcessStatus;
import land.sebastianwie.shopware_uploadtool.transfer.ArticleProcessor.ProcessMode;
import land.sebastianwie.shopware_uploadtool.uiNew.LoginPanel.LoginInformation;
import land.sebastianwie.shopware_uploadtool.uiNew.components.StatusPanel;
import land.sebastianwie.shopware_uploadtool.uiNew.tabs.Tab;
import land.sebastianwie.shopware_uploadtool.uiNew.tabs.DeleteTab.DeleteInformation;
import land.sebastianwie.shopware_uploadtool.uiNew.tabs.DownloadTab.DownloadInformation;
import land.sebastianwie.shopware_uploadtool.uiNew.tabs.UpdateStockTab.UpdateStockInformation;
import land.sebastianwie.shopware_uploadtool.uiNew.tabs.UploadTab.UploadInformation;

import com.alee.laf.WebLookAndFeel;

public class Controller {

	private static final String LOGINPRESETS_FILENAME = System.getProperty("user.home")
			+ "/.shopwareUploadToolProperties.xml";
	private Properties loginPresets;
	private LoginInformation loginInformation;

	private ApiConnector apiConnector;

	private JFileChooser fc;

	private SWWindow w;

	private int runningActions = 0;

	private static final int UPLOAD_NEW = 1, UPDATE = 2, DOWNLOAD = 4, DOWNLOAD_ALL = 8, DELETE = 16,
			UPDATE_STOCK = 32;

	public enum Filetype {
		XLS_ALL(new FileNameExtensionFilter("Alle Excel-Dateitypen (.xlsx, .xls)", "xlsx", "xls")), XLSX(new FileNameExtensionFilter(
				"Office OpenXML Spreadsheet (.xlsx)", "xlsx")), XLSX_MS(new FileNameExtensionFilter(
				"Microsoft Excel 2007/2010/2013 Spreadsheet (.xlsx)", "xlsx")), XLS(new FileNameExtensionFilter(
				"Microsoft Excel 97/2000/2003 Spreadsheet (.xls)", "xls")), CSV(new FileNameExtensionFilter(
				"Comma-Separated Values (.csv)", "csv")), XML(new FileNameExtensionFilter("XML-Properties File (.xml)", "xml"));

		private FileFilter filter;

		private Filetype(FileFilter filter) {
			this.filter = filter;
		}

		public FileFilter getFilter() {
			return filter;
		}
	}

	private final WindowListener exitListener;

	public Controller() {
		loginPresets = new Properties();
		try {
			loginPresets.loadFromXML(new FileInputStream(LOGINPRESETS_FILENAME));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(w, "Keine Login-Properties-Datei gefunden.", "Login",
					JOptionPane.WARNING_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(w, "Unbekannter Fehler: " + e + "\nProgramm wird beendet.", "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}

		w = new SWWindow(this);
		fc = new JFileChooser();
		exitListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				JOptionPane
						.showMessageDialog(
								w,
								"Mindestens ein Übertragungsvorgang ist noch aktiv."
										+ "\nBitte warten Sie auf dessen Beendigung oder brechen Sie diesen ab, bevor Sie dieses Fenster schließen.",
								"Übertragung aktiv", JOptionPane.WARNING_MESSAGE);
			}
		};
	}

	private void lockWindow(int action) {
		if (runningActions <= 0) {
			w.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			w.addWindowListener(exitListener);
		}
		runningActions += action;
	}

	private void unlockWindow(int action) {
		runningActions -= action;
		if (runningActions <= 0) {
			w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			w.removeWindowListener(exitListener);
		}
	}

	public void saveLoginPresets() {
		if (loginPresets != null) {
			try {
				if (confirmSave(new File(LOGINPRESETS_FILENAME)))
					loginPresets.storeToXML(new FileOutputStream(LOGINPRESETS_FILENAME), "Login-Properties");
				else
					return;
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(w, "Datei kann nicht gespeichert werden: Ein Ordner mit dem Namen \""
						+ LOGINPRESETS_FILENAME + "\" existiert bereits.", "Login", JOptionPane.WARNING_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(w, "Unbekannter Fehler: " + e + "\nProgramm wird beendet.", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
			JOptionPane.showMessageDialog(w, "Einstellungen erfolgreich gespeichert.", "Erfolg",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public Properties getLoginPresets() {
		return loginPresets;
	}

	public void setLoginPresets(Properties loginPresets) {
		this.loginPresets = loginPresets;
	}

	public void login(LoginInformation i) {
		this.loginInformation = i;

		try {
			apiConnector = new ApiConnector(i.swServer, i.swUsername, i.swApiKey);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(w, "Die Eingegebenen Informationen sind ungültig.", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		w.setDisplayedPanel(new TabContainer(this));
		w.setTitle("Shopware Upload-Tool - " + i.swServer);
	}

	private boolean confirmSave(File file) {
		if (file.isFile()) {
			int selection = JOptionPane.showConfirmDialog(w, "Eine Datei \"" + file.getAbsolutePath()
					+ "\" existiert bereits. Möchen Sie diese überschreiben?", "Bestätigung",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (selection == JOptionPane.YES_OPTION)
				return true;
			else
				return false;
		} else if (file.isDirectory()) {
			JOptionPane.showMessageDialog(w,
					"Ein Ordner mit dem Namen existiert bereits. Datei kann nicht gespeichert werden.", "Fehler",
					JOptionPane.WARNING_MESSAGE);
			return false;
		} else {
			return true;
		}
	}

	public File selectFile(String filename, boolean toSave, Filetype[] filetypes) {
		// JFileChooser fc = new JFileChooser();
		fc.setSelectedFile(new File(filename));

		fc.resetChoosableFileFilters();
		if (filetypes != null && filetypes.length > 0) {
			for (Filetype filetype : filetypes) {
				fc.addChoosableFileFilter(filetype.getFilter());
			}
			fc.setFileFilter(filetypes[0].getFilter());
		}

		int status;
		if (toSave)
			status = fc.showSaveDialog(w);
		else
			status = fc.showOpenDialog(w);
		if (status == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			if (!toSave)
				if (f.isFile())
					return f;
				else
					return null;
			else if (confirmSave(f))
				return f;
			else
				return null;
		}

		return null;
	}

	private void showConfirmation() {
		JOptionPane.showMessageDialog(w, "Datei erfolgreich gespeichert.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
	}

	public void makeTemplate() {
		File f = selectFile("template.xlsx", true, new Filetype[] { Filetype.XLSX, Filetype.XLSX_MS });
		if (f != null) {
			ExcelCreator ec = new ExcelCreator();
			ec.saveAs(f);
			this.showConfirmation();
		}
	}

	public void upload(Tab callingTab, UploadInformation info) {
		if (info.importFile == null || !info.importFile.isFile() || info.errorFile == null) {
			JOptionPane.showMessageDialog(w, "Bitte wählen Sie gültige Dateien aus", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		callingTab.lock();
		lockWindow(UPLOAD_NEW);
		ActionInformation ainfo = new ActionInformation();
		ainfo.mode = ProcessMode.UPLOAD;
		ainfo.toRead = info.importFile;
		ainfo.toWrite = info.errorFile;
		ainfo.callingTab = callingTab;
		ainfo.statusPanel = info.statusPanel;
		ainfo.toUnlockWindow = UPLOAD_NEW;
		Thread t = new ActionPerformer(ainfo);
		t.start();
		// try {
		// t.join();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// callingTab.unlock();
	}

	public void update(Tab callingTab, UploadInformation info) {
		if (info.importFile == null || !info.importFile.isFile() || info.errorFile == null) {
			JOptionPane.showMessageDialog(w, "Bitte wählen Sie gültige Dateien aus", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		callingTab.lock();
		lockWindow(UPDATE);
		ActionInformation ainfo = new ActionInformation();
		ainfo.mode = ProcessMode.UPDATE;
		ainfo.toRead = info.importFile;
		ainfo.toWrite = info.errorFile;
		ainfo.callingTab = callingTab;
		ainfo.statusPanel = info.statusPanel;
		ainfo.toUnlockWindow = UPDATE;
		Thread t = new ActionPerformer(ainfo);
		t.start();
		// try {
		// t.join();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// callingTab.unlock();
	}

	public void download(Tab callingTab, DownloadInformation info) {
		if (info.sourceFile == null || !info.sourceFile.isFile() || info.destFile == null) {
			JOptionPane.showMessageDialog(w, "Bitte wählen Sie gültige Dateien aus", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		callingTab.lock();
		lockWindow(DOWNLOAD);
		ActionInformation ainfo = new ActionInformation();
		ainfo.mode = ProcessMode.DOWNLOAD;
		ainfo.toRead = info.sourceFile;
		ainfo.toWrite = info.destFile;
		ainfo.callingTab = callingTab;
		ainfo.statusPanel = info.statusPanel;
		ainfo.toUnlockWindow = DOWNLOAD;
		Thread t = new ActionPerformer(ainfo);
		t.start();
	}

	public void downloadAll(Tab callingTab, DownloadInformation info) {
		if (info.destFile == null) {
			JOptionPane.showMessageDialog(w, "Bitte wählen Sie gültige Dateien aus", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		callingTab.lock();
		lockWindow(DOWNLOAD_ALL);
		ActionInformation ainfo = new ActionInformation();
		ainfo.mode = ProcessMode.DOWNLOAD_ALL;
		ainfo.toRead = info.sourceFile;
		ainfo.toWrite = info.destFile;
		ainfo.callingTab = callingTab;
		ainfo.statusPanel = info.statusPanel;
		ainfo.toUnlockWindow = DOWNLOAD_ALL;
		Thread t = new ActionPerformer(ainfo);
		t.start();
	}

	public void delete(Tab callingTab, DeleteInformation info) {
		if (info.toDelete == null || !info.toDelete.isFile() || info.errors == null) {
			JOptionPane.showMessageDialog(w, "Bitte wählen Sie gültige Dateien aus", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		int decision = JOptionPane
				.showConfirmDialog(
						w,
						"Möchten Sie mit dem Löschen dieser Artikel fortfahren?\nDas Löschen ist endgültig und kann nicht rückgängig gemacht werden!",
						"Hinweis", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (decision != JOptionPane.YES_OPTION)
			return;

		callingTab.lock();
		lockWindow(DELETE);
		ActionInformation ainfo = new ActionInformation();
		ainfo.mode = ProcessMode.DELETE;
		ainfo.toRead = info.toDelete;
		ainfo.toWrite = info.errors;
		ainfo.callingTab = callingTab;
		ainfo.statusPanel = info.statusPanel;
		ainfo.toUnlockWindow = DELETE;
		Thread t = new ActionPerformer(ainfo);
		t.start();
	}

	public void updateStocks(Tab callingTab, UpdateStockInformation info) {
		if (info.topMFile == null || !info.topMFile.isFile() || info.errorFile == null) {
			JOptionPane.showMessageDialog(w, "Bitte wählen Sie gültige Dateien aus", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		callingTab.lock();
		lockWindow(UPDATE_STOCK);
		TopmArticleList source = new TopmExcelList(info.topMFile);
		ExcelCreator errorCreator = new ExcelCreator(true);
		ProcessStatus status = new ProcessStatus(Integer.MAX_VALUE);
		ArticleStockProcessor processor = new ArticleStockProcessor(errorCreator, status, source, loginInformation);
		ActionListener cancelButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				status.cancel();
			}
		};
		callingTab.getCancelButton().addActionListener(cancelButtonListener);
		new Thread() {
			public void run() {
				processor.start();
				info.statusPanel.setStatus("Bestandsupdate läuft seit "
						+ new SimpleDateFormat("HH:mm:ss").format(new Date()));
				while (processor.isAlive()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					info.statusPanel.setProgress((int) Math.round(status.getRatio() * 1000));
					info.statusPanel.setTotal(status.getTodo());
					info.statusPanel.setDone(status.getDone(), status.getErrors());
					info.statusPanel.setElapsedTime(status.getPassedTime());
					info.statusPanel.setRemainingTime(status.getEstimatedRemainingTime());
				}

				info.statusPanel.setStatus("Bestandsupdate beendet - " + status.getDone() + " von " + status.getTodo()
						+ " in " + StatusPanel.formatTime(status.getPassedTime()) + " übertragen");
				errorCreator.saveAs(info.errorFile);
				callingTab.unlock();
				unlockWindow(UPDATE_STOCK);
				callingTab.getCancelButton().removeActionListener(cancelButtonListener);
			}
		}.start();

	}

	private void performAction(ActionInformation info) {
		ExcelReader reader = null;
		int totalNumber = Integer.MAX_VALUE;
		if (info.mode != ProcessMode.DOWNLOAD_ALL) {
			reader = new ExcelReader(info.toRead);
			if (info.mode == ProcessMode.UPDATE || info.mode == ProcessMode.UPLOAD)
				totalNumber = reader.getNumberOfArticles() + reader.getNumberOfVariants();
			else
				totalNumber = reader.getNumberOfArticles();
		}

		ProcessStatus status = new ProcessStatus(totalNumber);

		ExcelCreator writer = new ExcelCreator(true);

		ArticleProcessor processor = new ArticleProcessor(apiConnector, reader, writer, info.mode, status,
				loginInformation);

		info.statusPanel.reset();
		info.statusPanel.setStatus(info.mode + " läuft seit " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
		info.statusPanel.setTotal(status.getTodo());

		ActionListener cancelButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.cancel();
			}
		};
		info.callingTab.getCancelButton().addActionListener(cancelButtonListener);

		processor.start();
		while (processor.isAlive()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			info.statusPanel.setProgress((int) Math.round(status.getRatio() * 1000));
			info.statusPanel.setTotal(status.getTodo());
			info.statusPanel.setDone(status.getDone(), status.getErrors());
			info.statusPanel.setElapsedTime(status.getPassedTime());
			info.statusPanel.setRemainingTime(status.getEstimatedRemainingTime());
		}

		info.callingTab.getCancelButton().removeActionListener(cancelButtonListener);

		info.statusPanel.setStatus(info.mode + " beendet - " + status.getDone() + " von " + status.getTodo() + " in "
				+ StatusPanel.formatTime(status.getPassedTime()) + " übertragen");
		writer.saveAs(info.toWrite);
		info.callingTab.unlock();
		unlockWindow(info.toUnlockWindow);
	}

	public static void main(String[] args) {
		// try {
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// } catch (Exception e) {
		//
		// }
		WebLookAndFeel.install();
		new Controller();
	}

	private class ActionInformation {
		public ProcessMode mode;
		public File toRead;
		public File toWrite;
		public Tab callingTab;
		public StatusPanel statusPanel;
		public int toUnlockWindow;
	}

	private class ActionPerformer extends Thread {
		private ActionInformation info;

		public ActionPerformer(ActionInformation info) {
			this.info = info;
		}

		@Override
		public void run() {
			performAction(info);
		}
	}
}
