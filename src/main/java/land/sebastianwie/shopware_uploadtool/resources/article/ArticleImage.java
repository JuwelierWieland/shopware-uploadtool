package land.sebastianwie.shopware_uploadtool.resources.article;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import land.sebastianwie.shopware_uploadtool.util.ToJsonConvertible;

import org.json.JSONObject;

public class ArticleImage implements ToJsonConvertible {

	private int mediaId;
	private String mediaName;
	private String link;
	private boolean mainImage;

	private boolean useId;

	/**
	 * Erstellt ein neues Bild.
	 * 
	 * @param mediaId
	 *            Die Shopware-Media-Id des Bildes.
	 * @param mainImage
	 *            Gibt an, ob dies das Hauptbild des Artikels ist.
	 */
	public ArticleImage(int mediaId, boolean mainImage) {
		this.mediaId = mediaId;
		this.mainImage = mainImage;
		this.useId = true;
	}

	public ArticleImage(int mediaId, String mediaName, boolean mainImage) {
		this.mediaId = mediaId;
		this.mediaName = mediaName;
		this.mainImage = mainImage;
		this.useId = true;
	}

	/**
	 * Erstellt ein neues Bild. Das Bild wird bei Erfolg in Shopware
	 * hochgeladen, sodass man die Quelle anschlie&szlig;end l&ouml;schen kann.
	 * 
	 * @param link
	 *            Der Pfad des Bildes.
	 * @param mainImage
	 *            Gibt an, ob dies das Hauptbild des Artikels ist.
	 */
	public ArticleImage(String link, boolean mainImage) {
		if (link == null)
			throw new IllegalArgumentException();
		this.link = link;
		this.mainImage = mainImage;
		this.useId = false;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public int getMediaId() {
		return mediaId;
	}

	public boolean usesId() {
		return useId;
	}

	public boolean isMainImage() {
		return mainImage;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		if (useId) {
			result.put("mediaId", this.mediaId);
		} else {
			result.put("link", this.link);
		}
		result.put("main", mainImage ? 1 : 2);
		return result;
	}

	/**
	 * Erstellt ein {@link ArticleImage} aus einem {@link JSONObject}
	 * 
	 * @param data
	 *            das {@link JSONObject}
	 * @return Ein {@link ArticleImage}
	 */
	public static ArticleImage createFromJSONObject(JSONObject data) {
		if (data == null)
			return null;
		boolean mainImage = false;
		if (data.has("main") && !data.isNull("main"))
			mainImage = data.getInt("main") == 1;
		String imageName = null;
		if (data.has("path") && !data.isNull("path") && data.has("extension") && !data.isNull("extension"))
			imageName = data.getString("path") + "." + data.getString("extension");
		return new ArticleImage(data.getInt("mediaId"), imageName, mainImage);
	}

	public static ArticleImage[] createFromString(String arg0) {
		String[] lines = arg0.split("\n");
		Set<ArticleImage> images = new HashSet<>();
		// @f:off
		Pattern p = Pattern.compile("((?<main>\\Qmain:\\E)\\s+)?(" // [ "main:"
																	// Whitespace
																	// ]
				+ "(?<id>\\d+)(\\s+\\(.*\\))?|" // ID
				+ "(?<path>" // oder
				+ "(" // [
				+ "\\S+\\Q://\\E" // Protokoll
				+ "\\S+" // Domain(unterstes level) oder IP(erste Zahl)
				+ "(\\.\\S+)+" // Domain(alle anderen Level) oder IP(alle
								// anderen Zahlen)
				+ "\\/" // Slash
				+ ")?" // ]
				+ "(\\S+)?" // [ Relativer Pfad ]
				+ "((\\/\\S+)*\\/)?" // [ Subdirectories oder absoluter Pfad ]
				+ "(\\S*\\.\\S+)" // Datei mit Endung
				+ ")" + ")");
		// @f:on
		for (String line : lines) {
			Matcher m = p.matcher(line.trim());
			if (!m.matches())
				continue;
			String main = m.group("main");
			String id = m.group("id");
			String path = m.group("path");

			boolean mainImage = false;
			if ("main:".equals(main))
				mainImage = true;
			if (id != null && id.length() > 0)
				images.add(new ArticleImage(Integer.parseInt(id), mainImage));
			else if (path != null && path.length() > 0) {
				images.add(new ArticleImage(path, mainImage));
			}
		}
		return images.toArray(new ArticleImage[images.size()]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + (mainImage ? 1231 : 1237);
		result = prime * result + mediaId;
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
		ArticleImage other = (ArticleImage) obj;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (mainImage != other.mainImage)
			return false;
		if (mediaId != other.mediaId)
			return false;
		if (useId != other.useId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (this.useId) {
			return (mainImage ? "main: " : "") + Integer.toString(mediaId) + (mediaName != null ? " (" + mediaName + ")" : "");
		} else {
			return (mainImage ? "main: " : "") + link;
		}
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(createFromString("3242 (asdf)")));
	}

}
