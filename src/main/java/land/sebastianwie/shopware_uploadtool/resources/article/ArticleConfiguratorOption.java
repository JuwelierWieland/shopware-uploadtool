package land.sebastianwie.shopware_uploadtool.resources.article;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import land.sebastianwie.shopware_uploadtool.util.ToJsonConvertible;

import org.json.JSONObject;

public class ArticleConfiguratorOption implements ToJsonConvertible {

	private String group;
	private String option;

	private String name;
	private int groupId;

	private boolean usesGroupId;

	public static Map<Integer, String> configuratorIdMap = new HashMap<>();

	public ArticleConfiguratorOption(String group, String option) {
		this.group = group;
		this.option = option;
		this.usesGroupId = false;
	}

	public ArticleConfiguratorOption(int groupId, String name) {
		this.name = name;
		this.groupId = groupId;
		this.usesGroupId = true;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		if (usesGroupId) {
			result.put("name", name);
			result.put("groupId", groupId);
		} else {
			// result.put("group", new JSONObject().put("name", group));
			// result.put("option", new JSONObject().put("name", option));
			result.put("group", group);
			result.put("option", option);
		}
		return result;
	}

	public static ArticleConfiguratorOption createFromJSONObject(JSONObject data) {
		if (data == null)
			return null;
		if (data.has("group") && !data.isNull("group") && data.has("option") && !data.isNull("option"))
			return new ArticleConfiguratorOption(data.getString("group"), data.getString("option"));
		else
			return new ArticleConfiguratorOption(data.getInt("groupId"), data.getString("name"));
	}

	public ArticleConfiguratorOption convert() {
		if (!usesGroupId)
			return new ArticleConfiguratorOption(group, option);
		else if (configuratorIdMap.containsKey(groupId))
			return new ArticleConfiguratorOption(configuratorIdMap.get(groupId), name);
		else
			return null;
	}

	public boolean usesGroupId() {
		return usesGroupId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((option == null) ? 0 : option.hashCode());
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
		ArticleConfiguratorOption other = (ArticleConfiguratorOption) obj;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (option == null) {
			if (other.option != null)
				return false;
		} else if (!option.equals(other.option))
			return false;
		return true;
	}

	public String toString() {
		if (this.usesGroupId())
			if (this.convert() != null)
				return this.convert().toString();
			else
				return groupId + ": " + name;
		else
			return group + " = " + option;
	}

	public static ArticleConfiguratorOption[] createFromString(String data) {
		String[] lines = data.split("\n");
		Set<ArticleConfiguratorOption> result = new HashSet<>();

		// @f:off
		Pattern p = Pattern.compile("(" + "(?<groupId>\\d+):\\s*" + "(?<name>(\\S+)(\\s+\\S+)*)" + ")|(" + "(?<group>(\\S+)(\\s+\\S+)*)"
				+ "\\s*=\\s*" + "(?<option>(\\S+)(\\s+\\S+)*)" + ")");
		// @f:on
		for (String line : lines) {
			Matcher m = p.matcher(line.trim());
			if (!m.matches())
				continue;
			String groupId = m.group("groupId");
			String name = m.group("name");
			String group = m.group("group");
			String option = m.group("option");

			if (group != null && group.length() > 0 && option != null && option.length() > 0)
				result.add(new ArticleConfiguratorOption(group, option));
			else if (groupId != null && groupId.length() > 0 && name != null && name.length() > 0)
				result.add(new ArticleConfiguratorOption(Integer.parseInt(groupId), name));
		}
		return result.toArray(new ArticleConfiguratorOption[result.size()]);
	}

	public static void main(String[] args) {
		ArticleConfiguratorOption[] options = createFromString("asdf = qwer\n42: asdfadsf\njjjjjjjj\nhallo=welt");
		for (ArticleConfiguratorOption option : options)
			System.out.println(option);
	}
}
