package land.sebastianwie.shopware_uploadtool.resources.article;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import land.sebastianwie.shopware_uploadtool.util.JSONUtils;
import land.sebastianwie.shopware_uploadtool.util.ToJsonConvertible;

import org.json.JSONArray;
import org.json.JSONObject;

public class Article implements ToJsonConvertible {
	static final DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	private static final String delimiter = " ";

	private byte active = -1;

	private ArticleDetail mainDetail = new ArticleDetail();
	private Set<ArticleConfiguratorGroup> configuratorGroups;
	private Set<ArticleVariant> variants;

	private int id = -1;
	private int pseudoId = -1;

	private String name;
	private String description;
	private String descriptionLong;

	private Set<String> keywords;

	private Set<ArticleCategory> categories;
	private boolean replaceCategories = true;

	private int supplierId = -1;

	private int customProductId = -1;

	private String template;

	private int filterGroupID = -1;
	private List<ArticleProperty> propertyValues;
	// private boolean replaceProperties = true;

	private Date availableFrom;

	private byte highlight = -1;
	private byte notification = -1;

	private int taxId = -1;

	private List<ArticleImage> images;
	private boolean replaceImages = true;

	public ArticleDetail getMainDetail() {
		return mainDetail;
	}

	public void setMainDetail(ArticleDetail mainDetail) {
		this.mainDetail = mainDetail;
	}

	/**
	 * @return true, falls der Artikel aktiv ist (d.h. er wird im Shop
	 *         gelistet). false, falls er nicht aktiv ist oder diese Variable
	 *         nicht aktiv ist (d.h. ignoriert wird).
	 */
	public boolean isActive() {
		return active == 1;
	}

	/**
	 * @return true, falls der Aktivit&auml;tsstatus aktiv ist (d.h. nicht
	 *         ignoriert wird), sonst false.
	 */
	public boolean activeActive() {
		return active != -1;
	}

	/**
	 * Verursacht, dass der Artikel im Shop gelistet wird. Es kann immernoch
	 * sein, dass er nicht gelistet wird, falls er kein Foto hat oder keiner
	 * Kategorie zugewiesen ist.
	 */
	public void setActive() {
		this.setActive(true);
	}

	/**
	 * Verursacht, dass der Artikel im Shop nicht gelistet wird.
	 */
	public void setInactive() {
		this.setActive(false);
	}

	public void setActive(boolean active) {
		this.active = (byte) (active ? 1 : 0);
	}

	/**
	 * Verursacht, dass der Aktivit&auml;tsstatus deaktiviert wird (d.h.
	 * ignoriert wird).
	 */
	public void unsetActive() {
		this.active = -1;
	}

	/**
	 * @return true, falls der sich der Artikel im Abverkauf befindet, sonst
	 *         false.
	 */
	public boolean isVariantActive() {
		return mainDetail.isVariantActive();
	}

	/**
	 * @return true, falls der Abverkaufsstatus aktiv ist.
	 */
	public boolean activeVariantActive() {
		return mainDetail.activeVariantActive();
	}

	public void setVariantActive(boolean variantActive) {
		mainDetail.setVariantActive(variantActive);
	}

	/**
	 * Deaktiviert den Abverkaufsstatus
	 */
	public void unsetVariantActive() {
		mainDetail.unsetVariantActive();
	}

	/**
	 * Gibt die Shopware-ID des Artikels zur&uuml;ck. Die ID kann nicht gesetzt
	 * werden, sondern nur von Artikeln, die vorher von der API erhalten wurden,
	 * ausgelesen werden.
	 * 
	 * @return Die Shopware-ID des Artikels.
	 */
	public int getId() {
		return id;
	}

	public int getPseudoId() {
		if (id != -1)
			return id;
		return pseudoId;
	}

	public void setPseudoId(int pseudoId) {
		if (id == -1)
			this.pseudoId = pseudoId;
	}

	/**
	 * Gibt die Artikelnummer des Artikels zur&uuml;ck.
	 * 
	 * @return Die Artikenummer des Artikels.
	 */
	public String getNumber() {
		return mainDetail.getNumber();
	}

