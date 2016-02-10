package land.sebastianwie.shopware_uploadtool.uiNew.components;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

public class StatusPanel extends JPanel {
	private static final long serialVersionUID = 5420274845267718978L;

	private JLabel statusLbl, totalLbl, doneLbl, elapsedLbl, remainingLbl;
	private JTextField statusTxt, totalTxt, doneTxt, elapsedTxt, remainingTxt;

	private JProgressBar progressBar;

	public StatusPanel() {
		this.setBorder(new LineBorder(Color.BLACK, 1, false));
		SpringLayout layout = new SpringLayout();
		super.setLayout(layout);

		statusLbl = new JLabel("Status:");
		// progressLbl = new JLabel("Fortschritt:");
		totalLbl = new JLabel("Gesamt Artikel:");
		doneLbl = new JLabel("Artikel fertig:");
		elapsedLbl = new JLabel("Verstrichene Zeit:");
		remainingLbl = new JLabel("Verbleibende Zeit:");

		statusTxt = new JTextField();
		// progressTxt = new JTextField();
		totalTxt = new JTextField();
		doneTxt = new JTextField();
		elapsedTxt = new JTextField();
		remainingTxt = new JTextField();

		statusTxt.setEditable(false);
		// progressTxt.setEditable(false);
		totalTxt.setEditable(false);
		totalTxt.setEditable(false);
		doneTxt.setEditable(false);
		elapsedTxt.setEditable(false);
		remainingTxt.setEditable(false);

		progressBar = new JProgressBar(0, 1000);
		progressBar.setStringPainted(true);

		this.reset();

		layout.putConstraint(SpringLayout.WEST, statusLbl, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, statusLbl, 200, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, statusLbl, 10, SpringLayout.NORTH, this);
		super.add(statusLbl);

		layout.putConstraint(SpringLayout.WEST, statusTxt, 10, SpringLayout.EAST, statusLbl);
		layout.putConstraint(SpringLayout.BASELINE, statusTxt, 0, SpringLayout.BASELINE, statusLbl);
		layout.putConstraint(SpringLayout.EAST, statusTxt, -10, SpringLayout.EAST, this);
		super.add(statusTxt);

		layout.putConstraint(SpringLayout.WEST, progressBar, 0, SpringLayout.WEST, statusLbl);
		layout.putConstraint(SpringLayout.NORTH, progressBar, 8, SpringLayout.SOUTH, statusLbl);
		layout.putConstraint(SpringLayout.EAST, progressBar, 0, SpringLayout.EAST, statusTxt);
		super.add(progressBar);

		layout.putConstraint(SpringLayout.WEST, totalLbl, 0, SpringLayout.WEST, statusLbl);
		layout.putConstraint(SpringLayout.EAST, totalLbl, 0, SpringLayout.EAST, statusLbl);
		layout.putConstraint(SpringLayout.NORTH, totalLbl, 12, SpringLayout.SOUTH, progressBar);
		super.add(totalLbl);

		layout.putConstraint(SpringLayout.WEST, totalTxt, 10, SpringLayout.EAST, totalLbl);
		layout.putConstraint(SpringLayout.EAST, totalTxt, 0, SpringLayout.EAST, statusTxt);
		layout.putConstraint(SpringLayout.BASELINE, totalTxt, 0, SpringLayout.BASELINE, totalLbl);
		super.add(totalTxt);

		layout.putConstraint(SpringLayout.WEST, doneLbl, 0, SpringLayout.WEST, statusLbl);
		layout.putConstraint(SpringLayout.EAST, doneLbl, 0, SpringLayout.EAST, statusLbl);
		layout.putConstraint(SpringLayout.NORTH, doneLbl, 8, SpringLayout.SOUTH, totalLbl);
		super.add(doneLbl);

		layout.putConstraint(SpringLayout.WEST, doneTxt, 10, SpringLayout.EAST, doneLbl);
		layout.putConstraint(SpringLayout.EAST, doneTxt, 0, SpringLayout.EAST, statusTxt);
		layout.putConstraint(SpringLayout.BASELINE, doneTxt, 0, SpringLayout.BASELINE, doneLbl);
		super.add(doneTxt);

		layout.putConstraint(SpringLayout.WEST, elapsedLbl, 0, SpringLayout.WEST, statusLbl);
		layout.putConstraint(SpringLayout.EAST, elapsedLbl, 0, SpringLayout.EAST, statusLbl);
		layout.putConstraint(SpringLayout.NORTH, elapsedLbl, 8, SpringLayout.SOUTH, doneLbl);
		super.add(elapsedLbl);

		layout.putConstraint(SpringLayout.WEST, elapsedTxt, 10, SpringLayout.EAST, elapsedLbl);
		layout.putConstraint(SpringLayout.EAST, elapsedTxt, 0, SpringLayout.EAST, statusTxt);
		layout.putConstraint(SpringLayout.BASELINE, elapsedTxt, 0, SpringLayout.BASELINE, elapsedLbl);
		super.add(elapsedTxt);

		layout.putConstraint(SpringLayout.WEST, remainingLbl, 0, SpringLayout.WEST, statusLbl);
		layout.putConstraint(SpringLayout.EAST, remainingLbl, 0, SpringLayout.EAST, statusLbl);
		layout.putConstraint(SpringLayout.NORTH, remainingLbl, 8, SpringLayout.SOUTH, elapsedLbl);
		super.add(remainingLbl);

		layout.putConstraint(SpringLayout.WEST, remainingTxt, 10, SpringLayout.EAST, remainingLbl);
		layout.putConstraint(SpringLayout.EAST, remainingTxt, 0, SpringLayout.EAST, statusTxt);
		layout.putConstraint(SpringLayout.BASELINE, remainingTxt, 0, SpringLayout.BASELINE, remainingLbl);
		super.add(remainingTxt);
	}

