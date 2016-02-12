package land.sebastianwie.shopware_uploadtool.uiNew;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class LoginPanel extends JPanel {
	private static final long serialVersionUID = 370581390104322046L;

	private Controller c;

	private JLabel info, serverLabel, usernameLabel, apiKeyLabel, ftpServerLabel, ftpUsernameLabel, ftpPasswordLabel, mySQLServerLabel,
			mySQLUsernameLabel, mySQLPasswordLabel, mySQLSchemaLabel, supplierJsonUrlLabel;
	private JTextField serverTxt, usernameTxt, ftpServerTxt, ftpUsernameTxt, mySQLServerTxt, mySQLUsernameTxt, mySQLSchemaTxt,
			supplierJsonUrlTxt;
	private JPasswordField apiKeyTxt, ftpPasswordTxt, mySQLPasswordTxt;
	private JButton saveButton, loginButton;

	public LoginPanel(Controller c) {
		SpringLayout layout = new SpringLayout();
		super.setLayout(layout);
		super.setOpaque(true);
		this.c = c;
		Properties loginPresets = c.getLoginPresets();

		info = new JLabel(
				"<html>Shopware Upload Tool - 2015, Sebastian Wieland<br>Bitte geben Sie die Zugangsdaten zu Ihren Shopware-Shop an:</html>");
		serverLabel = new JLabel("Shopware-Server");
		usernameLabel = new JLabel("Shopware-Benutzername");
		apiKeyLabel = new JLabel("Shopware-Api-Schlüssel");

		ftpServerLabel = new JLabel("FTP-Server");
		ftpUsernameLabel = new JLabel("FTP-Benutzername");
		ftpPasswordLabel = new JLabel("FTP-Passwort");

		mySQLServerLabel = new JLabel("MySQL-Server");
		mySQLUsernameLabel = new JLabel("MySQL-Benutzername");
		mySQLPasswordLabel = new JLabel("MySQL-Passwort");
		mySQLSchemaLabel = new JLabel("MySQL-Datenbank");

		supplierJsonUrlLabel = new JLabel("Suppliers-Map URL");

		serverTxt = new JTextField(loginPresets.getProperty("swServer"));
		usernameTxt = new JTextField(loginPresets.getProperty("swUsername"));
		apiKeyTxt = new JPasswordField(loginPresets.getProperty("swApiKey"));

		ftpServerTxt = new JTextField(loginPresets.getProperty("ftpServer"));
		ftpUsernameTxt = new JTextField(loginPresets.getProperty("ftpUsername"));
		ftpPasswordTxt = new JPasswordField(loginPresets.getProperty("ftpPassword"));

		mySQLServerTxt = new JTextField(loginPresets.getProperty("mySqlServer"));
		mySQLUsernameTxt = new JTextField(loginPresets.getProperty("mySqlUsername"));
		mySQLPasswordTxt = new JPasswordField(loginPresets.getProperty("mySqlPassword"));
		mySQLSchemaTxt = new JTextField(loginPresets.getProperty("mySqlSchema"));

		supplierJsonUrlTxt = new JTextField(loginPresets.getProperty("supplierJsonUrl"));

		saveButton = new JButton("Einstellungen speichern");
		loginButton = new JButton("Anmelden");

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveLoginPresets();
			}
		});

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});

		layout.putConstraint(SpringLayout.WEST, info, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, info, 10, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, info, -10, SpringLayout.EAST, this);
		this.add(info);

		layout.putConstraint(SpringLayout.NORTH, serverLabel, 25, SpringLayout.SOUTH, info);
		layout.putConstraint(SpringLayout.WEST, serverLabel, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, serverLabel, 300, SpringLayout.WEST, this);
		this.add(serverLabel);

		layout.putConstraint(SpringLayout.WEST, serverTxt, 10, SpringLayout.EAST, serverLabel);
		layout.putConstraint(SpringLayout.BASELINE, serverTxt, 0, SpringLayout.BASELINE, serverLabel);
		layout.putConstraint(SpringLayout.EAST, serverTxt, -10, SpringLayout.EAST, this);
		this.add(serverTxt);

		layout.putConstraint(SpringLayout.NORTH, usernameLabel, 10, SpringLayout.SOUTH, serverLabel);
		layout.putConstraint(SpringLayout.WEST, usernameLabel, 0, SpringLayout.WEST, serverLabel);
		layout.putConstraint(SpringLayout.EAST, usernameLabel, 0, SpringLayout.EAST, serverLabel);
		this.add(usernameLabel);

		layout.putConstraint(SpringLayout.WEST, usernameTxt, 10, SpringLayout.EAST, serverLabel);
		layout.putConstraint(SpringLayout.BASELINE, usernameTxt, 0, SpringLayout.BASELINE, usernameLabel);
		layout.putConstraint(SpringLayout.EAST, usernameTxt, 0, SpringLayout.EAST, serverTxt);
		this.add(usernameTxt);

		layout.putConstraint(SpringLayout.NORTH, apiKeyLabel, 10, SpringLayout.SOUTH, usernameLabel);
		layout.putConstraint(SpringLayout.WEST, apiKeyLabel, 0, SpringLayout.WEST, serverLabel);
		layout.putConstraint(SpringLayout.EAST, apiKeyLabel, 0, SpringLayout.EAST, serverLabel);
		this.add(apiKeyLabel);

		layout.putConstraint(SpringLayout.WEST, apiKeyTxt, 10, SpringLayout.EAST, serverLabel);
		layout.putConstraint(SpringLayout.BASELINE, apiKeyTxt, 0, SpringLayout.BASELINE, apiKeyLabel);
		layout.putConstraint(SpringLayout.EAST, apiKeyTxt, 0, SpringLayout.EAST, serverTxt);
		this.add(apiKeyTxt);

		layout.putConstraint(SpringLayout.NORTH, ftpServerLabel, 20, SpringLayout.SOUTH, apiKeyLabel);
		layout.putConstraint(SpringLayout.WEST, ftpServerLabel, 0, SpringLayout.WEST, serverLabel);
		layout.putConstraint(SpringLayout.EAST, ftpServerLabel, 0, SpringLayout.EAST, serverLabel);
		this.add(ftpServerLabel);

		layout.putConstraint(SpringLayout.WEST, ftpServerTxt, 10, SpringLayout.EAST, serverLabel);
		layout.putConstraint(SpringLayout.BASELINE, ftpServerTxt, 0, SpringLayout.BASELINE, ftpServerLabel);
		layout.putConstraint(SpringLayout.EAST, ftpServerTxt, 0, SpringLayout.EAST, serverTxt);
		this.add(ftpServerTxt);

		layout.putConstraint(SpringLayout.NORTH, ftpUsernameLabel, 10, SpringLayout.SOUTH, ftpServerLabel);
		layout.putConstraint(SpringLayout.WEST, ftpUsernameLabel, 0, SpringLayout.WEST, serverLabel);
		layout.putConstraint(SpringLayout.EAST, ftpUsernameLabel, 0, SpringLayout.EAST, serverLabel);
		this.add(ftpUsernameLabel);

		layout.putConstraint(SpringLayout.WEST, ftpUsernameTxt, 10, SpringLayout.EAST, serverLabel);
		layout.putConstraint(SpringLayout.BASELINE, ftpUsernameTxt, 0, SpringLayout.BASELINE, ftpUsernameLabel);
		layout.putConstraint(SpringLayout.EAST, ftpUsernameTxt, 0, SpringLayout.EAST, serverTxt);
		this.add(ftpUsernameTxt);

		layout.putConstraint(SpringLayout.NORTH, ftpPasswordLabel, 10, SpringLayout.SOUTH, ftpUsernameLabel);
		layout.putConstraint(SpringLayout.WEST, ftpPasswordLabel, 0, SpringLayout.WEST, serverLabel);
		layout.putConstraint(SpringLayout.EAST, ftpPasswordLabel, 0, SpringLayout.EAST, serverLabel);
		this.add(ftpPasswordLabel);

		layout.putConstraint(SpringLayout.WEST, ftpPasswordTxt, 10, SpringLayout.EAST, serverLabel);
		layout.putConstraint(SpringLayout.BASELINE, ftpPasswordTxt, 0, SpringLayout.BASELINE, ftpPasswordLabel);
		layout.putConstraint(SpringLayout.EAST, ftpPasswordTxt, 0, SpringLayout.EAST, serverTxt);
		this.add(ftpPasswordTxt);

		layout.putConstraint(SpringLayout.NORTH, mySQLServerLabel, 20, SpringLayout.SOUTH, ftpPasswordLabel);
		layout.putConstraint(SpringLayout.WEST, mySQLServerLabel, 0, SpringLayout.WEST, serverLabel);
		layout.putConstraint(SpringLayout.EAST, mySQLServerLabel, 0, SpringLayout.EAST, serverLabel);
		this.add(mySQLServerLabel);

		layout.putConstraint(SpringLayout.WEST, mySQLServerTxt, 10, SpringLayout.EAST, serverLabel);
		layout.putConstraint(SpringLayout.BASELINE, mySQLServerTxt, 0, SpringLayout.BASELINE, mySQLServerLabel);
		layout.putConstraint(SpringLayout.EAST, mySQLServerTxt, 0, SpringLayout.EAST, serverTxt);
		this.add(mySQLServerTxt);

		layout.putConstraint(SpringLayout.NORTH, mySQLUsernameLabel, 10, SpringLayout.SOUTH, mySQLServerLabel);
		layout.putConstraint(SpringLayout.WEST, mySQLUsernameLabel, 0, SpringLayout.WEST, serverLabel);
		layout.putConstraint(SpringLayout.EAST, mySQLUsernameLabel, 0, SpringLayout.EAST, serverLabel);
		this.add(mySQLUsernameLabel);

		layout.putConstraint(SpringLayout.WEST, mySQLUsernameTxt, 10, SpringLayout.EAST, serverLabel);
		layout.putConstraint(SpringLayout.BASELINE, mySQLUsernameTxt, 0, SpringLayout.BASELINE, mySQLUsernameLabel);
		layout.putConstraint(SpringLayout.EAST, mySQLUsernameTxt, 0, SpringLayout.EAST, serverTxt);
		this.add(mySQLUsernameTxt);

		layout.putConstraint(SpringLayout.NORTH, mySQLPasswordLabel, 10, SpringLayout.SOUTH, mySQLUsernameLabel);
		layout.putConstraint(SpringLayout.WEST, mySQLPasswordLabel, 0, SpringLayout.WEST, serverLabel);
		layout.putConstraint(SpringLayout.EAST, mySQLPasswordLabel, 0, SpringLayout.EAST, serverLabel);
		this.add(mySQLPasswordLabel);

		layout.putConstraint(SpringLayout.WEST, mySQLPasswordTxt, 10, SpringLayout.EAST, serverLabel);
		layout.putConstraint(SpringLayout.BASELINE, mySQLPasswordTxt, 0, SpringLayout.BASELINE, mySQLPasswordLabel);
		layout.putConstraint(SpringLayout.EAST, mySQLPasswordTxt, 0, SpringLayout.EAST, serverTxt);
		this.add(mySQLPasswordTxt);

		layout.putConstraint(SpringLayout.NORTH, mySQLSchemaLabel, 10, SpringLayout.SOUTH, mySQLPasswordLabel);
		layout.putConstraint(SpringLayout.WEST, mySQLSchemaLabel, 0, SpringLayout.WEST, serverLabel);
		layout.putConstraint(SpringLayout.EAST, mySQLSchemaLabel, 0, SpringLayout.EAST, serverLabel);
		this.add(mySQLSchemaLabel);

		layout.putConstraint(SpringLayout.WEST, mySQLSchemaTxt, 10, SpringLayout.EAST, serverLabel);
		layout.putConstraint(SpringLayout.BASELINE, mySQLSchemaTxt, 0, SpringLayout.BASELINE, mySQLSchemaLabel);
		layout.putConstraint(SpringLayout.EAST, mySQLSchemaTxt, 0, SpringLayout.EAST, serverTxt);
		this.add(mySQLSchemaTxt);

		layout.putConstraint(SpringLayout.NORTH, supplierJsonUrlLabel, 20, SpringLayout.SOUTH, mySQLSchemaLabel);
		layout.putConstraint(SpringLayout.WEST, supplierJsonUrlLabel, 0, SpringLayout.WEST, serverLabel);
		layout.putConstraint(SpringLayout.EAST, supplierJsonUrlLabel, 0, SpringLayout.EAST, serverLabel);
		this.add(supplierJsonUrlLabel);

		layout.putConstraint(SpringLayout.WEST, supplierJsonUrlTxt, 10, SpringLayout.EAST, serverLabel);
		layout.putConstraint(SpringLayout.BASELINE, supplierJsonUrlTxt, 0, SpringLayout.BASELINE, supplierJsonUrlLabel);
		layout.putConstraint(SpringLayout.EAST, supplierJsonUrlTxt, 0, SpringLayout.EAST, serverTxt);
		this.add(supplierJsonUrlTxt);

		layout.putConstraint(SpringLayout.WEST, loginButton, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, loginButton, -10, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, loginButton, -10, SpringLayout.EAST, this);
		this.add(loginButton);

		layout.putConstraint(SpringLayout.WEST, saveButton, 0, SpringLayout.WEST, loginButton);
		layout.putConstraint(SpringLayout.SOUTH, saveButton, -5, SpringLayout.NORTH, loginButton);
		layout.putConstraint(SpringLayout.EAST, saveButton, 0, SpringLayout.EAST, loginButton);
		this.add(saveButton);
	}

	private void saveLoginPresets() {
		Properties loginPresets = new Properties();
		loginPresets.put("swServer", serverTxt.getText());
		loginPresets.put("swUsername", usernameTxt.getText());
		loginPresets.put("swApiKey", new String(apiKeyTxt.getPassword()));
		loginPresets.put("ftpServer", ftpServerTxt.getText());
		loginPresets.put("ftpUsername", ftpUsernameTxt.getText());
		loginPresets.put("ftpPassword", new String(ftpPasswordTxt.getPassword()));
		loginPresets.put("mySqlServer", mySQLServerTxt.getText());
		loginPresets.put("mySqlUsername", mySQLUsernameTxt.getText());
		loginPresets.put("mySqlPassword", new String(mySQLPasswordTxt.getPassword()));
		loginPresets.put("mySqlSchema", mySQLSchemaTxt.getText());
		loginPresets.put("supplierJsonUrl", supplierJsonUrlTxt.getText());
		c.setLoginPresets(loginPresets);
		c.saveLoginPresets();
	}

	private void login() {
		LoginInformation i = new LoginInformation();
		i.swServer = serverTxt.getText();
		i.swUsername = usernameTxt.getText();
		i.swApiKey = new String(apiKeyTxt.getPassword());
		i.ftpServer = ftpServerTxt.getText();
		i.ftpUsername = ftpUsernameTxt.getText();
		i.ftpPassword = new String(ftpPasswordTxt.getPassword());
		i.mySqlServer = mySQLServerTxt.getText();
		i.mySqlUsername = mySQLUsernameTxt.getText();
		i.mySqlPassword = new String(mySQLPasswordTxt.getPassword());
		i.mySqlSchema = mySQLSchemaTxt.getText();
		i.supplierJsonUrl = supplierJsonUrlTxt.getText();

		c.login(i);
	}

	public class LoginInformation {
		public String swServer;
		public String swUsername;
		public String swApiKey;
		public String ftpServer;
		public String ftpUsername;
		public String ftpPassword;
		public String mySqlServer;
		public String mySqlUsername;
		public String mySqlPassword;
		public String mySqlSchema;
		public String supplierJsonUrl;
	}

}
