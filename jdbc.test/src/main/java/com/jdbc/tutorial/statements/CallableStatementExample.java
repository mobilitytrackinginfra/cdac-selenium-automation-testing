package com.jdbc.tutorial.statements;

import com.jdbc.tutorial.connection.ConnectionManager;

import java.math.BigDecimal;
import java.sql.*;

/**
 * CallableStatementExample - Demonstrates CallableStatement for stored procedures.
 * 
 * CallableStatement is used for:
 * 1. Calling stored procedures
 * 2. Calling stored functions
 * 3. Handling IN, OUT, and INOUT parameters
 * 
 * Note: This example uses H2 syntax which differs slightly from MySQL.
 * 
 * @author JDBC Tutorial
 */
public class CallableStatementExample {
    
    /**
     * Setup tables and stored procedures.
     */
    public static void setup(Connection conn) throws SQLException {
        System.out.println("\n--- Setting up tables and procedures ---");
        
        try (Statement stmt = conn.createStatement()) {
            // Create employees table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS emp_demo (
                    emp_id INT PRIMARY KEY AUTO_INCREMENT,
                    emp_name VARCHAR(100) NOT NULL,
                    department VARCHAR(50),
                    salary DECIMAL(10, 2),
                    hire_date DATE
                )
            """);
            
            // Insert sample data
            stmt.execute("""
                INSERT INTO emp_demo (emp_name, department, salary, hire_date) VALUES
                ('John Smith', 'Engineering', 75000.00, '2020-01-15'),
                ('Sarah Johnson', 'Marketing', 65000.00, '2019-06-20'),
                ('Mike Wilson', 'Engineering', 85000.00, '2018-03-10'),
                ('Emily Brown', 'HR', 55000.00, '2021-02-28'),
                ('David Lee', 'Engineering', 90000.00, '2017-11-05')
            """);
            
            System.out.println("✓ Employee table created with sample data");
            
            // Create stored procedure - Get employee by ID (IN parameter)
            stmt.execute("""
                CREATE ALIAS IF NOT EXISTS get_employee_by_id AS $$
                ResultSet getEmployeeById(Connection conn, int empId) throws SQLException {
                    PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM emp_demo WHERE emp_id = ?"
                    );
                    ps.setInt(1, empId);
                    return ps.executeQuery();
                }
                $$
            """);
            
            // Create stored procedure - Get employees by department
            stmt.execute("""
                CREATE ALIAS IF NOT EXISTS get_employees_by_dept AS $$
                ResultSet getEmployeesByDept(Connection conn, String dept) throws SQLException {
                    PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM emp_demo WHERE department = ?"
                    );
                    ps.setString(1, dept);
                    return ps.executeQuery();
                }
                $$
            """);
            
            // Create stored procedure - Calculate department average salary
            stmt.execute("""
                CREATE ALIAS IF NOT EXISTS calc_dept_avg_salary AS $$
                double calcDeptAvgSalary(Connection conn, String dept) throws SQLException {
                    PreparedStatement ps = conn.prepareStatement(
                        "SELECT AVG(salary) as avg_sal FROM emp_demo WHERE department = ?"
                    );
                    ps.setString(1, dept);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        return rs.getDouble("avg_sal");
                    }
                    return 0.0;
                }
                $$
            """);
            
            // Create stored procedure - Give raise (updates salary)
            stmt.execute("""
                CREATE ALIAS IF NOT EXISTS give_raise AS $$
                int giveRaise(Connection conn, int empId, double percentage) throws SQLException {
                    PreparedStatement ps = conn.prepareStatement(
                        "UPDATE emp_demo SET salary = salary * (1 + ? / 100) WHERE emp_id = ?"
                    );
                    ps.setDouble(1, percentage);
                    ps.setInt(2, empId);
                    return ps.executeUpdate();
                }
                $$
            """);
            
            // Create stored procedure - Count employees in department
            stmt.execute("""
                CREATE ALIAS IF NOT EXISTS count_employees AS $$
                int countEmployees(Connection conn, String dept) throws SQLException {
                    PreparedStatement ps = conn.prepareStatement(
                        "SELECT COUNT(*) as cnt FROM emp_demo WHERE department = ?"
                    );
                    ps.setString(1, dept);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        return rs.getInt("cnt");
                    }
                    return 0;
                }
                $$
            """);
            
            System.out.println("✓ Stored procedures created");
        }
    }
    
    /**
     * Demonstrate calling stored procedure with IN parameter returning ResultSet.
     */
    public static void callProcedureWithResultSet(Connection conn) throws SQLException {
        System.out.println("\n--- Stored Procedure Returning ResultSet ---");
        
        // H2 syntax: CALL procedure_name(params)
        String sql = "CALL get_employee_by_id(?)";
        
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setInt(1, 1);  // IN parameter
            
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\nEmployee Details:");
                    System.out.println("  ID: " + rs.getInt("emp_id"));
                    System.out.println("  Name: " + rs.getString("emp_name"));
                    System.out.println("  Department: " + rs.getString("department"));
                    System.out.println("  Salary: $" + rs.getBigDecimal("salary"));
                    System.out.println("  Hire Date: " + rs.getDate("hire_date"));
                }
            }
        }
    }
    
    /**
     * Demonstrate calling procedure that returns multiple rows.
     */
    public static void callProcedureMultipleRows(Connection conn) throws SQLException {
        System.out.println("\n--- Procedure Returning Multiple Rows ---");
        
        String sql = "CALL get_employees_by_dept(?)";
        
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, "Engineering");
            
            try (ResultSet rs = cstmt.executeQuery()) {
                System.out.println("\nEngineering Department Employees:");
                System.out.println("-".repeat(50));
                
                while (rs.next()) {
                    System.out.printf("  %s - $%.2f (Hired: %s)%n",
                            rs.getString("emp_name"),
                            rs.getBigDecimal("salary"),
                            rs.getDate("hire_date"));
                }
            }
        }
    }
    
    /**
     * Demonstrate calling stored function that returns a value.
     */
    public static void callStoredFunction(Connection conn) throws SQLException {
        System.out.println("\n--- Calling Stored Function ---");
        
        // H2 functions can be called directly
        String sql = "CALL calc_dept_avg_salary(?)";
        
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, "Engineering");
            
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    double avgSalary = rs.getDouble(1);
                    System.out.printf("Average salary in Engineering: $%.2f%n", avgSalary);
                }
            }
        }
    }
    
    /**
     * Demonstrate procedure that performs UPDATE.
     */
    public static void callUpdateProcedure(Connection conn) throws SQLException {
        System.out.println("\n--- Procedure Performing UPDATE ---");
        
        // Show salary before raise
        System.out.println("Before raise:");
        showEmployeeSalary(conn, 1);
        
        // Give 10% raise
        String sql = "CALL give_raise(?, ?)";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setInt(1, 1);      // Employee ID
            cstmt.setDouble(2, 10);  // 10% raise
            
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    int rowsAffected = rs.getInt(1);
                    System.out.println("✓ Raise applied, rows affected: " + rowsAffected);
                }
            }
        }
        
        // Show salary after raise
        System.out.println("\nAfter 10% raise:");
        showEmployeeSalary(conn, 1);
    }
    
    /**
     * Helper method to show employee salary.
     */
    private static void showEmployeeSalary(Connection conn, int empId) throws SQLException {
        String sql = "SELECT emp_name, salary FROM emp_demo WHERE emp_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, empId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.printf("  %s: $%.2f%n", 
                            rs.getString("emp_name"), 
                            rs.getBigDecimal("salary"));
                }
            }
        }
    }
    
    /**
     * Demonstrate function returning scalar value.
     */
    public static void callScalarFunction(Connection conn) throws SQLException {
        System.out.println("\n--- Function Returning Scalar Value ---");
        
        String sql = "CALL count_employees(?)";
        
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, "Engineering");
            
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("Number of employees in Engineering: " + count);
                }
            }
        }
    }
    
    /**
     * Demonstrate MySQL-style OUT parameters (simulation for H2).
     * Note: H2 handles this differently than MySQL.
     */
    public static void demonstrateOutParameters(Connection conn) throws SQLException {
        System.out.println("\n--- Simulating OUT Parameters ---");
        
        // In MySQL, you would use syntax like:
        // {CALL proc_name(?, ?, ?)}
        // and register OUT parameters with registerOutParameter()
        
        // For demonstration, we'll show the concept:
        System.out.println("MySQL OUT parameter syntax (for reference):");
        System.out.println("""
            // Create procedure with OUT parameter:
            // CREATE PROCEDURE update_salary(IN emp_id INT, IN new_salary DECIMAL,
            //                               OUT old_salary DECIMAL, OUT status VARCHAR(50))
            
            // Java code:
            // String sql = "{CALL update_salary(?, ?, ?, ?)}";
            // CallableStatement cstmt = conn.prepareCall(sql);
            // cstmt.setInt(1, empId);                           // IN
            // cstmt.setBigDecimal(2, newSalary);                // IN
            // cstmt.registerOutParameter(3, Types.DECIMAL);     // OUT
            // cstmt.registerOutParameter(4, Types.VARCHAR);     // OUT
            // cstmt.execute();
            // BigDecimal oldSalary = cstmt.getBigDecimal(3);    // Get OUT value
            // String status = cstmt.getString(4);               // Get OUT value
            """);
    }
    
    /**
     * Demonstrate INOUT parameters (concept).
     */
    public static void demonstrateInOutParameters(Connection conn) throws SQLException {
        System.out.println("\n--- INOUT Parameters (Concept) ---");
        
        System.out.println("MySQL INOUT parameter syntax (for reference):");
        System.out.println("""
            // Create procedure with INOUT parameter:
            // CREATE PROCEDURE increment_counter(INOUT counter INT)
            
            // Java code:
            // String sql = "{CALL increment_counter(?)}";
            // CallableStatement cstmt = conn.prepareCall(sql);
            // cstmt.setInt(1, 5);                               // Set initial value
            // cstmt.registerOutParameter(1, Types.INTEGER);     // Also register as OUT
            // cstmt.execute();
            // int result = cstmt.getInt(1);                     // Get modified value
            """);
    }
    
    /**
     * Demonstrate batch calls to stored procedures.
     */
    public static void batchProcedureCalls(Connection conn) throws SQLException {
        System.out.println("\n--- Batch Procedure Calls ---");
        
        // While CallableStatement doesn't support batch for procedures returning results,
        // we can loop through multiple calls
        
        String[] departments = {"Engineering", "Marketing", "HR"};
        String sql = "CALL count_employees(?)";
        
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            System.out.println("\nEmployee count by department:");
            
            for (String dept : departments) {
                cstmt.setString(1, dept);
                try (ResultSet rs = cstmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.printf("  %s: %d employees%n", dept, rs.getInt(1));
                    }
                }
            }
        }
    }
    
    /**
     * Clean up.
     */
    public static void cleanup(Connection conn) throws SQLException {
        System.out.println("\n--- Cleanup ---");
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS emp_demo");
            stmt.execute("DROP ALIAS IF EXISTS get_employee_by_id");
            stmt.execute("DROP ALIAS IF EXISTS get_employees_by_dept");
            stmt.execute("DROP ALIAS IF EXISTS calc_dept_avg_salary");
            stmt.execute("DROP ALIAS IF EXISTS give_raise");
            stmt.execute("DROP ALIAS IF EXISTS count_employees");
            System.out.println("✓ Cleanup completed");
        }
    }
    
    /**
     * Run all CallableStatement demonstrations.
     */
    public static void demonstrate() {
        System.out.println("\n========================================");
        System.out.println("  CALLABLE STATEMENT DEMONSTRATION");
        System.out.println("========================================");
        
        Connection conn = null;
        try {
            conn = ConnectionManager.getH2Connection();
            
            setup(conn);
            callProcedureWithResultSet(conn);
            callProcedureMultipleRows(conn);
            callStoredFunction(conn);
            callUpdateProcedure(conn);
            callScalarFunction(conn);
            demonstrateOutParameters(conn);
            demonstrateInOutParameters(conn);
            batchProcedureCalls(conn);
            cleanup(conn);
            
            System.out.println("\n✓ All CallableStatement examples completed successfully!");
            
        } catch (SQLException e) {
            System.err.println("✗ CallableStatement error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionManager.closeConnection(conn);
        }
    }
    
    public static void main(String[] args) {
        demonstrate();
    }
}

