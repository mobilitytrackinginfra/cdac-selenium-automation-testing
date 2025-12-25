package com.sonarqube.vulnerabilities;

import java.io.*;
import java.sql.*;
import java.security.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.naming.*;
import javax.naming.directory.*;
import javax.xml.parsers.*;
import org.xml.sax.*;

/**
 * This class demonstrates various SECURITY VULNERABILITY patterns that SonarQube can detect.
 * These are issues that can be exploited by attackers to compromise the application.
 * 
 * WARNING: This code is intentionally vulnerable for demonstration purposes.
 * DO NOT use any of this code in production!
 */
public class SecurityVulnerabilities {

    // ================== SQL INJECTION ==================
    
    // Vulnerability: SQL Injection - concatenating user input
    public ResultSet sqlInjection(Connection conn, String userId) throws SQLException {
        String query = "SELECT * FROM users WHERE id = '" + userId + "'";
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query); // User can inject: ' OR '1'='1
    }

    // Vulnerability: SQL Injection in LIKE clause
    public ResultSet sqlInjectionLike(Connection conn, String searchTerm) throws SQLException {
        String query = "SELECT * FROM products WHERE name LIKE '%" + searchTerm + "%'";
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

    // Vulnerability: SQL Injection with string format
    public ResultSet sqlInjectionFormat(Connection conn, String table, String column) throws SQLException {
        String query = String.format("SELECT %s FROM %s", column, table);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

    
    // ================== PATH TRAVERSAL ==================
    
    // Vulnerability: Path Traversal - reading arbitrary files
    public String pathTraversal(String filename) throws IOException {
        String basePath = "/var/www/files/";
        File file = new File(basePath + filename); // User can use: ../../../etc/passwd
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return reader.readLine();
    }

    // Vulnerability: Path traversal in file download
    public byte[] downloadFile(String requestedFile) throws IOException {
        File file = new File("/uploads/" + requestedFile);
        return java.nio.file.Files.readAllBytes(file.toPath());
    }

    // ================== HARDCODED CREDENTIALS ==================
    
    // Vulnerability: Hardcoded password
    private static final String DB_PASSWORD = "admin123"; // Hardcoded credential!
    
    // Vulnerability: Hardcoded credentials in connection string
    public Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/db?user=root&password=secret123";
        return DriverManager.getConnection(url);
    }

    // Vulnerability: Hardcoded API key
    public void callApi() {
        String apiKey = "sk-12345abcde67890fghij"; // Hardcoded API key!
        System.out.println("Using API key: " + apiKey);
    }

    // Vulnerability: Hardcoded SSH private key
    private static final String PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEpAIBAAKCAQEA0Z3VS5JJcds3xfn/ygWyF\n" +
            "-----END RSA PRIVATE KEY-----";

}

