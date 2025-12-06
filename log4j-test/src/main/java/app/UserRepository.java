package app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class UserRepository {

    private static final Logger logger = LogManager.getLogger(UserRepository.class);

    // in-memory "DB"
    private static final Set<String> USERS = new HashSet<>();

    public boolean exists(String username) {
        logger.debug("Checking existence of user {} in repository", username);
        return USERS.contains(username);
    }

    public void save(String username, String password) {
        // NOTE: never log plain passwords like this in real life – this is just a demo
        logger.debug("Saving user {} in repository", username);
        USERS.add(username);
        logger.info("User {} saved to repository (total users: {})", username, USERS.size());
    }
}
