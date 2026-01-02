package com.jdbc.tutorial.metadata;

import com.jdbc.tutorial.connection.ConnectionManager;

import java.sql.*;

/**
 * MetaDataExample - Demonstrates DatabaseMetaData and ResultSetMetaData.
 * 
 * MetaData provides information about:
 * 1. Database capabilities and features
 * 2. Schema structure (tables, columns, keys)
 * 3. ResultSet column information
 * 
 * @author JDBC Tutorial
 */
public class MetaDataExample {
    
    /**
     * Setup sample tables for demonstration.
     */
    public static void setup(Connection conn) throws SQLException {
        System.out.println("\n--- Setting up sample tables ---");
        
        try (Statement stmt = conn.createStatement()) {
            // Departments table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS meta_departments (
                    dept_id INT PRIMARY KEY AUTO_INCREMENT,
                    dept_name VARCHAR(100) NOT NULL UNIQUE,
                    location VARCHAR(100),
                    budget DECIMAL(15, 2)
                )
            """);
            
            // Employees table with foreign key
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS meta_employees (
                    emp_id INT PRIMARY KEY AUTO_INCREMENT,
                    first_name VARCHAR(50) NOT NULL,
                    last_name VARCHAR(50) NOT NULL,
                    email VARCHAR(100) UNIQUE,
                    hire_date DATE,
                    salary DECIMAL(10, 2),
                    dept_id INT,
                    FOREIGN KEY (dept_id) REFERENCES meta_departments(dept_id)
                )
            """);
            
            // Create an index
            stmt.execute("CREATE INDEX idx_emp_email ON meta_employees(email)");
            
            // Insert sample data
            stmt.execute("""
                INSERT INTO meta_departments (dept_name, location, budget) VALUES
                ('Engineering', 'Building A', 500000.00),
                ('Marketing', 'Building B', 300000.00)
            """);
            
            stmt.execute("""
                INSERT INTO meta_employees (first_name, last_name, email, hire_date, salary, dept_id) VALUES
                ('John', 'Doe', 'john.doe@company.com', '2020-01-15', 75000.00, 1),
                ('Jane', 'Smith', 'jane.smith@company.com', '2019-06-20', 82000.00, 1),
                ('Bob', 'Wilson', 'bob.wilson@company.com', '2021-03-10', 65000.00, 2)
            """);
            
