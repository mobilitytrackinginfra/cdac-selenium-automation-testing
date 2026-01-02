package com.jdbc.tutorial.transaction;

import com.jdbc.tutorial.connection.ConnectionManager;

import java.sql.*;

/**
 * TransactionExample - Demonstrates transaction management in JDBC.
 * 
 * Transaction concepts:
 * 1. ACID properties (Atomicity, Consistency, Isolation, Durability)
 * 2. Auto-commit mode
 * 3. Manual commit and rollback
 * 4. Savepoints
 * 5. Transaction isolation levels
 * 
 * @author JDBC Tutorial
 */
public class TransactionExample {
    
    /**
     * Setup tables for demonstration.
     */
    public static void setup(Connection conn) throws SQLException {
        System.out.println("\n--- Setting up tables ---");
        
        try (Statement stmt = conn.createStatement()) {
            // Bank accounts table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS accounts (
                    account_id INT PRIMARY KEY,
                    account_holder VARCHAR(100) NOT NULL,
                    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
                    account_type VARCHAR(20) DEFAULT 'SAVINGS'
                )
            """);
            
            // Transaction log table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS transaction_log (
                    log_id INT PRIMARY KEY AUTO_INCREMENT,
                    from_account INT,
                    to_account INT,
                    amount DECIMAL(15, 2),
                    transaction_type VARCHAR(50),
                    status VARCHAR(20),
                    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Insert sample accounts
            stmt.execute("""
                INSERT INTO accounts (account_id, account_holder, balance, account_type) VALUES
                (1001, 'Alice Johnson', 5000.00, 'SAVINGS'),
                (1002, 'Bob Smith', 3000.00, 'CHECKING'),
                (1003, 'Carol Williams', 7500.00, 'SAVINGS'),
                (1004, 'David Brown', 2000.00, 'CHECKING')
            """);
            
            System.out.println("✓ Tables created with sample data");
        }
    }
    
    /**
     * Demonstrate auto-commit mode.
     */
    public static void autoCommitDemo(Connection conn) throws SQLException {
        System.out.println("\n--- Auto-Commit Mode ---");
        
        System.out.println("Default auto-commit: " + conn.getAutoCommit());
        
        // With auto-commit ON, each statement is immediately committed
        try (PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE accounts SET balance = balance + 100 WHERE account_id = ?")) {
            
            pstmt.setInt(1, 1001);
            int rows = pstmt.executeUpdate();
            System.out.println("Updated " + rows + " row(s) - automatically committed");
        }
        
        showBalance(conn, 1001);
    }
    
    /**
     * Demonstrate manual transaction with commit.
     */
    public static void manualCommitDemo(Connection conn) throws SQLException {
        System.out.println("\n--- Manual Commit Demo ---");
        
        boolean originalAutoCommit = conn.getAutoCommit();
        
        try {
            // Disable auto-commit for manual transaction control
            conn.setAutoCommit(false);
            System.out.println("Auto-commit disabled");
            
            showBalance(conn, 1001);
            showBalance(conn, 1002);
            
            // Perform multiple operations as single transaction
            try (PreparedStatement debit = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE account_id = ?");
                 PreparedStatement credit = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance + ? WHERE account_id = ?")) {
                
                double amount = 500.00;
                
                // Debit from account 1001
                debit.setDouble(1, amount);
                debit.setInt(2, 1001);
                debit.executeUpdate();
                System.out.println("Debited $" + amount + " from account 1001");
                
                // Credit to account 1002
                credit.setDouble(1, amount);
                credit.setInt(2, 1002);
                credit.executeUpdate();
                System.out.println("Credited $" + amount + " to account 1002");
                
                // Both operations succeed - commit transaction
                conn.commit();
                System.out.println("✓ Transaction committed successfully");
            }
            
            System.out.println("\nAfter transfer:");
            showBalance(conn, 1001);
            showBalance(conn, 1002);
            
        } finally {
            conn.setAutoCommit(originalAutoCommit);
        }
    }
    
    /**
     * Demonstrate transaction rollback.
     */
    public static void rollbackDemo(Connection conn) throws SQLException {
        System.out.println("\n--- Rollback Demo ---");
        
        boolean originalAutoCommit = conn.getAutoCommit();
        
        try {
            conn.setAutoCommit(false);
            
            System.out.println("Before failed transaction:");
            showBalance(conn, 1001);
            showBalance(conn, 1003);
            
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE account_id = ?")) {
                
                double amount = 1000.00;
                
                // Debit from account 1001
                pstmt.setDouble(1, amount);
                pstmt.setInt(2, 1001);
                pstmt.executeUpdate();
                System.out.println("Debited $" + amount + " from account 1001");
                
                // Simulate an error condition
                boolean errorCondition = true;
                if (errorCondition) {
                    throw new SQLException("Simulated error - insufficient funds in target");
                }
                
                // This won't execute
                pstmt.setDouble(1, -amount);  // Credit
                pstmt.setInt(2, 1003);
                pstmt.executeUpdate();
                
                conn.commit();
                
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
                conn.rollback();
                System.out.println("✓ Transaction rolled back");
            }
            
            System.out.println("\nAfter rollback:");
            showBalance(conn, 1001);
            showBalance(conn, 1003);
            
        } finally {
            conn.setAutoCommit(originalAutoCommit);
        }
    }
    
    /**
     * Demonstrate savepoints for partial rollback.
     */
    public static void savepointDemo(Connection conn) throws SQLException {
        System.out.println("\n--- Savepoint Demo ---");
        
        boolean originalAutoCommit = conn.getAutoCommit();
        
        try {
            conn.setAutoCommit(false);
            
            System.out.println("Initial balances:");
            showBalance(conn, 1001);
            showBalance(conn, 1002);
            showBalance(conn, 1003);
            
            Savepoint savepoint1 = null;
            Savepoint savepoint2 = null;
            
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance + ? WHERE account_id = ?")) {
                
                // First operation - deposit to account 1001
                pstmt.setDouble(1, 200.00);
                pstmt.setInt(2, 1001);
                pstmt.executeUpdate();
                System.out.println("Operation 1: Deposited $200 to account 1001");
                
                // Create first savepoint
                savepoint1 = conn.setSavepoint("SAVEPOINT_1");
                System.out.println("✓ Savepoint 1 created");
                
                // Second operation - deposit to account 1002
                pstmt.setDouble(1, 300.00);
                pstmt.setInt(2, 1002);
                pstmt.executeUpdate();
                System.out.println("Operation 2: Deposited $300 to account 1002");
                
                // Create second savepoint
                savepoint2 = conn.setSavepoint("SAVEPOINT_2");
                System.out.println("✓ Savepoint 2 created");
                
                // Third operation - deposit to account 1003
                pstmt.setDouble(1, 400.00);
                pstmt.setInt(2, 1003);
                pstmt.executeUpdate();
                System.out.println("Operation 3: Deposited $400 to account 1003");
                
                // Simulate error - rollback to savepoint 1
                System.out.println("\nSimulating error after operation 3...");
                conn.rollback(savepoint1);
                System.out.println("✓ Rolled back to Savepoint 1 (operations 2 & 3 undone)");
                
                // Commit remaining (operation 1)
                conn.commit();
                System.out.println("✓ Transaction committed");
            }
            
            System.out.println("\nFinal balances (only operation 1 persisted):");
            showBalance(conn, 1001);  // Should show +200
            showBalance(conn, 1002);  // Should be unchanged
            showBalance(conn, 1003);  // Should be unchanged
            
        } finally {
            conn.setAutoCommit(originalAutoCommit);
        }
    }
    
    /**
     * Demonstrate transaction isolation levels.
     */
    public static void isolationLevelsDemo(Connection conn) throws SQLException {
        System.out.println("\n--- Transaction Isolation Levels ---");
        
        // Get current isolation level
        int currentLevel = conn.getTransactionIsolation();
        System.out.println("Current isolation level: " + getIsolationLevelName(currentLevel));
        
        System.out.println("\nAvailable isolation levels:");
        System.out.println("-".repeat(50));
        
        // TRANSACTION_NONE
        System.out.println("1. TRANSACTION_NONE (0)");
        System.out.println("   - Transactions not supported");
        
        // READ_UNCOMMITTED
        System.out.println("\n2. TRANSACTION_READ_UNCOMMITTED (1)");
        System.out.println("   - Dirty reads allowed");
        System.out.println("   - Non-repeatable reads allowed");
        System.out.println("   - Phantom reads allowed");
        System.out.println("   - Lowest isolation, highest concurrency");
        
        // READ_COMMITTED
        System.out.println("\n3. TRANSACTION_READ_COMMITTED (2)");
        System.out.println("   - Dirty reads prevented");
        System.out.println("   - Non-repeatable reads allowed");
        System.out.println("   - Phantom reads allowed");
        System.out.println("   - Default for many databases");
        
        // REPEATABLE_READ
        System.out.println("\n4. TRANSACTION_REPEATABLE_READ (4)");
        System.out.println("   - Dirty reads prevented");
        System.out.println("   - Non-repeatable reads prevented");
        System.out.println("   - Phantom reads allowed");
        System.out.println("   - MySQL default");
        
        // SERIALIZABLE
        System.out.println("\n5. TRANSACTION_SERIALIZABLE (8)");
        System.out.println("   - All phenomena prevented");
        System.out.println("   - Highest isolation, lowest concurrency");
        System.out.println("   - Transactions execute serially");
        
        System.out.println("-".repeat(50));
        
        // Demonstrate changing isolation level
        try {
            System.out.println("\nChanging isolation level to READ_COMMITTED...");
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            System.out.println("New isolation level: " + 
                    getIsolationLevelName(conn.getTransactionIsolation()));
            
            // Restore original level
            conn.setTransactionIsolation(currentLevel);
        } catch (SQLException e) {
            System.err.println("Could not change isolation level: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrate complete fund transfer with logging.
     */
    public static void completeTransferDemo(Connection conn) throws SQLException {
        System.out.println("\n--- Complete Fund Transfer Demo ---");
        
        boolean originalAutoCommit = conn.getAutoCommit();
        
        try {
            conn.setAutoCommit(false);
            
            int fromAccount = 1003;
            int toAccount = 1004;
            double amount = 500.00;
            
            System.out.println("Transferring $" + amount + " from account " + 
                    fromAccount + " to account " + toAccount);
            
            System.out.println("\nBefore transfer:");
            showBalance(conn, fromAccount);
            showBalance(conn, toAccount);
            
            // Check if source has sufficient balance
            double sourceBalance = getBalance(conn, fromAccount);
            if (sourceBalance < amount) {
                throw new SQLException("Insufficient balance");
            }
            
            try (PreparedStatement debit = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE account_id = ?");
                 PreparedStatement credit = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance + ? WHERE account_id = ?");
                 PreparedStatement log = conn.prepareStatement(
                    "INSERT INTO transaction_log (from_account, to_account, amount, transaction_type, status) " +
                    "VALUES (?, ?, ?, ?, ?)")) {
                
                // Debit
                debit.setDouble(1, amount);
                debit.setInt(2, fromAccount);
                int debitRows = debit.executeUpdate();
                
                // Credit
                credit.setDouble(1, amount);
                credit.setInt(2, toAccount);
                int creditRows = credit.executeUpdate();
                
                // Log transaction
                log.setInt(1, fromAccount);
                log.setInt(2, toAccount);
                log.setDouble(3, amount);
                log.setString(4, "TRANSFER");
                log.setString(5, "SUCCESS");
                log.executeUpdate();
                
                if (debitRows == 1 && creditRows == 1) {
                    conn.commit();
                    System.out.println("✓ Transfer completed and committed");
                } else {
                    conn.rollback();
                    System.out.println("✗ Transfer failed - rolled back");
                }
            }
            
            System.out.println("\nAfter transfer:");
            showBalance(conn, fromAccount);
            showBalance(conn, toAccount);
            
            // Show transaction log
            showTransactionLog(conn);
            
        } finally {
            conn.setAutoCommit(originalAutoCommit);
        }
    }
    
    /**
     * Helper method to show account balance.
     */
    private static void showBalance(Connection conn, int accountId) throws SQLException {
        String sql = "SELECT account_holder, balance FROM accounts WHERE account_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.printf("  Account %d (%s): $%.2f%n",
                            accountId,
                            rs.getString("account_holder"),
                            rs.getBigDecimal("balance"));
                }
            }
        }
    }
    
    /**
     * Helper method to get account balance.
     */
    private static double getBalance(Connection conn, int accountId) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance");
                }
            }
        }
        return 0;
    }
    
    /**
     * Helper method to show transaction log.
     */
    private static void showTransactionLog(Connection conn) throws SQLException {
        System.out.println("\nTransaction Log:");
        System.out.println("-".repeat(70));
        
        String sql = "SELECT * FROM transaction_log ORDER BY timestamp DESC LIMIT 5";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                System.out.printf("  %s: %s $%.2f from %d to %d - %s%n",
                        rs.getTimestamp("timestamp"),
                        rs.getString("transaction_type"),
                        rs.getBigDecimal("amount"),
                        rs.getInt("from_account"),
                        rs.getInt("to_account"),
                        rs.getString("status"));
            }
        }
    }
    
    /**
     * Get isolation level name.
     */
    private static String getIsolationLevelName(int level) {
        return switch (level) {
            case Connection.TRANSACTION_NONE -> "TRANSACTION_NONE";
            case Connection.TRANSACTION_READ_UNCOMMITTED -> "TRANSACTION_READ_UNCOMMITTED";
            case Connection.TRANSACTION_READ_COMMITTED -> "TRANSACTION_READ_COMMITTED";
            case Connection.TRANSACTION_REPEATABLE_READ -> "TRANSACTION_REPEATABLE_READ";
            case Connection.TRANSACTION_SERIALIZABLE -> "TRANSACTION_SERIALIZABLE";
            default -> "UNKNOWN (" + level + ")";
        };
    }
    
    /**
     * Clean up.
     */
    public static void cleanup(Connection conn) throws SQLException {
        System.out.println("\n--- Cleanup ---");
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS transaction_log");
            stmt.execute("DROP TABLE IF EXISTS accounts");
            System.out.println("✓ Tables dropped");
        }
    }
    
    /**
     * Run all transaction demonstrations.
     */
    public static void demonstrate() {
        System.out.println("\n========================================");
        System.out.println("   TRANSACTION MANAGEMENT DEMO");
        System.out.println("========================================");
        
        Connection conn = null;
        try {
            conn = ConnectionManager.getH2Connection();
            
            setup(conn);
            autoCommitDemo(conn);
            manualCommitDemo(conn);
            rollbackDemo(conn);
            savepointDemo(conn);
            isolationLevelsDemo(conn);
            completeTransferDemo(conn);
            cleanup(conn);
            
            System.out.println("\n✓ All transaction examples completed successfully!");
            
        } catch (SQLException e) {
            System.err.println("✗ Transaction error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.closeConnection(conn);
        }
    }
    
    public static void main(String[] args) {
        demonstrate();
    }
}

