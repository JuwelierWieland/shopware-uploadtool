package land.sebastianwie.shopware_uploadtool.resources.article;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import land.sebastianwie.shopware_uploadtool.util.JSONUtils;
import land.sebastianwie.shopware_uploadtool.util.ToJsonConvertible;

import org.json.JSONArray;
import org.json.JSONObject;

public class ArticleDetail implements ToJsonConvertible {

	private int id = -1;
	private int pseudoId = -1;
	private String number;
	private String newNumber;
	private boolean usesNewNumber;
	private byte variantActive = -1;

	private String additionalText;

	private String ean;

	private int inStock = -1;
	private int stockMin = -1;

	private double height = -1;
	private double width = -1;
	private double weight = -1;

	private String packUnit;
	private String purchaseUnit;
	private String referenceUnit;

	private String shippingTime;
	private byte shippingFree = -1;

	private String[] attributes;

	private Date releaseDate;

	private List<ArticlePrice> prices;
	private boolean replacePrices = true;

	private Set<ArticleConfiguratorOption> configuratorOptions;

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getPseudoId() {
		if (id != -1)
			return id;
		return pseudoId;
	}
	
	public void setPseudoId(int pseudoId) {
		this.pseudoId = pseudoId;
	}

	public boolean isVariantActive() {
		return variantActive == 1;
	}

	public boolean activeVariantActive() {
		return variantActive != -1;
	}

	public void setVariantActive(boolean sale) {
		this.variantActive = (byte) (sale ? 1 : 0);
	}

	public void unsetVariantActive() {
		this.variantActive = -1;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
		if (!usesNewNumber)
			this.newNumber = number;
	}

	public String getNewNumber() {
		return newNumber;
	}

	public void setNewNumber(String newNumber) {
		this.newNumber = newNumber;
		this.usesNewNumber = true;
	}

