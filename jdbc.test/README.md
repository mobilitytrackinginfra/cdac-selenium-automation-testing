# JDBC Tutorial Project

A comprehensive Java project demonstrating all basic JDBC (Java Database Connectivity) topics.

## 📋 Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Topics Covered](#topics-covered)
- [Running the Project](#running-the-project)
- [Database Configuration](#database-configuration)
- [Examples Overview](#examples-overview)

## 🎯 Overview

This project provides hands-on examples for learning JDBC concepts. It uses **H2 in-memory database** by default for ease of use (no installation required), with optional **MySQL** support.

## 📦 Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- (Optional) MySQL 8.0 for MySQL examples

## 📁 Project Structure

```
jdbc.test/
├── pom.xml                           # Maven configuration
├── README.md                         # This file
└── src/
    └── main/
        ├── java/com/jdbc/tutorial/
        │   ├── Main.java             # Entry point with interactive menu
        │   ├── connection/
        │   │   ├── ConnectionManager.java      # Connection utilities
        │   │   └── ConnectionPoolExample.java  # HikariCP pooling
        │   ├── statements/
        │   │   ├── StatementExample.java       # Basic Statement
        │   │   ├── PreparedStatementExample.java # Parameterized queries
        │   │   └── CallableStatementExample.java # Stored procedures
        │   ├── resultset/
        │   │   └── ResultSetExample.java       # ResultSet navigation
        │   ├── batch/
        │   │   └── BatchProcessingExample.java # Batch operations
        │   ├── transaction/
        │   │   └── TransactionExample.java     # Transaction management
        │   ├── metadata/
        │   │   └── MetaDataExample.java        # Database/ResultSet metadata
        │   └── rowset/
        │       └── RowSetExample.java          # RowSet demonstrations
        └── resources/
            ├── db.properties         # Database configuration
            └── schema.sql            # MySQL schema with sample data
```

## 📚 Topics Covered

| # | Topic | Description |
|---|-------|-------------|
| 1 | **Connection Management** | Establishing and managing database connections |
| 2 | **Statement** | Basic SQL queries (CREATE, SELECT, UPDATE, DELETE) |
| 3 | **PreparedStatement** | Parameterized queries (prevents SQL injection) |
| 4 | **CallableStatement** | Calling stored procedures and functions |
| 5 | **ResultSet** | Scrollable, updatable result sets, navigation |
| 6 | **Batch Processing** | Efficient bulk operations |
| 7 | **Transaction Management** | ACID, commits, rollbacks, savepoints, isolation levels |
| 8 | **MetaData** | DatabaseMetaData & ResultSetMetaData |
| 9 | **RowSet** | Disconnected data access (JdbcRowSet, CachedRowSet, etc.) |
| 10 | **Connection Pooling** | HikariCP connection pool |

## 🚀 Running the Project

### Build the Project

```bash
mvn clean compile
```

### Run with Interactive Menu

```bash
mvn exec:java -Dexec.mainClass="com.jdbc.tutorial.Main"
```

### Run All Demonstrations

```bash
mvn exec:java -Dexec.mainClass="com.jdbc.tutorial.Main" -Dexec.args="--all"
```

### Run Individual Examples

```bash
# Statement Example
mvn exec:java -Dexec.mainClass="com.jdbc.tutorial.statements.StatementExample"

# PreparedStatement Example
mvn exec:java -Dexec.mainClass="com.jdbc.tutorial.statements.PreparedStatementExample"

# Transaction Example
mvn exec:java -Dexec.mainClass="com.jdbc.tutorial.transaction.TransactionExample"
```

### Package and Run JAR

```bash
mvn clean package
java -jar target/jdbc.test-0.0.1-SNAPSHOT.jar
```

## ⚙️ Database Configuration

### Default (H2 In-Memory)

The project uses H2 in-memory database by default. No configuration needed!

### MySQL Configuration

1. Create database in MySQL:
   ```sql
   CREATE DATABASE jdbc_tutorial;
   ```

2. Run the schema:
   ```bash
   mysql -u root -p jdbc_tutorial < src/main/resources/schema.sql
   ```

3. Update `src/main/resources/db.properties`:
   ```properties
   db.driver=com.mysql.cj.jdbc.Driver
   db.url=jdbc:mysql://localhost:3306/jdbc_tutorial
   db.username=root
   db.password=your_password
   ```

## 📖 Examples Overview

### 1. Connection Management
```java
// Get H2 connection (default)
Connection conn = ConnectionManager.getH2Connection();

// Get MySQL connection (configured in db.properties)
Connection conn = ConnectionManager.getConnection();

// Print connection info
ConnectionManager.printConnectionInfo(conn);
```

### 2. Statement (Basic Queries)
```java
Statement stmt = conn.createStatement();

// SELECT
ResultSet rs = stmt.executeQuery("SELECT * FROM employees");

// INSERT/UPDATE/DELETE
int rows = stmt.executeUpdate("INSERT INTO ...");

// DDL
stmt.execute("CREATE TABLE ...");
```

### 3. PreparedStatement (Parameterized)
```java
PreparedStatement pstmt = conn.prepareStatement(
    "SELECT * FROM products WHERE category = ? AND price < ?"
);
pstmt.setString(1, "Electronics");
pstmt.setBigDecimal(2, new BigDecimal("500.00"));
ResultSet rs = pstmt.executeQuery();
```

### 4. CallableStatement (Stored Procedures)
```java
CallableStatement cstmt = conn.prepareCall("{CALL get_employee_by_id(?)}");
cstmt.setInt(1, 101);
ResultSet rs = cstmt.executeQuery();
```

### 5. ResultSet Navigation
```java
// Scrollable ResultSet
Statement stmt = conn.createStatement(
    ResultSet.TYPE_SCROLL_INSENSITIVE,
    ResultSet.CONCUR_UPDATABLE
);
ResultSet rs = stmt.executeQuery(sql);

rs.first();      // Move to first row
rs.last();       // Move to last row
rs.absolute(5);  // Move to row 5
rs.relative(-2); // Move 2 rows back
```

### 6. Batch Processing
```java
PreparedStatement pstmt = conn.prepareStatement(insertSQL);
for (Product p : products) {
    pstmt.setString(1, p.getName());
    pstmt.setDouble(2, p.getPrice());
    pstmt.addBatch();
}
int[] results = pstmt.executeBatch();
```

### 7. Transaction Management
```java
conn.setAutoCommit(false);
try {
    // Perform operations
    stmt.executeUpdate("UPDATE accounts SET balance = ...");
    stmt.executeUpdate("INSERT INTO transactions ...");
    
    conn.commit();  // Success
} catch (SQLException e) {
    conn.rollback();  // Failure - undo changes
}
```

### 8. MetaData
```java
// Database info
DatabaseMetaData dbMeta = conn.getMetaData();
System.out.println(dbMeta.getDatabaseProductName());
System.out.println(dbMeta.getDriverVersion());

// ResultSet column info
ResultSetMetaData rsMeta = rs.getMetaData();
int columnCount = rsMeta.getColumnCount();
for (int i = 1; i <= columnCount; i++) {
    System.out.println(rsMeta.getColumnName(i));
    System.out.println(rsMeta.getColumnTypeName(i));
}
```

### 9. RowSet (Disconnected)
```java
RowSetFactory factory = RowSetProvider.newFactory();
CachedRowSet crs = factory.createCachedRowSet();

crs.setUrl(url);
crs.setCommand("SELECT * FROM products");
crs.execute();  // Data cached, connection closed

// Work offline
while (crs.next()) {
    System.out.println(crs.getString("name"));
}

// Sync changes back
crs.acceptChanges();
```

### 10. Connection Pooling
```java
// Initialize HikariCP pool
ConnectionPoolExample.initializePool();

// Get connection from pool
Connection conn = ConnectionPoolExample.getConnection();

// Use connection...

// Return to pool (via try-with-resources)
conn.close();  // Actually returns to pool

// Print pool stats
ConnectionPoolExample.printPoolStats();
```

## 📋 Schema Tables

| Table | Description |
|-------|-------------|
| `departments` | Company departments |
| `employees` | Employee records with FK to departments |
| `customers` | Customer information |
| `products` | Product catalog |
| `orders` | Customer orders |
| `order_items` | Order line items |
| `audit_log` | Audit trail for changes |

## 🔧 Dependencies

- **MySQL Connector/J 8.2.0** - MySQL JDBC driver
- **H2 Database 2.2.224** - Embedded/in-memory database
- **HikariCP 5.1.0** - High-performance connection pool
- **SLF4J 2.0.9** - Logging facade

## 📝 License

This project is for educational purposes.

---

**Happy Learning! 🎓**

