package test;
import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelReaderBasic {

    public static void main(String[] args) throws IOException {
        String path = "testdata.xlsx";

        try (FileInputStream fis = new FileInputStream(path);
             Workbook workbook = WorkbookFactory.create(fis)) {

        	System.out.println(workbook.getClass());
            Sheet sheet = workbook.getSheet("Sheet1"); // or getSheetAt(0)

            int lastRow = sheet.getLastRowNum(); // 0-based

            for (int r = 0; r <= lastRow; r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                int lastCol = row.getLastCellNum(); // 1-based
                
                Cell c1 = row.createCell(6);
                c1.setCellValue("Test Value");
                for (int c = 0; c < lastCol; c++) {
                    Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String value = getCellValueAsString(cell);
                    System.out.print(value + " | ");
                }
                System.out.println();
            }
          workbook.write(new FileOutputStream(path));  
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getDateCellValue().toString()
                    : String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK   -> "";
            default      -> "";
        };
    }
}
