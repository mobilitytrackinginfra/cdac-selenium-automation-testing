package com.sonarqube.securityhotspots;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import java.util.regex.*;
import javax.crypto.*;
import javax.servlet.http.*;

/**
 * This class demonstrates various SECURITY HOTSPOT patterns that SonarQube can detect.
 * Security hotspots are security-sensitive code that requires manual review to determine
 * if it's actually a vulnerability.
 * 
 * Unlike vulnerabilities (which are definitely issues), hotspots need human review
 * to determine if they're safe in their specific context.
 */
public class SecurityHotspots {

    // ================== AUTHENTICATION HOTSPOTS ==================
    
    // Hotspot: Using basic authentication
    public void basicAuthentication(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            // Basic auth is security-sensitive - review if appropriate
            String credentials = new String(Base64.getDecoder().decode(authHeader.substring(6)));
            System.out.println("Auth: " + credentials);
        }
    }

    // Hotspot: Custom authentication implementation
    public boolean customAuthentication(String username, String password) {
        // Custom auth is security-sensitive - should use proven libraries
        return username.equals("admin") && password.equals("password");
    }

    // Hotspot: Session management
    public void sessionManagement(HttpServletRequest request) {
        HttpSession session = request.getSession(true); // Creating session is security-sensitive
        session.setMaxInactiveInterval(3600); // Session timeout configuration
    }

    // ================== CRYPTOGRAPHY HOTSPOTS ==================
    
    // Hotspot: Using cryptographic operations
    public byte[] encryptData(byte[] data) throws Exception {
        // Cryptographic operations are security-sensitive
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // Key size is security-sensitive
        SecretKey key = keyGen.generateKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    // Hotspot: Generating random values for security purposes
    public byte[] generateSecureToken() {
        SecureRandom random = new SecureRandom();
        byte[] token = new byte[32];
        random.nextBytes(token); // Review: Is SecureRandom properly seeded?
        return token;
    }

    // Hotspot: Hashing passwords
    public byte[] hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        // Review: Should use password-specific hashing like bcrypt
        return md.digest(password.getBytes());
    }

    // ================== FILE SYSTEM HOTSPOTS ==================
    
    // Hotspot: Reading files
    public String readFile(String path) throws IOException {
        // File reading is security-sensitive
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return reader.readLine();
        }
    }

    // Hotspot: Writing files
    public void writeFile(String path, String content) throws IOException {
        // File writing is security-sensitive
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        }
    }

    // Hotspot: Setting file permissions
    public void setFilePermissions(String path) {
        File file = new File(path);
        file.setReadable(true); // Permission changes are security-sensitive
        file.setWritable(true);
        file.setExecutable(false);
    }

    // Hotspot: Creating temporary files
    public File createTempFile() throws IOException {
        // Temp file creation is security-sensitive
        return File.createTempFile("prefix", ".tmp");
    }

    // ================== NETWORK HOTSPOTS ==================
    
    // Hotspot: Opening network connections
    public void openConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection(); // Network connections are security-sensitive
        conn.connect();
    }

    // Hotspot: Server socket
    public void startServer(int port) throws IOException {
        // Opening ports is security-sensitive
        ServerSocket server = new ServerSocket(port);
        // Review: Is this port intended to be exposed?
    }

    // Hotspot: DNS lookup
    public void dnsLookup(String hostname) throws UnknownHostException {
        // DNS lookups can be security-sensitive
        InetAddress address = InetAddress.getByName(hostname);
        System.out.println("IP: " + address.getHostAddress());
    }

    // ================== COMMAND EXECUTION HOTSPOTS ==================
    
    // Hotspot: Executing system commands
    public void executeCommand(String[] command) throws IOException {
        // Command execution is security-sensitive
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.start();
    }

    // Hotspot: Using Runtime.exec
    public void runtimeExec(String command) throws IOException {
        // Review: Is command properly sanitized?
        Runtime.getRuntime().exec(command);
    }

    // ================== DATABASE HOTSPOTS ==================
    
    // Hotspot: Database connection configuration
    public void configureDatabase() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db";
        String user = "dbuser";
        // Database configuration is security-sensitive
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("useSSL", "false"); // Review: Should SSL be enabled?
    }

    // ================== LOGGING HOTSPOTS ==================
    
    // Hotspot: Logging user data
    public void logUserData(String username, String action) {
        // Logging user data is security-sensitive
        System.out.println("User " + username + " performed " + action);
        // Review: Is any sensitive data being logged?
    }

    // Hotspot: Log configuration
    public void configureLogging(String level) {
        // Log level configuration is security-sensitive
        // Review: DEBUG level might expose sensitive information
    }

    // ================== REGEX HOTSPOTS ==================
    
    // Hotspot: User-supplied regex
    public boolean matchPattern(String input, String userPattern) {
        // User-supplied regex is security-sensitive (ReDoS)
        Pattern pattern = Pattern.compile(userPattern);
        return pattern.matcher(input).matches();
    }

    // ================== SERIALIZATION HOTSPOTS ==================
    
    // Hotspot: Object deserialization
    public Object deserialize(InputStream input) throws Exception {
        // Deserialization is security-sensitive
        ObjectInputStream ois = new ObjectInputStream(input);
        return ois.readObject();
    }

    // ================== HTTP HOTSPOTS ==================
    
    // Hotspot: HTTP headers manipulation
    public void setHeaders(HttpServletResponse response) {
        // HTTP header setting is security-sensitive
        response.setHeader("Access-Control-Allow-Origin", "*"); // CORS - review
        response.setHeader("X-Frame-Options", "SAMEORIGIN"); // Clickjacking protection
        response.setHeader("Content-Security-Policy", "default-src 'self'");
    }

    // Hotspot: Cookie creation
    public void createCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(86400); // Cookie configuration is security-sensitive
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    // ================== ENVIRONMENT HOTSPOTS ==================
    
    // Hotspot: Reading environment variables
    public String getEnvVariable(String name) {
        // Environment access is security-sensitive
        return System.getenv(name);
    }

    // Hotspot: System properties
    public String getSystemProperty(String name) {
        // System property access is security-sensitive
        return System.getProperty(name);
    }

    // ================== REFLECTION HOTSPOTS ==================
    
    // Hotspot: Using reflection
    public Object createInstance(String className) throws Exception {
        // Reflection is security-sensitive
        Class<?> clazz = Class.forName(className);
        return clazz.getDeclaredConstructor().newInstance();
    }

    // Hotspot: Accessing private fields
    public void accessPrivateField(Object obj, String fieldName) throws Exception {
        java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Security-sensitive
        System.out.println(field.get(obj));
    }

    // ================== COMPRESSION HOTSPOTS ==================
    
    // Hotspot: Decompressing data (zip bomb risk)
    public void decompress(InputStream input) throws IOException {
        java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(input);
        // Review: Implement protection against zip bombs
        java.util.zip.ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            // Process entry
        }
    }

    // ================== LDAP HOTSPOTS ==================
    
    // Hotspot: LDAP operations
    public void ldapSearch(String filter) throws Exception {
        // LDAP operations are security-sensitive
        Hashtable<String, String> env = new Hashtable<>();
        env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(javax.naming.Context.PROVIDER_URL, "ldap://localhost:389");
    }

    // ================== PRIVILEGE HOTSPOTS ==================
    
    // Hotspot: Running with elevated privileges
    public void checkPrivileges() {
        SecurityManager sm = System.getSecurityManager();
        // Security manager operations are security-sensitive
        if (sm != null) {
            sm.checkPermission(new RuntimePermission("exitVM"));
        }
    }
}

