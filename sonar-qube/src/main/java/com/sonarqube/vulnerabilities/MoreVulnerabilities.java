package com.sonarqube.vulnerabilities;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.*;

import javax.crypto.Cipher;
import javax.net.ssl.*;
import javax.servlet.http.*;
import java.util.*;
import java.util.regex.*;

/**
 * Additional security vulnerabilities that SonarQube can detect.
 * 
 * WARNING: This code is intentionally vulnerable for demonstration purposes.
 * DO NOT use any of this code in production!
 */
public class MoreVulnerabilities {

    // ================== XSS (Cross-Site Scripting) ==================
    
    // Vulnerability: Reflected XSS - outputting user input without encoding
    public void reflectedXSS(HttpServletResponse response, String userInput) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("Hello, " + userInput); // XSS vulnerability!
        out.println("</body></html>");
    }

    // Vulnerability: XSS in attribute
    public String xssInAttribute(String userInput) {
        return "<div data-value=\"" + userInput + "\">Content</div>"; // XSS in attribute
    }

    // Vulnerability: XSS in JavaScript context
    public String xssInJavaScript(String userInput) {
        return "<script>var data = '" + userInput + "';</script>"; // XSS in JS
    }

    // ================== CSRF (Cross-Site Request Forgery) ==================
    
    // Vulnerability: No CSRF token validation
    public void vulnerableToCsrf(HttpServletRequest request) {
        String action = request.getParameter("action");
        // Processing action without CSRF token validation
        if ("delete".equals(action)) {
            // deleteUser(); - No CSRF protection!
        }
    }

    // ================== SSL/TLS VULNERABILITIES ==================
    
    // Vulnerability: Accepting all SSL certificates (trust all)
    public HttpsURLConnection insecureSSL(String urlString) throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                public void checkServerTrusted(X509Certificate[] certs, String authType) { }
            }
        };
        
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        
        URL url = new URL(urlString);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(sc.getSocketFactory());
        return connection;
    }

    // Vulnerability: Disabling hostname verification
    public void disableHostnameVerification() {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true); // Accepts any hostname!
    }

    // Vulnerability: Using weak SSL protocol
    public SSLContext weakSSLProtocol() throws Exception {
        return SSLContext.getInstance("SSLv3"); // SSLv3 is deprecated and insecure!
    }

    // ================== COOKIE VULNERABILITIES ==================
    
    // Vulnerability: Cookie without Secure flag
    public void insecureCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("sessionId", "abc123");
        // cookie.setSecure(true); - Missing!
        response.addCookie(cookie);
    }

    // Vulnerability: Cookie without HttpOnly flag
    public void cookieWithoutHttpOnly(HttpServletResponse response) {
        Cookie cookie = new Cookie("authToken", "xyz789");
        cookie.setSecure(true);
        // cookie.setHttpOnly(true); - Missing! Vulnerable to XSS stealing cookies
        response.addCookie(cookie);
    }

    // ================== INFORMATION EXPOSURE ==================
    
    // Vulnerability: Exposing stack trace to user
    public String exposeStackTrace(String input) {
        try {
            return processInput(input);
        } catch (Exception e) {
            return e.toString() + "\n" + Arrays.toString(e.getStackTrace()); // Exposes internal details
        }
    }

    // Vulnerability: Exposing sensitive information in error messages
    public void sensitiveInfoInError(String username) throws Exception {
        if (userExists(username)) {
            throw new Exception("User " + username + " already exists in database table 'users' on server db.internal.com");
        }
    }

    // Vulnerability: Logging sensitive data
    public void logSensitiveData(String username, String password) {
        System.out.println("Login attempt - User: " + username + ", Password: " + password); // Logging password!
    }

    // ================== REGEX DENIAL OF SERVICE ==================
    
    // Vulnerability: ReDoS - catastrophic backtracking
    public boolean vulnerableRegex1(String input) {
        Pattern pattern = Pattern.compile("^(a+)+$"); // Evil regex
        return pattern.matcher(input).matches();
    }

    // Vulnerability: ReDoS with nested quantifiers
    public boolean vulnerableRegex2(String input) {
        return input.matches("(.*a){10,}"); // Catastrophic backtracking
    }

    // ================== FILE UPLOAD VULNERABILITIES ==================
    
    // Vulnerability: Unrestricted file upload
    public void unsafeFileUpload(String filename, byte[] content) throws IOException {
        // No validation of file type or content
        File file = new File("/uploads/" + filename);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
        }
    }

    // Vulnerability: File upload with insufficient validation
    public void insufficientFileValidation(String filename, byte[] content) throws IOException {
        // Only checking extension - can be bypassed
        if (filename.endsWith(".jpg") || filename.endsWith(".png")) {
            File file = new File("/uploads/" + filename);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(content);
            }
        }
    }

    // ================== UNVALIDATED REDIRECTS ==================
    
    // Vulnerability: Unvalidated forward
    public void unvalidatedForward(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String page = request.getParameter("page");
        request.getRequestDispatcher(page).forward(request, response); // Can access restricted pages
    }

    // ================== SERVER-SIDE REQUEST FORGERY (SSRF) ==================
    
    // Vulnerability: SSRF - user controls URL
    public String ssrfVulnerability(String userProvidedUrl) throws IOException {
        URL url = new URL(userProvidedUrl); // User can access internal resources
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        return reader.readLine();
    }

    // ================== INSECURE PERMISSION ==================
    
    // Vulnerability: World-readable file
    public void createWorldReadableFile(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
        file.setReadable(true, false); // World readable
        file.setWritable(true, false); // World writable
    }

    // ================== RACE CONDITIONS ==================
    
    // Vulnerability: Time-of-check to time-of-use (TOCTOU)
    public void toctouVulnerability(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) { // Check
            // Attacker can replace file between check and use
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) { // Use
                System.out.println(reader.readLine());
            }
        }
    }

    // ================== NULL CIPHER ==================
    
    // Vulnerability: Using NULL cipher
    public void nullCipher() throws Exception {
        Cipher cipher = Cipher.getInstance("NULL"); // No encryption!
    }

    // Helper methods
    private String processInput(String input) throws Exception {
        throw new Exception("Processing failed");
    }

    private boolean userExists(String username) {
        return true;
    }
}

