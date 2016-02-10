package land.sebastianwie.shopware_uploadtool.topm;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import land.sebastianwie.shopware_uploadtool.util.DocumentUtils;

import org.dom4j.Document;
import org.dom4j.Node;

/**
 * Mit dieser Klasse kann man TOPM-Artikelnummern in Shopware-Artikelnummern umwandeln.
 * @author Sebastian Wieland
 *
 */
public class SupplierMapper {

	private Document supplierXML;
	private static final String DELIMITER = "-";

	/**
	 * Erstellt einen neuen Mapper
	 * @param suppliers Die XML-Datei, in der die Hersteller aufgelistet sind.
	 */
	public SupplierMapper(Document suppliers) {
		this.supplierXML = suppliers;
	}

	/**
	 * Findet die Shopware-Hersteller-Id für einen Artikel heraus.
	 * @param topmSupplier
	 * @param ordernumber
	 * @return
	 */
	public int getShopwareSupplierId(int topmSupplier, String ordernumber) {
		@SuppressWarnings("unchecked")
		List<Node> suppliers = supplierXML.selectNodes("/suppliers/supplier");

		for (Node supplier : suppliers) {
			try {
				Number xmlTopmID = supplier.numberValueOf("topmid");
				if (xmlTopmID != null && xmlTopmID.intValue() == topmSupplier) {
					Pattern orderNumberPattern = Pattern.compile(supplier.valueOf("bnregex"));
					Matcher orderNumberMatcher = orderNumberPattern.matcher(ordernumber);
					if (orderNumberMatcher.matches()) {
						Number result = supplier.numberValueOf("swsid");
						if (result != null)
							return result.intValue();
					}
				}
			} catch (Exception e) {
				System.err.println(e);
			}
		}

		return -1;
	}
	
	/**
	 * Generiert die Shopware-Artikelnummer für einen Artikel. Dieser Artikel muss nicht unbedingt in Shopware existieren.
	 * @param topmSupplier
	 * @param ordernumber
	 * @return
	 */
	public String toShopwareArtNr(int topmSupplier, String ordernumber) {
		int swSupplierID = getShopwareSupplierId(topmSupplier, ordernumber);
		if (swSupplierID == -1)
			return null;
		return swSupplierID + DELIMITER + mask(ordernumber);
	}
	
	private static String mask(String ordernumber) {
		return ordernumber.replaceAll("[^\\w.-]", "_");
	}
	
	public static void main(String[] args) {
		Document suppliers = DocumentUtils.fromURL("/home/sebastian/tmp/suppliers.xml");
		SupplierMapper mapper = new SupplierMapper(suppliers);
		System.out.println(mapper.toShopwareArtNr(475, "7&234.56"));
	}

}
