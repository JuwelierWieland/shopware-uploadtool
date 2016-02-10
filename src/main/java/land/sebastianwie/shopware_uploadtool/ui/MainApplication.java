package land.sebastianwie.shopware_uploadtool.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import land.sebastianwie.shopware_uploadtool.api.ApiConnector;
import land.sebastianwie.shopware_uploadtool.excel.article.ExcelCreator;
import land.sebastianwie.shopware_uploadtool.excel.article.ExcelReader;
import land.sebastianwie.shopware_uploadtool.transfer.ArticleProcessor;
import land.sebastianwie.shopware_uploadtool.transfer.ProcessStatus;
import land.sebastianwie.shopware_uploadtool.transfer.ArticleProcessor.ProcessMode;

public class MainApplication {
	ApiConnector ac;
	// MysqlConnector mc;

	JFrame frame;

	LoginPanel loginPanel;
	MainPanel mainPanel;

	public static String hostname;
	public static String username;
	public static String apikey;

	public static String dbhostname;
	public static String dbusername;
	public static String dbpassword;
	public static String dbdatabase;

	public MainApplication() {
		frame = new JFrame("Shopware Upload Tool");
		frame.setBounds(100, 100, 640, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		String hostname = null;
		String username = null;
		String apikey = null;
		String ftphost = null;
		String ftpuser = null;
		String ftppassword = null;
		String dbhost = null;
		String dbuser = null;
		String dbpassword = null;
		String dbdatabase = null;
		loginPanel = new LoginPanel(this, hostname, username, apikey, ftphost,
				ftpuser, ftppassword, dbhost, dbuser, dbpassword, dbdatabase);
		frame.setContentPane(loginPanel);
		frame.validate();
		frame.setResizable(false);
	}

	public void login(String hostname, String username, String key,
			String dbhostname, String dbusername, String dbpassword,
			String dbdatabase) {
		try {
			MainApplication.hostname = hostname;
			MainApplication.username = username;
			MainApplication.apikey = key;
			ac = new ApiConnector(hostname, username, key);

			MainApplication.dbhostname = dbhostname;
			MainApplication.dbusername = dbusername;
			MainApplication.dbpassword = dbpassword;
			MainApplication.dbdatabase = dbdatabase;
			// mc = new MysqlConnector(dbhostname, dbusername, dbpassword,
			// dbdatabase);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(frame, "Ungültige Eingabe");
			return;
			// } catch (SQLException e) {
			// JOptionPane.showMessageDialog(frame,
			// "Fehler. Möglicherweise stimmen die MySQL-Zugangsdaten nicht");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame,
					"Unbekannter Fehler" + e.getMessage());
		}

		mainPanel = new MainPanel(this);
		frame.setContentPane(mainPanel);
		frame.setTitle("Shopware Upload Tool - " + hostname);
		frame.validate();
		loginPanel = null;
	}

	public void performUpload(ProcessMode action, String toUpload,
			String errors, String images, MainPanel panel) {
		ExcelReader er = null;
		int number = 0;
		if (action != ProcessMode.DOWNLOAD_ALL) {
			er = new ExcelReader(toUpload);
			if (action == ProcessMode.UPDATE || action == ProcessMode.UPLOAD)
				number = er.getNumberOfArticles() + er.getNumberOfVariants();
			else
				number = er.getNumberOfArticles();
		}
		ProcessStatus status = new ProcessStatus(number);
		ExcelCreator ec = new ExcelCreator(true);
		ArticleProcessor ap = new ArticleProcessor(ac, er, ec, action, status);

		ActionListener cancelButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				status.cancel();
			}
		};
		panel.getCancelButton().setEnabled(true);
		panel.getCancelButton().addActionListener(cancelButtonListener);
		panel.setStatus(action + " läuft seit "
				+ new SimpleDateFormat("HH:mm:ss").format(new Date()));
		panel.setArtikelGesamt(status.getTodo());
		panel.lock();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		new Thread() {
			public void run() {
				ap.start();
				while (ap.isAlive()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					panel.setProgress((int) Math.round(status.getRatio() * 100));
					panel.setArtikelGesamt(status.getTodo());
					panel.setArtikelFertig(status.getDone(), status.getErrors());
					panel.setVerstricheneZeit(status.getPassedTime());
					panel.setVerbleibendeZeit(status
							.getEstimatedRemainingTime());
				}
				panel.unlock();
				panel.getCancelButton().setEnabled(false);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				panel.getCancelButton().removeActionListener(
						cancelButtonListener);
				panel.setStatus(action + " beendet - " + status.getDone()
						+ " von " + status.getTodo() + " in "
						+ formatTime(status.getPassedTime()) + " übertragen");
				ec.saveAs(errors);
			};
		}.start();
	}

	public static String formatTime(long time) {
		long seconds = (time / 1000) % 60;
		long minutes = (time / 60000) % 60;
		long hours = (time / 3600000) % 24;
		long days = (time / 86400000);
		String output = new String();
		if (days > 0)
			output = days + " Tage, " + hours + " Stunden";
		else if (days == 0 && hours > 4)
			output = hours + " Stunden";
		else if (days == 0 && hours > 0)
			output = hours + " Stunden, " + minutes + " Minuten";
		else if (days == 0 && hours == 0 && minutes > 30)
			output = minutes + " Minuten";
		else if (days == 0 && hours == 0 && minutes > 0)
			output = minutes + " Minuten, " + seconds + " Sekunden";
		else
			output = seconds + " Sekunden";
		return output;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		MainApplication c = new MainApplication();
	}

}
