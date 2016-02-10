package land.sebastianwie.shopware_uploadtool.transfer;

import java.util.HashMap;
import java.util.Map;

import land.sebastianwie.shopware_uploadtool.api.ApiConnector;
import land.sebastianwie.shopware_uploadtool.excel.article.ExcelCreator;
import land.sebastianwie.shopware_uploadtool.resources.article.Article;
import land.sebastianwie.shopware_uploadtool.topm.SupplierMapper;
import land.sebastianwie.shopware_uploadtool.topm.TopMArticle;
import land.sebastianwie.shopware_uploadtool.topm.TopmArticleList;
import land.sebastianwie.shopware_uploadtool.uiNew.LoginPanel.LoginInformation;
import land.sebastianwie.shopware_uploadtool.util.DocumentUtils;

public class ArticleStockProcessor extends Thread {
	private ExcelCreator errorCreator;
	private ProcessStatus status;
	private TopmArticleList source;
	private LoginInformation loginInfo;

	private static final String SUPPLIERS_FILE = System.getProperty("user.home") + "/.shopwareUploadToolSuppliers.xml";

	public ArticleStockProcessor(ExcelCreator errorCreator, ProcessStatus status, TopmArticleList source,
			LoginInformation info) {
		this.errorCreator = errorCreator;
		this.status = status;
		this.source = source;
		this.loginInfo = info;
	}

	public void run() {
		try {
			this.status.start();
			ApiConnector ac = new ApiConnector(loginInfo.swServer, loginInfo.swUsername, loginInfo.swApiKey);
			SupplierMapper mapper = null;
			try {
				mapper = new SupplierMapper(DocumentUtils.fromURL(SUPPLIERS_FILE));
			} catch (Exception e) {
				errorCreator.addArticle(
						new Article(),
						"Fehler: Keine Supplier-Mapper Datei vorhanden (~/.shopwareUploadToolSuppliers.xml)\n"
								+ e.getMessage());
				this.status.setTodo(1);
				this.status.setErrors(1);
				this.status.setDone(1);
				return;
			}

			Article[] articles = ac.getAllArticles();
			status.setTodo(articles.length * 2);
			Map<String, Integer> articleMap = new HashMap<>();
			for (int i = 0; i < articles.length; i++) {
				if (this.status.isCancelled()) {
					this.status.setDone(0);
					return;
				}
				try {
					Article fullArticle = ac.getArticle(articles[i].getId());
					articleMap.put(fullArticle.getNumber(), fullArticle.getInStock());
					articles[i] = null;
				} catch (Exception e) {
					errorCreator.addArticle(articles[i], e + " - " + e.getMessage());
					this.status.incErrors();
				}
				this.status.incDone();
			}

			//status.setTodo(2);
			//Map<String, Article> articleMap = new HashMap<>();
			// articleMap.put("22-FS4813", ac.getArticle("22-FS4813"));

			for (TopMArticle topmarticle : source) {
				if (this.status.isCancelled())
					break;
				Article converted = topmarticle.toArticle(mapper);
				if (converted == null) {
					//this.status.incDone();
					continue;
				} else if (articleMap.get(converted.getNumber()) == null) {
					//this.status.incDone();
					//this.status.decTodo();
					continue;
				} else if (articleMap.get(converted.getNumber()).equals(converted.getInStock())) {
					this.status.incDone();
					continue;
				} else {
					try {
						ac.updateArticle(converted.getNumber(), converted);
					} catch (Exception e) {
						errorCreator.addArticle(converted, e + " - " + e.getMessage());
						this.status.incErrors();
					}
					this.status.incDone();
				}
			}

			this.status.setTodo(this.status.getDone());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
}