	/**
	 * Setzt die Artikelnummer des Artikels.
	 * 
	 * @param number
	 *            Die neue Artikelnummer
	 */
	public void setNumber(String number) {
		mainDetail.setNumber(number);
	}

	public String getNewNumber() {
		return mainDetail.getNewNumber();
	}

	public void setNewNumber(String newNumber) {
		mainDetail.setNewNumber(newNumber);
	}

	public void resetNewNumber() {
		mainDetail.resetNewNumber();
	}

	public String getEan() {
		return mainDetail.getEan();
	}

	public void setEan(String ean) {
		mainDetail.setEan(ean);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescriptionLong() {
		return descriptionLong;
	}

	public void setDescriptionLong(String descriptionLong) {
		this.descriptionLong = descriptionLong;
	}

	public Set<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(Set<String> keywords) {
		if (this.keywords == null)
			this.keywords = new HashSet<>();
		else
			this.keywords.clear();
		for (String keyword : keywords) {
			this.keywords.add(keyword.toLowerCase().trim());
		}
	}

	public void addKeyword(String keyword) {
		if (this.keywords == null)
			this.keywords = new HashSet<>();
		this.keywords.add(keyword.toLowerCase().trim());
	}

	public boolean removeKeyword(String keyword) {
		if (this.keywords == null)
			return false;
		return this.keywords.remove(keyword.toLowerCase());
	}

	public Set<ArticleCategory> getCategories() {
		return categories;
	}

	public void setCategories(Set<ArticleCategory> categories) {
		this.categories = categories;
	}

	public void setCategories(ArticleCategory[] categories) {
		for (ArticleCategory category : categories)
			this.addCategory(category);
	}

	public void addCategory(ArticleCategory category) {
		if (this.categories == null)
			this.categories = new HashSet<>();
		this.categories.add(category);
	}

	public boolean removeCategory(ArticleCategory category) {
		if (this.categories == null)
			return false;
		return this.categories.remove(category);
	}

	/**
	 * Verursacht, dass bei einem Update die bisherigen Kategorien gel&ouml;scht
	 * werden, wenn neue Kategorien angegeben werden. Dies ist Standard.
	 */
	public void replaceCategories() {
		this.replaceCategories = true;
	}

	/**
	 * Verursacht, dass bei einem Update die bisherigen Kategorien erhalten
	 * bleiben, wenn neue Kategorien angegeben werden.
	 */
	public void keepCategories() {
		this.replaceCategories = false;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public List<ArticlePrice> getPrices() {
		return mainDetail.getPrices();
	}

	public void setPrices(List<ArticlePrice> prices) {
		mainDetail.setPrices(prices);
	}

	public void setPrices(ArticlePrice[] prices) {
		mainDetail.setPrices(prices);
	}

	public void addPrice(ArticlePrice price) {
		mainDetail.addPrice(price);
	}

	public boolean removePrice(ArticlePrice price) {
		return mainDetail.removePrice(price);
	}

	/**
	 * Verursacht, dass bei einem Update die bisherigen Preise gel&ouml;scht
	 * werden, wenn neue Preise angegeben werden. Dies ist Standard.
	 */
	public void replacePrices() {
		mainDetail.replacePrices();
	}

	/**
	 * Verursacht, dass bei einem Update die bisherigen Kategorien erhalten
	 * bleiben, wenn neue Kategorien angegeben werden.
	 */
	public void keepPrices() {
		mainDetail.keepPrices();
	}

	public int getCustomProductId() {
		return customProductId;
	}

	public void setCustomProductId(int customProductId) {
		this.customProductId = customProductId;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public int getFilterGroupID() {
		return filterGroupID;
	}

	public void setFilterGroupID(int filterGroupID) {
		this.filterGroupID = filterGroupID;
	}

	public List<ArticleProperty> getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(List<ArticleProperty> propertyValues) {
		this.propertyValues = propertyValues;
	}

	public void setPropertyValues(ArticleProperty[] propertyValues) {
		for (ArticleProperty propertyValue : propertyValues)
			this.addPropertyValue(propertyValue);
	}

	public void addPropertyValue(ArticleProperty propertyValue) {
		if (this.propertyValues == null)
			this.propertyValues = new ArrayList<>();
		this.propertyValues.add(propertyValue);
	}

	public boolean removePropertyValue(ArticleProperty propertyValue) {
		if (this.propertyValues == null)
			return false;
		return this.propertyValues.remove(propertyValue);
	}

	/*
	 * @Deprecated public void optionReplaceProperties() {
	 * this.replaceProperties = true; }
	 * 
	 * @Deprecated public void optionKeepProperties() { this.replaceProperties =
	 * false; }
	 */

	public Date getAvailableFrom() {
		return availableFrom;
	}

	public void setAvailableFrom(Date availableFrom) {
		this.availableFrom = availableFrom;
	}

	public Date getReleaseDate() {
		return mainDetail.getReleaseDate();
	}

	public void setReleaseDate(Date releaseDate) {
		mainDetail.setReleaseDate(releaseDate);
	}

	/**
	 * @return true, falls der Artikel hervorgehoben wird, false, falls nicht
	 *         oder der Hervorhebestatus deaktiviert ist.
	 */
	public boolean isHighlight() {
		return highlight == 1;
	}

	/**
	 * @return true, falls der Hervorhebestatus aktiv ist, sonst false.
	 */
	public boolean activeHighlight() {
		return highlight != -1;
	}

	public void setHighlight(boolean highlight) {
		this.highlight = (byte) (highlight ? 1 : 0);
	}

	/**
	 * Deaktiviert den Hervorhebestatus.
	 */
	public void unsetHighlight() {
		this.highlight = -1;
	}

	/**
	 * @return True, falls Benachrichtigungen aktiviert sind, false, wenn nicht
	 *         oder der Benachrichtigungsstatus deaktiviert ist.
	 */
	public boolean isNotification() {
		return notification == 1;
	}

	/**
	 * @return True, falls der Benachrichtigungsstatus aktiv ist, sonst false.
	 */
	public boolean activeNotification() {
		return notification != -1;
	}

	public void setNotification(boolean notification) {
		this.notification = (byte) (notification ? 1 : 0);
	}

	/**
	 * Deaktiviert den Benachrichtigungsstatus.
	 */
	public void unsetNotification() {
		this.notification = -1;
	}

	public String getPackUnit() {
		return mainDetail.getPackUnit();
	}

	public void setPackUnit(String packUnit) {
		mainDetail.setPackUnit(packUnit);
	}

	public String getPurchaseUnit() {
		return mainDetail.getPurchaseUnit();
	}

	public void setPurchaseUnit(String purchaseUnit) {
		mainDetail.setPurchaseUnit(purchaseUnit);
	}

	public String getReferenceUnit() {
		return mainDetail.getReferenceUnit();
	}

	public void setReferenceUnit(String referenceUnit) {
		mainDetail.setReferenceUnit(referenceUnit);
	}

	public int getStockMin() {
		return mainDetail.getStockMin();
	}

	public void setStockMin(int stockMin) {
		mainDetail.setStockMin(stockMin);
	}

	public int getInStock() {
		return mainDetail.getInStock();
	}

	public void setInStock(int inStock) {
		mainDetail.setInStock(inStock);
	}

	public String getShippingTime() {
		return mainDetail.getShippingTime();
	}

	public void setShippingTime(String shippingTime) {
		mainDetail.setShippingTime(shippingTime);
	}

	/**
	 * @return true, falls der Artikel Versandkostenfrei ist, false, wenn nicht
	 *         oder wenn diese Option deaktiviert wurde.
	 */
	public boolean isShippingFree() {
		return mainDetail.isShippingFree();
	}

	/**
	 * @return true, wenn diese Option aktiv ist, sonst false.
	 */
	public boolean activeShippingFree() {
		return mainDetail.activeShippingFree();
	}

	public void setShippingFree(boolean shippingFree) {
		mainDetail.setShippingFree(shippingFree);
	}

	/**
	 * Deaktiviert diese Option
	 */
	public void unsetShippingFree() {
		mainDetail.unsetShippingFree();
	}

	public String getAdditionalText() {
		return mainDetail.getAdditionalText();
	}

	public void setAdditionalText(String additionalText) {
		mainDetail.setAdditionalText(additionalText);
	}

	public String[] getAttributes() {
		return mainDetail.getAttributes();
	}

	public String getAttribute(int index) {
		return mainDetail.getAttribute(index);
	}

	public void setAttributes(String[] attributes) {
		mainDetail.setAttributes(attributes);
	}

	public void setAttribute(int index, String attribute) {
		mainDetail.setAttribute(index, attribute);
	}

	public int getTaxId() {
		return taxId;
	}

	public void setTaxId(int taxId) {
		this.taxId = taxId;
	}

	public double getHeight() {
		return mainDetail.getHeight();
	}

	public void setHeight(double height) {
		mainDetail.setHeight(height);
	}

	public double getWidth() {
		return mainDetail.getWidth();
	}

	public void setWidth(double width) {
		mainDetail.setWidth(width);
	}

	public double getWeight() {
		return mainDetail.getWeight();
	}

	public void setWeight(double weight) {
		mainDetail.setWeight(weight);
	}

	public List<ArticleImage> getImages() {
		return images;
	}

	public void setImages(List<ArticleImage> images) {
		this.images = images;
	}

	public void setImages(ArticleImage[] images) {
		for (ArticleImage image : images)
			this.addImage(image);
	}

	public void addImage(ArticleImage image) {
		if (this.images == null)
			this.images = new ArrayList<>();
		this.images.add(image);
	}

	public boolean removeImage(ArticleImage image) {
		if (this.images == null)
			return false;
		return this.images.remove(image);
	}

	/**
	 * Verursacht, dass bei einem Update die bisherigen Bilder gel&ouml;scht
	 * werden, wenn neue Bilder angegeben werden.
	 */
	public void replaceImages() {
		this.replaceImages = true;
	}

	/**
	 * Verursacht, dass bei einem Update die bisherigen Bilder erhalten bleiben,
	 * wenn neue Bilder angegeben werden. Dies ist Standard.
	 */
	public void keepImages() {
		this.replaceImages = false;
	}

	public Set<ArticleConfiguratorGroup> getConfiguratorGroups() {
		return configuratorGroups;
	}

	public void setConfiguratorGroups(Set<ArticleConfiguratorGroup> groups) {
		this.configuratorGroups = groups;
	}

	public void setConfiguratorGroups(ArticleConfiguratorGroup[] groups) {
		for (ArticleConfiguratorGroup group : groups)
			this.addConfiguratorGroup(group);
	}

	public void addConfiguratorGroup(ArticleConfiguratorGroup group) {
		if (this.configuratorGroups == null)
			this.configuratorGroups = new HashSet<>();
		this.configuratorGroups.add(group);
	}

	public boolean removeConfiguratorGroup(ArticleConfiguratorGroup group) {
		if (this.configuratorGroups == null)
			return false;
		return this.configuratorGroups.remove(group);
	}

	public Set<ArticleVariant> getVariants() {
		return variants;
	}

	public void setVariants(Set<ArticleVariant> groups) {
		this.variants = groups;
	}

	public void setVariants(ArticleVariant[] groups) {
		for (ArticleVariant group : groups)
			this.addVariant(group);
	}

	public void addVariant(ArticleVariant group) {
		if (this.variants == null)
			this.variants = new HashSet<>();
		this.variants.add(group);
	}

	public boolean removeVariant(ArticleVariant group) {
		if (this.variants == null)
			return false;
		return this.variants.remove(group);
	}

	public boolean isReplaceCategories() {
		return replaceCategories;
	}

	public void setReplaceCategories(boolean replaceCategories) {
		this.replaceCategories = replaceCategories;
	}

	public boolean isReplacePrices() {
		return mainDetail.isReplacePrices();
	}

	public void setReplacePrices(boolean replacePrices) {
		mainDetail.setReplacePrices(replacePrices);
	}

	public boolean isReplaceImages() {
		return replaceImages;
	}

	public void setReplaceImages(boolean replaceImages) {
		this.replaceImages = replaceImages;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		JSONObject mainDetail = this.mainDetail.toJSONObject();

		JSONArray categories = JSONUtils.createJSONArray(this.getCategories());
		JSONArray propertyValues = JSONUtils.createJSONArray(this.getPropertyValues());
		JSONArray images = JSONUtils.createJSONArray(this.getImages());
		JSONArray configuratorGroups = JSONUtils.createJSONArray(this.getConfiguratorGroups());
		JSONArray variants = JSONUtils.createJSONArray(this.getVariants());

		if (getName() != null)
			result.put("name", getName());
		if (getDescription() != null)
			result.put("description", getDescription());
		if (getDescriptionLong() != null)
			result.put("descriptionLong", getDescriptionLong());
		if (getAvailableFrom() != null)
			result.put("availableFrom", dateformat.format(getAvailableFrom()));
		if (getReleaseDate() != null)
			result.put("releaseDate", dateformat.format(getReleaseDate()));
		if (getFilterGroupID() != -1)
			result.put("filterGroupId", getFilterGroupID());
		if (getTaxId() != -1)
			result.put("taxId", getTaxId());
		if (getSupplierId() != -1)
			result.put("supplierId", getSupplierId());
		if (activeHighlight())
			result.put("highlight", isHighlight());
		if (activeNotification())
			result.put("notification", isNotification());
		if (getKeywords() != null)
			result.put("keywords", String.join(delimiter, getKeywords()));
		if (activeActive())
			result.put("active", isActive());
		if (getTemplate() != null)
			result.put("template", getTemplate());

		if (mainDetail.length() > 0)
			result.put("mainDetail", mainDetail);

		if (categories != null && categories.length() > 0) {
			result.put("categories", categories);
			result.put("__options_categories", new JSONObject().put("replace", this.isReplaceCategories()));
		}
		if (propertyValues != null && propertyValues.length() > 0) {
			result.put("propertyValues", propertyValues);
			// result.put("__option_propertyValues", new
			// JSONObject().put("replace", replaceProperties));
		}
		if (images != null && images.length() > 0) {
			result.put("images", images);
			result.put("__options_images", new JSONObject().put("replace", this.isReplaceImages()));
		}
		if (configuratorGroups != null && configuratorGroups.length() > 0) {
			result.put("configuratorSet", new JSONObject().put("groups", configuratorGroups));
		}
		if (variants != null && variants.length() > 0) {
			result.put("variants", variants);
		}

		return result;
	}

	/**
	 * Erstellt ein {@link Article}-Objekt aus einem {@link JSONObject}
	 * 
	 * @param data
	 *            Das {@link JSONObject}, das den Artikel enth&auml;lt
	 * @return Ein {@link Article}
	 */
	public static Article createFromJSONObject(JSONObject data) {
		if (data == null)
			return null;
		Article result = new Article();
		if (data.has("id") && !data.isNull("id"))
			result.id = data.getInt("id");
		if (data.has("name") && !data.isNull("name"))
			result.setName(data.getString("name"));
		if (data.has("description") && !data.isNull("description"))
			result.setDescription(data.getString("description"));
		if (data.has("descriptionLong") && !data.isNull("descriptionLong"))
			result.setDescriptionLong(data.getString("descriptionLong"));
		if (data.has("availableFrom") && !data.isNull("availableFrom"))
			try {
				result.setAvailableFrom(dateformat.parse(data.getString("availableFrom")));
			} catch (ParseException e) {
				result.setAvailableFrom(new Date());
			}
		/*
		 * if (data.has("releaseDate") && !data.isNull("releaseDate")) try {
		 * result
		 * .setReleaseDate(dateformat.parse(data.getString("releaseDate"))); }
		 * catch (ParseException e) { result.setReleaseDate(new Date()); }
		 */
		if (data.has("filterGroupId") && !data.isNull("filterGroupId"))
			result.setFilterGroupID(data.getInt("filterGroupId"));
		if (data.has("taxId") && !data.isNull("taxId"))
			result.setTaxId(data.getInt("taxId"));
		if (data.has("supplierId") && !data.isNull("supplierId"))
			result.setSupplierId(data.getInt("supplierId"));
		if (data.has("highlight") && !data.isNull("highlight"))
			result.setHighlight(data.getBoolean("highlight"));
		if (data.has("notification") && !data.isNull("notification"))
			result.setNotification(data.getBoolean("notification"));
		if (data.has("keywords") && !data.isNull("keywords"))
			result.setKeywords(new HashSet<>(Arrays.asList(data.getString("keywords").split(delimiter))));
		if (data.has("active") && !data.isNull("active"))
			result.setActive(data.getBoolean("active"));
		if (data.has("template") && !data.isNull("template"))
			result.setTemplate(data.getString("template"));

		if (data.has("categories") && !data.isNull("categories")) {
			if (data.get("categories") instanceof JSONObject) {
				JSONObject categories = data.getJSONObject("categories");
				Set<String> keySet = categories.keySet();
				for (String key : keySet) {
					result.addCategory(ArticleCategory.createFromJSONObject(categories.getJSONObject(key)));
				}
			} else if (data.get("categories") instanceof JSONArray) {
				JSONArray categories = data.getJSONArray("categories");
				for (int i = 0; i < categories.length(); i++) {
					result.addCategory(ArticleCategory.createFromJSONObject(categories.getJSONObject(i)));
				}
			}
		}

		if (data.has("propertyValues") && !data.isNull("propertyValues")) {
			JSONArray propertyValues = data.getJSONArray("propertyValues");
			for (int i = 0; i < propertyValues.length(); i++) {
				result.addPropertyValue(ArticleProperty.createFromJSONObject(propertyValues.getJSONObject(i)));
			}
		}

		if (data.has("images") && !data.isNull("images")) {
			JSONArray images = data.getJSONArray("images");
			for (int i = 0; i < images.length(); i++) {
				result.addImage(ArticleImage.createFromJSONObject(images.getJSONObject(i)));
			}
		}
		if (data.has("configuratorSet") && !data.isNull("configuratorSet")) {
			JSONObject set = data.getJSONObject("configuratorSet");
			if (set.has("groups") && !set.isNull("groups")) {
				JSONArray groups = set.getJSONArray("groups");
				for (int i = 0; i < groups.length(); i++) {
					ArticleConfiguratorGroup group = ArticleConfiguratorGroup.createFromJSONObject(groups.getJSONObject(i));
					ArticleConfiguratorOption.configuratorIdMap.put(group.getId(), group.getName());
					result.addConfiguratorGroup(group);
				}
			}
		}
		if (data.has("mainDetail") && !data.isNull("mainDetail")) {
			result.setMainDetail(ArticleDetail.createFromJSONObject(data.getJSONObject("mainDetail")));
		}
		if (data.has("details") && !data.isNull("details")) {
			JSONArray details = data.getJSONArray("details");
			for (int i = 0; i < details.length(); i++) {
				ArticleVariant variant = ArticleVariant.createFromJSONObject(details.getJSONObject(i));
				variant.setMotherId(result.getId());
				variant.setMotherNumber(result.getNumber());
				result.addVariant(variant);
			}
		}
		if (data.has("mainDetail") && !data.isNull("mainDetail")) {
			result.setMainDetail(ArticleDetail.createFromJSONObject(data.getJSONObject("mainDetail")));
		}

		return result;
	}

	/**
	 * Gibt den Artikel in JSON-Schreibweise aus.
	 */
	@Override
	public String toString() {
		return this.toJSONObject().put("id", id).toString(4);
	}

	public static void main(String[] args) {
		Article a = new Article();
		a.setName("Testartikel");
		a.setDescription("Testbeschreibung");
		a.addImage(new ArticleImage(83, true));
		a.removeImage(new ArticleImage(83, true));
		a.addKeyword("key");
		a.addKeyword("lAMe");
		a.addKeyword("    WORD");
		a.removeKeyword("   lame");
		System.out.println(a.getKeywords());
		a.addPrice(new ArticlePrice(15));
		a.setAttribute(4, "asdf");
		a.addCategory(new ArticleCategory(123));
		System.out.println(a);
	}
}
