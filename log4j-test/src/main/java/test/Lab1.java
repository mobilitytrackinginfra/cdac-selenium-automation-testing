package test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Lab1 {

	private static final Logger logger = LogManager.getLogger(Lab1.class);
	
	public static void main(String[] args) {
        logger.trace("Trace log");
        logger.debug("Debug log");
        logger.info("Info log");
        logger.warn("Warning log");
        logger.error("Error log");
        logger.fatal("Fatal log");
    }
}