	public void reset() {
		statusTxt.setText("Nicht gestartet");
		this.setProgress(0);
		this.setTotal(0);
		this.setDone(0, 0);
		this.setElapsedTime(0);
		this.setRemainingTime(0);
	}

	public void setStatus(String text) {
		statusTxt.setText(text);
	}

	public void setProgress(int p) {
		progressBar.setString(p / 10 + " %");
		progressBar.setValue(p);
	}

	public void setTotal(int t) {
		totalTxt.setText(t + " Artikel");
	}

	public void setDone(int d, int f) {
		doneTxt.setText(d + " Artikel fertig, davon " + f + " fehlgeschlagen.");
	}

	public void setElapsedTime(long time) {
		elapsedTxt.setText(formatTime(time));
	}

	public void setRemainingTime(long time) {
		remainingTxt.setText(formatTime(time));
	}

	public static String formatTime(long time) {
		long seconds = (time / 1000) % 60;
		long minutes = (time / 60000) % 60;
		long hours = (time / 3600000) % 24;
		long days = (time / 86400000);
		String output = new String();
		if (days > 1 && hours != 1)
			output = days + " Tage, " + hours + " Stunden";
		else if (days > 1 && hours == 1)
			output = days + " Tage, " + hours + " Stunde";
		else if (days == 1 && hours != 1)
			output = days + " Tag, " + hours + " Stunden";
		else if (days == 1 && hours == 1)
			output = days + " Tag, " + hours + " Stunde";
		else if (days == 0 && hours > 4)
			output = hours + " Stunden";
		else if (days == 0 && hours > 1 && minutes != 1)
			output = hours + " Stunden, " + minutes + " Minuten";
		else if (days == 0 && hours > 1 && minutes == 1)
			output = hours + " Stunden, " + minutes + " Minute";
		else if (days == 0 && hours == 1 && minutes != 1)
			output = hours + " Stunde, " + minutes + " Minuten";
		else if (days == 0 && hours == 1 && minutes == 1)
			output = hours + " Stunde, " + minutes + " Minute";
		else if (days == 0 && hours == 0 && minutes > 30)
			output = minutes + " Minuten";
		else if (days == 0 && hours == 0 && minutes > 1 && seconds != 1)
			output = minutes + " Minuten, " + seconds + " Sekunden";
		else if (days == 0 && hours == 0 && minutes > 1 && seconds == 1)
			output = minutes + " Minuten, " + seconds + " Sekunde";
		else if (days == 0 && hours == 0 && minutes == 1 && seconds != 1)
			output = minutes + " Minute, " + seconds + " Sekunden";
		else if (days == 0 && hours == 0 && minutes == 1 && seconds == 1)
			output = minutes + " Minute, " + seconds + " Sekunde";
		else if (days == 0 && hours == 0 && minutes == 0 && seconds == 1)
			output = seconds + " Sekunde";
		else
			output = seconds + " Sekunden";
		return output;
	}
}