            System.out.println("✓ Sample tables created with data");
        }
    }
    
    /**
     * Demonstrate DatabaseMetaData - General database information.
     */
    public static void databaseInfo(Connection conn) throws SQLException {
        System.out.println("\n--- Database Information ---");
        
        DatabaseMetaData dbMetaData = conn.getMetaData();
        
        System.out.println("\nDatabase Product Information:");
        System.out.println("-".repeat(50));
        System.out.println("Database Product Name: " + dbMetaData.getDatabaseProductName());
        System.out.println("Database Product Version: " + dbMetaData.getDatabaseProductVersion());
        System.out.println("Database Major Version: " + dbMetaData.getDatabaseMajorVersion());
        System.out.println("Database Minor Version: " + dbMetaData.getDatabaseMinorVersion());
        
        System.out.println("\nDriver Information:");
        System.out.println("-".repeat(50));
        System.out.println("Driver Name: " + dbMetaData.getDriverName());
        System.out.println("Driver Version: " + dbMetaData.getDriverVersion());
        System.out.println("JDBC Major Version: " + dbMetaData.getJDBCMajorVersion());
        System.out.println("JDBC Minor Version: " + dbMetaData.getJDBCMinorVersion());
        
        System.out.println("\nConnection Information:");
        System.out.println("-".repeat(50));
        System.out.println("URL: " + dbMetaData.getURL());
        System.out.println("User Name: " + dbMetaData.getUserName());
        System.out.println("Read Only: " + dbMetaData.isReadOnly());
        System.out.println("Max Connections: " + dbMetaData.getMaxConnections());
    }
    
    /**
     * Demonstrate DatabaseMetaData - Feature support.
     */
    public static void featureSupport(Connection conn) throws SQLException {
        System.out.println("\n--- Database Feature Support ---");
        
        DatabaseMetaData dbMetaData = conn.getMetaData();
        
        System.out.println("\nSQL Features:");
        System.out.println("-".repeat(50));
        System.out.println("Supports Transactions: " + dbMetaData.supportsTransactions());
        System.out.println("Supports Savepoints: " + dbMetaData.supportsSavepoints());
        System.out.println("Supports Batch Updates: " + dbMetaData.supportsBatchUpdates());
        System.out.println("Supports Stored Procedures: " + dbMetaData.supportsStoredProcedures());
        System.out.println("Supports Outer Joins: " + dbMetaData.supportsOuterJoins());
        System.out.println("Supports Full Outer Joins: " + dbMetaData.supportsFullOuterJoins());
        System.out.println("Supports Group By: " + dbMetaData.supportsGroupBy());
        System.out.println("Supports Union: " + dbMetaData.supportsUnion());
        System.out.println("Supports Union All: " + dbMetaData.supportsUnionAll());
        System.out.println("Supports Subqueries: " + dbMetaData.supportsSubqueriesInComparisons());
        
        System.out.println("\nResultSet Features:");
        System.out.println("-".repeat(50));
        System.out.println("Supports Scrollable ResultSet: " + 
                dbMetaData.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE));
        System.out.println("Supports Updatable ResultSet: " + 
                dbMetaData.supportsResultSetConcurrency(
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE));
        System.out.println("Supports Holdable ResultSet: " + 
                dbMetaData.supportsResultSetHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT));
    }
    
    /**
     * Demonstrate DatabaseMetaData - SQL Limits.
     */
    public static void sqlLimits(Connection conn) throws SQLException {
        System.out.println("\n--- SQL Limits ---");
        
        DatabaseMetaData dbMetaData = conn.getMetaData();
        
        System.out.println("-".repeat(50));
        System.out.println("Max Table Name Length: " + dbMetaData.getMaxTableNameLength());
        System.out.println("Max Column Name Length: " + dbMetaData.getMaxColumnNameLength());
        System.out.println("Max Columns in Table: " + dbMetaData.getMaxColumnsInTable());
        System.out.println("Max Columns in SELECT: " + dbMetaData.getMaxColumnsInSelect());
        System.out.println("Max Columns in Index: " + dbMetaData.getMaxColumnsInIndex());
        System.out.println("Max Columns in GROUP BY: " + dbMetaData.getMaxColumnsInGroupBy());
        System.out.println("Max Columns in ORDER BY: " + dbMetaData.getMaxColumnsInOrderBy());
        System.out.println("Max Row Size: " + dbMetaData.getMaxRowSize() + " bytes");
        System.out.println("Max Statement Length: " + dbMetaData.getMaxStatementLength());
        System.out.println("Max Tables in SELECT: " + dbMetaData.getMaxTablesInSelect());
    }
    
    /**
     * Demonstrate getting table information.
     */
    public static void tableInfo(Connection conn) throws SQLException {
        System.out.println("\n--- Table Information ---");
        
        DatabaseMetaData dbMetaData = conn.getMetaData();
        
        // Get tables
        // Parameters: catalog, schemaPattern, tableNamePattern, types
        try (ResultSet tables = dbMetaData.getTables(null, null, "META_%", new String[]{"TABLE"})) {
            
            System.out.println("\nTables matching 'META_%':");
            System.out.println("-".repeat(70));
            System.out.printf("%-25s %-15s %-15s%n", "Table Name", "Table Type", "Schema");
            System.out.println("-".repeat(70));
            
            while (tables.next()) {
                System.out.printf("%-25s %-15s %-15s%n",
                        tables.getString("TABLE_NAME"),
                        tables.getString("TABLE_TYPE"),
                        tables.getString("TABLE_SCHEM"));
            }
        }
    }
    
    /**
     * Demonstrate getting column information.
     */
    public static void columnInfo(Connection conn) throws SQLException {
        System.out.println("\n--- Column Information ---");
        
        DatabaseMetaData dbMetaData = conn.getMetaData();
        
        // Get columns for meta_employees table
        try (ResultSet columns = dbMetaData.getColumns(null, null, "META_EMPLOYEES", null)) {
            
            System.out.println("\nColumns in META_EMPLOYEES:");
            System.out.println("-".repeat(80));
            System.out.printf("%-15s %-15s %-10s %-10s %-10s%n", 
                    "Column", "Type", "Size", "Nullable", "Default");
            System.out.println("-".repeat(80));
            
            while (columns.next()) {
                String nullable = columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable 
                        ? "YES" : "NO";
                
                System.out.printf("%-15s %-15s %-10d %-10s %-10s%n",
                        columns.getString("COLUMN_NAME"),
                        columns.getString("TYPE_NAME"),
                        columns.getInt("COLUMN_SIZE"),
                        nullable,
                        columns.getString("COLUMN_DEF"));
            }
        }
    }
    
    /**
     * Demonstrate getting primary key information.
     */
    public static void primaryKeyInfo(Connection conn) throws SQLException {
        System.out.println("\n--- Primary Key Information ---");
        
        DatabaseMetaData dbMetaData = conn.getMetaData();
        
        String[] tables = {"META_DEPARTMENTS", "META_EMPLOYEES"};
        
        for (String tableName : tables) {
            try (ResultSet pks = dbMetaData.getPrimaryKeys(null, null, tableName)) {
                System.out.println("\nPrimary Keys for " + tableName + ":");
                
                while (pks.next()) {
                    System.out.printf("  Column: %s, Key Name: %s, Sequence: %d%n",
                            pks.getString("COLUMN_NAME"),
                            pks.getString("PK_NAME"),
                            pks.getInt("KEY_SEQ"));
                }
            }
        }
    }
    
    /**
     * Demonstrate getting foreign key information.
     */
    public static void foreignKeyInfo(Connection conn) throws SQLException {
        System.out.println("\n--- Foreign Key Information ---");
        
        DatabaseMetaData dbMetaData = conn.getMetaData();
        
        // Get imported keys (foreign keys in a table)
        try (ResultSet fks = dbMetaData.getImportedKeys(null, null, "META_EMPLOYEES")) {
            
            System.out.println("\nForeign Keys in META_EMPLOYEES:");
            
            while (fks.next()) {
                System.out.println("-".repeat(50));
                System.out.println("FK Name: " + fks.getString("FK_NAME"));
                System.out.println("FK Column: " + fks.getString("FKCOLUMN_NAME"));
                System.out.println("References: " + fks.getString("PKTABLE_NAME") + 
                        "(" + fks.getString("PKCOLUMN_NAME") + ")");
                System.out.println("Update Rule: " + getKeyRule(fks.getInt("UPDATE_RULE")));
                System.out.println("Delete Rule: " + getKeyRule(fks.getInt("DELETE_RULE")));
            }
        }
    }
    
    /**
     * Demonstrate getting index information.
     */
    public static void indexInfo(Connection conn) throws SQLException {
        System.out.println("\n--- Index Information ---");
        
        DatabaseMetaData dbMetaData = conn.getMetaData();
        
        // Parameters: catalog, schema, table, unique, approximate
        try (ResultSet indexes = dbMetaData.getIndexInfo(null, null, "META_EMPLOYEES", false, false)) {
            
            System.out.println("\nIndexes on META_EMPLOYEES:");
            System.out.println("-".repeat(70));
            System.out.printf("%-20s %-15s %-15s %-10s%n", "Index Name", "Column", "Type", "Unique");
            System.out.println("-".repeat(70));
            
            while (indexes.next()) {
                String indexName = indexes.getString("INDEX_NAME");
                if (indexName != null) {
                    String indexType = switch (indexes.getInt("TYPE")) {
                        case DatabaseMetaData.tableIndexStatistic -> "STATISTIC";
                        case DatabaseMetaData.tableIndexClustered -> "CLUSTERED";
                        case DatabaseMetaData.tableIndexHashed -> "HASHED";
                        case DatabaseMetaData.tableIndexOther -> "OTHER";
                        default -> "UNKNOWN";
                    };
                    
                    System.out.printf("%-20s %-15s %-15s %-10s%n",
                            indexName,
                            indexes.getString("COLUMN_NAME"),
                            indexType,
                            !indexes.getBoolean("NON_UNIQUE") ? "YES" : "NO");
                }
            }
        }
    }
    
    /**
     * Demonstrate ResultSetMetaData.
     */
    public static void resultSetMetaData(Connection conn) throws SQLException {
        System.out.println("\n--- ResultSetMetaData ---");
        
        String sql = "SELECT e.emp_id, e.first_name, e.last_name, e.email, e.salary, " +
                    "d.dept_name, d.budget " +
                    "FROM meta_employees e " +
                    "JOIN meta_departments d ON e.dept_id = d.dept_id";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            ResultSetMetaData rsMetaData = rs.getMetaData();
            
            int columnCount = rsMetaData.getColumnCount();
            System.out.println("\nQuery ResultSet has " + columnCount + " columns:");
            System.out.println("-".repeat(100));
            System.out.printf("%-5s %-15s %-15s %-12s %-8s %-10s %-10s%n",
                    "#", "Column Name", "Label", "Type", "Size", "Nullable", "Table");
            System.out.println("-".repeat(100));
            
            for (int i = 1; i <= columnCount; i++) {
                String nullable = switch (rsMetaData.isNullable(i)) {
                    case ResultSetMetaData.columnNoNulls -> "NO";
                    case ResultSetMetaData.columnNullable -> "YES";
                    default -> "UNKNOWN";
                };
                
                System.out.printf("%-5d %-15s %-15s %-12s %-8d %-10s %-10s%n",
                        i,
                        rsMetaData.getColumnName(i),
                        rsMetaData.getColumnLabel(i),
                        rsMetaData.getColumnTypeName(i),
                        rsMetaData.getColumnDisplaySize(i),
                        nullable,
                        rsMetaData.getTableName(i));
            }
            
            System.out.println("\nAdditional Column Properties:");
            System.out.println("-".repeat(70));
            
            for (int i = 1; i <= columnCount; i++) {
                System.out.println("\nColumn " + i + " (" + rsMetaData.getColumnName(i) + "):");
                System.out.println("  Class Name: " + rsMetaData.getColumnClassName(i));
                System.out.println("  Auto Increment: " + rsMetaData.isAutoIncrement(i));
                System.out.println("  Case Sensitive: " + rsMetaData.isCaseSensitive(i));
                System.out.println("  Searchable: " + rsMetaData.isSearchable(i));
                System.out.println("  Writable: " + rsMetaData.isWritable(i));
                System.out.println("  Read Only: " + rsMetaData.isReadOnly(i));
                System.out.println("  Signed: " + rsMetaData.isSigned(i));
                
                if (rsMetaData.getPrecision(i) > 0) {
                    System.out.println("  Precision: " + rsMetaData.getPrecision(i));
                    System.out.println("  Scale: " + rsMetaData.getScale(i));
                }
            }
        }
    }
    
    /**
     * Demonstrate getting data type information.
     */
    public static void dataTypeInfo(Connection conn) throws SQLException {
        System.out.println("\n--- Supported Data Types ---");
        
        DatabaseMetaData dbMetaData = conn.getMetaData();
        
        try (ResultSet types = dbMetaData.getTypeInfo()) {
            
            System.out.println("-".repeat(70));
            System.out.printf("%-20s %-15s %-15s%n", "Type Name", "SQL Type", "Precision");
            System.out.println("-".repeat(70));
            
            int count = 0;
            while (types.next() && count < 15) {  // Limit output
                System.out.printf("%-20s %-15d %-15d%n",
                        types.getString("TYPE_NAME"),
                        types.getInt("DATA_TYPE"),
                        types.getLong("PRECISION"));
                count++;
            }
            System.out.println("... (showing first 15 types)");
        }
    }
    
    /**
     * Helper method to get key rule description.
     */
    private static String getKeyRule(int rule) {
        return switch (rule) {
            case DatabaseMetaData.importedKeyCascade -> "CASCADE";
            case DatabaseMetaData.importedKeySetNull -> "SET NULL";
            case DatabaseMetaData.importedKeySetDefault -> "SET DEFAULT";
            case DatabaseMetaData.importedKeyRestrict -> "RESTRICT";
            case DatabaseMetaData.importedKeyNoAction -> "NO ACTION";
            default -> "UNKNOWN";
        };
    }
    
    /**
     * Clean up.
     */
    public static void cleanup(Connection conn) throws SQLException {
        System.out.println("\n--- Cleanup ---");
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS meta_employees");
            stmt.execute("DROP TABLE IF EXISTS meta_departments");
            System.out.println("✓ Tables dropped");
        }
    }
    
    /**
     * Run all MetaData demonstrations.
     */
    public static void demonstrate() {
        System.out.println("\n========================================");
        System.out.println("      METADATA DEMONSTRATION");
        System.out.println("========================================");
        
        Connection conn = null;
        try {
            conn = ConnectionManager.getH2Connection();
            
            setup(conn);
            databaseInfo(conn);
            featureSupport(conn);
            sqlLimits(conn);
            tableInfo(conn);
            columnInfo(conn);
            primaryKeyInfo(conn);
            foreignKeyInfo(conn);
            indexInfo(conn);
            resultSetMetaData(conn);
            dataTypeInfo(conn);
            cleanup(conn);
            
            System.out.println("\n✓ All MetaData examples completed successfully!");
            
        } catch (SQLException e) {
            System.err.println("✗ MetaData error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.closeConnection(conn);
        }
    }
    
    public static void main(String[] args) {
        demonstrate();
    }
}

