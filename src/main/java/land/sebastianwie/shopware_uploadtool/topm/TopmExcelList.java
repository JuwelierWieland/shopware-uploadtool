package land.sebastianwie.shopware_uploadtool.topm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class TopmExcelList implements TopmArticleList {

	private Workbook wb;

	private Sheet articles;
	private Map<String, Integer> headingIndexes;

	public TopmExcelList(String filename) {
		try {
			wb = WorkbookFactory.create(new File(filename));
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		init();
	}

	public TopmExcelList(File filename) {
		try {
			wb = WorkbookFactory.create(filename);
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		init();
	}

	private void init() {
		if (wb.getNumberOfSheets() >= 1)
			articles = wb.getSheetAt(0);
		else
			articles = null;

		readHeading();
	}

	private void readHeading() {
		if (articles == null)
			return;

		headingIndexes = new HashMap<>();
		Row headingRow = articles.getRow(2);
		for (Cell c : headingRow) {
			headingIndexes.put(c.getStringCellValue(), c.getColumnIndex());
		}
	}

	@Override
	public Iterator<TopMArticle> iterator() {
		Iterator<Row> rowIterator = articles.iterator();
		for (int i = 0; i < 3 && rowIterator.hasNext(); i++)
			rowIterator.next();
		return new Iterator<TopMArticle>() {
			@Override
			public boolean hasNext() {
				return rowIterator.hasNext();
			}

			@Override
			public TopMArticle next() {
				Row row = rowIterator.next();
				TopMArticle result = new TopMArticle();
				Cell bnrCell = row.getCell(headingIndexes.get("Bestell-Nr."));
				Cell stockCell = row.getCell(headingIndexes.get("Gesamtbest"));
				Cell supplierCell = row.getCell(headingIndexes.get("HE"));

				bnrCell.setCellType(Cell.CELL_TYPE_STRING);
				result.setBestellnummer(bnrCell.getStringCellValue());
				if (stockCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
					result.setInStock((int) stockCell.getNumericCellValue());
				if (supplierCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
					result.setHerstellerID((int) supplierCell.getNumericCellValue());
				return result;
			}
		};
	}

	@Override
	public int getArticleCount() {
		if (articles == null)
			return 0;
		else
			return Math.min(0, articles.getPhysicalNumberOfRows() - 3);
	}
}
