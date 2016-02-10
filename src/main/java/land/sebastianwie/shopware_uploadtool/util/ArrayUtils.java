package land.sebastianwie.shopware_uploadtool.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ArrayUtils {
	public static int getIndex(Object[] array, Object toSearch) {
		for (int i = 0; i < array.length; i++)
			if (toSearch.equals(array[i]))
				return i;
		return -1;
	}

	public static String join(String delimiter, Iterable<?> elements) {
		StringBuilder sb = new StringBuilder();
		Iterator<?> i = elements.iterator();
		while (i.hasNext()) {
			Object element = i.next();
			sb.append(element);
			if (i.hasNext())
				sb.append(delimiter);
		}
		return sb.toString();
	}

	public static String findKeyOf(String value, Map<String, String> map) {
		Set<Entry<String, String>> entrySet = map.entrySet();
		for (Entry<String, String> entry : entrySet) {
			if (value.equals(entry.getValue()))
				return entry.getKey();
		}
		return null;
	}
}
