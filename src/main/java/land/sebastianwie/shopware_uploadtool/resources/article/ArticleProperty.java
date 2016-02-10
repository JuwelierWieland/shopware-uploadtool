package land.sebastianwie.shopware_uploadtool.resources.article;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import land.sebastianwie.shopware_uploadtool.util.ToJsonConvertible;

import org.json.JSONObject;

public class ArticleProperty implements ToJsonConvertible {
	private String propertyName;
	private String propertyValue;

	private int propertyId;
	private boolean useId;

	public boolean usesId() {
		return useId;
	}

	public int getId() {
		return propertyId;
	}

	/**
	 * Erstellt eine neue Eigenschaft f&uuml; einen Artikel. Weder Eigenschaft,
	 * noch Eigenschaftswert m&uuml;ssen vorher in Shopware vorhanden sein.
	 * Falls sie bereits vorhanden sind, werden diese nicht doppelt angelegt.
	 * 
	 * @param propertyName
	 *            Der Eigenschaftsname (z.B. Farbe)
	 * @param propertyValue
	 *            Der Eigenschaftswert (z.B. blau)
	 */
	public ArticleProperty(String propertyName, String propertyValue) {
		if (propertyName == null || propertyValue == null)
			throw new IllegalArgumentException();

		this.propertyName = propertyName;
		this.propertyValue = propertyValue;

		this.useId = false;
	}

	/**
	 * Erstellt eine neue Eigenschaft f&uuml;r einen Artikel. Die Eigenschaft
	 * muss dazu vorher bereits in Shopware vorhanden sein.
	 * 
	 * @param propertyId
	 *            Die Shopware-ID der Eigenschaft
	 */
	public ArticleProperty(int propertyId) {
		this.propertyId = propertyId;

		this.useId = true;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		if (this.useId) {
			result.put("id", propertyId);
		} else {
			result.put("option", new JSONObject().put("name", propertyName));
			result.put("value", propertyValue);
		}
		return result;
	}

	public static ArticleProperty createFromJSONObject(JSONObject data) {
		if (data == null)
			return null;
		if (data.has("option"))
			return new ArticleProperty(data.getJSONObject("option").getString("name"), data.getString("value"));
		return new ArticleProperty(data.getInt("id"));
	}

	public static ArticleProperty[] createFromString(String arg0) {
		String[] lines = arg0.split("\n");
		Set<ArticleProperty> properties = new HashSet<>();
		// @f:off
		Pattern p = Pattern.compile("(?<id>\\d+)|" // ID
				+ "(" // oder
				+ "(?<name>(\\S+)(\\s+\\S+)*)" // name
				+ "\\s*\\=\\s*" // [Whitespace]=[Whitespace]
				+ "(?<value>(\\S+)(\\s+\\S+)*)" // Wert
				+ ")");
		// @f:on
		for (String line : lines) {
			Matcher m = p.matcher(line.trim());
			if (!m.matches())
				continue;
			String id = m.group("id");
			String name = m.group("name");
			String value = m.group("value");

			if (id != null && id.length() > 0)
				properties.add(new ArticleProperty(Integer.parseInt(id)));
			else if (name != null && value != null && name.length() > 0 && value.length() > 0)
				properties.add(new ArticleProperty(name, value));
		}
		return properties.toArray(new ArticleProperty[properties.size()]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + propertyId;
		result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
		result = prime * result + ((propertyValue == null) ? 0 : propertyValue.hashCode());
		result = prime * result + (useId ? 1231 : 1237);
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
		ArticleProperty other = (ArticleProperty) obj;
		if (propertyId != other.propertyId)
			return false;
		if (propertyName == null) {
			if (other.propertyName != null)
				return false;
		} else if (!propertyName.equals(other.propertyName))
			return false;
		if (propertyValue == null) {
			if (other.propertyValue != null)
				return false;
		} else if (!propertyValue.equals(other.propertyValue))
			return false;
		if (useId != other.useId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (useId) {
			return Integer.toString(propertyId);
		} else {
			return propertyName + " = " + propertyValue;
		}
	}

}
