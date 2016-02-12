package land.sebastianwie.shopware_uploadtool.topm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Mit dieser Klasse kann man TOPM-Artikelnummern in Shopware-Artikelnummern
 * umwandeln.
 * 
 * @author Sebastian Wieland
 *
 */
public class SupplierMapper {

	private static final String DELIMITER = "-";

	private JSONObject suppliersJson;
	private Map<String, Pattern> regexPatterns;

	public SupplierMapper(String jsonUrl) throws IOException {
		HttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(jsonUrl);
		HttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();

		StringBuilder result = new StringBuilder();
		if (entity != null) {
			InputStream instream = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
			try {
				char[] buffer = new char[1024];
				int readChars = 0;
				while ((readChars = reader.read(buffer)) > 0)
					result.append(buffer, 0, readChars);

				this.suppliersJson = new JSONObject(result.toString());
			} finally {
				instream.close();
				reader.close();
			}
		}
	}

	/**
	 * Findet die Shopware-Hersteller-Id für einen Artikel heraus.
	 * 
	 * @param topmSupplier
	 * @param ordernumber
	 * @return
	 */
	public int getShopwareSupplierId(int topmSupplier, String ordernumber) {
		JSONArray suppliers = this.suppliersJson.getJSONArray("suppliers");
		JSONObject defaults = this.suppliersJson.getJSONObject("defaults");
		String defaultBnRegex = defaults.getString("bnregex");

		for (Object supplierObject : suppliers) {
			if (supplierObject instanceof JSONObject) {
				JSONObject supplier = (JSONObject) supplierObject;
				if (!supplier.has("swsid") || !supplier.has("topmid"))
					continue;
				if (topmSupplier != supplier.getInt("topmid"))
					continue;

				String bnRegex = defaultBnRegex;
				if (supplier.has("bnregex"))
					bnRegex = supplier.getString("bnregex");

				Pattern orderNumberPattern = this.getRegexPattern(bnRegex);
				Matcher orderNumberMatcher = orderNumberPattern.matcher(ordernumber);

				if (orderNumberMatcher.matches())
					return supplier.getInt("swsid");
			}
		}
		
		return -1;
	}

	private Pattern getRegexPattern(String regex) {
		if (this.regexPatterns == null)
			this.regexPatterns = new HashMap<>();

		if (!this.regexPatterns.containsKey(regex))
			this.regexPatterns.put(regex, Pattern.compile(regex));

		return this.regexPatterns.get(regex);
	}

	/**
	 * Generiert die Shopware-Artikelnummer für einen Artikel. Dieser Artikel
	 * muss nicht unbedingt in Shopware existieren.
	 * 
	 * @param topmSupplier
	 * @param ordernumber
	 * @return
	 */
	public String toShopwareArtNr(int topmSupplier, String ordernumber) {
		int swSupplierID = getShopwareSupplierId(topmSupplier, ordernumber);
		if (swSupplierID == -1)
			return null;
		return swSupplierID + DELIMITER + mask(ordernumber);
	}

	private static String mask(String ordernumber) {
		return ordernumber.replaceAll("[^\\w.-]", "_");
	}

	public static void main(String[] args) throws IOException {
		SupplierMapper mapper = new SupplierMapper("https://raw.githubusercontent.com/sebastianwieland/wielandfiles/master/suppliers.json");
		System.out.println(mapper.toShopwareArtNr(475, "7&234.56"));
	}

}
