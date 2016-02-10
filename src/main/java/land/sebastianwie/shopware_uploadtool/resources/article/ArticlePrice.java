package land.sebastianwie.shopware_uploadtool.resources.article;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import land.sebastianwie.shopware_uploadtool.util.ToJsonConvertible;

import org.json.JSONObject;

public class ArticlePrice implements ToJsonConvertible {

	private double price;
	private double pseudoPrice;
	private String customerGroupKey;
	private int from;
	private int to;

	/**
	 * Legt den Bruttopreis eines Produkts fest.
	 * 
	 * @param price
	 *            Der Preis des Produkts
	 */
	public ArticlePrice(double price) {
		this(price, 1, -1);
	}

	/**
	 * Legt den Bruttopreis und den Brutto-Pseudopreis eines Artikels fest
	 * 
	 * @param price
	 *            Der Preis
	 * @param pseudo_price
	 *            Der Pseudopreis
	 */
	public ArticlePrice(double price, double pseudo_price) {
		this(price, 1, -1, pseudo_price, "EK");
	}

	/**
	 * Legt den Brutto eines Produkts fest.
	 * 
	 * @param price
	 *            Der Preis des Produkts
	 * @param from
	 *            Das Minimum an Artikeln, die bestellt werden m&uuml;ssen,
	 *            damit dieser Preis zustande kommt.
	 * @param to
	 *            Das Maximum an Artikeln, die bestellt werden d&uuml;rfen,
	 *            damit dieser Preis zustande kommt. 0 oder negative Werte
	 *            bedeuten, dass es kein Maximum gibt.
	 */
	public ArticlePrice(double price, int from, int to) {
		this(price, from, to, price, "EK");
	}

	/**
	 * Legt den Brutto eines Produkts fest.
	 * 
	 * @param price
	 *            Der Preis des Produkts
	 * @param from
	 *            Das Minimum an Artikeln, die bestellt werden m&uuml;ssen,
	 *            damit dieser Preis zustande kommt.
	 * @param to
	 *            Das Maximum an Artikeln, die bestellt werden d&uuml;rfen,
	 *            damit dieser Preis zustande kommt. 0 oder negative Werte
	 *            bedeuten, dass es kein Maximum gibt.
	 * @param pseudoPrice
	 *            Der Pseudopreis des Artikels
	 * @param customerGroupKey
	 *            Die Identifikation der K&auml;ufergruppe.
	 *            Standardm&auml;&szlig;ig ist dies &quot;EK&quot;
	 */
	public ArticlePrice(double price, int from, int to, double pseudoPrice, String customerGroupKey) {
		if (pseudoPrice == 0)
			pseudoPrice = price;
		if (price > pseudoPrice)
			throw new IllegalArgumentException();
		if (price < 0 || pseudoPrice < 0)
			throw new IllegalArgumentException();
		if (customerGroupKey == null)
			throw new IllegalArgumentException();
		if (from < 1 || (to > 0 && from > to))
			throw new IllegalArgumentException();

		this.price = new BigDecimal(price).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
		this.pseudoPrice = new BigDecimal(pseudoPrice).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
		this.customerGroupKey = customerGroupKey;
		this.from = from;
		this.to = to;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		result.put("price", this.price);
		if (this.pseudoPrice != this.price)
			result.put("pseudoPrice", this.pseudoPrice);
		result.put("customerGroupKey", this.customerGroupKey);

		result.put("from", this.from);

		if (this.to >= this.from)
			result.put("to", this.to);
		else
			result.put("to", "beliebig");

		return result;
	}

	public static ArticlePrice createFromJSONObject(JSONObject data) {
		if (data == null)
			return null;
		double price = data.getDouble("price");
		double pseudoPrice = price;
		if (data.has("pseudoPrice"))
			pseudoPrice = data.getDouble("pseudoPrice");

		String customerGroupKey = "EK";
		if (data.has("customerGroupKey"))
			customerGroupKey = data.getString("customerGroupKey");

		int from = 1;
		if (data.has("from"))
			from = data.getInt("from");

		int to = -1;
		if (data.has("to") && data.get("to") instanceof Integer)
			to = data.getInt("to");
		else {
			try {
				to = Integer.parseInt(data.getString("to"));
			} catch (NumberFormatException e) {
			}
		}

		return new ArticlePrice(price, from, to, pseudoPrice, customerGroupKey);
	}

