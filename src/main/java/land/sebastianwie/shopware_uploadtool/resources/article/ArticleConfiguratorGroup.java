package land.sebastianwie.shopware_uploadtool.resources.article;

import land.sebastianwie.shopware_uploadtool.util.ToJsonConvertible;

import org.json.JSONObject;

public class ArticleConfiguratorGroup implements ToJsonConvertible {

	private int id;
	private String name;

	public ArticleConfiguratorGroup(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		// result.put("id", id);
		result.put("name", name);
		// result.put("position", 1);
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ArticleConfiguratorGroup other = (ArticleConfiguratorGroup) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

	public static ArticleConfiguratorGroup createFromJSONObject(JSONObject data) {
		if (data == null)
			return null;
		return new ArticleConfiguratorGroup(data.getInt("id"), data.getString("name"));
	}

	public static ArticleConfiguratorGroup[] createFromString(String data) {
		String[] lines = data.split("\n");
		ArticleConfiguratorGroup[] groups = new ArticleConfiguratorGroup[lines.length];
		for (int i = 0; i < lines.length; i++) {
			groups[i] = new ArticleConfiguratorGroup(-1, lines[i].trim());
		}
		return groups;
	}
}
