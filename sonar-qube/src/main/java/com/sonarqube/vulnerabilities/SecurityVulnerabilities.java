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

    // ================== COMMAND INJECTION ==================
    
    // Vulnerability: OS Command Injection
    public void commandInjection(String filename) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("cat " + filename); // User can inject: "; rm -rf /"
    }

    // Vulnerability: Command injection via ProcessBuilder
    public void commandInjectionProcessBuilder(String userInput) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("sh", "-c", "echo " + userInput);
        pb.start();
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

    // ================== WEAK CRYPTOGRAPHY ==================
    
    // Vulnerability: Using weak hash algorithm (MD5)
    public String weakHashMD5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5"); // Weak!
        byte[] hash = md.digest(input.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    // Vulnerability: Using weak hash algorithm (SHA1)
    public String weakHashSHA1(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1"); // Weak for passwords!
        byte[] hash = md.digest(input.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    // Vulnerability: Using DES encryption (weak)
    public byte[] weakEncryptionDES(String data) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES"); // Weak algorithm!
        SecretKey key = keyGen.generateKey();
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data.getBytes());
    }

    // Vulnerability: ECB mode encryption
    public byte[] weakEncryptionECB(byte[] data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // ECB mode is weak!
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    // Vulnerability: Hardcoded encryption key
    public byte[] hardcodedKey(byte[] data) throws Exception {
        byte[] keyBytes = "1234567890123456".getBytes(); // Hardcoded key!
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    // ================== INSECURE RANDOM ==================
    
    // Vulnerability: Using java.util.Random for security-sensitive operations
    public String generateToken() {
        Random random = new Random(); // Not cryptographically secure!
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            token.append((char) ('a' + random.nextInt(26)));
        }
        return token.toString();
    }

    // Vulnerability: Predictable seed for Random
    public String generateSessionId() {
        Random random = new Random(System.currentTimeMillis()); // Predictable seed!
        return String.valueOf(random.nextLong());
    }

    // ================== LDAP INJECTION ==================
    
    // Vulnerability: LDAP Injection
    public void ldapInjection(String username) throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:389");
        
        DirContext ctx = new InitialDirContext(env);
        String filter = "(uid=" + username + ")"; // LDAP injection vulnerability!
        ctx.search("ou=users,dc=example,dc=com", filter, new SearchControls());
    }

    // ================== XML EXTERNAL ENTITY (XXE) ==================
    
    // Vulnerability: XXE - XML External Entity Injection
    public void xxeVulnerability(InputStream xmlInput) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // Missing: factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.parse(xmlInput); // XXE vulnerable!
    }

    // ================== XPATH INJECTION ==================
    
    // Vulnerability: XPath Injection
    public void xpathInjection(String username, String password) throws Exception {
        javax.xml.xpath.XPath xpath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        String expression = "//users/user[@name='" + username + "' and @pass='" + password + "']";
        // XPath injection vulnerability!
    }

    // ================== LOG INJECTION ==================
    
    // Vulnerability: Log Injection (log forging)
    public void logInjection(String userInput) {
        System.out.println("User logged in: " + userInput); // Can inject newlines to forge logs
    }

    // ================== OPEN REDIRECT ==================
    
    // Vulnerability: Open Redirect
    public String openRedirect(String redirectUrl) {
        return "redirect:" + redirectUrl; // User can redirect to malicious site
    }

    // ================== INSECURE DESERIALIZATION ==================
    
    // Vulnerability: Deserializing untrusted data
    public Object insecureDeserialization(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject(); // Can lead to remote code execution!
    }

    // ================== TRUST BOUNDARY VIOLATION ==================
    
    // Vulnerability: Storing sensitive data in session from untrusted source
    public void trustBoundaryViolation(Map<String, Object> session, String userInput) {
        session.put("isAdmin", userInput); // Trusting user input for authorization!
    }
}

