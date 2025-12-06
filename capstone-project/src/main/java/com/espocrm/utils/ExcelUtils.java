package com.espocrm.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Utility to read tabular test data from an Excel workbook.  The
 * workbook can contain multiple sheets, each representing a
 * different entity under test.  Methods are provided to
 * transform sheet contents into objects suitable for TestNG
 * data providers.
 */
public class ExcelUtils {

    private final String filePath;

    /**
     * Creates a new utility bound to the specified workbook.
     *
     * @param filePath absolute or relative path to the .xlsx file
     */
    public ExcelUtils(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads the given sheet and returns a list of maps, one per
     * row of data.  The first row of the sheet is treated as
     * the header row and defines the keys for the maps.  Blank
     * cells are converted to empty strings.
     *
     * @param sheetName name of the sheet to read
     * @return list of maps mapping column names to values
     */
    public List<Map<String, String>> getData(String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
            Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet " + sheetName + " not found in " + filePath);
            }

            Iterator<Row> rowIterator = sheet.rowIterator();
            if (!rowIterator.hasNext()) {
                return dataList;
            }
            // Read header row
            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            headerRow.forEach(cell -> headers.add(getCellValue(cell)));

            // Read data rows
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, String> rowData = new HashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i);
                    rowData.put(headers.get(i), cell != null ? getCellValue(cell) : "");
                }
                dataList.add(rowData);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file " + filePath, e);
        }
        return dataList;
    }

    /**
     * Converts a sheet into an array of objects suitable for
     * TestNG's {@code @DataProvider}.  Each element in the
     * returned array contains a single map of column names to
     * values corresponding to one row in the sheet.
     *
     * @param sheetName sheet to read
     * @return two‑dimensional array of row maps
     */
    public Object[] getDataAsArray(String sheetName) {
        List<Map<String, String>> dataList = getData(sheetName);
        return dataList.toArray();
    }

    /**
     * Returns the string representation of a cell using
     * {@link DataFormatter} to handle different cell types.
     *
     * @param cell cell to extract value from
     * @return string value of the cell
     */
    private static String getCellValue(Cell cell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }
}