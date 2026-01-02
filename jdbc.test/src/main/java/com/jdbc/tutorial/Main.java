package com.jdbc.tutorial;

import com.jdbc.tutorial.batch.BatchProcessingExample;
import com.jdbc.tutorial.connection.ConnectionManager;
import com.jdbc.tutorial.connection.ConnectionPoolExample;
import com.jdbc.tutorial.metadata.MetaDataExample;
import com.jdbc.tutorial.resultset.ResultSetExample;
import com.jdbc.tutorial.rowset.RowSetExample;
import com.jdbc.tutorial.statements.CallableStatementExample;
import com.jdbc.tutorial.statements.PreparedStatementExample;
import com.jdbc.tutorial.statements.StatementExample;
import com.jdbc.tutorial.transaction.TransactionExample;

import java.util.Scanner;

/**
 * Main - Entry point for JDBC Tutorial Project.
 * 
 * This project demonstrates all basic JDBC topics:
 * 1. Connection Management
 * 2. Statement (basic queries)
 * 3. PreparedStatement (parameterized queries)
 * 4. CallableStatement (stored procedures)
 * 5. ResultSet (data navigation)
 * 6. Batch Processing
 * 7. Transaction Management
 * 8. MetaData (database & resultset info)
 * 9. RowSet (disconnected data)
 * 10. Connection Pooling (HikariCP)
 * 
 * @author JDBC Tutorial
 */
public class Main {
    
    private static final String BANNER = """
            
            ╔═══════════════════════════════════════════════════════════════╗
            ║                                                               ║
            ║         ██╗██████╗ ██████╗  ██████╗                          ║
            ║         ██║██╔══██╗██╔══██╗██╔════╝                          ║
            ║         ██║██║  ██║██████╔╝██║                               ║
            ║    ██   ██║██║  ██║██╔══██╗██║                               ║
            ║    ╚█████╔╝██████╔╝██████╔╝╚██████╗                          ║
            ║     ╚════╝ ╚═════╝ ╚═════╝  ╚═════╝                          ║
            ║                                                               ║
            ║              JDBC TUTORIAL PROJECT                            ║
            ║         Comprehensive JDBC Demonstration                      ║
            ║                                                               ║
            ╚═══════════════════════════════════════════════════════════════╝
            """;
    
    private static final String MENU = """
            
            ┌───────────────────────────────────────────────────────────────┐
            │                     SELECT A DEMO                             │
            ├───────────────────────────────────────────────────────────────┤
            │  1.  Connection Management                                    │
            │  2.  Statement (Basic CRUD)                                   │
            │  3.  PreparedStatement (Parameterized Queries)                │
            │  4.  CallableStatement (Stored Procedures)                    │
            │  5.  ResultSet (Navigation & Types)                           │
            │  6.  Batch Processing                                         │
            │  7.  Transaction Management                                   │
            │  8.  MetaData (Database & ResultSet Info)                     │
            │  9.  RowSet (Disconnected Data)                               │
            │  10. Connection Pooling (HikariCP)                            │
            │  11. Run ALL Demonstrations                                   │
            │  0.  Exit                                                     │
            └───────────────────────────────────────────────────────────────┘
            """;
    
