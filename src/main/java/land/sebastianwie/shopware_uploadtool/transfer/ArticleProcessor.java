package land.sebastianwie.shopware_uploadtool.transfer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.HashSet;
import java.util.List;
import java.util.Map;
//import java.util.Set;






import javax.swing.JOptionPane;

import land.sebastianwie.shopware_uploadtool.api.ApiConnector;
import land.sebastianwie.shopware_uploadtool.api.ArticleUploadStatus;
import land.sebastianwie.shopware_uploadtool.db.MysqlConnector;
import land.sebastianwie.shopware_uploadtool.excel.article.ExcelCreator;
import land.sebastianwie.shopware_uploadtool.excel.article.ExcelReader;
import land.sebastianwie.shopware_uploadtool.resources.article.Article;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticleCategory;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticleImage;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticleProperty;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticleVariant;
import land.sebastianwie.shopware_uploadtool.ui.MainApplication;
import land.sebastianwie.shopware_uploadtool.uiNew.LoginPanel.LoginInformation;

import org.apache.http.client.ClientProtocolException;

public class ArticleProcessor extends Thread {
	private ApiConnector ac;
	private ExcelReader reader;
	private ExcelCreator errorCreator;
	private ProcessStatus status;
	private ProcessMode action;

	private String dbhostname;
	private String dbusername;
	private String dbpassword;
	private String dbdatabase;

	private static Map<Integer, ArticleProperty> propertyIdMap;
	private static Map<Integer, ArticleCategory> categoryIdMap;
	private static Map<Integer, ArticleImage> imageIdMap;

	public enum ProcessMode {
		UPLOAD("Upload"), UPDATE("Update"), DELETE("Löschen"), DOWNLOAD("Herunterladen"), DOWNLOAD_ALL("Herunterladen");
		private final String label;

		ProcessMode(String label) {
			this.label = label;
		}

		public String toString() {
			return label;
		}
	}

	public ArticleProcessor(ApiConnector ac, ExcelReader reader, ExcelCreator errorCreator, ProcessMode action,
			ProcessStatus status) {
		this(ac, reader, errorCreator, action, status, null);
		this.dbhostname = MainApplication.dbhostname;
		this.dbusername = MainApplication.dbusername;
		this.dbpassword = MainApplication.dbpassword;
		this.dbdatabase = MainApplication.dbdatabase;
	}

	public ArticleProcessor(ApiConnector ac, ExcelReader reader, ExcelCreator errorCreator, ProcessMode action,
			ProcessStatus status, LoginInformation login) {
		if ((ac == null || reader == null || errorCreator == null || action == null || status == null)
				&& action != ProcessMode.DOWNLOAD_ALL)
			throw new IllegalArgumentException();
		else if (ac == null || errorCreator == null || action == null || status == null)
			throw new IllegalArgumentException();
		this.ac = ac;
		this.reader = reader;
		this.errorCreator = errorCreator;
		this.status = status;
		this.action = action;
		if (login != null) {
			this.dbhostname = login.mySqlServer;
			this.dbusername = login.mySqlUsername;
			this.dbpassword = login.mySqlPassword;
			this.dbdatabase = login.mySqlSchema;
		}
		if (propertyIdMap == null)
			propertyIdMap = new HashMap<>();
		if (categoryIdMap == null)
			categoryIdMap = new HashMap<>();
		if (imageIdMap == null)
			imageIdMap = new HashMap<>();
	}

