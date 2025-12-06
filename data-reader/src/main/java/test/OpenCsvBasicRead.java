package test;
import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;

public class OpenCsvBasicRead {
    public static void main(String[] args) throws Exception {
        String path = "testdata.csv";

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            String[] cells;
            while ((cells = reader.readNext()) != null) {
            	for(String val : cells) {
            		System.out.print(val + " | ");
            	}
            	System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
