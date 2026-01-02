package com.jdbc.tutorial.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ConnectionPoolExample - Demonstrates connection pooling using HikariCP.
 * 
 * Connection pooling is essential for:
 * 1. Better performance (reuse connections)
 * 2. Resource management
 * 3. Handling concurrent requests
 * 
 * @author JDBC Tutorial
 */
public class ConnectionPoolExample {
    
    private static HikariDataSource dataSource;
    
    /**
     * Initialize the connection pool with HikariCP.
     */
    public static void initializePool() {
        Properties props = ConnectionManager.getProperties();
        
        HikariConfig config = new HikariConfig();
        
        // Basic connection settings
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setUsername(props.getProperty("db.username"));
        config.setPassword(props.getProperty("db.password"));
        config.setDriverClassName(props.getProperty("db.driver"));
        
        // Pool settings
        config.setMaximumPoolSize(Integer.parseInt(
                props.getProperty("pool.size", "10")));
        config.setMinimumIdle(Integer.parseInt(
                props.getProperty("pool.minIdle", "5")));
        config.setMaxLifetime(Long.parseLong(
                props.getProperty("pool.maxLifetime", "1800000")));
        config.setConnectionTimeout(Long.parseLong(
                props.getProperty("pool.connectionTimeout", "30000")));
        config.setIdleTimeout(Long.parseLong(
                props.getProperty("pool.idleTimeout", "600000")));
        
        // Performance optimization
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        // Pool name for monitoring
        config.setPoolName("JDBCTutorialPool");
        
        dataSource = new HikariDataSource(config);
        System.out.println("✓ HikariCP connection pool initialized");
    }
    
    /**
     * Initialize pool for H2 database (for testing).
     */
    public static void initializeH2Pool() {
        HikariConfig config = new HikariConfig();
        
        config.setJdbcUrl("jdbc:h2:mem:jdbc_tutorial;DB_CLOSE_DELAY=-1;MODE=MySQL");
        config.setUsername("sa");
        config.setPassword("");
        config.setDriverClassName("org.h2.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setPoolName("H2TestPool");
        
        dataSource = new HikariDataSource(config);
        System.out.println("✓ H2 HikariCP connection pool initialized");
    }
    
    /**
     * Get connection from the pool.
     * 
     * @return Connection from pool
     * @throws SQLException if unable to get connection
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Connection pool not initialized. Call initializePool() first.");
        }
        return dataSource.getConnection();
    }
    
    /**
     * Get the DataSource for dependency injection.
     * 
     * @return HikariDataSource instance
     */
    public static DataSource getDataSource() {
        return dataSource;
    }
    
    /**
     * Close the connection pool.
     */
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("✓ Connection pool closed");
        }
    }
    
    /**
     * Print pool statistics.
     */
    public static void printPoolStats() {
        if (dataSource != null) {
            System.out.println("\n=== Connection Pool Statistics ===");
            System.out.println("Pool Name: " + dataSource.getPoolName());
            System.out.println("Active Connections: " + dataSource.getHikariPoolMXBean().getActiveConnections());
            System.out.println("Idle Connections: " + dataSource.getHikariPoolMXBean().getIdleConnections());
            System.out.println("Total Connections: " + dataSource.getHikariPoolMXBean().getTotalConnections());
            System.out.println("Threads Awaiting Connection: " + 
                    dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
            System.out.println("=================================\n");
        }
    }
    
    /**
     * Demonstrate connection pool usage.
     */
    public static void demonstratePooling() {
        System.out.println("\n========================================");
        System.out.println("  CONNECTION POOLING DEMONSTRATION");
        System.out.println("========================================\n");
        
        try {
            // Initialize H2 pool for demonstration
            initializeH2Pool();
            
            // Create a simple table
            try (Connection conn = getConnection()) {
                conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS pool_test (id INT PRIMARY KEY, name VARCHAR(100))"
                );
            }
            
            printPoolStats();
            
            // Simulate multiple operations using pooled connections
            System.out.println("Simulating 5 database operations using pooled connections...\n");
            
            for (int i = 1; i <= 5; i++) {
                // Connection is automatically returned to pool when try-with-resources closes it
                try (Connection conn = getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(
                             "INSERT INTO pool_test (id, name) VALUES (?, ?)")) {
                    
                    pstmt.setInt(1, i);
                    pstmt.setString(2, "Item " + i);
                    pstmt.executeUpdate();
                    
                    System.out.println("Operation " + i + " completed - Connection returned to pool");
                }
            }
            
            System.out.println();
            printPoolStats();
            
            // Verify data
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM pool_test");
                 ResultSet rs = pstmt.executeQuery()) {
                
                System.out.println("Verifying inserted data:");
                while (rs.next()) {
                    System.out.println("  ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Pool demonstration error: " + e.getMessage());
        } finally {
            closePool();
        }
    }
    
    public static void main(String[] args) {
        demonstratePooling();
    }
}

