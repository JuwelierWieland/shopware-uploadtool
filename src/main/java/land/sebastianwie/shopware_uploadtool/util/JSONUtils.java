package land.sebastianwie.shopware_uploadtool.util;

import org.json.JSONArray;

public class JSONUtils {
	public static JSONArray createJSONArray(Iterable<? extends ToJsonConvertible> list) {
		if (list == null)
			return null;
		JSONArray result = new JSONArray();
		for (ToJsonConvertible elem : list) {
			result.put(elem.toJSONObject());
		}
		return result;
	}

}
