package land.sebastianwie.shopware_uploadtool.resources.article;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import land.sebastianwie.shopware_uploadtool.util.ToJsonConvertible;

import org.json.JSONObject;

public class ArticleCategory implements ToJsonConvertible {

	private int id;
	private String categoryName;

	/**
	 * Erstellt eine neue Artikelkategorie.
	 * 
	 * @param id
	 *            Die Shopware-ID der Artikelkategorie.
	 */
	public ArticleCategory(int id) {
		this.id = id;
	}

	public ArticleCategory(int id, String categoryName) {
		this.id = id;
		this.categoryName = categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getId() {
		return id;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		result.put("id", id);
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		ArticleCategory other = (ArticleCategory) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public static ArticleCategory createFromJSONObject(JSONObject data) {
		if (data == null)
			return null;
		return new ArticleCategory(data.getInt("id"), data.getString("name"));
	}

	public static ArticleCategory[] createFromString(String arg0) {
		String[] lines = arg0.split("\n");
		Set<ArticleCategory> categories = new HashSet<>();
		Pattern p = Pattern.compile("(?<id>\\d+)(\\s+\\(.*\\))?");
		for (String line : lines) {
			// try {
			// int categoryId = Integer.parseInt(line.trim());
			// categories.add(new ArticleCategory(categoryId));
			// } catch (NumberFormatException e) {
			// }
			Matcher m = p.matcher(line.trim());
			if (!m.matches())
				continue;
			String id = m.group("id");
			if (id != null && id.length() > 0)
				categories.add(new ArticleCategory(Integer.parseInt(id)));
		}
		return categories.toArray(new ArticleCategory[categories.size()]);
	}

	@Override
	public String toString() {
		return Integer.toString(id) + (categoryName != null ? " (" + categoryName + ")" : "");
	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.toString(createFromString("123 (asdf)")));
	}

}