	public static ArticlePrice[] createFromString(String arg0) {
		String[] lines = arg0.split("\n");
		Set<ArticlePrice> prices = new HashSet<>();
		// @f:off
		Pattern p = Pattern.compile("(?<euro>\\d*)" // Euro (Zahl)
				+ "((\\.|,)?" // [ Komma oder Punkt
				+ "(?<cent>\\d{1,2}))?" // Cent (Zahl, 1 oder 2 stellen) ]
				+ "(\\s+\\(" // [ Whitespace, Klammer auf
				+ "(?<peuro>\\d*)" // Euro (Zahl)
				+ "((\\.|,)?" // [ Komma oder Punkt
				+ "(?<pcent>\\d{1,2}))?" // Cent (Zahl, 1 oder 2 Stellen) ]
				+ "\\))?" // Klammer zu ]
				+ "(\\s+" // [ Whitespace
				+ "(?<from>\\d+)" // Von (Zahl)
				+ "\\s*\\-\\>\\s*" // [Whitespace], "->", [Whitespace]
				+ "(?<to>(\\d+|[X]))" // Bis (Zahl oder X (für "beliebig"))
				+ ")?" // ]
				+ "(\\s+" // [ Whitespace
				+ "\\[" // Eckige Klammer auf
				+ "(?<ckey>\\w+)" // Customer-Group-Key
				+ "\\]" // Eckige Klammer zu
				+ ")?" // ]
		);
		// @f:on
		for (String line : lines) {
			Matcher m = p.matcher(line.trim());
			if (!m.matches())
				continue;
			String euro = m.group("euro");
			String cent = m.group("cent");
			String peuro = m.group("peuro");
			String pcent = m.group("pcent");
			String fromStr = m.group("from");
			String toStr = m.group("to");
			String ckey = m.group("ckey");

			double price = 0;
			double pseudoPrice = 0;
			int from = 1;
			int to = -1;

			if (euro != null && euro.length() > 0)
				price = Double.parseDouble(euro);
			if (cent != null && cent.length() == 1)
				price += Double.parseDouble(cent) / 10;
			else if (cent != null && cent.length() == 2)
				price += Double.parseDouble(cent) / 100;

			if (peuro != null && peuro.length() > 0)
				pseudoPrice = Double.parseDouble(peuro);
			if (pcent != null && pcent.length() == 1)
				pseudoPrice += Double.parseDouble(pcent) / 10;
			if (pcent != null && pcent.length() == 2)
				pseudoPrice += Double.parseDouble(pcent) / 100;

			if (fromStr != null && fromStr.length() > 0) {
				int tmpFrom = Integer.parseInt(fromStr);
				if (tmpFrom > 1)
					from = tmpFrom;
			}
			if (toStr != null && toStr.length() > 0)
				if (!"X".equals(toStr)) {
					int tmpTo = Integer.parseInt(toStr);
					if (tmpTo >= from)
						to = tmpTo;
				}
			if (ckey == null || ckey.length() == 0)
				ckey = "EK";

			prices.add(new ArticlePrice(price, from, to, pseudoPrice, ckey));

		}
		return prices.toArray(new ArticlePrice[prices.size()]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customerGroupKey == null) ? 0 : customerGroupKey.hashCode());
		result = prime * result + from;
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(pseudoPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + to;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArticlePrice other = (ArticlePrice) obj;
		if (customerGroupKey == null) {
			if (other.customerGroupKey != null)
				return false;
		} else if (!customerGroupKey.equals(other.customerGroupKey))
			return false;
		if (from != other.from)
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		if (Double.doubleToLongBits(pseudoPrice) != Double.doubleToLongBits(other.pseudoPrice))
			return false;
		if (to != other.to)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = new String();
		result += this.price;
		if (this.pseudoPrice != this.price)
			result += " (" + this.pseudoPrice + ")";
		result += " " + this.from;
		result += "->" + (this.to >= this.from ? this.to : "X");
		result += " [" + this.customerGroupKey + "]";
		return result;
	}

}