	public void getInformationFromDb(MysqlConnector mc, Article a) throws ClassNotFoundException, SQLException {
		if (a.getPropertyValues() != null) {
			List<ArticleProperty> convertedProperties = new ArrayList<>();
			for (ArticleProperty ap : a.getPropertyValues()) {
				if (!ap.usesId())
					convertedProperties.add(ap);
				else if (propertyIdMap.containsKey(ap.getId())) {
					convertedProperties.add(propertyIdMap.get(ap.getId()));
				} else {
					try {
						int id = ap.getId();
						ap = mc.getPropertyById(id);
						propertyIdMap.put(id, ap);
						// propertyIdMap.put(ap.getId(), mc.getPropertyById(ap.getId()));
					} finally {
						convertedProperties.add(ap);
					}
				}
			}
			a.setPropertyValues(convertedProperties);
		}

		if (a.getId() != -1)
			a.setCustomProductId(mc.getCustomProductId(a.getId()));

		/*
		 * if (a.getCategories() != null) { Set<ArticleCategory> convertedCategories = new HashSet<>(); for
		 * (ArticleCategory ac : a.getCategories()) { if (categoryIdMap.containsKey(ac.getId())) {
		 * convertedCategories.add(categoryIdMap.get(ac.getId())); } else { try { int id = ac.getId(); ac = new
		 * ArticleCategory(id, mc.getCategoryNameById(id)); categoryIdMap.put(id, ac); } finally {
		 * convertedCategories.add(ac); } } } a.setCategories(convertedCategories); }
		 */

		/*
		 * if (a.getImages() != null) { List<ArticleImage> convertedImages = new ArrayList<>(); for (ArticleImage ai :
		 * a.getImages()) { if (!ai.usesId()) { convertedImages.add(ai); } else if
		 * (imageIdMap.containsKey(ai.getMediaId())) { convertedImages.add(imageIdMap.get(ai.getMediaId())); } else {
		 * try { int id = ai.getMediaId(); ai = new ArticleImage(id, mc.getImageNameByMediaId(id), ai.isMainImage());
		 * imageIdMap.put(id, ai); } finally { convertedImages.add(ai); } } } a.setImages(convertedImages); }
		 */
	}

	public void downloadArticles() {
		MysqlConnector mc;
		try {
			mc = new MysqlConnector(dbhostname, dbusername, dbpassword, dbdatabase);
		} catch (ClassNotFoundException | SQLException e1) {
			JOptionPane.showMessageDialog(null, "Fehler beim Aufbauen der MySQL-Verbindung");
			return;
		}
		for (Article a : reader.articles()) {
			if (this.status.isCancelled())
				break;
			Article info = a;
			String message = null;
			try {
				if (a.getPseudoId() != -1)
					info = ac.getArticle(a.getPseudoId());
				else if (a.getNumber() != null && a.getNumber().length() > 0)
					info = ac.getArticle(a.getNumber());
				else
					message = "Ungültige ID oder Artikelnummer";
			} catch (ClientProtocolException e) {
				message = "ClientProtocolException: " + e.getMessage();
			} catch (IOException e) {
				message = "IOException: " + e.getMessage();
			} catch (Exception e) {
				message = "Unknown Exception: " + e.getMessage();
			}
			if (info == null) {
				message = "Artikel nicht gefunden";
				this.status.incErrors();
				info = a;
			} else if (message == null) {
				message = "Erfolgreich";
			} else
				this.status.incErrors();

			try {
				getInformationFromDb(mc, info);
			} catch (ClassNotFoundException | SQLException e) {
				message = "Erfolgreich - Details von der Datenbank konnten nicht geladen werden";
			}

			errorCreator.addArticle(info, message);
			this.status.incDone();
		}
		try {
			mc.closeConnection();
		} catch (SQLException e) {
		}
	}

