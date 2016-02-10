package land.sebastianwie.shopware_uploadtool.resources.article.images;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

public class FtpConnector {
	private final String hostname;
	private final String username;
	private final String password;

	private FTPClient ftp;

	public FtpConnector(String hostname, String username, String password, String startdir) {
		this.hostname = hostname;
		this.username = username;
		this.password = password;

		ftp = new FTPClient();
		FTPClientConfig config = new FTPClientConfig();
		ftp.configure(config);

		try {
			int reply;
			ftp.connect(this.hostname);
			System.out.println(ftp.getReplyString());
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return;
			}
			ftp.login(this.username, this.password);
			// System.out.println(Arrays.toString(ftp.listNames()));
			ftp.changeWorkingDirectory(startdir);
		} catch (IOException e) {
			if (ftp.isConnected())
				try {
					ftp.disconnect();
				} catch (IOException e1) {
				}
		}

	}

	public String currentDir() {
		try {
			return ftp.printWorkingDirectory();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean createDir(String dir) {
		try {
			return ftp.makeDirectory(dir);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean changeDir(String dir) {
		try {
			return ftp.changeWorkingDirectory(dir);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean removeDir(String dir) {
		try {
			return ftp.removeDirectory(dir);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String[] listFiles() {
		try {
			return ftp.listNames();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean upload(File file) throws FileNotFoundException {
		InputStream stream = new FileInputStream(file);
		try {
			return ftp.storeFile(file.getName(), stream);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteFile(String filename) {
		try {
			return ftp.deleteFile(filename);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void close() {
		if (ftp.isConnected())
			try {
				ftp.logout();
				ftp.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public static void main(String[] args) {
		FtpConnector connector = new FtpConnector("p234623.mittwaldserver.info", "p234623", "Ugasibep$118", "/html/shopware/uploads");
		System.out.println(connector.currentDir());
		System.out.println(Arrays.toString(connector.listFiles()));
		try {
			System.out.println(connector.upload(new File("text.xlsx")));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(connector.deleteFile("test.xlsx"));
		System.out.println(Arrays.toString(connector.listFiles()));
		connector.close();
	}
}
