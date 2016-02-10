package land.sebastianwie.shopware_uploadtool.excel.article;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import land.sebastianwie.shopware_uploadtool.api.ApiConnector;
import land.sebastianwie.shopware_uploadtool.resources.article.Article;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticleVariant;
import land.sebastianwie.shopware_uploadtool.util.ArrayUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelCreator {
	private Workbook wb;
	private Sheet articles;
	private Sheet variants;

	private boolean withErrors;

	private CellStyle headingStyle;
	private CellStyle idStyle;
	private CellStyle standardStyle;
	private CellStyle wrapStyle;
	private CellStyle dateStyle;

	private int row;
	private int vRow;

	/**
	 * Reihenfolge der Spalten in den Excel-Tabellen, die erstellt werden.
	 * Nutzer haben sich nicht an die Reihenfolge zu halten und können Spalten
	 * anordnen, wie sie wollen.
	 */
	private static final TableColumn[] order = { TableColumn.ID, TableColumn.NUMBER, TableColumn.PARENT_ID, TableColumn.PARENT_NUMBER,
			TableColumn.ACTIVE, TableColumn.CONFIGURATOR_GROUPS, TableColumn.VARIANT, TableColumn.VARIANT_ACTIVE, TableColumn.ERRORS,
			TableColumn.NEW_NUMBER, TableColumn.HIGHLIGHT, TableColumn.SUPPLIER, TableColumn.EAN, TableColumn.NAME,
			TableColumn.DESCRIPTION, TableColumn.DESCRIPTION_LONG, TableColumn.ADDITIONAL_TEXT, TableColumn.PRICES,
			TableColumn.OPTION_REPLACE_PRICES, TableColumn.CUSTOM_PRODUCTS, TableColumn.ATTR18, TableColumn.TEMPLATE, TableColumn.KEYWORDS,
			TableColumn.CATEGORIES, TableColumn.OPTION_REPLACE_CATEGORIES, TableColumn.FILTER_GROUP, TableColumn.PROPERTY_VALUES,
			TableColumn.IN_STOCK, TableColumn.STOCK_MIN, TableColumn.SHIPPING_FREE, TableColumn.AVAILABLE_FROM, TableColumn.RELEASE_DATE,
			TableColumn.SHIPPING_TIME, TableColumn.NOTIFICATION, TableColumn.PACK_UNIT, TableColumn.PURCHASE_UNIT,
			TableColumn.REFERENCE_UNIT, TableColumn.HEIGHT, TableColumn.WIDTH, TableColumn.WEIGHT, TableColumn.TAX_ID, TableColumn.IMAGES,
			TableColumn.OPTION_REPLACE_IMAGES };

	public ExcelCreator() {
		this(false);
	}

	/**
	 * Erstellt einen neuen ExcelCreator.
	 * 
	 * @param withErrors
	 *            Gibt an, ob eine "Fehlerursache"-Spalte mit erstellt werden
	 *            soll.
	 */
	public ExcelCreator(boolean withErrors) {
		this.withErrors = withErrors;
		wb = new XSSFWorkbook();
		articles = wb.createSheet(WorkbookUtil.createSafeSheetName("Artikelliste"));
		variants = wb.createSheet(WorkbookUtil.createSafeSheetName("Varianten"));
		articles.createFreezePane(3, 1);
		variants.createFreezePane(2, 1);

		Font headingFont = wb.createFont();
		headingFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headingFont.setFontName("Arial");
		headingFont.setFontHeightInPoints((short) 8);

		headingStyle = wb.createCellStyle();
		headingStyle.setFont(headingFont);
		// headingStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headingStyle.setAlignment(CellStyle.ALIGN_CENTER);

		Font idFont = wb.createFont();
		idFont.setFontName("Monospace");
		idFont.setFontHeightInPoints((short) 8);

		idStyle = wb.createCellStyle();
		idStyle.setFont(idFont);
		idStyle.setAlignment(CellStyle.ALIGN_LEFT);

		Font standardFont = wb.createFont();
		standardFont.setFontName("Arial");
		standardFont.setFontHeightInPoints((short) 8);

		standardStyle = wb.createCellStyle();
		standardStyle.setFont(standardFont);
		// standardStyle.setWrapText(true);

		wrapStyle = wb.createCellStyle();
		wrapStyle.setFont(standardFont);
		wrapStyle.setWrapText(true);

		dateStyle = wb.createCellStyle();
		dateStyle.setFont(standardFont);
		dateStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("DD.MM.YYYY"));

		this.createHeading();
	}

	/**
	 * Erstellt die Überschriften nach der Reihenfolge, die angegeben wurde.
	 */
	private void createHeading() {
		Row headingRow = articles.createRow(0);
		Row vHeadingRow = variants.createRow(0);
		int i = 0;
		int j = 0;
		for (TableColumn heading : order) {
			if (!heading.variantOnly()) {
				if (!withErrors && heading == TableColumn.ERRORS)
					continue;
				Cell cell = headingRow.createCell(i++);
				cell.setCellValue(heading.label());
				cell.setCellStyle(headingStyle);
			}
			if (heading.variant()) {
				Cell vCell = vHeadingRow.createCell(j++);
				vCell.setCellStyle(headingStyle);
				vCell.setCellValue(heading.vLabel());
			}
		}
	}

	public void saveAs(String filename) {
		FileOutputStream fileout = null;
		try {
			fileout = new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}

		try {
			wb.write(fileout);
			fileout.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void saveAs(File file) {
		FileOutputStream fileout = null;
		try {
			fileout = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}

		try {
			wb.write(fileout);
			fileout.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Fügt einen Artikel hinzu.
	 * 
	 * @param article
	 */
	public void addArticle(Article article) {
		this.addArticle(article, null);
	}

	/**
	 * Fügt eine Artikelvariante hinzu.
	 * 
	 * @param variant
	 */
	public void addVariant(ArticleVariant variant) {
		this.addVariant(variant, null);
	}

	/**
	 * Fügt einen Artikel hinzu.
	 * 
	 * @param article
	 * @param error
	 *            Fehlermeldung, die in die Fehler-Spalte kommen wird.
	 */
	public void addArticle(Article article, String error) {
		if (article == null)
			return;

		if (article.getVariants() != null && article.getVariants().size() > 0)
			for (ArticleVariant variant : article.getVariants())
				this.addVariant(variant);

		Row row = articles.createRow(++this.row);
		for (int i = 0, j = 0; i < order.length; i++) {
			if (order[i].variantOnly() || (order[i] == TableColumn.ERRORS && !withErrors))
				continue;
			Cell cell = row.createCell(j++);
			cell.setCellStyle(standardStyle);
			switch (order[i]) {
			case ID:
				if (article.getId() != -1)
					cell.setCellValue(article.getId());
				cell.setCellStyle(idStyle);
				break;
			case NUMBER:
				if (article.getNumber() != null)
					cell.setCellValue(article.getNumber());
				break;
			case NEW_NUMBER:
				if (article.getNewNumber() != null)
					cell.setCellValue(article.getNewNumber());
				break;
			case ACTIVE:
				if (article.activeActive())
					cell.setCellValue(article.isActive());
				break;
			case HIGHLIGHT:
				if (article.activeHighlight())
					cell.setCellValue(article.isHighlight());
				break;
			case SUPPLIER:
				if (article.getSupplierId() != -1)
					cell.setCellValue(article.getSupplierId());
				break;
			case EAN:
				if (article.getEan() != null)
					cell.setCellValue(article.getEan());
				break;
			case NAME:
				if (article.getName() != null)
					cell.setCellValue(article.getName());
				break;
			case DESCRIPTION:
				if (article.getDescription() != null)
					cell.setCellValue(article.getDescription());
				break;
			case DESCRIPTION_LONG:
				if (article.getDescriptionLong() != null)
					cell.setCellValue(article.getDescriptionLong());
				break;
			case PRICES:
				if (article.getPrices() != null)
					cell.setCellValue(ArrayUtils.join("\n", article.getPrices()));
				cell.setCellStyle(wrapStyle);
				break;
			case KEYWORDS:
				if (article.getKeywords() != null)
					cell.setCellValue(ArrayUtils.join(", ", article.getKeywords()));
				break;
			case CATEGORIES:
				if (article.getCategories() != null)
					cell.setCellValue(ArrayUtils.join("\n", article.getCategories()));
				cell.setCellStyle(wrapStyle);
				break;
			case FILTER_GROUP:
				if (article.getFilterGroupID() != -1)
					cell.setCellValue(article.getFilterGroupID());
				break;
			case PROPERTY_VALUES:
				if (article.getPropertyValues() != null)
					cell.setCellValue(ArrayUtils.join("\n", article.getPropertyValues()));
				cell.setCellStyle(wrapStyle);
				break;
			case IN_STOCK:
				if (article.getInStock() != -1)
					cell.setCellValue(article.getInStock());
				break;
			case STOCK_MIN:
				if (article.getStockMin() != -1)
					cell.setCellValue(article.getStockMin());
				break;
			case SALE:
			case VARIANT_ACTIVE:
				if (article.activeVariantActive())
					cell.setCellValue(article.isVariantActive());
				break;
			case CONFIGURATOR_GROUPS:
				if (article.getConfiguratorGroups() != null)
					cell.setCellValue(ArrayUtils.join("\n", article.getConfiguratorGroups()));
				cell.setCellStyle(wrapStyle);
				break;
			case VARIANT:
				if (article.getMainDetail().getConfiguratorOptions() != null)
					cell.setCellValue(ArrayUtils.join("\n", article.getMainDetail().getConfiguratorOptions()));
				cell.setCellStyle(wrapStyle);
				break;
			case AVAILABLE_FROM:
				if (article.getAvailableFrom() != null)
					cell.setCellValue(article.getAvailableFrom());
				cell.setCellStyle(dateStyle);
				break;
			case RELEASE_DATE:
				if (article.getReleaseDate() != null)
					cell.setCellValue(article.getReleaseDate());
				cell.setCellStyle(dateStyle);
				break;
			case SHIPPING_TIME:
				if (article.getShippingTime() != null)
					cell.setCellValue(article.getShippingTime());
				break;
			case NOTIFICATION:
				if (article.activeNotification())
					cell.setCellValue(article.isNotification());
				break;
			case PACK_UNIT:
				if (article.getPackUnit() != null)
					cell.setCellValue(article.getPackUnit());
				break;
			case PURCHASE_UNIT:
				if (article.getPurchaseUnit() != null)
					cell.setCellValue(article.getPurchaseUnit());
				break;
			case REFERENCE_UNIT:
				if (article.getReferenceUnit() != null)
					cell.setCellValue(article.getReferenceUnit());
				break;
			case HEIGHT:
				if (article.getHeight() >= 0)
					cell.setCellValue(article.getHeight());
				break;
			case WIDTH:
				if (article.getWidth() >= 0)
					cell.setCellValue(article.getWidth());
				break;
			case WEIGHT:
				if (article.getWeight() >= 0)
					cell.setCellValue(article.getWeight());
				break;
			case TAX_ID:
				if (article.getTaxId() != -1)
					cell.setCellValue(article.getTaxId());
				break;
			case IMAGES:
				if (article.getImages() != null)
					cell.setCellValue(ArrayUtils.join("\n", article.getImages()));
				cell.setCellStyle(wrapStyle);
				break;
			case ERRORS:
				if (withErrors && error != null)
					cell.setCellValue(error);
				break;
			case OPTION_REPLACE_CATEGORIES:
				cell.setCellValue(article.isReplaceCategories());
				break;
			case OPTION_REPLACE_IMAGES:
				cell.setCellValue(article.isReplaceImages());
				break;
			case OPTION_REPLACE_PRICES:
				cell.setCellValue(article.isReplacePrices());
				break;
			case CUSTOM_PRODUCTS:
				if (article.getCustomProductId() != -1)
					cell.setCellValue(article.getCustomProductId());
				break;
			case SHIPPING_FREE:
				if (article.activeShippingFree())
					cell.setCellValue(article.isShippingFree());
				break;
			case ADDITIONAL_TEXT:
				if (article.getAdditionalText() != null)
					cell.setCellValue(article.getAdditionalText());
				break;
			case TEMPLATE:
				if (article.getTemplate() != null)
					cell.setCellValue(article.getTemplate());
				break;
			case ATTR18:
				if (article.getMainDetail().getAttribute(18) != null)
					cell.setCellValue(article.getMainDetail().getAttribute(18));
				break;
			case PARENT_NUMBER:
			case PARENT_ID:
				break;
			}
		}
	}

	/**
	 * Fügt eine Artikelvariante hinzu.
	 * 
	 * @param variant
	 * @param error
	 */
	public void addVariant(ArticleVariant variant, String error) {
		if (variant == null)
			return;
		Row row = variants.createRow(++this.vRow);
		for (int i = 0, j = 0; i < order.length; i++) {
			if (!order[i].variant()) {
				continue;
			}
			Cell cell = row.createCell(j++);
			cell.setCellStyle(standardStyle);
			switch (order[i]) {
			case ID:
				if (variant.getDetail().getId() != -1)
					cell.setCellValue(variant.getDetail().getId());
				cell.setCellStyle(idStyle);
				break;
			case PARENT_ID:
				if (variant.getMotherId() != -1)
					cell.setCellValue(variant.getMotherId());
				cell.setCellStyle(idStyle);
				break;
			case PARENT_NUMBER:
				if (variant.getMotherNumber() != null)
					cell.setCellValue(variant.getMotherNumber());
				break;
			case NUMBER:
				if (variant.getDetail().getNumber() != null)
					cell.setCellValue(variant.getDetail().getNumber());
				break;
			case NEW_NUMBER:
				if (variant.getDetail().getNewNumber() != null)
					cell.setCellValue(variant.getDetail().getNewNumber());
				break;
			case EAN:
				if (variant.getDetail().getEan() != null)
					cell.setCellValue(variant.getDetail().getEan());
				break;
			case ADDITIONAL_TEXT:
				if (variant.getDetail().getAdditionalText() != null)
					cell.setCellValue(variant.getDetail().getAdditionalText());
				break;
			case PRICES:
				if (variant.getDetail().getPrices() != null)
					cell.setCellValue(ArrayUtils.join("\n", variant.getDetail().getPrices()));
				cell.setCellStyle(wrapStyle);
				break;
			case IN_STOCK:
				if (variant.getDetail().getInStock() != -1)
					cell.setCellValue(variant.getDetail().getInStock());
				break;
			case STOCK_MIN:
				if (variant.getDetail().getStockMin() != -1)
					cell.setCellValue(variant.getDetail().getStockMin());
				break;
			case SALE:
			case VARIANT_ACTIVE:
				if (variant.getDetail().activeVariantActive())
					cell.setCellValue(variant.getDetail().isVariantActive());
				break;
			case VARIANT:
				if (variant.getDetail().getConfiguratorOptions() != null)
					cell.setCellValue(ArrayUtils.join("\n", variant.getDetail().getConfiguratorOptions()));
				cell.setCellStyle(wrapStyle);
				break;
			case RELEASE_DATE:
				if (variant.getDetail().getReleaseDate() != null)
					cell.setCellValue(variant.getDetail().getReleaseDate());
				cell.setCellStyle(dateStyle);
				break;
			case SHIPPING_TIME:
				if (variant.getDetail().getShippingTime() != null)
					cell.setCellValue(variant.getDetail().getShippingTime());
				break;
			case PACK_UNIT:
				if (variant.getDetail().getPackUnit() != null)
					cell.setCellValue(variant.getDetail().getPackUnit());
				break;
			case REFERENCE_UNIT:
				if (variant.getDetail().getReferenceUnit() != null)
					cell.setCellValue(variant.getDetail().getReferenceUnit());
				break;
			case PURCHASE_UNIT:
				if (variant.getDetail().getPurchaseUnit() != null)
					cell.setCellValue(variant.getDetail().getPurchaseUnit());
				break;
			case SHIPPING_FREE:
				if (variant.getDetail().activeShippingFree())
					cell.setCellValue(variant.getDetail().isShippingFree());
				break;
			case HEIGHT:
				if (variant.getDetail().getHeight() != -1)
					cell.setCellValue(variant.getDetail().getHeight());
				break;
			case WEIGHT:
				if (variant.getDetail().getWeight() != -1)
					cell.setCellValue(variant.getDetail().getWeight());
				break;
			case WIDTH:
				if (variant.getDetail().getWidth() != -1)
					cell.setCellValue(variant.getDetail().getWidth());
				break;
			case OPTION_REPLACE_PRICES:
				cell.setCellValue(variant.getDetail().isReplacePrices());
				break;
			case ERRORS:
				if (withErrors && error != null)
					cell.setCellValue(error);
				break;
			case ATTR18:
				if (variant.getDetail().getAttribute(18) != null)
					cell.setCellValue(variant.getDetail().getAttribute(18));
				break;
			case ACTIVE:
			case AVAILABLE_FROM:
			case CATEGORIES:
			case CUSTOM_PRODUCTS:
			case CONFIGURATOR_GROUPS:
			case DESCRIPTION:
			case DESCRIPTION_LONG:
			case FILTER_GROUP:
			case HIGHLIGHT:
			case IMAGES:
			case KEYWORDS:
			case NAME:
			case NOTIFICATION:
			case OPTION_REPLACE_CATEGORIES:
			case OPTION_REPLACE_IMAGES:
			case PROPERTY_VALUES:
			case SUPPLIER:
			case TAX_ID:
			case TEMPLATE:
				break;
			}
		}
	}

	public Workbook getWorkbook() {
		return wb;
	}

	public static void main(String[] args) throws IOException {
		String hostname = "shopware.p234623.webspaceconfig.de";
		String username = "wieland";
		String password = "seZ45bdcRF023J4GHVftogFAL6F909XtcrJLMc0l";

		ExcelCreator creator = new ExcelCreator();
		ApiConnector ac = new ApiConnector(hostname, username, password);
		for (int i = 1; i <= 500; i++) {
			Article a = ac.getArticle(i);
			System.out.println(a);
			creator.addArticle(a);
		}
		creator.saveAs("test.xlsx");
	}

}