	public void downloadAllArticles() {
		MysqlConnector mc;
		try {
			Article[] articles = ac.getAllArticles();
			this.status.setTodo(articles.length);
			mc = new MysqlConnector(dbhostname, dbusername, dbpassword, dbdatabase);
			for (int i = 0; i < articles.length; i++) {
				if (this.status.isCancelled())
					break;
				String message = null;
				try {
					articles[i] = ac.getArticle(articles[i].getId());
				} catch (ClientProtocolException e) {
					message = "ClientProtocolException: " + e.getMessage();
				} catch (IOException e) {
					message = "IOException: " + e.getMessage();
				} catch (Exception e) {
					message = "Unknown Exception: " + e.getMessage();
				}
				if (message == null)
					message = "Erfolgreich";
				else
					this.status.incErrors();

				try {
					getInformationFromDb(mc, articles[i]);
				} catch (ClassNotFoundException | SQLException e) {
					message = "Erfolgreich - Details von der Datenbank konnten nicht geladen werden";
				}

				errorCreator.addArticle(articles[i], message);

				this.status.incDone();
			}
		} catch (ClassNotFoundException | SQLException e) {
			JOptionPane.showMessageDialog(null, "Fehler beim Aufbauen der Datenbankverbindung");
			return;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fehler beim Herunterladen der Artikelliste");
			return;
		}
		try {
			mc.closeConnection();
		} catch (SQLException e) {
		}
	}

	public void updateArticles() {
		MysqlConnector mc;
		try {
			mc = new MysqlConnector(dbhostname, dbusername, dbpassword, dbdatabase);
		} catch (ClassNotFoundException | SQLException e1) {
			JOptionPane.showMessageDialog(null, "Fehler beim Aufbauen der MySQL-Verbindung");
			return;
		}
		for (Article a : reader.articles()) {
			if (this.status.isCancelled())
				break;
			ArticleUploadStatus aus = null;
			try {
				if (a.getPseudoId() != -1)
					aus = ac.updateArticle(a.getPseudoId(), a);
				else if (a.getNumber() != null && a.getNumber().length() > 0)
					aus = ac.updateArticle(a.getNumber(), a);
				else
					errorCreator.addArticle(a, "Ungültige ID oder Artikelnummer");

				if (aus == null || aus.getId() == -1) {
					errorCreator.addArticle(a, aus.getMessage());
					this.status.incErrors();
				} else if (a.getCustomProductId() == 0) {
					mc.deleteCustomProduct(aus.getId());
				} else if (a.getCustomProductId() != -1) {
					mc.setCustomProduct(aus.getId(), a.getCustomProductId());
				}
			} catch (ClientProtocolException e) {
				errorCreator.addArticle(a, "ClientProtocolException: " + e.getMessage());
				this.status.incErrors();
			} catch (IOException e) {
				errorCreator.addArticle(a, "IOException: " + e.getMessage());
				this.status.incErrors();
			} catch (SQLException e) {
				errorCreator.addArticle(a, "SQLException: " + e.getMessage());
				this.status.incErrors();
			} catch (Exception e) {
				errorCreator.addArticle(a, "Unknown Exception: " + e.getMessage());
				this.status.incErrors();
			}
			this.status.incDone();
		}

		for (ArticleVariant a : reader.variants()) {
			if (this.status.isCancelled())
				break;
			ArticleUploadStatus aus = null;
			try {
				if (a.getDetail().getPseudoId() != -1)
					aus = ac.updateVariant(a.getDetail().getPseudoId(), a);
				else if (a.getDetail().getNumber() != null && a.getDetail().getNumber().length() > 0)
					aus = ac.updateVariant(a.getDetail().getNumber(), a);
				else
					errorCreator.addVariant(a, "Ungültige ID oder Artikelnummer");

				if (aus == null || aus.getId() == -1) {
					errorCreator.addVariant(a, aus.getMessage());
					this.status.incErrors();
				}
			} catch (ClientProtocolException e) {
				errorCreator.addVariant(a, "ClientProtocolException: " + e.getMessage());
				this.status.incErrors();
			} catch (IOException e) {
				errorCreator.addVariant(a, "IOException: " + e.getMessage());
				this.status.incErrors();
			}
			this.status.incDone();

		}
		try {
			mc.closeConnection();
		} catch (SQLException e) {
		}
	}

