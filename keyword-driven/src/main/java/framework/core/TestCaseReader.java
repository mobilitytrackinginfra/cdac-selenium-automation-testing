package framework.core;

import framework.models.TestCase;
import framework.models.TestStep;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestCaseReader {

    public List<TestCase> loadTestCases(String excelPath) {
        List<TestCase> result = new ArrayList<>();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(new File(excelPath));
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            Map<String, TestCase> testCaseMap = new LinkedHashMap<>();

            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRowNum; i++) { // skip header row
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                String tcId        = getCellValueAsString(row.getCell(0));
                String action      = getCellValueAsString(row.getCell(1));
                String locatorType = getCellValueAsString(row.getCell(2));
                String locatorVal  = getCellValueAsString(row.getCell(3));
                String data        = getCellValueAsString(row.getCell(4));

                if (tcId == null || tcId.trim().isEmpty()) {
                    continue;
                }

                TestCase testCase = testCaseMap.get(tcId);
                if (testCase == null) {
                    testCase = new TestCase(tcId);
                    testCaseMap.put(tcId, testCase);
                }

                TestStep step = new TestStep(action,locatorType,locatorVal,data);
                testCase.addStep(step);
            }

            result = new ArrayList<TestCase>(testCaseMap.values());
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException("Error reading Excel: " + e.getMessage(), e);
        } finally {
            if (fis != null) {
                try { fis.close(); } catch (Exception ignore) {}
            }
        }

        return result;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        CellType cellType = cell.getCellType();

        switch (cellType) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double d = cell.getNumericCellValue();
                    if (d == (long) d) {
                        return String.valueOf((long) d);
                    } else {
                        return String.valueOf(d);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue().trim();
                } catch (IllegalStateException e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BLANK:
            case _NONE:
            case ERROR:
            default:
                return "";
        }
    }
}
