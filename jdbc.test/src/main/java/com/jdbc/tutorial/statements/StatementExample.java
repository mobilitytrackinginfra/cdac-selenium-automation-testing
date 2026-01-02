package com.jdbc.tutorial.statements;

import com.jdbc.tutorial.connection.ConnectionManager;

import java.sql.*;

/**
 * StatementExample - Demonstrates basic Statement usage in JDBC.
 * 
 * Statement is used for:
 * 1. Simple SQL queries without parameters
 * 2. DDL statements (CREATE, ALTER, DROP)
 * 3. One-time queries
 * 
 * Note: Statement is vulnerable to SQL injection!
 * Use PreparedStatement for parameterized queries.
 * 
 * @author JDBC Tutorial
 */
public class StatementExample {
    
    /**
     * Demonstrate CREATE TABLE using Statement.
     */
    public static void createTable(Connection conn) throws SQLException {
        System.out.println("\n--- CREATE TABLE Example ---");
        
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS demo_table (
                id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(100) NOT NULL,
                description TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("✓ Table 'demo_table' created successfully");
        }
    }
    
    /**
     * Demonstrate INSERT using Statement.
     */
    public static void insertData(Connection conn) throws SQLException {
        System.out.println("\n--- INSERT Example ---");
        
        String[] insertSQLs = {
            "INSERT INTO demo_table (name, description) VALUES ('Item 1', 'First item')",
            "INSERT INTO demo_table (name, description) VALUES ('Item 2', 'Second item')",
            "INSERT INTO demo_table (name, description) VALUES ('Item 3', 'Third item')"
        };
        
        try (Statement stmt = conn.createStatement()) {
            for (String sql : insertSQLs) {
                int rowsAffected = stmt.executeUpdate(sql);
                System.out.println("✓ Inserted " + rowsAffected + " row(s)");
            }
        }
    }
    
