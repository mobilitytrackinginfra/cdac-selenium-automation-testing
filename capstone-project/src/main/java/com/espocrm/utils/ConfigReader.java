package com.espocrm.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class responsible for loading external configuration
 * from a properties file.  Test code can query settings such
 * as the base URL of the application under test, credentials
 * or desired browser without hard‑coding these values.  The
 * properties file is loaded once on first request and then
 * cached for subsequent accesses.
 */
public final class ConfigReader {

    private static final String CONFIG_PATH = "src/test/resources/config.properties";
    private static Properties properties;

    // Private constructor prevents instantiation
    private ConfigReader() {}

    /**
     * Loads the properties file from the default location on
     * disk.  This method is called lazily the first time
     * a property is requested.  Subsequent calls reuse the
     * cached Properties object.
     */
    private static void loadProperties() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream input = new FileInputStream(CONFIG_PATH)) {
                properties.load(input);
            } catch (IOException e) {
                throw new RuntimeException("Unable to load configuration from " + CONFIG_PATH, e);
            }
        }
    }

    /**
     * Returns the value of the given configuration key.  If
     * the key is not present in the properties file a null
     * reference is returned.
     *
     * @param key configuration property name
     * @return value associated with the key or null
     */
    public static String getProperty(String key) {
        loadProperties();
        return properties.getProperty(key);
    }
}