	public void resetNewNumber() {
		this.newNumber = this.number;
		this.usesNewNumber = false;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public List<ArticlePrice> getPrices() {
		return prices;
	}

	public void setPrices(List<ArticlePrice> prices) {
		this.prices = prices;
	}

	public void setPrices(ArticlePrice[] prices) {
		for (ArticlePrice price : prices)
			this.addPrice(price);
	}

	public void addPrice(ArticlePrice price) {
		if (this.prices == null)
			this.prices = new ArrayList<>();
		this.prices.add(price);
	}

	public boolean removePrice(ArticlePrice price) {
		if (this.prices == null)
			return false;
		return this.prices.remove(price);
	}

	public void replacePrices() {
		this.replacePrices = true;
	}

	public void keepPrices() {
		this.replacePrices = false;
	}

	public Set<ArticleConfiguratorOption> getConfiguratorOptions() {
		return configuratorOptions;
	}

	public void setConfiguratorOptions(Set<ArticleConfiguratorOption> configuratorGroups) {
		this.configuratorOptions = configuratorGroups;
	}

	public void setConfiguratorOptions(ArticleConfiguratorOption[] configuratorGroups) {
		for (ArticleConfiguratorOption group : configuratorGroups)
			this.addConfiguratorOption(group);
	}

	public void addConfiguratorOption(ArticleConfiguratorOption configuratorGroup) {
		if (this.configuratorOptions == null)
			this.configuratorOptions = new HashSet<>();
		this.configuratorOptions.add(configuratorGroup);
	}

	public boolean removeConfiguratorOption(ArticleConfiguratorOption group) {
		if (this.configuratorOptions == null)
			return false;
		return this.configuratorOptions.remove(group);
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getPackUnit() {
		return packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public String getPurchaseUnit() {
		return purchaseUnit;
	}

	public void setPurchaseUnit(String purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
	}

	public String getReferenceUnit() {
		return referenceUnit;
	}

	public void setReferenceUnit(String referenceUnit) {
		this.referenceUnit = referenceUnit;
	}

	public int getStockMin() {
		return stockMin;
	}

	public void setStockMin(int stockMin) {
		this.stockMin = stockMin;
	}

	public int getInStock() {
		return inStock;
	}

	public void setInStock(int inStock) {
		this.inStock = inStock;
	}

	public boolean isShippingFree() {
		return shippingFree == 1;
	}

	public boolean activeShippingFree() {
		return shippingFree != -1;
	}

	public void setShippingFree(boolean shippingFree) {
		this.shippingFree = (byte) (shippingFree ? 1 : 0);
	}

	public void unsetShippingFree() {
		this.shippingFree = -1;
	}

	public String getShippingTime() {
		return shippingTime;
	}

	public void setShippingTime(String shippingTime) {
		this.shippingTime = shippingTime;
	}

	public String getAdditionalText() {
		return additionalText;
	}

	public void setAdditionalText(String additionalText) {
		this.additionalText = additionalText;
	}

	public String[] getAttributes() {
		return attributes;
	}

	public String getAttribute(int index) {
		if (attributes == null)
			return null;
		return attributes[index - 1];
	}

	public void setAttributes(String[] attributes) {
		if (attributes.length == 20)
			this.attributes = attributes;
	}

	public void setAttribute(int index, String attribute) {
		if (this.attributes == null)
			this.attributes = new String[20];
		this.attributes[index - 1] = attribute;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public boolean isReplacePrices() {
		return replacePrices;
	}

	public void setReplacePrices(boolean replacePrices) {
		this.replacePrices = replacePrices;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();

		JSONObject attributes = new JSONObject();
		JSONArray prices = JSONUtils.createJSONArray(this.getPrices());
		JSONArray configuratorOptions = JSONUtils.createJSONArray(this.getConfiguratorOptions());

		if (this.getAttributes() != null) {
			for (int i = 1; i <= 20; i++)
				attributes.put("attr" + i, this.getAttribute(i));
			result.put("attribute", attributes);
		}

		if (activeVariantActive())
			result.put("active", isVariantActive() ? 1 : 0);
		if (getShippingTime() != null)
			result.put("shippingTime", getShippingTime());
		if (activeShippingFree())
			result.put("shippingFree", isShippingFree());
		if (getStockMin() >= 0)
			result.put("stockMin", getStockMin());
		if (getInStock() >= 0)
			result.put("inStock", getInStock());
		if (getNumber() != null)
			result.put("number", usesNewNumber ? getNewNumber() : getNumber());
		if (getEan() != null)
			result.put("ean", getEan());
		if (getPackUnit() != null)
			result.put("packUnit", getPackUnit());
		if (getPurchaseUnit() != null)
			result.put("purchaseUnit", getPurchaseUnit());
		if (getReferenceUnit() != null)
			result.put("referenceUnit", getReferenceUnit());
		if (getAdditionalText() != null)
			result.put("additionalText", getAdditionalText());
		if (getHeight() >= 0)
			result.put("height", getHeight());
		if (getWidth() >= 0)
			result.put("width", getWidth());
		if (getWeight() >= 0)
			result.put("weight", getWeight());
		if (getReleaseDate() != null)
			result.put("releaseDate", Article.dateformat.format(getReleaseDate()));

		if (prices != null && prices.length() > 0) {
			result.put("prices", prices);
			result.put("__option_prices", new JSONObject().put("replace", this.isReplacePrices()));
		}

		if (configuratorOptions != null && configuratorOptions.length() > 0) {
			result.put("configuratorOptions", configuratorOptions);
		}

		return result;
	}

	public static ArticleDetail createFromJSONObject(JSONObject data) {
		ArticleDetail result = new ArticleDetail();
		if (data.has("id") && !data.isNull("id"))
			result.setId(data.getInt("id"));
		if (data.has("active") && !data.isNull("active"))
			result.setVariantActive(data.getInt("active") == 1);
		if (data.has("shippingTime") && !data.isNull("shippingTime"))
			result.setShippingTime(data.getString("shippingTime"));
		if (data.has("shippingFree") && !data.isNull("shippingFree"))
			result.setShippingFree(data.getBoolean("shippingFree"));
		if (data.has("stockMin") && !data.isNull("stockMin"))
			result.setStockMin(data.getInt("stockMin"));
		if (data.has("inStock") && !data.isNull("inStock"))
			result.setInStock(data.getInt("inStock"));
		if (data.has("number") && !data.isNull("number"))
			result.setNumber(data.getString("number"));
		if (data.has("ean") && !data.isNull("ean"))
			result.setEan(data.getString("ean"));
		if (data.has("packUnit") && !data.isNull("packUnit"))
			result.setPackUnit(data.getString("packUnit"));
		if (data.has("purchaseUnit") && !data.isNull("purchaseUnit"))
			result.setPurchaseUnit(data.getString("purchaseUnit"));
		if (data.has("referenceUnit") && !data.isNull("referenceUnit"))
			result.setReferenceUnit(data.getString("referenceUnit"));
		if (data.has("additionalText") && !data.isNull("additionalText"))
			result.setAdditionalText(data.getString("additionalText"));
		if (data.has("height") && !data.isNull("height"))
			result.setHeight(data.getDouble("height"));
		if (data.has("width") && !data.isNull("width"))
			result.setWidth(data.getDouble("width"));
		if (data.has("weight") && !data.isNull("weight"))
			result.setWeight(data.getDouble("weight"));
		if (data.has("releaseDate") && !data.isNull("releaseDate"))
			try {
				result.setReleaseDate(Article.dateformat.parse(data.getString("releaseDate")));
			} catch (ParseException e) {
				result.setReleaseDate(new Date());
			}

		if (data.has("attribute") && !data.isNull("attribute")) {
			JSONObject attribute = data.getJSONObject("attribute");
			for (int i = 1; i <= 20; i++) {
				if (!attribute.isNull("attr" + i))
					result.setAttribute(i, attribute.getString("attr" + i));
			}
		}

		if (data.has("prices") && !data.isNull("prices")) {
			JSONArray prices = data.getJSONArray("prices");
			for (int i = 0; i < prices.length(); i++) {
				result.addPrice(ArticlePrice.createFromJSONObject(prices.getJSONObject(i)));
			}
		}

		if (data.has("configuratorOptions") && !data.isNull("configuratorOptions")) {
			JSONArray configuratorOptions = data.getJSONArray("configuratorOptions");
			for (int i = 0; i < configuratorOptions.length(); i++) {
				result.addConfiguratorOption(ArticleConfiguratorOption.createFromJSONObject(configuratorOptions
						.getJSONObject(i)).convert());
			}
		}

		return result;
	}
}