	public void uploadArticles() {
		MysqlConnector mc;
		try {
			mc = new MysqlConnector(dbhostname, dbusername, dbpassword, dbdatabase);
		} catch (ClassNotFoundException | SQLException e1) {
			JOptionPane.showMessageDialog(null, "Fehler beim Aufbauen der MySQL-Verbindung");
			return;
		}
		Map<String, Integer> numberIdMap = new HashMap<>();
		for (Article a : reader.articles()) {
			if (this.status.isCancelled())
				break;
			try {
				ArticleUploadStatus aus = ac.addArticle(a);
				if (aus.getId() == -1) {
					errorCreator.addArticle(a, "Upload failed. " + aus.getMessage());
					this.status.incErrors();
				} else {
					if (a.getCustomProductId() != -1) {
						mc.setCustomProduct(aus.getId(), a.getCustomProductId());
					}
					numberIdMap.put(a.getNewNumber(), aus.getId());
				}
			} catch (ClientProtocolException e) {
				errorCreator.addArticle(a, "ClientProtocolException: " + e.getMessage());
				this.status.incErrors();
			} catch (IOException e) {
				errorCreator.addArticle(a, "IOException: " + e.getMessage());
				this.status.incErrors();
			} catch (SQLException e) {
				errorCreator.addArticle(a, "SQLException: " + e.getMessage());
				this.status.incErrors();
			} catch (Exception e) {
				errorCreator.addArticle(a, "Unknown Exception: " + e.getMessage());
				this.status.incErrors();
			}
			this.status.incDone();
		}

		for (ArticleVariant a : reader.variants()) {
			if (this.status.isCancelled())
				break;
			try {
				if (a.getMotherId() != -1) {
					ArticleUploadStatus aus = ac.addVariant(a);
					if (aus.getId() == -1) {
						errorCreator.addVariant(a, "Upload failed. " + aus.getMessage());
						this.status.incErrors();
					}
				} else if (numberIdMap.containsKey(a.getMotherNumber())) {
					a.setMotherId(numberIdMap.get(a.getMotherNumber()));
					ArticleUploadStatus aus = ac.addVariant(a);
					if (aus.getId() == -1) {
						errorCreator.addVariant(a, "Upload failed. " + aus.getMessage());
						this.status.incErrors();
					}
				} else {
					errorCreator.addVariant(a, "Kein Eltern-Artikel zu Variante vorhanden");
					this.status.incErrors();
				}
			} catch (ClientProtocolException e) {
				errorCreator.addVariant(a, "ClientProtocolException: " + e.getMessage());
				this.status.incErrors();
			} catch (IOException e) {
				errorCreator.addVariant(a, "IOException: " + e.getMessage());
				this.status.incErrors();
			} catch (Exception e) {
				errorCreator.addVariant(a, "Unknown Exception: " + e.getMessage());
				this.status.incErrors();
			}
			this.status.incDone();
		}
		try {
			mc.closeConnection();
		} catch (SQLException e) {
		}
	}

	@SuppressWarnings("deprecation")
	public void deleteArticles() {
		for (Article a : reader.articles()) {
			if (this.status.isCancelled())
				break;
			String message;
			try {
				if (a.getPseudoId() != -1)
					message = ac.deleteArticle(a.getPseudoId());
				else if (a.getNumber() != null && a.getNumber().length() > 0)
					message = ac.deleteArticle(a.getNumber());
				else
					message = "Ungültige ID oder Artikelnummer";

			} catch (ClientProtocolException e) {
				message = "ClientProtocolException: " + e.getMessage();
			} catch (IOException e) {
				message = "IOException: " + e.getMessage();
			} catch (Exception e) {
				message = "Unknown Exception: " + e.getMessage();
			}
			if (message != null) {
				errorCreator.addArticle(a, message);
				this.status.incErrors();
			}
			this.status.incDone();
		}
	}

	@Override
	public void run() {
		this.status.start();
		switch (action) {
		case UPLOAD:
			this.uploadArticles();
			break;
		case UPDATE:
			this.updateArticles();
			break;
		case DELETE:
			this.deleteArticles();
			break;
		case DOWNLOAD:
			this.downloadArticles();
			break;
		case DOWNLOAD_ALL:
			this.downloadAllArticles();
			break;
		}
	}

}