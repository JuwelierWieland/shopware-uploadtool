package land.sebastianwie.shopware_uploadtool.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import land.sebastianwie.shopware_uploadtool.resources.article.Article;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticleVariant;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Der {@link ApiConnector} stellt die Br&uuml;cke zwischen {@link JSONObject}s
 * und Java-Objekten dar.
 * 
 * @author Sebastian Wieland
 *
 */
public class ApiConnector {
	// private static final String hostname =
	// "shopware.p223131.webspaceconfig.de";
	// private static final String hostname =
	// "shopware.p234623.webspaceconfig.de";
	// private static final String username = "wieland";
	// private static final String password =
	// "seZ45bdcRF023J4GHVftogFAL6F909XtcrJLMc0l";

	private static final List<NameValuePair> use_number = Arrays.asList(new BasicNameValuePair[] { new BasicNameValuePair("useNumberAsId",
			"true") });

	private static final List<NameValuePair> consider_tax = Arrays.asList(new BasicNameValuePair[] { new BasicNameValuePair(
			"considerTaxInput", "true") });

	private static final List<NameValuePair> number_and_tax = Arrays.asList(new BasicNameValuePair[] {
			new BasicNameValuePair("considerTaxInput", "true"), new BasicNameValuePair("useNumberAsId", "true") });

	private ApiClient ac;

	/**
	 * Initialisiert den {@link ApiClient}
	 */
	public ApiConnector(String hostname, String username, String password) {
		ac = new ApiClient(hostname, username, password);
	}

	/**
	 * F&uuml;gt einen neuen Artikel hinzu.
	 * 
	 * @param article
	 *            Der neue Artikel. Ein Artikel besteht aus mindestens einem
	 *            Namen, einer Nummer und einer TaxId (Standardm&auml;&szlig;ig
	 *            1).
	 * @return Bei Erfolg: Die Shopware ID des Artikels. Sonst: -1
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public ArticleUploadStatus addArticle(Article article) throws ClientProtocolException, IOException {
		JSONObject result = ac.post("articles", article.toJSONObject());
		if (!result.getBoolean("success")) {
			String errorMessage = result.getString("message");
			if (result.has("errors") && !result.isNull("errors"))
				errorMessage += " - " + result.getJSONArray("errors").toString(1);
			return new ArticleUploadStatus(-1, errorMessage);
		}
		return new ArticleUploadStatus(result.getJSONObject("data").getInt("id"), "Upload successful");
	}

	public ArticleUploadStatus addVariant(ArticleVariant variant) throws ClientProtocolException, IOException {
		JSONObject result = ac.post("variants", variant.toJSONObject());
		if (!result.getBoolean("success"))
			return new ArticleUploadStatus(-1, result.getString("message"));
		return new ArticleUploadStatus(result.getJSONObject("data").getInt("id"), "Upload successufl");
	}

	/**
	 * Aktualisiert einen Artikel.
	 * 
	 * @param id
	 *            Die Shopware-ID des Artikels, der aktualisiert werden soll.
	 * @param article
	 *            Die &Auml;nderungen, die am Artikel vorgenommen werden.
	 * @return null, falls die &Auml;nderung erfolgreich war, sonst die
	 *         Fehlermeldung.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public ArticleUploadStatus updateArticle(int id, Article article) throws ClientProtocolException, IOException {
		JSONObject result = ac.put("articles/" + id, article.toJSONObject());
		if (result.getBoolean("success"))
			return new ArticleUploadStatus(result.getJSONObject("data").getInt("id"), "Update successful");
		else
			return new ArticleUploadStatus(-1, result.getString("message"));
	}

	/**
	 * Aktualisiert einen Artikel.
	 * 
	 * @param number
	 *            Die Artikelnummer des Artikels, der aktualisiert werden soll.
	 * @param article
	 *            Die &Auml;nderungen, die am Artikel vorgenommen werden.
	 * @return null, falls die &Auml;nderung erfolgreich war, sonst die
	 *         Fehlermeldung.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public ArticleUploadStatus updateArticle(String number, Article article) throws ClientProtocolException, IOException {
		JSONObject result = ac.put("articles/" + number, article.toJSONObject(), use_number);
		if (result.getBoolean("success"))
			return new ArticleUploadStatus(result.getJSONObject("data").getInt("id"), "Update successful");
		else
			return new ArticleUploadStatus(-1, result.getString("message"));
	}

	public ArticleUploadStatus updateVariant(int id, ArticleVariant variant) throws ClientProtocolException, IOException {
		JSONObject result = ac.put("variants/" + id, variant.toJSONObject());
		if (result.getBoolean("success"))
			return new ArticleUploadStatus(result.getJSONObject("data").getInt("id"), "Update successful");
		else
			return new ArticleUploadStatus(-1, result.getString("message"));
	}

	public ArticleUploadStatus updateVariant(String number, ArticleVariant variant) throws ClientProtocolException, IOException {
		JSONObject result = ac.put("variants/" + number, variant.toJSONObject(), use_number);
		if (result.getBoolean("success"))
			return new ArticleUploadStatus(result.getJSONObject("data").getInt("id"), "Update successful");
		else
			return new ArticleUploadStatus(-1, result.getString("message"));
	}

	/**
	 * L&ouml;scht einen Artikel.
	 * 
	 * @param id
	 *            Die Shopware-ID des Artikels, der gel&ouml;scht werden soll.
	 * @return null, falls das L&ouml;schen erfolgreich war, sonst diei
	 *         Fehlermeldung.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public String deleteArticle(int id) throws ClientProtocolException, IOException {
		JSONObject result = ac.delete("articles/" + id);
		if (result.getBoolean("success"))
			return null;
		else
			return result.getString("message");
	}

	/**
	 * L&ouml;scht einen Artikel.
	 * 
	 * @param number
	 *            Die Artikelnummer des Artikels, der gel&ouml;scht werden soll.
	 * @return null, falls das L&ouml;schen erfolgreich war, sonst diei
	 *         Fehlermeldung.
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @deprecated Da Shopware nicht das L&ouml;schen &uuml;ber die
	 *             Artikelnummer unterst&uuml;tzt, werden hierf&uuml;r zwei
	 *             Requests ausgef&uuml;hrt: Einer, um die ID herauszufinden und
	 *             einer, um eben diesen Artikel zu l&ouml;schen.
	 */
	@Deprecated
	public String deleteArticle(String number) throws ClientProtocolException, IOException {
		// JSONObject result = ac.delete("articles/" + number, use_number);
		// Shopware unterstützt derzeit kein Löschen nach Nummer, deshalb hier
		// eine hässliche Lösung
		Article toDelete = getArticle(number);
		if (toDelete == null)
			return "Unbekannter Fehler";
		return deleteArticle(toDelete.getId());
	}

