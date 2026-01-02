package com.jdbc.tutorial.statements;

import com.jdbc.tutorial.connection.ConnectionManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * PreparedStatementExample - Demonstrates PreparedStatement usage in JDBC.
 * 
 * PreparedStatement advantages:
 * 1. Prevents SQL Injection attacks
 * 2. Better performance (precompiled)
 * 3. Type-safe parameter binding
 * 4. Handles special characters automatically
 * 
 * @author JDBC Tutorial
 */
public class PreparedStatementExample {
    
    /**
     * Setup tables for demonstration.
     */
    public static void setupTables(Connection conn) throws SQLException {
        System.out.println("\n--- Setting up tables ---");
        
        try (Statement stmt = conn.createStatement()) {
            // Products table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS products (
                    product_id INT PRIMARY KEY AUTO_INCREMENT,
                    product_name VARCHAR(100) NOT NULL,
                    category VARCHAR(50),
                    price DECIMAL(10, 2),
                    stock_quantity INT DEFAULT 0,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Users table for various data types demo
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    user_id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) NOT NULL,
                    email VARCHAR(100),
                    birth_date DATE,
                    balance DECIMAL(15, 2),
                    is_active BOOLEAN DEFAULT TRUE,
                    profile_image BLOB,
                    bio TEXT,
                    last_login TIMESTAMP
                )
            """);
            
            System.out.println("✓ Tables created successfully");
        }
    }
    
    /**
     * Demonstrate basic INSERT with PreparedStatement.
     */
    public static void basicInsert(Connection conn) throws SQLException {
        System.out.println("\n--- Basic INSERT with PreparedStatement ---");
        
        String sql = "INSERT INTO products (product_name, category, price, stock_quantity) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // First product
            pstmt.setString(1, "Laptop");
            pstmt.setString(2, "Electronics");
            pstmt.setBigDecimal(3, new BigDecimal("999.99"));
            pstmt.setInt(4, 50);
            int rows1 = pstmt.executeUpdate();
            
            // Second product - reuse same PreparedStatement
            pstmt.setString(1, "Wireless Mouse");
            pstmt.setString(2, "Electronics");
            pstmt.setBigDecimal(3, new BigDecimal("29.99"));
            pstmt.setInt(4, 200);
            int rows2 = pstmt.executeUpdate();
            
            // Third product
            pstmt.setString(1, "Office Chair");
            pstmt.setString(2, "Furniture");
            pstmt.setBigDecimal(3, new BigDecimal("249.99"));
            pstmt.setInt(4, 30);
            int rows3 = pstmt.executeUpdate();
            
            System.out.println("✓ Inserted " + (rows1 + rows2 + rows3) + " products");
        }
    }
    
    /**
     * Demonstrate INSERT with generated keys.
     */
    public static void insertWithGeneratedKeys(Connection conn) throws SQLException {
        System.out.println("\n--- INSERT with Generated Keys ---");
        
        String sql = "INSERT INTO products (product_name, category, price, stock_quantity) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, "Standing Desk");
            pstmt.setString(2, "Furniture");
            pstmt.setBigDecimal(3, new BigDecimal("599.99"));
            pstmt.setInt(4, 15);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        System.out.println("✓ Inserted product with ID: " + generatedKeys.getLong(1));
                    }
                }
            }
        }
    }
    
    /**
     * Demonstrate SELECT with parameters.
     */
    public static void selectWithParameters(Connection conn) throws SQLException {
        System.out.println("\n--- SELECT with Parameters ---");
        
        String sql = "SELECT * FROM products WHERE category = ? AND price < ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "Electronics");
            pstmt.setBigDecimal(2, new BigDecimal("500.00"));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\nElectronics products under $500:");
                System.out.println("-".repeat(60));
                
                while (rs.next()) {
                    System.out.printf("ID: %d, Name: %s, Price: $%.2f, Stock: %d%n",
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getBigDecimal("price"),
                            rs.getInt("stock_quantity"));
                }
            }
        }
    }
    
    /**
     * Demonstrate UPDATE with parameters.
     */
    public static void updateWithParameters(Connection conn) throws SQLException {
        System.out.println("\n--- UPDATE with Parameters ---");
        
        String sql = "UPDATE products SET price = ?, stock_quantity = ? WHERE product_name = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, new BigDecimal("899.99"));  // New price
            pstmt.setInt(2, 75);                               // New stock
            pstmt.setString(3, "Laptop");                      // Product to update
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✓ Updated " + rowsAffected + " row(s)");
        }
    }
    
    /**
     * Demonstrate DELETE with parameters.
     */
    public static void deleteWithParameters(Connection conn) throws SQLException {
        System.out.println("\n--- DELETE with Parameters ---");
        
        String sql = "DELETE FROM products WHERE stock_quantity < ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, 20);  // Delete products with stock < 20
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✓ Deleted " + rowsAffected + " row(s) with low stock");
        }
    }
    
    /**
     * Demonstrate handling different data types.
     */
    public static void differentDataTypes(Connection conn) throws SQLException {
        System.out.println("\n--- Different Data Types ---");
        
        String sql = """
            INSERT INTO users (username, email, birth_date, balance, is_active, bio, last_login)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // String types
            pstmt.setString(1, "john_doe");
            pstmt.setString(2, "john@example.com");
            
            // Date type
            pstmt.setDate(3, Date.valueOf(LocalDate.of(1990, 5, 15)));
            
            // Decimal type
            pstmt.setBigDecimal(4, new BigDecimal("10500.75"));
            
            // Boolean type
            pstmt.setBoolean(5, true);
            
