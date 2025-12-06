package app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository = new UserRepository();
    private final PasswordValidator passwordValidator = new PasswordValidator();

    public void register(String username, String password) {
        logger.debug("Starting registration process for {}", username);

        if (!passwordValidator.isStrong(password)) {
            logger.info("Password validation failed for user {}", username);
            throw new IllegalArgumentException("Weak password");
        }

        logger.debug("Password validation passed for {}", username);

        // simulate some business logic
        logger.debug("Checking if username {} already exists", username);
        if (userRepository.exists(username)) {
            logger.warn("Username {} already exists", username);
            throw new IllegalArgumentException("Username already taken");
        }

        logger.debug("Saving user {} to database", username);
        userRepository.save(username, password);

        logger.info("User {} successfully registered in service layer", username);
    }
}
