package app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserService userService = new UserService();

    public void registerUser(String username, String password) {
        logger.info("Received request to register user: {}", username);

        try {
            userService.register(username, password);
            logger.info("User {} registered successfully", username);
        } catch (IllegalArgumentException e) {
            logger.warn("Validation failed for user {}: {}", username, e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while registering user {}", username, e);
        }
    }
}