            // Text/CLOB type
            pstmt.setString(6, "Software developer with 10+ years of experience.");
            
            // Timestamp type
            pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            
            pstmt.executeUpdate();
            System.out.println("✓ Inserted user with various data types");
        }
        
        // Read back the data
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            pstmt.setString(1, "john_doe");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\nUser Details:");
                    System.out.println("  Username: " + rs.getString("username"));
                    System.out.println("  Email: " + rs.getString("email"));
                    System.out.println("  Birth Date: " + rs.getDate("birth_date"));
                    System.out.println("  Balance: $" + rs.getBigDecimal("balance"));
                    System.out.println("  Active: " + rs.getBoolean("is_active"));
                    System.out.println("  Bio: " + rs.getString("bio"));
                    System.out.println("  Last Login: " + rs.getTimestamp("last_login"));
                }
            }
        }
    }
    
    /**
     * Demonstrate handling NULL values.
     */
    public static void handleNullValues(Connection conn) throws SQLException {
        System.out.println("\n--- Handling NULL Values ---");
        
        String sql = """
            INSERT INTO users (username, email, birth_date, balance, is_active, bio, last_login)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "guest_user");
            pstmt.setNull(2, Types.VARCHAR);        // NULL email
            pstmt.setNull(3, Types.DATE);           // NULL birth_date
            pstmt.setBigDecimal(4, BigDecimal.ZERO);
            pstmt.setBoolean(5, false);
            pstmt.setNull(6, Types.CLOB);           // NULL bio
            pstmt.setNull(7, Types.TIMESTAMP);      // NULL last_login
            
            pstmt.executeUpdate();
            System.out.println("✓ Inserted user with NULL values");
        }
        
        // Check for NULL when reading
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            pstmt.setString(1, "guest_user");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String email = rs.getString("email");
                    boolean wasNull = rs.wasNull();
                    System.out.println("Email: " + (wasNull ? "NULL" : email));
                    
                    Date birthDate = rs.getDate("birth_date");
                    wasNull = rs.wasNull();
                    System.out.println("Birth Date: " + (wasNull ? "NULL" : birthDate));
                }
            }
        }
    }
    
    /**
     * Demonstrate LIKE clause with wildcards.
     */
    public static void likeClauseExample(Connection conn) throws SQLException {
        System.out.println("\n--- LIKE Clause Example ---");
        
        // First, add some more products
        String insertSql = "INSERT INTO products (product_name, category, price, stock_quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            String[][] products = {
                {"Wireless Keyboard", "Electronics", "79.99", "100"},
                {"Wireless Headphones", "Electronics", "149.99", "80"},
                {"USB Cable", "Electronics", "9.99", "500"}
            };
            
            for (String[] product : products) {
                pstmt.setString(1, product[0]);
                pstmt.setString(2, product[1]);
                pstmt.setBigDecimal(3, new BigDecimal(product[2]));
                pstmt.setInt(4, Integer.parseInt(product[3]));
                pstmt.executeUpdate();
            }
        }
        
        // Search using LIKE
        String searchSql = "SELECT * FROM products WHERE product_name LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(searchSql)) {
            pstmt.setString(1, "Wireless%");  // Products starting with "Wireless"
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\nProducts starting with 'Wireless':");
                while (rs.next()) {
                    System.out.printf("  - %s ($%.2f)%n", 
                            rs.getString("product_name"), 
                            rs.getBigDecimal("price"));
                }
            }
        }
    }
    
    /**
     * Demonstrate IN clause with PreparedStatement.
     */
    public static void inClauseExample(Connection conn) throws SQLException {
        System.out.println("\n--- IN Clause Example ---");
        
        // For dynamic IN clause, we need to build the query
        String[] categories = {"Electronics", "Furniture"};
        String placeholders = String.join(",", java.util.Collections.nCopies(categories.length, "?"));
        String sql = "SELECT * FROM products WHERE category IN (" + placeholders + ")";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < categories.length; i++) {
                pstmt.setString(i + 1, categories[i]);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\nProducts in Electronics and Furniture:");
                while (rs.next()) {
                    System.out.printf("  - %s (%s) - $%.2f%n",
                            rs.getString("product_name"),
                            rs.getString("category"),
                            rs.getBigDecimal("price"));
                }
            }
        }
    }
    
    /**
     * Clean up tables.
     */
    public static void cleanup(Connection conn) throws SQLException {
        System.out.println("\n--- Cleanup ---");
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS products");
            stmt.execute("DROP TABLE IF EXISTS users");
            System.out.println("✓ Tables dropped");
        }
    }
    
    /**
     * Run all PreparedStatement demonstrations.
     */
    public static void demonstrate() {
        System.out.println("\n========================================");
        System.out.println("  PREPARED STATEMENT DEMONSTRATION");
        System.out.println("========================================");
        
        Connection conn = null;
        try {
            conn = ConnectionManager.getH2Connection();
            
            setupTables(conn);
            basicInsert(conn);
            insertWithGeneratedKeys(conn);
            selectWithParameters(conn);
            updateWithParameters(conn);
            differentDataTypes(conn);
            handleNullValues(conn);
            likeClauseExample(conn);
            inClauseExample(conn);
            deleteWithParameters(conn);
            cleanup(conn);
            
            System.out.println("\n✓ All PreparedStatement examples completed successfully!");
            
        } catch (SQLException e) {
            System.err.println("✗ PreparedStatement error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.closeConnection(conn);
        }
    }
    
    public static void main(String[] args) {
        demonstrate();
    }
}

