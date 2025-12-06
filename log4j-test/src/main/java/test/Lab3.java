package test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Lab3 {
    private static final Logger logger = LogManager.getLogger(Lab3.class);

    public static void main(String[] args) {
        try {
            int x = 10 / 0;
        } catch (Exception e) {
            logger.error("Exception occurred while dividing", e);
        }
    }
}