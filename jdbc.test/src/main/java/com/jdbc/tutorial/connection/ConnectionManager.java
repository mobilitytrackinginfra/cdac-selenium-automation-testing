package com.jdbc.tutorial.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ConnectionManager - Utility class for managing database connections.
 * 
 * This class demonstrates:
 * 1. Loading database configuration from properties file
 * 2. Establishing database connections
 * 3. Proper resource management
 * 
 * @author JDBC Tutorial
 */
public class ConnectionManager {
    
    private static Properties properties = new Properties();
    private static boolean isInitialized = false;
    
    // Static block to load properties on class loading
    static {
        loadProperties();
    }
    
    /**
     * Load database properties from configuration file
     */
    private static void loadProperties() {
        try (InputStream input = ConnectionManager.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input != null) {
                properties.load(input);
                isInitialized = true;
                System.out.println("✓ Database properties loaded successfully");
            } else {
                System.err.println("✗ Unable to find db.properties");
            }
        } catch (IOException e) {
            System.err.println("✗ Error loading properties: " + e.getMessage());
        }
    }
    
    /**
     * Get a database connection using properties file configuration.
     * 
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (!isInitialized) {
            throw new SQLException("Database properties not initialized");
        }
        
        String driver = properties.getProperty("db.driver");
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        
        try {
            // Load the JDBC driver (optional for JDBC 4.0+, but good practice)
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC Driver not found: " + driver, e);
        }
        
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println("✓ Database connection established");
        return connection;
    }
    
    /**
     * Get a connection with custom URL (useful for H2 testing).
     * 
     * @param url Database URL
     * @param username Database username
     * @param password Database password
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection(String url, String username, String password) 
            throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println("✓ Database connection established to: " + url);
        return connection;
    }
    
    /**
     * Get H2 in-memory connection for testing.
     * This is useful when MySQL is not available.
     * 
     * @return Connection to H2 in-memory database
     * @throws SQLException if connection fails
     */
    public static Connection getH2Connection() throws SQLException {
        String url = "jdbc:h2:mem:jdbc_tutorial;DB_CLOSE_DELAY=-1;MODE=MySQL";
        Connection connection = DriverManager.getConnection(url, "sa", "");
        System.out.println("✓ H2 in-memory connection established");
        return connection;
    }
    
    /**
     * Close a connection safely.
     * 
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("✓ Database connection closed");
                }
            } catch (SQLException e) {
                System.err.println("✗ Error closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Get database properties.
     * 
     * @return Properties object containing database configuration
     */
    public static Properties getProperties() {
        return properties;
    }
    
    /**
     * Print connection information for debugging.
     * 
     * @param connection Connection to inspect
     */
    public static void printConnectionInfo(Connection connection) {
        try {
            System.out.println("\n=== Connection Information ===");
            System.out.println("Database Product: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("Database Version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver Name: " + connection.getMetaData().getDriverName());
            System.out.println("Driver Version: " + connection.getMetaData().getDriverVersion());
            System.out.println("URL: " + connection.getMetaData().getURL());
            System.out.println("User: " + connection.getMetaData().getUserName());
            System.out.println("Auto-Commit: " + connection.getAutoCommit());
            System.out.println("Transaction Isolation: " + getIsolationLevelName(connection.getTransactionIsolation()));
            System.out.println("==============================\n");
        } catch (SQLException e) {
            System.err.println("Error getting connection info: " + e.getMessage());
        }
    }
    
    /**
     * Convert transaction isolation level to readable name.
     */
    private static String getIsolationLevelName(int level) {
        return switch (level) {
            case Connection.TRANSACTION_NONE -> "TRANSACTION_NONE";
            case Connection.TRANSACTION_READ_UNCOMMITTED -> "TRANSACTION_READ_UNCOMMITTED";
            case Connection.TRANSACTION_READ_COMMITTED -> "TRANSACTION_READ_COMMITTED";
            case Connection.TRANSACTION_REPEATABLE_READ -> "TRANSACTION_REPEATABLE_READ";
            case Connection.TRANSACTION_SERIALIZABLE -> "TRANSACTION_SERIALIZABLE";
            default -> "UNKNOWN";
        };
    }
}

