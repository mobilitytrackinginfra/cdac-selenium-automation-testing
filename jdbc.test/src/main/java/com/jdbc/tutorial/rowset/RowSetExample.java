package com.jdbc.tutorial.rowset;

import com.jdbc.tutorial.connection.ConnectionManager;

import javax.sql.RowSet;
import javax.sql.rowset.*;
import java.sql.*;

/**
 * RowSetExample - Demonstrates RowSet usage in JDBC.
 * 
 * RowSet is an extension of ResultSet that provides:
 * 1. Disconnected data access
 * 2. Scrollable and updatable results
 * 3. JavaBeans component model support
 * 4. Event notification
 * 
 * Types of RowSet:
 * 1. JdbcRowSet - Connected, thin wrapper around ResultSet
 * 2. CachedRowSet - Disconnected, caches data in memory
 * 3. WebRowSet - CachedRowSet + XML serialization
 * 4. JoinRowSet - Combines multiple RowSets
 * 5. FilteredRowSet - Filtered view of data
 * 
 * @author JDBC Tutorial
 */
public class RowSetExample {
    
    private static final String DB_URL = "jdbc:h2:mem:rowset_demo;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    /**
     * Setup tables for demonstration.
     */
    public static void setup() throws SQLException {
        System.out.println("\n--- Setting up tables ---");
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            // Products table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS rs_products (
                    product_id INT PRIMARY KEY AUTO_INCREMENT,
                    product_name VARCHAR(100) NOT NULL,
                    category VARCHAR(50),
                    price DECIMAL(10, 2),
                    quantity INT DEFAULT 0
                )
            """);
            
            // Orders table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS rs_orders (
                    order_id INT PRIMARY KEY AUTO_INCREMENT,
                    customer_name VARCHAR(100),
                    product_id INT,
                    quantity INT,
                    order_date DATE
                )
            """);
            
            // Insert sample data
            stmt.execute("""
                INSERT INTO rs_products (product_name, category, price, quantity) VALUES
                ('Laptop', 'Electronics', 999.99, 50),
                ('Mouse', 'Electronics', 29.99, 200),
                ('Keyboard', 'Electronics', 79.99, 150),
                ('Monitor', 'Electronics', 299.99, 75),
                ('Desk', 'Furniture', 199.99, 30),
                ('Chair', 'Furniture', 149.99, 45),
                ('Lamp', 'Furniture', 39.99, 100),
                ('Notebook', 'Stationery', 9.99, 500),
                ('Pen Set', 'Stationery', 14.99, 300)
            """);
            
            stmt.execute("""
                INSERT INTO rs_orders (customer_name, product_id, quantity, order_date) VALUES
                ('John Doe', 1, 2, '2024-01-15'),
                ('Jane Smith', 2, 5, '2024-01-16'),
                ('Bob Wilson', 5, 1, '2024-01-17'),
                ('Alice Brown', 1, 1, '2024-01-18'),
                ('Charlie Davis', 3, 3, '2024-01-19')
            """);
            
            System.out.println("✓ Sample tables created with data");
        }
    }
    
    /**
     * Demonstrate JdbcRowSet - Connected RowSet.
     */
    public static void jdbcRowSetDemo() throws SQLException {
        System.out.println("\n--- JdbcRowSet Demo (Connected RowSet) ---");
        
        RowSetFactory factory = RowSetProvider.newFactory();
        
        try (JdbcRowSet jdbcRs = factory.createJdbcRowSet()) {
            // Configure connection
            jdbcRs.setUrl(DB_URL);
            jdbcRs.setUsername(DB_USER);
            jdbcRs.setPassword(DB_PASSWORD);
            
            // Set SQL command
            jdbcRs.setCommand("SELECT * FROM rs_products WHERE category = ?");
            jdbcRs.setString(1, "Electronics");
            
            // Execute query
            jdbcRs.execute();
            
            System.out.println("\nElectronics products:");
            System.out.println("-".repeat(60));
            
            while (jdbcRs.next()) {
                System.out.printf("ID: %d, Name: %-15s, Price: $%.2f, Qty: %d%n",
                        jdbcRs.getInt("product_id"),
                        jdbcRs.getString("product_name"),
                        jdbcRs.getBigDecimal("price"),
                        jdbcRs.getInt("quantity"));
            }
            
            // JdbcRowSet is scrollable and updatable by default
            System.out.println("\nUpdating first product price through JdbcRowSet:");
            jdbcRs.first();
            double oldPrice = jdbcRs.getDouble("price");
            jdbcRs.updateDouble("price", oldPrice * 0.9);  // 10% discount
            jdbcRs.updateRow();
            System.out.println("✓ Price updated from $" + oldPrice + " to $" + (oldPrice * 0.9));
        }
    }
    
    /**
     * Demonstrate CachedRowSet - Disconnected RowSet.
     */
    public static void cachedRowSetDemo() throws SQLException {
        System.out.println("\n--- CachedRowSet Demo (Disconnected RowSet) ---");
        
        RowSetFactory factory = RowSetProvider.newFactory();
        
        try (CachedRowSet cachedRs = factory.createCachedRowSet()) {
            // Configure connection
            cachedRs.setUrl(DB_URL);
            cachedRs.setUsername(DB_USER);
            cachedRs.setPassword(DB_PASSWORD);
            
            // Set command and execute
            cachedRs.setCommand("SELECT * FROM rs_products ORDER BY price DESC");
            cachedRs.execute();  // Data is now cached, connection is closed
            
            System.out.println("\nProducts (ordered by price, cached in memory):");
            System.out.println("-".repeat(60));
            System.out.println("Note: Connection is now CLOSED, data is cached!");
            
            while (cachedRs.next()) {
                System.out.printf("%-15s - $%.2f (%s)%n",
                        cachedRs.getString("product_name"),
                        cachedRs.getBigDecimal("price"),
                        cachedRs.getString("category"));
            }
            
            // Demonstrate scrolling on cached data
            System.out.println("\nScrolling demonstration:");
            cachedRs.first();
            System.out.println("First: " + cachedRs.getString("product_name"));
            cachedRs.last();
            System.out.println("Last: " + cachedRs.getString("product_name"));
            cachedRs.absolute(5);
            System.out.println("Row 5: " + cachedRs.getString("product_name"));
            
            // Modify data offline
            System.out.println("\nModifying data offline:");
            cachedRs.first();
            cachedRs.updateInt("quantity", cachedRs.getInt("quantity") + 100);
            cachedRs.updateRow();
            System.out.println("✓ Quantity updated in cache");
            
            // Insert new row
            cachedRs.moveToInsertRow();
            cachedRs.updateString("product_name", "New Cached Product");
            cachedRs.updateString("category", "Test");
            cachedRs.updateBigDecimal("price", new java.math.BigDecimal("99.99"));
            cachedRs.updateInt("quantity", 25);
            cachedRs.insertRow();
            cachedRs.moveToCurrentRow();
            System.out.println("✓ New row inserted in cache");
            
            // Sync changes back to database
            System.out.println("\nSyncing changes to database...");
            cachedRs.acceptChanges();
            System.out.println("✓ Changes synced to database");
        }
    }
    
    /**
     * Demonstrate paging with CachedRowSet.
     */
    public static void pagingDemo() throws SQLException {
        System.out.println("\n--- Paging with CachedRowSet ---");
        
        RowSetFactory factory = RowSetProvider.newFactory();
        
        try (CachedRowSet cachedRs = factory.createCachedRowSet()) {
            cachedRs.setUrl(DB_URL);
            cachedRs.setUsername(DB_USER);
            cachedRs.setPassword(DB_PASSWORD);
            
            // Set page size
            cachedRs.setPageSize(3);  // 3 rows per page
            
            cachedRs.setCommand("SELECT * FROM rs_products ORDER BY product_id");
            cachedRs.execute();
            
            int pageNum = 1;
            do {
                System.out.println("\nPage " + pageNum + ":");
                System.out.println("-".repeat(40));
                
                while (cachedRs.next()) {
                    System.out.printf("  %d. %s%n",
                            cachedRs.getInt("product_id"),
                            cachedRs.getString("product_name"));
                }
                
                pageNum++;
            } while (cachedRs.nextPage());  // Move to next page
            
            System.out.println("\nTotal pages displayed: " + (pageNum - 1));
        }
    }
    
    /**
     * Demonstrate FilteredRowSet.
     */
    public static void filteredRowSetDemo() throws SQLException {
        System.out.println("\n--- FilteredRowSet Demo ---");
        
        RowSetFactory factory = RowSetProvider.newFactory();
        
        try (FilteredRowSet filteredRs = factory.createFilteredRowSet()) {
            filteredRs.setUrl(DB_URL);
            filteredRs.setUsername(DB_USER);
            filteredRs.setPassword(DB_PASSWORD);
            
            filteredRs.setCommand("SELECT * FROM rs_products");
            filteredRs.execute();
            
            System.out.println("\nAll products (before filter):");
            int count = 0;
            while (filteredRs.next()) {
                count++;
            }
            System.out.println("Total rows: " + count);
            
            // Apply custom filter
            filteredRs.setFilter(new Predicate() {
                @Override
                public boolean evaluate(RowSet rs) {
                    try {
                        // Filter: price > 50
                        return rs.getBigDecimal("price").doubleValue() > 50.0;
                    } catch (SQLException e) {
                        return false;
                    }
                }
                
                @Override
                public boolean evaluate(Object value, int column) throws SQLException {
                    return true;
                }
                
                @Override
                public boolean evaluate(Object value, String columnName) throws SQLException {
                    return true;
                }
            });
            
            System.out.println("\nProducts with price > $50 (after filter):");
            System.out.println("-".repeat(50));
            
            filteredRs.beforeFirst();
            while (filteredRs.next()) {
                System.out.printf("  %-15s - $%.2f%n",
                        filteredRs.getString("product_name"),
                        filteredRs.getBigDecimal("price"));
            }
            
            // Remove filter
            filteredRs.setFilter(null);
            System.out.println("\nFilter removed - all rows accessible again");
        }
    }
    
    /**
     * Demonstrate WebRowSet for XML serialization.
     */
    public static void webRowSetDemo() throws SQLException {
        System.out.println("\n--- WebRowSet Demo (XML Serialization) ---");
        
        RowSetFactory factory = RowSetProvider.newFactory();
        
        try (WebRowSet webRs = factory.createWebRowSet()) {
            webRs.setUrl(DB_URL);
            webRs.setUsername(DB_USER);
            webRs.setPassword(DB_PASSWORD);
            
            webRs.setCommand("SELECT product_id, product_name, price FROM rs_products LIMIT 3");
            webRs.execute();
            
            // Write to XML (to string for demonstration)
            java.io.StringWriter writer = new java.io.StringWriter();
            webRs.writeXml(writer);
            
            String xml = writer.toString();
            
            // Show just a portion of the XML
            System.out.println("\nXML Output (first 500 chars):");
            System.out.println("-".repeat(60));
            System.out.println(xml.substring(0, Math.min(500, xml.length())) + "...");
            
            System.out.println("\n✓ WebRowSet can be serialized to XML and transmitted over network");
            System.out.println("✓ Can be read back using webRs.readXml(reader)");
        }
    }
    
    /**
     * Demonstrate populating CachedRowSet from ResultSet.
     */
    public static void populateFromResultSet() throws SQLException {
        System.out.println("\n--- Populate CachedRowSet from ResultSet ---");
        
        RowSetFactory factory = RowSetProvider.newFactory();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rs_products WHERE category = 'Furniture'");
             CachedRowSet cachedRs = factory.createCachedRowSet()) {
            
            // Populate CachedRowSet from existing ResultSet
            cachedRs.populate(rs);
            
            System.out.println("\nCachedRowSet populated from ResultSet:");
            System.out.println("-".repeat(50));
            
            while (cachedRs.next()) {
                System.out.printf("  %-15s - $%.2f%n",
                        cachedRs.getString("product_name"),
                        cachedRs.getBigDecimal("price"));
            }
        }
    }
    
    /**
     * Demonstrate RowSet event handling.
     */
    public static void eventHandlingDemo() throws SQLException {
        System.out.println("\n--- RowSet Event Handling ---");
        
        RowSetFactory factory = RowSetProvider.newFactory();
        
        try (CachedRowSet cachedRs = factory.createCachedRowSet()) {
            // Add listener
            cachedRs.addRowSetListener(new javax.sql.RowSetListener() {
                @Override
                public void rowSetChanged(javax.sql.RowSetEvent event) {
                    System.out.println("  [Event] RowSet changed");
                }
                
                @Override
                public void rowChanged(javax.sql.RowSetEvent event) {
                    System.out.println("  [Event] Row changed");
                }
                
                @Override
                public void cursorMoved(javax.sql.RowSetEvent event) {
                    System.out.println("  [Event] Cursor moved");
                }
            });
            
            cachedRs.setUrl(DB_URL);
            cachedRs.setUsername(DB_USER);
            cachedRs.setPassword(DB_PASSWORD);
            cachedRs.setCommand("SELECT * FROM rs_products LIMIT 3");
            
            System.out.println("\nExecuting query:");
            cachedRs.execute();
            
            System.out.println("\nNavigating rows:");
            cachedRs.next();  // Triggers cursor moved
            cachedRs.next();  // Triggers cursor moved
            
            System.out.println("\nUpdating row:");
            cachedRs.updateInt("quantity", 999);
            cachedRs.updateRow();  // Triggers row changed
        }
    }
    
    /**
     * Clean up.
     */
    public static void cleanup() throws SQLException {
        System.out.println("\n--- Cleanup ---");
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS rs_orders");
            stmt.execute("DROP TABLE IF EXISTS rs_products");
            System.out.println("✓ Tables dropped");
        }
    }
    
    /**
     * Run all RowSet demonstrations.
     */
    public static void demonstrate() {
        System.out.println("\n========================================");
        System.out.println("       ROWSET DEMONSTRATION");
        System.out.println("========================================");
        
        try {
            setup();
            jdbcRowSetDemo();
            cachedRowSetDemo();
            pagingDemo();
            filteredRowSetDemo();
            webRowSetDemo();
            populateFromResultSet();
            eventHandlingDemo();
            cleanup();
            
            System.out.println("\n✓ All RowSet examples completed successfully!");
            
        } catch (SQLException e) {
            System.err.println("✗ RowSet error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        demonstrate();
    }
}