	public String deleteVariant(int id) throws ClientProtocolException, IOException {
		JSONObject result = ac.delete("variants/" + id);
		if (result.getBoolean("success"))
			return null;
		else
			return result.getString("message");
	}

	public String deleteVariant(String number) throws ClientProtocolException, IOException {
		JSONObject result = ac.delete("variants/" + number, use_number);
		if (result.getBoolean("success"))
			return null;
		else
			return result.getString("message");
	}

	/**
	 * Fragt die Informationen eines Artikels ab.
	 * 
	 * @param id
	 *            Die Shopware-ID des Artikels.
	 * @return Ein {@link Article}-Objekt bei Erfolg, sonst null.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public Article getArticle(int id) throws ClientProtocolException, IOException {
		JSONObject result = ac.get("articles/" + id, consider_tax);
		if (!result.getBoolean("success"))
			return null;
		return Article.createFromJSONObject(result.getJSONObject("data"));
	}

	/**
	 * Fragt die Informationen eines Artikels ab.
	 * 
	 * @param number
	 *            Die Artikelnummer des Artikels.
	 * @return Ein {@link Article}-Objekt bei Erfolg, sonst null.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public Article getArticle(String number) throws ClientProtocolException, IOException {
		JSONObject result = ac.get("articles/" + number, number_and_tax);
		if (!result.getBoolean("success"))
			return null;
		return Article.createFromJSONObject(result.getJSONObject("data"));
	}

	public int getArticleCount() throws ClientProtocolException, IOException {
		List<NameValuePair> parameters = Arrays.asList(new BasicNameValuePair[] { new BasicNameValuePair("start", Integer.toString(0)),
				new BasicNameValuePair("limit", Integer.toString(0)) });

		JSONObject result = ac.get("articles", parameters);
		return result.getInt("total");
	}

	public Article[] getAllArticles() throws ClientProtocolException, IOException {
		int articleCount = getArticleCount();
		int requests = (articleCount / 1000) + 1;
		Article[][] array = new Article[requests][];
		for (int i = 0; i < requests; i++) {
			array[i] = getArticles(i * 1000, 1000);
		}

		List<Article> result = new LinkedList<>();
		for (Article[] part : array) {
			for (Article a : part) {
				result.add(a);
			}
		}
		return result.toArray(new Article[result.size()]);
	}

	/**
	 * Fragt die Informationen vieler Artikel ab.
	 * 
	 * @param start
	 *            Der erste Artikel
	 * @param limit
	 *            Die Anzahl der Artikel
	 * @return Ein Array mit allen Artikeln
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public Article[] getArticles(int start, int limit) throws ClientProtocolException, IOException {
		List<NameValuePair> parameters = Arrays.asList(new BasicNameValuePair[] { new BasicNameValuePair("start", Integer.toString(start)),
				new BasicNameValuePair("limit", Integer.toString(limit)) });
		JSONObject result = ac.get("articles", parameters);
		if (!result.getBoolean("success"))
			return null;
		JSONArray articles = result.getJSONArray("data");
		Article[] output = new Article[articles.length()];
		for (int i = 0; i < output.length; i++) {
			output[i] = Article.createFromJSONObject(articles.getJSONObject(i));
		}
		return output;
	}

}
