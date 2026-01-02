package com.jdbc.tutorial.batch;

import com.jdbc.tutorial.connection.ConnectionManager;

import java.sql.*;
import java.util.Random;

/**
 * BatchProcessingExample - Demonstrates batch operations in JDBC.
 * 
 * Batch processing benefits:
 * 1. Reduced network round trips
 * 2. Better performance for bulk operations
 * 3. Efficient database server utilization
 * 
 * @author JDBC Tutorial
 */
public class BatchProcessingExample {
    
    private static final Random random = new Random();
    
    /**
     * Setup tables for demonstration.
     */
    public static void setup(Connection conn) throws SQLException {
        System.out.println("\n--- Setting up tables ---");
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS batch_products (
                    product_id INT PRIMARY KEY AUTO_INCREMENT,
                    product_name VARCHAR(100) NOT NULL,
                    category VARCHAR(50),
                    price DECIMAL(10, 2),
                    quantity INT DEFAULT 0
                )
            """);
            
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS batch_orders (
                    order_id INT PRIMARY KEY AUTO_INCREMENT,
                    customer_name VARCHAR(100),
                    product_id INT,
                    quantity INT,
                    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            System.out.println("✓ Tables created");
        }
    }
    
    /**
     * Demonstrate batch INSERT with Statement.
     */
    public static void batchInsertWithStatement(Connection conn) throws SQLException {
        System.out.println("\n--- Batch INSERT with Statement ---");
        
        long startTime = System.currentTimeMillis();
        
        try (Statement stmt = conn.createStatement()) {
            // Add multiple SQL statements to batch
            stmt.addBatch("INSERT INTO batch_products (product_name, category, price, quantity) " +
                         "VALUES ('Product A', 'Electronics', 99.99, 100)");
            stmt.addBatch("INSERT INTO batch_products (product_name, category, price, quantity) " +
                         "VALUES ('Product B', 'Electronics', 149.99, 75)");
            stmt.addBatch("INSERT INTO batch_products (product_name, category, price, quantity) " +
                         "VALUES ('Product C', 'Furniture', 299.99, 30)");
            stmt.addBatch("INSERT INTO batch_products (product_name, category, price, quantity) " +
                         "VALUES ('Product D', 'Stationery', 9.99, 500)");
            stmt.addBatch("INSERT INTO batch_products (product_name, category, price, quantity) " +
                         "VALUES ('Product E', 'Electronics', 199.99, 60)");
            
            // Execute batch
            int[] results = stmt.executeBatch();
            
            long endTime = System.currentTimeMillis();
            
            System.out.println("Batch executed. Results for each statement:");
            for (int i = 0; i < results.length; i++) {
                String status = switch (results[i]) {
                    case Statement.SUCCESS_NO_INFO -> "SUCCESS_NO_INFO";
                    case Statement.EXECUTE_FAILED -> "EXECUTE_FAILED";
                    default -> results[i] + " row(s) affected";
                };
                System.out.println("  Statement " + (i + 1) + ": " + status);
            }
            
            System.out.println("Time taken: " + (endTime - startTime) + " ms");
        }
    }
    
    /**
     * Demonstrate batch INSERT with PreparedStatement.
     */
    public static void batchInsertWithPreparedStatement(Connection conn) throws SQLException {
        System.out.println("\n--- Batch INSERT with PreparedStatement ---");
        
        String sql = "INSERT INTO batch_products (product_name, category, price, quantity) VALUES (?, ?, ?, ?)";
        
        long startTime = System.currentTimeMillis();
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String[] categories = {"Electronics", "Furniture", "Stationery", "Clothing"};
            
            // Add 100 products to batch
            for (int i = 1; i <= 100; i++) {
                pstmt.setString(1, "Batch Product " + i);
                pstmt.setString(2, categories[random.nextInt(categories.length)]);
                pstmt.setDouble(3, 10 + random.nextDouble() * 990);  // Price 10-1000
                pstmt.setInt(4, random.nextInt(500));
                
                pstmt.addBatch();
                
                // Execute in chunks of 25 (for demonstration)
                if (i % 25 == 0) {
                    pstmt.executeBatch();
                    System.out.println("  Executed batch for products " + (i - 24) + " to " + i);
                }
            }
            
            // Execute remaining
            pstmt.executeBatch();
            
            long endTime = System.currentTimeMillis();
            System.out.println("✓ 100 products inserted via batch");
            System.out.println("Time taken: " + (endTime - startTime) + " ms");
        }
    }
    
    /**
     * Compare batch vs individual inserts performance.
     */
    public static void comparePerformance(Connection conn) throws SQLException {
        System.out.println("\n--- Performance Comparison: Batch vs Individual ---");
        
        String sql = "INSERT INTO batch_orders (customer_name, product_id, quantity) VALUES (?, ?, ?)";
        int recordCount = 1000;
        
        // Method 1: Individual inserts
        long startIndividual = System.currentTimeMillis();
        
        conn.setAutoCommit(false);  // Start transaction
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 1; i <= recordCount; i++) {
                pstmt.setString(1, "Customer " + i);
                pstmt.setInt(2, random.nextInt(100) + 1);
                pstmt.setInt(3, random.nextInt(10) + 1);
                pstmt.executeUpdate();  // Individual execute
            }
            conn.commit();
        }
        
        long endIndividual = System.currentTimeMillis();
        long individualTime = endIndividual - startIndividual;
        
        // Clear the table
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM batch_orders");
        }
        conn.commit();
        
        // Method 2: Batch inserts
        long startBatch = System.currentTimeMillis();
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 1; i <= recordCount; i++) {
                pstmt.setString(1, "Customer " + i);
                pstmt.setInt(2, random.nextInt(100) + 1);
                pstmt.setInt(3, random.nextInt(10) + 1);
                pstmt.addBatch();
                
                // Execute every 100 records
                if (i % 100 == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch();  // Remaining records
            conn.commit();
        }
        
        long endBatch = System.currentTimeMillis();
        long batchTime = endBatch - startBatch;
        
        conn.setAutoCommit(true);
        
        System.out.println("\nResults for " + recordCount + " records:");
        System.out.println("  Individual inserts: " + individualTime + " ms");
        System.out.println("  Batch inserts: " + batchTime + " ms");
        System.out.println("  Improvement: " + String.format("%.2f", 
                (double) individualTime / batchTime) + "x faster");
    }
    
    /**
     * Demonstrate batch UPDATE.
     */
    public static void batchUpdate(Connection conn) throws SQLException {
        System.out.println("\n--- Batch UPDATE ---");
        
        String sql = "UPDATE batch_products SET price = price * ? WHERE category = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 10% increase for Electronics
            pstmt.setDouble(1, 1.10);
            pstmt.setString(2, "Electronics");
            pstmt.addBatch();
            
            // 5% increase for Furniture
            pstmt.setDouble(1, 1.05);
            pstmt.setString(2, "Furniture");
            pstmt.addBatch();
            
            // 15% discount for Stationery
            pstmt.setDouble(1, 0.85);
            pstmt.setString(2, "Stationery");
            pstmt.addBatch();
            
            int[] results = pstmt.executeBatch();
            
            System.out.println("Update results:");
            System.out.println("  Electronics (+10%): " + results[0] + " rows");
            System.out.println("  Furniture (+5%): " + results[1] + " rows");
            System.out.println("  Stationery (-15%): " + results[2] + " rows");
        }
    }
    
    /**
     * Demonstrate batch with mixed statements.
     */
    public static void batchMixedStatements(Connection conn) throws SQLException {
        System.out.println("\n--- Batch with Mixed Statements ---");
        
        try (Statement stmt = conn.createStatement()) {
            // Note: Only DML statements (INSERT, UPDATE, DELETE) should be batched
            // DDL statements (CREATE, ALTER, DROP) may not work in batch
            
            stmt.addBatch("INSERT INTO batch_products (product_name, category, price) " +
                         "VALUES ('Mixed Product 1', 'Test', 50.00)");
            stmt.addBatch("UPDATE batch_products SET quantity = 999 WHERE product_name = 'Product A'");
            stmt.addBatch("DELETE FROM batch_products WHERE product_name LIKE 'Batch Product 1%'");
            
            int[] results = stmt.executeBatch();
            
            System.out.println("Mixed batch results:");
            System.out.println("  INSERT: " + results[0]);
            System.out.println("  UPDATE: " + results[1]);
            System.out.println("  DELETE: " + results[2]);
        }
    }
    
    /**
     * Demonstrate handling batch exceptions.
     */
    public static void handleBatchExceptions(Connection conn) throws SQLException {
        System.out.println("\n--- Handling Batch Exceptions ---");
        
        conn.setAutoCommit(false);
        
        String sql = "INSERT INTO batch_products (product_name, category, price, quantity) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Valid insert
            pstmt.setString(1, "Valid Product");
            pstmt.setString(2, "Test");
            pstmt.setDouble(3, 99.99);
            pstmt.setInt(4, 10);
            pstmt.addBatch();
            
            // Another valid insert
            pstmt.setString(1, "Another Valid Product");
            pstmt.setString(2, "Test");
            pstmt.setDouble(3, 149.99);
            pstmt.setInt(4, 20);
            pstmt.addBatch();
            
            try {
                int[] results = pstmt.executeBatch();
                conn.commit();
                
                System.out.println("All batch statements executed successfully");
                for (int i = 0; i < results.length; i++) {
                    System.out.println("  Statement " + (i + 1) + ": " + results[i]);
                }
                
            } catch (BatchUpdateException bue) {
                System.err.println("Batch update error: " + bue.getMessage());
                
                // Get update counts for successful statements
                int[] updateCounts = bue.getUpdateCounts();
                System.out.println("Update counts before failure:");
                for (int i = 0; i < updateCounts.length; i++) {
                    String status = switch (updateCounts[i]) {
                        case Statement.SUCCESS_NO_INFO -> "SUCCESS_NO_INFO";
                        case Statement.EXECUTE_FAILED -> "EXECUTE_FAILED";
                        default -> String.valueOf(updateCounts[i]);
                    };
                    System.out.println("  Statement " + (i + 1) + ": " + status);
                }
                
                // Rollback on error
                conn.rollback();
                System.out.println("Transaction rolled back");
            }
            
        } finally {
            conn.setAutoCommit(true);
        }
    }
    
    /**
     * Demonstrate clearing batch.
     */
    public static void clearBatch(Connection conn) throws SQLException {
        System.out.println("\n--- Clear Batch ---");
        
        String sql = "INSERT INTO batch_products (product_name, category, price) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Add some statements
            pstmt.setString(1, "Temporary Product 1");
            pstmt.setString(2, "Temp");
            pstmt.setDouble(3, 10.00);
            pstmt.addBatch();
            
            pstmt.setString(1, "Temporary Product 2");
            pstmt.setString(2, "Temp");
            pstmt.setDouble(3, 20.00);
            pstmt.addBatch();
            
            System.out.println("Added 2 statements to batch");
            
            // Clear the batch (maybe due to validation failure)
            pstmt.clearBatch();
            System.out.println("Batch cleared");
            
            // Add new statements
            pstmt.setString(1, "Final Product");
            pstmt.setString(2, "Final");
            pstmt.setDouble(3, 100.00);
            pstmt.addBatch();
            
            int[] results = pstmt.executeBatch();
            System.out.println("Executed batch with " + results.length + " statement(s)");
        }
    }
    
    /**
     * Show batch statistics.
     */
    public static void showStatistics(Connection conn) throws SQLException {
        System.out.println("\n--- Final Statistics ---");
        
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT category, COUNT(*) as count, " +
                    "AVG(price) as avg_price FROM batch_products GROUP BY category");
            
            System.out.println("\nProducts by category:");
            System.out.println("-".repeat(50));
            System.out.printf("%-15s %10s %15s%n", "Category", "Count", "Avg Price");
            System.out.println("-".repeat(50));
            
            while (rs.next()) {
                System.out.printf("%-15s %10d %15.2f%n",
                        rs.getString("category"),
                        rs.getInt("count"),
                        rs.getDouble("avg_price"));
            }
            
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM batch_orders");
            if (rs.next()) {
                System.out.println("\nTotal orders: " + rs.getInt("total"));
            }
        }
    }
    
    /**
     * Clean up.
     */
    public static void cleanup(Connection conn) throws SQLException {
        System.out.println("\n--- Cleanup ---");
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS batch_orders");
            stmt.execute("DROP TABLE IF EXISTS batch_products");
            System.out.println("✓ Tables dropped");
        }
    }
    
    /**
     * Run all batch processing demonstrations.
     */
    public static void demonstrate() {
        System.out.println("\n========================================");
        System.out.println("   BATCH PROCESSING DEMONSTRATION");
        System.out.println("========================================");
        
        Connection conn = null;
        try {
            conn = ConnectionManager.getH2Connection();
            
            setup(conn);
            batchInsertWithStatement(conn);
            batchInsertWithPreparedStatement(conn);
            comparePerformance(conn);
            batchUpdate(conn);
            batchMixedStatements(conn);
            handleBatchExceptions(conn);
            clearBatch(conn);
            showStatistics(conn);
            cleanup(conn);
            
            System.out.println("\n✓ All batch processing examples completed successfully!");
            
        } catch (SQLException e) {
            System.err.println("✗ Batch processing error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.closeConnection(conn);
        }
    }
    
    public static void main(String[] args) {
        demonstrate();
    }
}

