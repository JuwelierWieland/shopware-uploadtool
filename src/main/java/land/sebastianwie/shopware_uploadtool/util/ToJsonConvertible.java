package land.sebastianwie.shopware_uploadtool.util;

import org.json.JSONObject;

/**
 * Dieses Interface stellt sicher, dass diese Eigenschaft zu einem JSON-Objekt
 * zusammengefasst werden kann.
 * 
 * @author Sebastian Wieland
 *
 */
public interface ToJsonConvertible {

	/**
	 * Erstellt ein {@link JSONObject} aus dem Objekt.
	 * 
	 * @return Ein {@link JSONObject}
	 */
	public JSONObject toJSONObject();
}
