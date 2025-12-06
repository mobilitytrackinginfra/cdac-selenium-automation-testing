package test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Lab2 {
    private static final Logger logger = LogManager.getLogger(Lab2.class);

    public static void main(String[] args) {
        logger.info("Architecture demo started");
        doWork();
    }

    private static void doWork() {
        Logger childLogger = LogManager.getLogger("com.myapp.moduleA");
        childLogger.debug("Debug message from moduleA");
        childLogger.error("Error from moduleA");
        for(int i=0;i<100000;i++) {
            childLogger.error("Error from moduleA");        	
        }
    }
}

