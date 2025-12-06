package app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PasswordValidator {

    private static final Logger logger = LogManager.getLogger(PasswordValidator.class);

    public boolean isStrong(String password) {
        logger.debug("Validating password strength");

        if (password == null || password.length() < 8) {
            logger.debug("Password is too short");
            return false;
        }
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);

        boolean strong = hasDigit && hasUpper && hasLower;

        logger.debug("Password strong={} (hasDigit={}, hasUpper={}, hasLower={})",
                strong, hasDigit, hasUpper, hasLower);

        return strong;
    }
}
