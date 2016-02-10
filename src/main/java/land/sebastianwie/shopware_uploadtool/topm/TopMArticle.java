package land.sebastianwie.shopware_uploadtool.topm;

import land.sebastianwie.shopware_uploadtool.resources.article.Article;

public class TopMArticle {
	private int herstellerID = -1;
	private int bestand = -1;
	private String bestellnummer = "";
	
	
	public int getHerstellerID() {
		return herstellerID;
	}
	public void setHerstellerID(int herstellerID) {
		this.herstellerID = herstellerID;
	}
	public int getInStock() {
		return bestand;
	}
	public void setInStock(int bestand) {
		this.bestand = bestand;
	}
	public String getBestellnummer() {
		return bestellnummer;
	}
	public void setBestellnummer(String bestellnummer) {
		this.bestellnummer = bestellnummer;
	}
	@Override
	public String toString() {
		return "TopMArticle [herstellerID=" + herstellerID + ", bestand=" + bestand + ", Bestellnummer="
				+ bestellnummer + "]";
	}
	
	public Article toArticle(SupplierMapper mapper) {
		Article result = new Article();
		result.setInStock(bestand);
		String swArtNr = mapper.toShopwareArtNr(herstellerID, bestellnummer);
		if (swArtNr == null)
			return null;
		result.setNumber(swArtNr);
		return result;
	}
	
}