    public static void main(String[] args) {
        System.out.println(BANNER);
        
        // Check for command line arguments
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("--all") || args[0].equalsIgnoreCase("-a")) {
                runAllDemos();
                return;
            } else if (args[0].equalsIgnoreCase("--help") || args[0].equalsIgnoreCase("-h")) {
                printHelp();
                return;
            }
        }
        
        // Interactive menu
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            System.out.println(MENU);
            System.out.print("Enter your choice (0-11): ");
            
            try {
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);
                
                switch (choice) {
                    case 0 -> {
                        running = false;
                        System.out.println("\nThank you for using JDBC Tutorial!");
                        System.out.println("Goodbye! 👋\n");
                    }
                    case 1 -> demonstrateConnection();
                    case 2 -> StatementExample.demonstrate();
                    case 3 -> PreparedStatementExample.demonstrate();
                    case 4 -> CallableStatementExample.demonstrate();
                    case 5 -> ResultSetExample.demonstrate();
                    case 6 -> BatchProcessingExample.demonstrate();
                    case 7 -> TransactionExample.demonstrate();
                    case 8 -> MetaDataExample.demonstrate();
                    case 9 -> RowSetExample.demonstrate();
                    case 10 -> ConnectionPoolExample.demonstratePooling();
                    case 11 -> runAllDemos();
                    default -> System.out.println("Invalid choice. Please enter 0-11.");
                }
                
                if (running && choice >= 1 && choice <= 11) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                }
                
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        
        scanner.close();
    }
    
    /**
     * Demonstrate connection management.
     */
    private static void demonstrateConnection() {
        System.out.println("\n========================================");
        System.out.println("   CONNECTION MANAGEMENT DEMO");
        System.out.println("========================================\n");
        
        try {
            // Test H2 connection (embedded database)
            System.out.println("Testing H2 In-Memory Connection:");
            System.out.println("-".repeat(40));
            
            var conn = ConnectionManager.getH2Connection();
            ConnectionManager.printConnectionInfo(conn);
            
            // Execute a simple query
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery("SELECT 'Hello from H2!' as greeting");
            if (rs.next()) {
                System.out.println("Query Result: " + rs.getString("greeting"));
            }
            rs.close();
            stmt.close();
            
            ConnectionManager.closeConnection(conn);
            
            System.out.println("\n✓ Connection demonstration completed!");
            
            // Show MySQL connection info (won't actually connect if MySQL isn't running)
            System.out.println("\nMySQL Connection Configuration:");
            System.out.println("-".repeat(40));
            var props = ConnectionManager.getProperties();
            System.out.println("Driver: " + props.getProperty("db.driver"));
            System.out.println("URL: " + props.getProperty("db.url"));
            System.out.println("User: " + props.getProperty("db.username"));
            System.out.println("\nNote: MySQL connection requires MySQL server to be running.");
            
        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }
    
    /**
     * Run all demonstrations.
     */
    private static void runAllDemos() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("        RUNNING ALL JDBC DEMONSTRATIONS");
        System.out.println("═".repeat(70));
        
        long startTime = System.currentTimeMillis();
        
        try {
            // 1. Connection
            demonstrateConnection();
            
            // 2. Statement
            StatementExample.demonstrate();
            
            // 3. PreparedStatement
            PreparedStatementExample.demonstrate();
            
            // 4. CallableStatement
            CallableStatementExample.demonstrate();
            
            // 5. ResultSet
            ResultSetExample.demonstrate();
            
            // 6. Batch Processing
            BatchProcessingExample.demonstrate();
            
            // 7. Transaction Management
            TransactionExample.demonstrate();
            
            // 8. MetaData
            MetaDataExample.demonstrate();
            
            // 9. RowSet
            RowSetExample.demonstrate();
            
            // 10. Connection Pooling
            ConnectionPoolExample.demonstratePooling();
            
            long endTime = System.currentTimeMillis();
            
            System.out.println("\n" + "═".repeat(70));
            System.out.println("        ALL DEMONSTRATIONS COMPLETED SUCCESSFULLY!");
            System.out.println("═".repeat(70));
            System.out.println("Total time: " + (endTime - startTime) + " ms");
            
        } catch (Exception e) {
            System.err.println("\n✗ Error during demonstrations: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Print help information.
     */
    private static void printHelp() {
        System.out.println("""
                
                JDBC Tutorial Project - Help
                ════════════════════════════
                
                Usage: java -jar jdbc.test.jar [options]
                
                Options:
                  --all, -a     Run all demonstrations automatically
                  --help, -h    Display this help message
                  
                Without options, the program runs in interactive mode
                with a menu to choose specific demonstrations.
                
                Available Demonstrations:
                  1.  Connection Management - Basic JDBC connections
                  2.  Statement - Basic SQL queries (CREATE, SELECT, UPDATE, DELETE)
                  3.  PreparedStatement - Parameterized queries (prevents SQL injection)
                  4.  CallableStatement - Stored procedures and functions
                  5.  ResultSet - Scrollable, updatable result sets
                  6.  Batch Processing - Efficient bulk operations
                  7.  Transaction Management - ACID, commits, rollbacks, savepoints
                  8.  MetaData - Database and ResultSet information
                  9.  RowSet - Disconnected data access
                  10. Connection Pooling - HikariCP connection pool
                
                Database Configuration:
                  Edit src/main/resources/db.properties to configure:
                  - MySQL connection (default)
                  - H2 in-memory database (for testing)
                
                Note: All demonstrations use H2 in-memory database by default
                for ease of use without requiring MySQL installation.
                """);
    }
}

