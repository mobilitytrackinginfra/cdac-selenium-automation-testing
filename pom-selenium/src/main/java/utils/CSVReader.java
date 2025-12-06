package utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public static List<User> getLoginUsers() throws Exception {
        String path = "testdata.csv";
        List<User> users = new ArrayList<>();

        try (com.opencsv.CSVReader reader = new com.opencsv.CSVReader(new FileReader(path))) {
            String[] cells;
            boolean processHeaders = false;
            while ((cells = reader.readNext()) != null) {
            	if(!processHeaders) {
            		processHeaders = true;
            	} else {
            		String username = cells[0];
            		String password = cells[1];
            		User usr = new User(username, password);
            		users.add(usr);
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}
