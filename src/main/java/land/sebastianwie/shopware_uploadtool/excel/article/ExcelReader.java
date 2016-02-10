package land.sebastianwie.shopware_uploadtool.excel.article;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import land.sebastianwie.shopware_uploadtool.resources.article.Article;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticleCategory;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticleConfiguratorGroup;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticleConfiguratorOption;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticleImage;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticlePrice;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticleProperty;
import land.sebastianwie.shopware_uploadtool.resources.article.ArticleVariant;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelReader {

	private Workbook wb;
	private Sheet articles;
	private Sheet variants;

	private Iterator<Row> rowIterator;
	private Iterator<Row> vRowIterator;

	private Map<String, TableColumn> headingLabelMap;
	private Map<String, TableColumn> headingVLabelMap;
	private Map<String, TableColumn> headingPropertyMap;
	private Map<TableColumn, Integer> indexMap;
	private Map<TableColumn, Integer> vIndexMap;

	public static final DateFormat df = new SimpleDateFormat("YYYY-MM-dd");

	public ExcelReader(String filename) {
		try {
			wb = WorkbookFactory.create(new File(filename));
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		init();
	}

	public ExcelReader(File filename) {
		try {
			wb = WorkbookFactory.create(filename);
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		init();
	}

	public ExcelReader(Workbook wb) {
		this.wb = wb;
		init();
	}

	private void init() {
		if (wb.getNumberOfSheets() >= 1)
			articles = wb.getSheetAt(0);
		else
			articles = null;
		if (wb.getNumberOfSheets() >= 2)
			variants = wb.getSheetAt(1);
		else
			variants = null;

		headingLabelMap = new HashMap<String, TableColumn>();
		headingVLabelMap = new HashMap<String, TableColumn>();
		headingPropertyMap = new HashMap<String, TableColumn>();
		for (TableColumn heading : TableColumn.values()) {
			headingLabelMap.put(heading.label(), heading);
			headingVLabelMap.put(heading.vLabel(), heading);
			headingPropertyMap.put(heading.property(), heading);
		}

		indexMap = new HashMap<>();
		vIndexMap = new HashMap<>();

		this.readHeading();

		if (articles != null) {
			rowIterator = articles.iterator();
			if (rowIterator.hasNext())
				rowIterator.next();
		}

		if (variants != null) {
			vRowIterator = variants.iterator();
			if (vRowIterator.hasNext())
				vRowIterator.next();
		}
	}

	private void readHeading() {
		if (articles != null) {
			Row headingRow = articles.getRow(0);
			for (Cell cell : headingRow) {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				String value = cell.getStringCellValue().trim();
				TableColumn column = null;
				if ((column = headingPropertyMap.get(value)) != null) {
					indexMap.put(column, cell.getColumnIndex());
				} else if ((column = headingLabelMap.get(value)) != null) {
					indexMap.put(column, cell.getColumnIndex());
				}
			}
		}
		if (variants != null) {
			Row headingVRow = variants.getRow(0);
			for (Cell cell : headingVRow) {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				String value = cell.getStringCellValue().trim();
				TableColumn column = null;
				if ((column = headingPropertyMap.get(value)) != null) {
					vIndexMap.put(column, cell.getColumnIndex());
				} else if ((column = headingVLabelMap.get(value)) != null) {
					vIndexMap.put(column, cell.getColumnIndex());
				}
			}
		}
	}

	public int getNumberOfArticles() {
		if (articles == null)
			return 0;
		return articles.getPhysicalNumberOfRows() - 1;
	}

	public int getNumberOfVariants() {
		if (variants == null)
			return 0;
		return variants.getPhysicalNumberOfRows() - 1;
	}

	public Iterable<Article> articles() {
		return new Iterable<Article>() {
			@Override
			public Iterator<Article> iterator() {
				return new Iterator<Article>() {

					@Override
					public boolean hasNext() {
						if (rowIterator == null)
							return false;
						return rowIterator.hasNext();
					}

					@Override
					public Article next() {
						if (articles == null || !rowIterator.hasNext())
							return null;
						Row row = rowIterator.next();
						Article result = new Article();

						for (TableColumn column : indexMap.keySet()) {
							Cell cell = row.getCell(indexMap.get(column));

							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK)
								continue;

							switch (column) {
							case ID:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setPseudoId((int) cell.getNumericCellValue());
								break;
							case NUMBER:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setNumber(cell.getStringCellValue().trim());
								break;
							case NEW_NUMBER:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setNewNumber(cell.getStringCellValue().trim());
								break;
							case ACTIVE:
								if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
									result.setActive(cell.getBooleanCellValue());
								else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setActive(cell.getNumericCellValue() != 0);
								break;
							case HIGHLIGHT:
								if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
									result.setHighlight(cell.getBooleanCellValue());
								else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setHighlight(cell.getNumericCellValue() != 0);
								break;
							case SUPPLIER:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setSupplierId((int) cell.getNumericCellValue());
								break;
							case EAN:
								cell.setCellType(Cell.CELL_TYPE_STRING); // Long reicht nicht, double ungenau
								result.setEan(cell.getStringCellValue().trim());
								break;
							case NAME:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setName(cell.getStringCellValue().trim());
								break;
							case DESCRIPTION:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setDescription(cell.getStringCellValue().trim());
								break;
							case DESCRIPTION_LONG:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setDescriptionLong(cell.getStringCellValue().trim());
								break;
							case PRICES:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.addPrice(new ArticlePrice(cell.getNumericCellValue()));
								else {
									cell.setCellType(Cell.CELL_TYPE_STRING);
									result.setPrices(ArticlePrice.createFromString(cell.getStringCellValue().trim()));
								}
								break;
							case KEYWORDS:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setKeywords(new HashSet<String>(Arrays.asList(cell.getStringCellValue().trim()
										.split(","))));
								break;
							case CATEGORIES:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.addCategory(new ArticleCategory((int) cell.getNumericCellValue()));
								else if (cell.getCellType() == Cell.CELL_TYPE_STRING)
									result.setCategories(ArticleCategory.createFromString(cell.getStringCellValue()
											.trim()));
								break;
							case FILTER_GROUP:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setFilterGroupID((int) cell.getNumericCellValue());
								break;
							case PROPERTY_VALUES:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setPropertyValues(ArticleProperty.createFromString(cell.getStringCellValue()
										.trim()));
								break;
							case IN_STOCK:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setInStock((int) cell.getNumericCellValue());
								break;
							case STOCK_MIN:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setStockMin((int) cell.getNumericCellValue());
								break;
							case SALE:
							case VARIANT_ACTIVE:
								if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
									result.setVariantActive(cell.getBooleanCellValue());
								else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setVariantActive(cell.getNumericCellValue() != 0);
								break;
							case AVAILABLE_FROM:
								if (cell.getCellType() == Cell.CELL_TYPE_STRING)
									try {
										result.setAvailableFrom(df.parse(cell.getStringCellValue().trim()));
									} catch (ParseException e) {
									}
								else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									int daysSince1970 = (int) (cell.getNumericCellValue() - 25569); // 25569 = 1.1.1970
																									// in excel
									Calendar cal = Calendar.getInstance();
									cal.setTimeInMillis(0);
									cal.add(Calendar.DATE, daysSince1970);
									result.setAvailableFrom(cal.getTime());
								}
								break;
							case RELEASE_DATE:
								if (cell.getCellType() == Cell.CELL_TYPE_STRING)
									try {
										result.setReleaseDate(df.parse(cell.getStringCellValue().trim()));
									} catch (ParseException e) {
									}
								else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									int daysSince1970 = (int) (cell.getNumericCellValue() - 25569); // 25569 = 1.1.1970
																									// in excel
									Calendar cal = Calendar.getInstance();
									cal.setTimeInMillis(0);
									cal.add(Calendar.DATE, daysSince1970);
									result.setReleaseDate(cal.getTime());
								}
								break;
							case SHIPPING_TIME:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setShippingTime(cell.getStringCellValue().trim());
								break;
							case NOTIFICATION:
								if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
									result.setNotification(cell.getBooleanCellValue());
								else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setNotification(cell.getNumericCellValue() != 0);
								break;
							case PACK_UNIT:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setPackUnit(cell.getStringCellValue().trim());
								break;
							case PURCHASE_UNIT:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setPurchaseUnit(cell.getStringCellValue().trim());
								break;
							case REFERENCE_UNIT:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setReferenceUnit(cell.getStringCellValue().trim());
								break;
							case HEIGHT:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setHeight(cell.getNumericCellValue());
								break;
							case WIDTH:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setWidth(cell.getNumericCellValue());
								break;
							case WEIGHT:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setWeight(cell.getNumericCellValue());
								break;
							case TAX_ID:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setTaxId((int) cell.getNumericCellValue());
								break;
							case IMAGES:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setImages(ArticleImage.createFromString(cell.getStringCellValue().trim()));
								break;
							case ERRORS:
								// Do nothing
								break;
							case OPTION_REPLACE_CATEGORIES:
								if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
									result.setReplaceCategories(cell.getBooleanCellValue());
								else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setReplaceCategories(cell.getNumericCellValue() != 0);
								break;
							case OPTION_REPLACE_IMAGES:
								if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
									result.setReplaceImages(cell.getBooleanCellValue());
								else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setReplaceImages(cell.getNumericCellValue() != 0);
								break;
							case OPTION_REPLACE_PRICES:
								if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
									result.setReplacePrices(cell.getBooleanCellValue());
								else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setReplacePrices(cell.getNumericCellValue() != 0);
								break;
							case CUSTOM_PRODUCTS:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setCustomProductId((int) cell.getNumericCellValue());
								break;
							case SHIPPING_FREE:
								if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
									result.setShippingFree(cell.getBooleanCellValue());
								else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setShippingFree(cell.getNumericCellValue() != 0);
								break;
							case ADDITIONAL_TEXT:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setAdditionalText(cell.getStringCellValue().trim());
								break;
							case CONFIGURATOR_GROUPS:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setConfiguratorGroups(ArticleConfiguratorGroup.createFromString(cell
										.getStringCellValue().trim()));
								break;
							case VARIANT:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.getMainDetail().setConfiguratorOptions(
										ArticleConfiguratorOption.createFromString(cell.getStringCellValue().trim()));
								break;
							case TEMPLATE:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setTemplate(cell.getStringCellValue().trim());
								break;
							case ATTR18:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.getMainDetail().setAttribute(18, cell.getStringCellValue().trim());
								break;
							case PARENT_ID:
							case PARENT_NUMBER:
								break;
							}
						}

						return result;
					}
				};
			}
		};
	}

	public Iterable<ArticleVariant> variants() {
		return new Iterable<ArticleVariant>() {

			@Override
			public Iterator<ArticleVariant> iterator() {
				return new Iterator<ArticleVariant>() {
					@Override
					public boolean hasNext() {
						if (vRowIterator == null)
							return false;
						return vRowIterator.hasNext();
					}

					@Override
					public ArticleVariant next() {
						if (variants == null || !vRowIterator.hasNext())
							return null;
						Row row = vRowIterator.next();
						ArticleVariant result = new ArticleVariant();
						for (TableColumn column : vIndexMap.keySet()) {
							Cell cell = row.getCell(vIndexMap.get(column));

							if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK)
								continue;

							switch (column) {
							case ID:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.getDetail().setPseudoId((int) cell.getNumericCellValue());
								break;
							case PARENT_ID:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.setMotherId((int) cell.getNumericCellValue());
								break;
							case PARENT_NUMBER:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.setMotherNumber(cell.getStringCellValue().trim());
								break;
							case NUMBER:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.getDetail().setNumber(cell.getStringCellValue().trim());
								break;
							case NEW_NUMBER:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.getDetail().setNewNumber(cell.getStringCellValue().trim());
								break;
							case EAN:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.getDetail().setEan(cell.getStringCellValue().trim());
								break;
							case ADDITIONAL_TEXT:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.getDetail().setAdditionalText(cell.getStringCellValue().trim());
								break;
							case PRICES:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.getDetail().addPrice(new ArticlePrice(cell.getNumericCellValue()));
								else {
									cell.setCellType(Cell.CELL_TYPE_STRING);
									result.getDetail().setPrices(
											ArticlePrice.createFromString(cell.getStringCellValue().trim()));
								}
								break;
							case IN_STOCK:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.getDetail().setInStock((int) cell.getNumericCellValue());
								break;
							case STOCK_MIN:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.getDetail().setStockMin((int) cell.getNumericCellValue());
								break;
							case SALE:
							case VARIANT_ACTIVE:
								if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
									result.getDetail().setVariantActive(cell.getBooleanCellValue());
								else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.getDetail().setVariantActive(cell.getNumericCellValue() != 0);
								break;
							case VARIANT:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.getDetail().setConfiguratorOptions(
										ArticleConfiguratorOption.createFromString(cell.getStringCellValue().trim()));
								break;
							case RELEASE_DATE:
								if (cell.getCellType() == Cell.CELL_TYPE_STRING)
									try {
										result.getDetail().setReleaseDate(df.parse(cell.getStringCellValue().trim()));
									} catch (ParseException e) {
									}
								else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									int daysSince1970 = (int) (cell.getNumericCellValue() - 25569); // 25569 = 1.1.1970
																									// in excel
									Calendar cal = Calendar.getInstance();
									cal.setTimeInMillis(0);
									cal.add(Calendar.DATE, daysSince1970);
									result.getDetail().setReleaseDate(cal.getTime());
								}
								break;
							case SHIPPING_TIME:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.getDetail().setShippingTime(String.valueOf(cell.getNumericCellValue()));
								else {
									cell.setCellType(Cell.CELL_TYPE_STRING);
									result.getDetail().setShippingTime(cell.getStringCellValue());
								}
								break;
							case PACK_UNIT:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.getDetail().setPackUnit(cell.getStringCellValue().trim());
								break;
							case PURCHASE_UNIT:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.getDetail().setPurchaseUnit(cell.getStringCellValue().trim());
								break;
							case REFERENCE_UNIT:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.getDetail().setReferenceUnit(cell.getStringCellValue().trim());
								break;
							case HEIGHT:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.getDetail().setHeight(cell.getNumericCellValue());
								break;
							case WIDTH:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.getDetail().setWidth(cell.getNumericCellValue());
								break;
							case WEIGHT:
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.getDetail().setWeight(cell.getNumericCellValue());
								break;
							case SHIPPING_FREE:
								if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
									result.getDetail().setShippingFree(cell.getBooleanCellValue());
								else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.getDetail().setShippingFree(cell.getNumericCellValue() != 0);
								break;
							case OPTION_REPLACE_PRICES:
								if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
									result.getDetail().setReplacePrices(cell.getBooleanCellValue());
								else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									result.getDetail().setReplacePrices(cell.getNumericCellValue() != 0);
								break;
							case ATTR18:
								cell.setCellType(Cell.CELL_TYPE_STRING);
								result.getDetail().setAttribute(18, cell.getStringCellValue().trim());
								break;
							case ACTIVE:
							case AVAILABLE_FROM:
							case CATEGORIES:
							case CONFIGURATOR_GROUPS:
							case CUSTOM_PRODUCTS:
							case DESCRIPTION:
							case DESCRIPTION_LONG:
							case ERRORS:
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
						return result;
					}
				};
			}
		};
	}

	public static void main(String[] args) {
		ExcelReader reader = new ExcelReader("test2.xlsx");
		for (Article a : reader.articles()) {
			System.out.println(a);
		}
	}
}
