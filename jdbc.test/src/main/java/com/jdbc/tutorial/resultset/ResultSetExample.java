package com.jdbc.tutorial.resultset;

import com.jdbc.tutorial.connection.ConnectionManager;

import java.sql.*;

/**
 * ResultSetExample - Demonstrates ResultSet types, navigation, and usage.
 * 
 * ResultSet Types:
 * 1. TYPE_FORWARD_ONLY (default) - Can only move forward
 * 2. TYPE_SCROLL_INSENSITIVE - Can scroll, doesn't see changes
 * 3. TYPE_SCROLL_SENSITIVE - Can scroll, sees changes
 * 
 * ResultSet Concurrency:
 * 1. CONCUR_READ_ONLY (default) - Cannot update through ResultSet
 * 2. CONCUR_UPDATABLE - Can update through ResultSet
 * 
 * @author JDBC Tutorial
 */
public class ResultSetExample {
    
    /**
     * Setup sample data.
     */
    public static void setup(Connection conn) throws SQLException {
        System.out.println("\n--- Setting up sample data ---");
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS employees_rs (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(100) NOT NULL,
                    department VARCHAR(50),
                    salary DECIMAL(10, 2),
                    hire_date DATE
                )
            """);
            
            stmt.execute("""
                INSERT INTO employees_rs (name, department, salary, hire_date) VALUES
                ('Alice Johnson', 'Engineering', 85000.00, '2019-03-15'),
                ('Bob Smith', 'Marketing', 65000.00, '2020-07-20'),
                ('Carol Williams', 'Engineering', 92000.00, '2018-01-10'),
                ('David Brown', 'HR', 58000.00, '2021-05-25'),
                ('Eva Martinez', 'Engineering', 78000.00, '2020-11-30'),
                ('Frank Lee', 'Sales', 72000.00, '2019-09-05'),
                ('Grace Chen', 'Marketing', 68000.00, '2021-02-14'),
                ('Henry Wilson', 'Engineering', 95000.00, '2017-06-18'),
                ('Ivy Taylor', 'HR', 62000.00, '2022-01-03'),
                ('Jack Davis', 'Sales', 75000.00, '2020-04-22')
            """);
            
            System.out.println("✓ Sample data created");
        }
    }
    
    /**
     * Demonstrate basic ResultSet iteration (Forward Only).
     */
    public static void forwardOnlyResultSet(Connection conn) throws SQLException {
        System.out.println("\n--- Forward-Only ResultSet (Default) ---");
        
        String sql = "SELECT * FROM employees_rs ORDER BY id";
        
        try (Statement stmt = conn.createStatement();  // Default: TYPE_FORWARD_ONLY
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\nIterating through results:");
            System.out.println("-".repeat(70));
            
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %-15s, Dept: %-12s, Salary: $%.2f%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getBigDecimal("salary"));
            }
        }
    }
    
    /**
     * Demonstrate Scrollable ResultSet.
     */
    public static void scrollableResultSet(Connection conn) throws SQLException {
        System.out.println("\n--- Scrollable ResultSet ---");
        
        String sql = "SELECT * FROM employees_rs ORDER BY id";
        
        try (Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Move to last row
            rs.last();
            System.out.println("Last row - ID: " + rs.getInt("id") + 
                             ", Name: " + rs.getString("name"));
            
            // Move to first row
            rs.first();
            System.out.println("First row - ID: " + rs.getInt("id") + 
                             ", Name: " + rs.getString("name"));
            
            // Move to specific row (absolute positioning)
            rs.absolute(5);
            System.out.println("Row 5 - ID: " + rs.getInt("id") + 
                             ", Name: " + rs.getString("name"));
            
            // Move relative to current position
            rs.relative(-2);  // Move 2 rows back
            System.out.println("Row 3 (after relative -2) - ID: " + rs.getInt("id") + 
                             ", Name: " + rs.getString("name"));
            
            // Move before first (for re-iteration)
            rs.beforeFirst();
            System.out.println("\nAfter beforeFirst(), fetching first few rows:");
            
            int count = 0;
            while (rs.next() && count < 3) {
                System.out.println("  Row: " + rs.getRow() + " - " + rs.getString("name"));
                count++;
            }
            
            // Move after last
            rs.afterLast();
            System.out.println("\nMoving backwards from afterLast:");
            
            count = 0;
            while (rs.previous() && count < 3) {
                System.out.println("  Row: " + rs.getRow() + " - " + rs.getString("name"));
                count++;
            }
        }
    }
    
    /**
     * Demonstrate Updatable ResultSet.
     */
    public static void updatableResultSet(Connection conn) throws SQLException {
        System.out.println("\n--- Updatable ResultSet ---");
        
        String sql = "SELECT * FROM employees_rs WHERE department = 'Engineering'";
        
        try (Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("Engineering employees before update:");
            while (rs.next()) {
                System.out.printf("  ID: %d, Name: %s, Salary: $%.2f%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("salary"));
            }
            
            // Update through ResultSet
            rs.beforeFirst();
            while (rs.next()) {
                // Give 5% raise to all engineers
                double currentSalary = rs.getDouble("salary");
                rs.updateDouble("salary", currentSalary * 1.05);
                rs.updateRow();  // Commit the update
            }
            
            System.out.println("\nEngineering employees after 5% raise:");
            rs.beforeFirst();
            while (rs.next()) {
                System.out.printf("  ID: %d, Name: %s, Salary: $%.2f%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("salary"));
            }
        }
    }
    
    /**
     * Demonstrate inserting rows through ResultSet.
     */
    public static void insertThroughResultSet(Connection conn) throws SQLException {
        System.out.println("\n--- Insert Through ResultSet ---");
        
        String sql = "SELECT * FROM employees_rs";
        
        try (Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Move to insert row
            rs.moveToInsertRow();
            
            // Set values for new row
            rs.updateString("name", "New Employee");
            rs.updateString("department", "IT");
            rs.updateDouble("salary", 70000.00);
            rs.updateDate("hire_date", Date.valueOf("2024-01-15"));
            
            // Insert the row
            rs.insertRow();
            
            // Move back to the result set
            rs.moveToCurrentRow();
            
            System.out.println("✓ New row inserted through ResultSet");
            
            // Verify
            rs.last();
            System.out.println("Last employee: " + rs.getString("name") + 
                             " (" + rs.getString("department") + ")");
        }
    }
    
    /**
     * Demonstrate deleting rows through ResultSet.
     */
    public static void deleteThroughResultSet(Connection conn) throws SQLException {
        System.out.println("\n--- Delete Through ResultSet ---");
        
        String sql = "SELECT * FROM employees_rs WHERE name = 'New Employee'";
        
        try (Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                rs.deleteRow();
                System.out.println("✓ Row deleted through ResultSet");
            }
        }
    }
    
    /**
     * Demonstrate ResultSet position and row information.
     */
    public static void resultSetPositionInfo(Connection conn) throws SQLException {
        System.out.println("\n--- ResultSet Position Information ---");
        
        String sql = "SELECT * FROM employees_rs ORDER BY id";
        
        try (Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("isBeforeFirst: " + rs.isBeforeFirst());
            
            rs.first();
            System.out.println("\nAfter first():");
            System.out.println("  isFirst: " + rs.isFirst());
            System.out.println("  isLast: " + rs.isLast());
            System.out.println("  getRow: " + rs.getRow());
            
            rs.last();
            System.out.println("\nAfter last():");
            System.out.println("  isFirst: " + rs.isFirst());
            System.out.println("  isLast: " + rs.isLast());
            System.out.println("  getRow: " + rs.getRow());
            
            rs.afterLast();
            System.out.println("\nAfter afterLast():");
            System.out.println("  isAfterLast: " + rs.isAfterLast());
        }
    }
    
    /**
     * Demonstrate getting data by column index vs column name.
     */
    public static void columnAccess(Connection conn) throws SQLException {
        System.out.println("\n--- Column Access Methods ---");
        
        String sql = "SELECT id, name, department, salary FROM employees_rs WHERE id = 1";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                System.out.println("Access by column name:");
                System.out.println("  id: " + rs.getInt("id"));
                System.out.println("  name: " + rs.getString("name"));
                System.out.println("  department: " + rs.getString("department"));
                System.out.println("  salary: " + rs.getBigDecimal("salary"));
                
                System.out.println("\nAccess by column index (1-based):");
                System.out.println("  Column 1: " + rs.getInt(1));
                System.out.println("  Column 2: " + rs.getString(2));
                System.out.println("  Column 3: " + rs.getString(3));
                System.out.println("  Column 4: " + rs.getBigDecimal(4));
                
                // Note: Column index access is slightly faster but less readable
            }
        }
    }
    
    /**
     * Demonstrate ResultSet with fetch size optimization.
     */
    public static void fetchSizeOptimization(Connection conn) throws SQLException {
        System.out.println("\n--- Fetch Size Optimization ---");
        
        String sql = "SELECT * FROM employees_rs";
        
        try (Statement stmt = conn.createStatement()) {
            // Set fetch size - hint to driver for number of rows to fetch at a time
            stmt.setFetchSize(5);
            System.out.println("Fetch size set to: " + stmt.getFetchSize());
            
            try (ResultSet rs = stmt.executeQuery(sql)) {
                // Can also set on ResultSet
                rs.setFetchSize(10);
                System.out.println("ResultSet fetch size: " + rs.getFetchSize());
                
                // Fetch direction
                rs.setFetchDirection(ResultSet.FETCH_FORWARD);
                System.out.println("Fetch direction: FETCH_FORWARD");
                
                int count = 0;
                while (rs.next()) {
                    count++;
                }
                System.out.println("Total rows processed: " + count);
            }
        }
    }
    
    /**
     * Demonstrate ResultSet holdability.
     */
    public static void resultSetHoldability(Connection conn) throws SQLException {
        System.out.println("\n--- ResultSet Holdability ---");
        
        // Get default holdability
        int defaultHoldability = conn.getHoldability();
        System.out.println("Default holdability: " + 
                (defaultHoldability == ResultSet.HOLD_CURSORS_OVER_COMMIT 
                        ? "HOLD_CURSORS_OVER_COMMIT" 
                        : "CLOSE_CURSORS_AT_COMMIT"));
        
        // HOLD_CURSORS_OVER_COMMIT: ResultSet remains open after commit
        // CLOSE_CURSORS_AT_COMMIT: ResultSet is closed when transaction commits
        
        String sql = "SELECT * FROM employees_rs LIMIT 5";
        
        try (Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY,
                ResultSet.HOLD_CURSORS_OVER_COMMIT);
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("ResultSet created with HOLD_CURSORS_OVER_COMMIT");
            System.out.println("Holdability: " + rs.getHoldability());
            
            while (rs.next()) {
                System.out.println("  " + rs.getString("name"));
            }
        }
    }
    
    /**
     * Clean up.
     */
    public static void cleanup(Connection conn) throws SQLException {
        System.out.println("\n--- Cleanup ---");
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS employees_rs");
            System.out.println("✓ Table dropped");
        }
    }
    
    /**
     * Run all ResultSet demonstrations.
     */
    public static void demonstrate() {
        System.out.println("\n========================================");
        System.out.println("     RESULTSET DEMONSTRATION");
        System.out.println("========================================");
        
        Connection conn = null;
        try {
            conn = ConnectionManager.getH2Connection();
            
            setup(conn);
            forwardOnlyResultSet(conn);
            scrollableResultSet(conn);
            updatableResultSet(conn);
            insertThroughResultSet(conn);
            deleteThroughResultSet(conn);
            resultSetPositionInfo(conn);
            columnAccess(conn);
            fetchSizeOptimization(conn);
            resultSetHoldability(conn);
            cleanup(conn);
            
            System.out.println("\n✓ All ResultSet examples completed successfully!");
            
        } catch (SQLException e) {
            System.err.println("✗ ResultSet error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.closeConnection(conn);
        }
    }
    
    public static void main(String[] args) {
        demonstrate();
    }
}

