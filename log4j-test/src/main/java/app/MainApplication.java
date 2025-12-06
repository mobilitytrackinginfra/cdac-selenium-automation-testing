package app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainApplication {

    private static final Logger logger = LogManager.getLogger(MainApplication.class);

    public static void main(String[] args) {
        logger.info("=== Application starting ===");

        UserController controller = new UserController();

        logger.info("Calling registerUser...");
        controller.registerUser("swapnil", "weak");   // will fail validation

        logger.info("Calling registerUser with strong password...");
        controller.registerUser("john_doe", "Str0ngP@ss123");  // should pass

        logger.info("=== Application finished ===");
    }
}
