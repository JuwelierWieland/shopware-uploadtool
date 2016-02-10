package land.sebastianwie.shopware_uploadtool.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import land.sebastianwie.shopware_uploadtool.resources.article.ArticleConfiguratorOption;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticleDetail;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticlePrice;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticleVariant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Diese Klasse stellt die Verbindung zur Shopware-API dar. Der Zugriff auf die API ist auf <a
 * href="http://wiki.shopware.de/Shopware-4-API_detail_861.html">http
 * ://wiki.shopware.de/Shopware-4-API_detail_861.html</a> beschrieben. Als Vorbild dient Die PHP-Klasse auf <a href=
 * "http://wiki.shopware.de/Shopware-4-REST-API-verwenden_detail_989.html"
 * >http://wiki.shopware.de/Shopware-4-REST-API-verwenden_detail_989.html</a>
 * 
 * @author Sebastian Wieland
 *
 */
public class ApiClient {
	/**
	 * Der Host, auf dem die API l&auml;uft.
	 */
	private HttpHost targetHost;
	/**
	 * Der Kontext, der jedem Request mitgesendet wird. In diesem werden auch die Zugangsdaten übertragen.
	 */
	private HttpClientContext context;
	/**
	 * Der Standard-HTTP-Client, der alle Requests ausf&uuml;hrt.
	 */
	private CloseableHttpClient httpclient;

	/**
	 * Der Pfad, in dem die API auf dem Server liegt. Auf Shopware 4 ist dies normalerweise "/api/"
	 */
	private static final String api_path = "/api/";
	/**
	 * Der Port, auf dem auf die API zugegriffen werden kann. Dieser ist normalerweise 80.
	 */
	private static final int port = 80;

	/**
	 * Der Konstruktor initialisiert Client und Kontext, welche sp&auml;ter f&uuml;r die Requests ben&ouml;tigt werden.
	 * Im Konstruktor selbst findet noch keine Kommunikation mit dem API-Server statt.
	 * 
	 * @see <a
	 *      href="http://hc.apache.org/httpcomponents-client-ga/tutorial/html/authentication.html#d5e706">http://hc.apache.org/httpcomponents-client-ga/tutorial/html/authentication.html#d5e706</a>
	 * 
	 * @param hostname
	 *            Der Hostname des API-Servers. In diesem werden nicht Protokoll, Port und Pfad angegeben.
	 * @param username
	 *            Der Benutzername des Shopware API-Benutzers
	 * @param password
	 *            Der API-Schl&uuml;ssel des API-Benutzers
	 */
	public ApiClient(String hostname, String username, String password) {
		httpclient = HttpClients.createDefault();

		targetHost = new HttpHost(hostname, port, "http");

		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
		CredentialsProvider crProvider = new BasicCredentialsProvider();
		crProvider.setCredentials(new AuthScope(targetHost), credentials);

		AuthCache authCache = new BasicAuthCache();
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(targetHost, basicAuth);

		context = HttpClientContext.create();
		context.setCredentialsProvider(crProvider);
		context.setAuthCache(authCache);
	}

