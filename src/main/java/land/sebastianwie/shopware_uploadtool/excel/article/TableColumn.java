package land.sebastianwie.shopware_uploadtool.excel.article;

public enum TableColumn {
	// @f:off
	ID("id", "Shopware-ID", (byte) 1, "Varianten-Shopware-ID"),
	NUMBER("number", "Artikelnummer", (byte) 1, "Varianten-Artikelnummer"),
	NEW_NUMBER("newNumber", "Neue Artikelnummer", (byte) 1, "Neue Varianten-Artikelnummer"),
	PARENT_ID("parentId", "Eltern-Shopware-ID", (byte) 2),
	PARENT_NUMBER("parentNumber", "Eltern-Artikelnummer", (byte) 2),
	ACTIVE("active", "Aktiv"),
	HIGHLIGHT("highlight", "Hervorgehoben"),
	SUPPLIER("supplierId", "Hersteller-ID"),
	EAN("ean", "EAN", (byte) 1),
	NAME("name", "Name"),
	DESCRIPTION("description", "Kurzbeschreibung"),
	DESCRIPTION_LONG("descriptionLong", "Beschreibung"),
	ADDITIONAL_TEXT("additionalText", "Varianten-Zusatztext", (byte) 1),
	PRICES("prices", "Preise", (byte) 1),
	CUSTOM_PRODUCTS("customProducts", "Individuelle Optionen"),
	ATTR18("attr18", "Gravurschema", (byte) 1),
	TEMPLATE("template", "Template"),
	KEYWORDS("keywords", "Suchbegriffe"),
	CATEGORIES("categories", "Kategorien"),
	FILTER_GROUP("filterGroupId", "Eigenschafts-Set-ID"),
	PROPERTY_VALUES("propertyValues", "Eigenschaften"),
	IN_STOCK("inStock", "Bestand", (byte) 1),
	STOCK_MIN("stockMin", "Mindestbestand", (byte) 1),
	SALE("sale", "Abverkauf", (byte) 1),
	VARIANT_ACTIVE("variantActive", "Variante Aktiv", (byte) 1),
	CONFIGURATOR_GROUPS("configuratorGroups", "Konfigurator-Gruppen"),
	VARIANT("variant", "Variante", (byte) 1),
	AVAILABLE_FROM("availableFrom", "Verfügbar ab"),
	RELEASE_DATE("releaseDate", "Erscheinungsdatum", (byte) 1),
	SHIPPING_TIME("shippingTime", "Lieferzeit", (byte) 1),
	NOTIFICATION("notification", "Benachrichtigung"),
	PACK_UNIT("packUnit", "Verpackungseinheit", (byte) 1),
	REFERENCE_UNIT("referenceUnit", "Referenzeinheit", (byte) 1),
	PURCHASE_UNIT("purchaseUnit", "Inhalt", (byte) 1),
	SHIPPING_FREE("shippingFree", "Versandkostenfrei", (byte) 1),
	HEIGHT("height", "Höhe", (byte) 1),
	WIDTH("width", "Breite", (byte) 1),
	WEIGHT("weight", "Gewicht", (byte) 1),
	TAX_ID("taxId", "Steuergruppen-ID"),
	IMAGES("images", "Bilder"),
	ERRORS("errors", "Fehlermeldungen", (byte) 1),
	OPTION_REPLACE_CATEGORIES("replaceCategories", "Kategorien ersetzen"),
	OPTION_REPLACE_PRICES("replacePrices", "Preise ersetzen", (byte) 1),
	OPTION_REPLACE_IMAGES("replaceImages", "Bilder ersetzen");
	// @f:on

	private final String property;
	private final String label;
	private final String vLabel;
	private final byte variant;

	TableColumn(String property, String label) {
		this(property, label, (byte) 0);
	}

	TableColumn(String property, String label, byte variant) {
		this(property, label, variant, null);
	}

	TableColumn(String property, String label, byte variant, String vLabel) {
		this.property = property;
		this.label = label;
		this.variant = variant;
		this.vLabel = vLabel;
	}

	public String property() {
		return property;
	}

	public String label() {
		return label;
	}

	public boolean variant() {
		return variant == 1 || variant == 2;
	}

	public boolean variantOnly() {
		return variant == 2;
	}

	public boolean noVariant() {
		return variant == 0;
	}

	public String vLabel() {
		if (vLabel == null)
			return label;
		return vLabel;
	}
}
