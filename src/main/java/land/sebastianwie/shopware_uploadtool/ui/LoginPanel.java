package land.sebastianwie.shopware_uploadtool.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class LoginPanel extends JPanel {

	private static final long serialVersionUID = 1229132149451942443L;
	@SuppressWarnings("unused")
	private MainApplication parent;
	private JTextField hostField;
	private JTextField userField;
	private JTextField keyField;
	private JTextField ftpHostField;
	private JTextField ftpUserField;
	private JTextField ftpPasswordField;
	private JLabel lblFtppasswort;
	private JTextField dbHostField;
	private JTextField dbUserField;
	private JTextField dbPasswordField;
	private JTextField dbDBField;

	public LoginPanel(MainApplication parent, String hostname, String username, String key, String ftphost,
			String ftpuser, String ftppassword, String dbhost, String dbuser, String dbpassword, String dbdatabase) {
		this.parent = parent;
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(100, 100, 440, 280);
		add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 160, 160 };
		gbl_panel.rowHeights = new int[] { 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24 };
		gbl_panel.columnWeights = new double[] { 1.0, 1.0 };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(gbl_panel);

		JLabel lblHost = new JLabel("Server");
		GridBagConstraints gbc_lblHost = new GridBagConstraints();
		gbc_lblHost.anchor = GridBagConstraints.EAST;
		gbc_lblHost.fill = GridBagConstraints.VERTICAL;
		gbc_lblHost.insets = new Insets(0, 0, 5, 5);
		gbc_lblHost.gridx = 0;
		gbc_lblHost.gridy = 0;
		panel.add(lblHost, gbc_lblHost);

		hostField = new JTextField(hostname);
		lblHost.setLabelFor(hostField);
		GridBagConstraints gbc_hostField = new GridBagConstraints();
		gbc_hostField.insets = new Insets(0, 0, 5, 5);
		gbc_hostField.fill = GridBagConstraints.HORIZONTAL;
		gbc_hostField.gridx = 1;
		gbc_hostField.gridy = 0;
		panel.add(hostField, gbc_hostField);
		hostField.setColumns(50);

		JLabel lblBenutzername = new JLabel("Benutzername");
		GridBagConstraints gbc_lblBenutzername = new GridBagConstraints();
		gbc_lblBenutzername.anchor = GridBagConstraints.EAST;
		gbc_lblBenutzername.fill = GridBagConstraints.VERTICAL;
		gbc_lblBenutzername.insets = new Insets(0, 0, 5, 5);
		gbc_lblBenutzername.gridx = 0;
		gbc_lblBenutzername.gridy = 1;
		panel.add(lblBenutzername, gbc_lblBenutzername);

		userField = new JTextField(username);
		lblBenutzername.setLabelFor(userField);
		GridBagConstraints gbc_userField = new GridBagConstraints();
		gbc_userField.insets = new Insets(0, 0, 5, 5);
		gbc_userField.fill = GridBagConstraints.HORIZONTAL;
		gbc_userField.gridx = 1;
		gbc_userField.gridy = 1;
		panel.add(userField, gbc_userField);
		userField.setColumns(50);

		JLabel lblApischlssel = new JLabel("Api-Schlüssel");
		GridBagConstraints gbc_lblApischlssel = new GridBagConstraints();
		gbc_lblApischlssel.anchor = GridBagConstraints.EAST;
		gbc_lblApischlssel.insets = new Insets(0, 0, 5, 5);
		gbc_lblApischlssel.fill = GridBagConstraints.VERTICAL;
		gbc_lblApischlssel.gridx = 0;
		gbc_lblApischlssel.gridy = 2;
		panel.add(lblApischlssel, gbc_lblApischlssel);

		keyField = new JTextField(key);
		lblApischlssel.setLabelFor(keyField);
		GridBagConstraints gbc_keyField = new GridBagConstraints();
		gbc_keyField.insets = new Insets(0, 0, 5, 5);
		gbc_keyField.fill = GridBagConstraints.HORIZONTAL;
		gbc_keyField.gridx = 1;
		gbc_keyField.gridy = 2;
		panel.add(keyField, gbc_keyField);
		keyField.setColumns(50);

		JLabel lblFtpserver = new JLabel("FTP-Server");
		GridBagConstraints gbc_lblFtpserver = new GridBagConstraints();
		gbc_lblFtpserver.anchor = GridBagConstraints.EAST;
		gbc_lblFtpserver.insets = new Insets(0, 0, 5, 5);
		gbc_lblFtpserver.gridx = 0;
		gbc_lblFtpserver.gridy = 3;
		panel.add(lblFtpserver, gbc_lblFtpserver);

		ftpHostField = new JTextField(ftphost);
		GridBagConstraints gbc_ftpHostField = new GridBagConstraints();
		gbc_ftpHostField.insets = new Insets(0, 0, 5, 5);
		gbc_ftpHostField.fill = GridBagConstraints.HORIZONTAL;
		gbc_ftpHostField.gridx = 1;
		gbc_ftpHostField.gridy = 3;
		ftpHostField.setEnabled(false);
		panel.add(ftpHostField, gbc_ftpHostField);
		ftpHostField.setColumns(10);

		JLabel lblFtpbenutzername = new JLabel("FTP-Benutzername");
		GridBagConstraints gbc_lblFtpbenutzername = new GridBagConstraints();
		gbc_lblFtpbenutzername.anchor = GridBagConstraints.EAST;
		gbc_lblFtpbenutzername.insets = new Insets(0, 0, 5, 5);
		gbc_lblFtpbenutzername.gridx = 0;
		gbc_lblFtpbenutzername.gridy = 4;
		panel.add(lblFtpbenutzername, gbc_lblFtpbenutzername);

		ftpUserField = new JTextField(ftpuser);
		GridBagConstraints gbc_ftpUserField = new GridBagConstraints();
		gbc_ftpUserField.insets = new Insets(0, 0, 5, 5);
		gbc_ftpUserField.fill = GridBagConstraints.HORIZONTAL;
		gbc_ftpUserField.gridx = 1;
		gbc_ftpUserField.gridy = 4;
		ftpUserField.setEnabled(false);
		panel.add(ftpUserField, gbc_ftpUserField);
		ftpUserField.setColumns(10);

		lblFtppasswort = new JLabel("FTP-Passwort");
		GridBagConstraints gbc_lblFtppasswort = new GridBagConstraints();
		gbc_lblFtppasswort.insets = new Insets(0, 0, 5, 5);
		gbc_lblFtppasswort.anchor = GridBagConstraints.EAST;
		gbc_lblFtppasswort.gridx = 0;
		gbc_lblFtppasswort.gridy = 5;
		panel.add(lblFtppasswort, gbc_lblFtppasswort);

		ftpPasswordField = new JTextField(ftppassword);
		GridBagConstraints gbc_ftpPasswordField = new GridBagConstraints();
		gbc_ftpPasswordField.insets = new Insets(0, 0, 5, 5);
		gbc_ftpPasswordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_ftpPasswordField.gridx = 1;
		gbc_ftpPasswordField.gridy = 5;
		panel.add(ftpPasswordField, gbc_ftpPasswordField);
		ftpPasswordField.setColumns(10);
		ftpPasswordField.setEnabled(false);

		JLabel lblMysqlhost = new JLabel("MySQL-Server");
		GridBagConstraints gbc_lblMysqlhost = new GridBagConstraints();
		gbc_lblMysqlhost.anchor = GridBagConstraints.EAST;
		gbc_lblMysqlhost.insets = new Insets(0, 0, 5, 5);
		gbc_lblMysqlhost.gridx = 0;
		gbc_lblMysqlhost.gridy = 6;
		panel.add(lblMysqlhost, gbc_lblMysqlhost);

		dbHostField = new JTextField(dbhost);
		GridBagConstraints gbc_dbHostField = new GridBagConstraints();
		gbc_dbHostField.insets = new Insets(0, 0, 5, 5);
		gbc_dbHostField.fill = GridBagConstraints.HORIZONTAL;
		gbc_dbHostField.gridx = 1;
		gbc_dbHostField.gridy = 6;
		panel.add(dbHostField, gbc_dbHostField);
		dbHostField.setColumns(10);

		JLabel lblMysqlnutzername = new JLabel("MySQL-Nutzername");
		GridBagConstraints gbc_lblMysqlnutzername = new GridBagConstraints();
		gbc_lblMysqlnutzername.anchor = GridBagConstraints.EAST;
		gbc_lblMysqlnutzername.insets = new Insets(0, 0, 5, 5);
		gbc_lblMysqlnutzername.gridx = 0;
		gbc_lblMysqlnutzername.gridy = 7;
		panel.add(lblMysqlnutzername, gbc_lblMysqlnutzername);

		dbUserField = new JTextField(dbuser);
		GridBagConstraints gbc_dbUserField = new GridBagConstraints();
		gbc_dbUserField.insets = new Insets(0, 0, 5, 5);
		gbc_dbUserField.fill = GridBagConstraints.HORIZONTAL;
		gbc_dbUserField.gridx = 1;
		gbc_dbUserField.gridy = 7;
		panel.add(dbUserField, gbc_dbUserField);
		dbUserField.setColumns(10);

		JLabel lblMysqlpasswort = new JLabel("MySQL-Passwort");
		GridBagConstraints gbc_lblMysqlpasswort = new GridBagConstraints();
		gbc_lblMysqlpasswort.anchor = GridBagConstraints.EAST;
		gbc_lblMysqlpasswort.insets = new Insets(0, 0, 5, 5);
		gbc_lblMysqlpasswort.gridx = 0;
		gbc_lblMysqlpasswort.gridy = 8;
		panel.add(lblMysqlpasswort, gbc_lblMysqlpasswort);

		dbPasswordField = new JTextField(dbpassword);
		GridBagConstraints gbc_dbPasswordField = new GridBagConstraints();
		gbc_dbPasswordField.insets = new Insets(0, 0, 5, 5);
		gbc_dbPasswordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_dbPasswordField.gridx = 1;
		gbc_dbPasswordField.gridy = 8;
		panel.add(dbPasswordField, gbc_dbPasswordField);
		dbPasswordField.setColumns(10);

		JLabel lblDatenbank = new JLabel("Datenbank");
		GridBagConstraints gbc_lblDatenbank = new GridBagConstraints();
		gbc_lblDatenbank.anchor = GridBagConstraints.EAST;
		gbc_lblDatenbank.insets = new Insets(0, 0, 5, 5);
		gbc_lblDatenbank.gridx = 0;
		gbc_lblDatenbank.gridy = 9;
		panel.add(lblDatenbank, gbc_lblDatenbank);

		dbDBField = new JTextField(dbdatabase);
		GridBagConstraints gbc_dbDBField = new GridBagConstraints();
		gbc_dbDBField.insets = new Insets(0, 0, 5, 5);
		gbc_dbDBField.fill = GridBagConstraints.HORIZONTAL;
		gbc_dbDBField.gridx = 1;
		gbc_dbDBField.gridy = 9;
		panel.add(dbDBField, gbc_dbDBField);
		dbDBField.setColumns(10);

		JButton btnLogin = new JButton("Bestätigen");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.login(hostField.getText().trim(), userField.getText().trim(), keyField.getText().trim(),
						dbHostField.getText().trim(), dbUserField.getText().trim(), dbPasswordField.getText().trim(),
						dbDBField.getText().trim());
			}
		});
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLogin.gridwidth = 3;
		gbc_btnLogin.gridx = 0;
		gbc_btnLogin.gridy = 10;
		panel.add(btnLogin, gbc_btnLogin);

		JLabel lblSebastianWieland = new JLabel("2014, Sebastian Wieland");
		lblSebastianWieland.setBounds(240, 453, 180, 15);
		add(lblSebastianWieland);
	}
}