    /**
     * Demonstrate INSERT with generated keys.
     */
    public static void insertWithGeneratedKeys(Connection conn) throws SQLException {
        System.out.println("\n--- INSERT with Generated Keys ---");
        
        String sql = "INSERT INTO demo_table (name, description) VALUES ('Auto Key Item', 'Testing auto keys')";
        
        try (Statement stmt = conn.createStatement()) {
            int rowsAffected = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long generatedId = generatedKeys.getLong(1);
                        System.out.println("✓ Inserted row with generated ID: " + generatedId);
                    }
                }
            }
        }
    }
    
    /**
     * Demonstrate SELECT using Statement.
     */
    public static void selectData(Connection conn) throws SQLException {
        System.out.println("\n--- SELECT Example ---");
        
        String sql = "SELECT * FROM demo_table ORDER BY id";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\nAll records in demo_table:");
            System.out.println("-".repeat(70));
            System.out.printf("%-5s %-20s %-30s %-15s%n", "ID", "Name", "Description", "Created At");
            System.out.println("-".repeat(70));
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                Timestamp createdAt = rs.getTimestamp("created_at");
                
                System.out.printf("%-5d %-20s %-30s %-15s%n", 
                        id, name, 
                        description != null ? description.substring(0, Math.min(30, description.length())) : "N/A",
                        createdAt != null ? createdAt.toString().substring(0, 10) : "N/A");
            }
            System.out.println("-".repeat(70));
        }
    }
    
    /**
     * Demonstrate UPDATE using Statement.
     */
    public static void updateData(Connection conn) throws SQLException {
        System.out.println("\n--- UPDATE Example ---");
        
        String sql = "UPDATE demo_table SET description = 'Updated description' WHERE id = 1";
        
        try (Statement stmt = conn.createStatement()) {
            int rowsAffected = stmt.executeUpdate(sql);
            System.out.println("✓ Updated " + rowsAffected + " row(s)");
        }
    }
    
    /**
     * Demonstrate DELETE using Statement.
     */
    public static void deleteData(Connection conn) throws SQLException {
        System.out.println("\n--- DELETE Example ---");
        
        String sql = "DELETE FROM demo_table WHERE id = 3";
        
        try (Statement stmt = conn.createStatement()) {
            int rowsAffected = stmt.executeUpdate(sql);
            System.out.println("✓ Deleted " + rowsAffected + " row(s)");
        }
    }
    
    /**
     * Demonstrate execute() method that can handle any SQL.
     */
    public static void executeGenericSQL(Connection conn) throws SQLException {
        System.out.println("\n--- Generic execute() Example ---");
        
        try (Statement stmt = conn.createStatement()) {
            // execute() returns true if the result is a ResultSet, false otherwise
            String selectSQL = "SELECT COUNT(*) as total FROM demo_table";
            
            boolean hasResultSet = stmt.execute(selectSQL);
            
            if (hasResultSet) {
                try (ResultSet rs = stmt.getResultSet()) {
                    if (rs.next()) {
                        System.out.println("✓ Total records: " + rs.getInt("total"));
                    }
                }
            } else {
                int updateCount = stmt.getUpdateCount();
                System.out.println("✓ Update count: " + updateCount);
            }
        }
    }
    
    /**
     * Demonstrate Statement with multiple results.
     */
    public static void multipleResults(Connection conn) throws SQLException {
        System.out.println("\n--- Multiple Results Example ---");
        
        // Note: MySQL requires allowMultiQueries=true in connection URL
        // H2 handles this differently
        
        try (Statement stmt = conn.createStatement()) {
            // First query
            ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) as count FROM demo_table");
            if (rs1.next()) {
                System.out.println("✓ Count query result: " + rs1.getInt("count"));
            }
            rs1.close();
            
            // Second query using same statement
            ResultSet rs2 = stmt.executeQuery("SELECT MAX(id) as max_id FROM demo_table");
            if (rs2.next()) {
                System.out.println("✓ Max ID query result: " + rs2.getInt("max_id"));
            }
            rs2.close();
        }
    }
    
    /**
     * Demonstrate setting Statement properties.
     */
    public static void statementProperties(Connection conn) throws SQLException {
        System.out.println("\n--- Statement Properties Example ---");
        
        try (Statement stmt = conn.createStatement()) {
            // Set query timeout (seconds)
            stmt.setQueryTimeout(30);
            System.out.println("Query Timeout: " + stmt.getQueryTimeout() + " seconds");
            
            // Set max rows to return
            stmt.setMaxRows(100);
            System.out.println("Max Rows: " + stmt.getMaxRows());
            
            // Set fetch size (hint to driver)
            stmt.setFetchSize(50);
            System.out.println("Fetch Size: " + stmt.getFetchSize());
            
            // Set fetch direction
            stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            System.out.println("Fetch Direction: FETCH_FORWARD");
            
            // Get max field size
            System.out.println("Max Field Size: " + stmt.getMaxFieldSize() + " bytes");
        }
    }
    
    /**
     * Clean up - DROP TABLE.
     */
    public static void dropTable(Connection conn) throws SQLException {
        System.out.println("\n--- DROP TABLE Example ---");
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS demo_table");
            System.out.println("✓ Table 'demo_table' dropped");
        }
    }
    
    /**
     * Run all Statement demonstrations.
     */
    public static void demonstrate() {
        System.out.println("\n========================================");
        System.out.println("      STATEMENT DEMONSTRATION");
        System.out.println("========================================");
        
        Connection conn = null;
        try {
            // Using H2 for demonstration
            conn = ConnectionManager.getH2Connection();
            
            createTable(conn);
            insertData(conn);
            insertWithGeneratedKeys(conn);
            selectData(conn);
            updateData(conn);
            selectData(conn);  // Show updated data
            deleteData(conn);
            executeGenericSQL(conn);
            multipleResults(conn);
            statementProperties(conn);
            dropTable(conn);
            
            System.out.println("\n✓ All Statement examples completed successfully!");
            
        } catch (SQLException e) {
            System.err.println("✗ Statement error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.closeConnection(conn);
        }
    }
    
    public static void main(String[] args) {
        demonstrate();
    }
}