	/**
	 * F&uuml;hrt einen zuvor erstellten {@link HttpRequest} aus. In dieser Methode kommuniziert der API-Client mit dem
	 * Server.
	 * 
	 * @param request
	 *            Der zuvor erstellte HTTP-Request
	 * @return Die Antwort des Servers, die nun ausgelesen werden kann.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private CloseableHttpResponse getResponse(HttpRequest request) throws ClientProtocolException, IOException {
		return httpclient.execute(targetHost, request, context);
	}

	/**
	 * Diese Methode ist nur dazu da, um den Code anderer Methoden ein wenig sch&ouml;ner zu halten, indem sie
	 * h&auml;ssliche try-catch Bl&ouml;cke von diesen anderen Methoden fern h&auml;lt.
	 * 
	 * @param response
	 *            Was die Methode macht, ist selbsterkl&auml;rend.
	 */
	private static void closeResponse(CloseableHttpResponse response) {
		try {
			response.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Liest den Inhalt einer {@link CloseableHttpResponse} aus.
	 * 
	 * @param response
	 *            Die zuvor erhaltene Antwort (Tipp: sie ist normalerweise nicht 42.)
	 * @return Ein String mit dem Inhalt der Seite, die man angefragt hat.
	 */
	private static String readResponse(CloseableHttpResponse response) {
		StringBuilder result = new StringBuilder();
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
				try {
					char[] buffer = new char[1024];
					int readChars = 0;
					while ((readChars = reader.read(buffer)) > 0)
						result.append(buffer, 0, readChars);
				} finally {
					instream.close();
					reader.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return result.toString();
	}

	/**
	 * Generiert eine API-Url
	 * 
	 * @param url
	 *            Die URL der Anfrage
	 * @param params
	 *            GET-Parameter, die der URL angehängt werden
	 * @return Die URL mit dem API-Pfad vorangestellt und den Parametern
	 */
	private static String generateUrl(String url, List<NameValuePair> params) {
		if (params == null)
			return api_path + url;

		String urlEncParams = URLEncodedUtils.format(params, "UTF-8");
		if (urlEncParams.isEmpty())
			return api_path + url;

		return api_path + url + "?" + urlEncParams;
	}

	/**
	 * F&uuml;gt das JSON-Objekt an den Request an
	 * 
	 * @param request
	 *            Der Request, der das JSON-Objekt erhalten soll
	 * @param data
	 *            Das JSON-Objekt
	 */
	private static void addJSONEntity(HttpEntityEnclosingRequest request, JSONObject data) {
		StringEntity entityData = new StringEntity(data.toString(), "UTF-8");
		request.addHeader("content-type", "application/json");
		request.setEntity(entityData);
	}

	/**
	 * F&uuml;hrt ein HttpRequest aus.
	 * 
	 * Falls die Rückgabe des Requests kein JSON-Objekt ist, wird diese als Fehler in einem neuen JSON-Objekt verpackt.
	 * @param request
	 *            Ein HttpRequest
	 * @return Das {@link JSONObject}, das als Antwort kommt.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private JSONObject request(HttpRequest request) throws ClientProtocolException, IOException {
		CloseableHttpResponse response = getResponse(request);
		String output = readResponse(response);
		closeResponse(response);
		JSONObject result;
		try {
			result = new JSONObject(output);
		} catch (JSONException e) {
			result = new JSONObject();
			result.put("success", false);
			result.put("message", output);
		}
		return result;
	}

	/**
	 * F&uuml;hrt einen GET-Request auf eine URL aus. Diesem Request k&ouml;nnen keine Parameter &uuml;bergeben werden.
	 * Der URL wird der konstant definierte api_path vorangef&uuml;gt.
	 * 
	 * @param url
	 *            Die Url, der api_path vorangestellt wird, auf die der Request ausgef&uuml;hrt wird.
	 * @return Das {@link JSONObject}, das als Antwort vom Server kommt.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public JSONObject get(String url) throws ClientProtocolException, IOException {
		return get(url, null);
	}

	/**
	 * F&uuml;hrt einen GET-Request auf eine URL aus. Diesem Request k&ouml;nnen keine Parameter &uuml;bergeben werden.
	 * Der URL wird der konstant definierte api_path vorangef&uuml;gt.
	 * 
	 * @param url
	 *            Die Url, der api_path vorangestellt wird, auf die der Request ausgef&uuml;hrt wird.
	 * @param params
	 *            Get-Parameter, die dem Request &uuml;bergeben werden k&ouml;nnen
	 * @return Das {@link JSONObject}, das als Antwort vom Server kommt.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public JSONObject get(String url, List<NameValuePair> params) throws ClientProtocolException, IOException {
		HttpGet httpget = new HttpGet(generateUrl(url, params));
		return request(httpget);
	}

	/**
	 * F&uuml;hrt einen Post-Request auf eine URL auf. Der URL wird der konstant definierte api_path vorangef&uuml;gt.
	 * 
	 * @param url
	 *            Die Url, der api_path vorangestellt wird, auf die der Request ausgef&uuml;hrt wird.
	 * @param data
	 *            Das {@link JSONObject}, das als Post-Variable &uuml;bergeben wird dem Request &uuml;bergeben werden
	 *            k&ouml;nnen.
	 * @return Das {@link JSONObject}, das als Antwort vom Server kommt.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public JSONObject post(String url, JSONObject data) throws ClientProtocolException, IOException {
		return post(url, data, null);
	}

	/**
	 * F&uuml;hrt einen Post-Request auf eine URL auf. Der URL wird der konstant definierte api_path vorangef&uuml;gt.
	 * 
	 * @param url
	 *            Die Url, der api_path vorangestellt wird, auf die der Request ausgef&uuml;hrt wird.
	 * @param data
	 *            Das {@link JSONObject}, das als Post-Variable &uuml;bergeben wird
	 * @param params
	 *            Get-Parameter, die dem Request &uuml;bergeben werden k&ouml;nnen.
	 * @return Das {@link JSONObject}, das als Antwort vom Server kommt.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public JSONObject post(String url, JSONObject data, List<NameValuePair> params) throws ClientProtocolException,
			IOException {
		HttpPost httppost = new HttpPost(generateUrl(url, params));
		addJSONEntity(httppost, data);
		return request(httppost);
	}

	/**
	 * F&uuml;hrt einen Put-Request auf eine URL auf. Der URL wird der konstant definierte api_path vorangef&uuml;gt.
	 * 
	 * @param url
	 *            Die Url, der api_path vorangestellt wird, auf die der Request ausgef&uuml;hrt wird.
	 * @param data
	 *            Das {@link JSONObject}, das als Post-Variable &uuml;bergeben wird
	 * @return Das {@link JSONObject}, das als Antwort vom Server kommt.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public JSONObject put(String url, JSONObject data) throws ClientProtocolException, IOException {
		return put(url, data, null);
	}

	/**
	 * F&uuml;hrt einen Put-Request auf eine URL auf. Der URL wird der konstant definierte api_path vorangef&uuml;gt.
	 * 
	 * @param url
	 *            Die Url, der api_path vorangestellt wird, auf die der Request ausgef&uuml;hrt wird.
	 * @param data
	 *            Das {@link JSONObject}, das als Post-Variable &uuml;bergeben wird
	 * @param params
	 *            Get-Parameter, die dem Request &uuml;bergeben werden k&ouml;nnen.
	 * @return Das {@link JSONObject}, das als Antwort vom Server kommt.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public JSONObject put(String url, JSONObject data, List<NameValuePair> params) throws ClientProtocolException,
			IOException {
		HttpPut httpput = new HttpPut(generateUrl(url, params));
		addJSONEntity(httpput, data);
		return request(httpput);
	}

	/**
	 * F&uuml;hrt einen Delete-Request auf eine URL auf. Der URL wird der konstant definierte api_path vorangef&uuml;gt.
	 * 
	 * @param url
	 *            Die Url, der api_path vorangestellt wird, auf die der Request ausgef&uuml;hrt wird.
	 * 
	 * @return Das {@link JSONObject}, das als Antwort vom Server kommt.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public JSONObject delete(String url) throws ClientProtocolException, IOException {
		return delete(url, null);
	}

	/**
	 * F&uuml;hrt einen Delete-Request auf eine URL auf. Der URL wird der konstant definierte api_path vorangef&uuml;gt.
	 * 
	 * @param url
	 *            Die Url, der api_path vorangestellt wird, auf die der Request ausgef&uuml;hrt wird.
	 * 
	 * @param params
	 *            Get-Parameter, die dem Request &uuml;bergeben werden k&ouml;nnen.
	 * @return Das {@link JSONObject}, das als Antwort vom Server kommt.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public JSONObject delete(String url, List<NameValuePair> params) throws ClientProtocolException, IOException {
		HttpDelete httpdelete = new HttpDelete(generateUrl(url, params));
		return request(httpdelete);
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		String hostname = "shopware.p223131.webspaceconfig.de";
		String username = "wieland";
		String password = "seZ45bdcRF023J4GHVftogFAL6F909XtcrJLMc0l";
		ArticleVariant v = new ArticleVariant();
		ArticleDetail d = v.getDetail();
		v.setMotherId(8604);
		d.setAdditionalText("Api");
		d.setNumber("test1.api");
		d.addPrice(new ArticlePrice(100));
		d.addConfiguratorOption(new ArticleConfiguratorOption("Ringgröße", "55"));

		System.out.println(v.toJSONObject().toString(4));
		ApiClient ac = new ApiClient(hostname, username, password);
//		JSONObject result = ac.put("variants/8609", v.toJSONObject());
		JSONObject result = ac.get("articles/8604");
		
		System.out.println(result.toString(4));
	}

}
