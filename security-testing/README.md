# Security Testing Lab

A deliberately vulnerable PHP web application designed for practicing security testing with Burp Suite and other penetration testing tools.

## ⚠️ WARNING

This application contains **intentional security vulnerabilities**. It is designed for educational and testing purposes only.

- **DO NOT** deploy this application on a production server
- **DO NOT** expose this application to the internet
- **DO NOT** use this application with real/sensitive data
- Use only in isolated, controlled environments (localhost/VM)

## 🚀 Setup

### Requirements
- XAMPP (or similar LAMP/WAMP stack)
- PHP 7.4+ with curl extension
- MySQL/MariaDB
- Burp Suite (Community or Professional)

### Installation

1. Copy the `security-testing` folder to your XAMPP htdocs directory:
   ```
   C:\xampp\htdocs\security-testing\
   ```

2. Start Apache and MySQL services in XAMPP

3. Navigate to setup page to create database and sample data:
   ```
   http://localhost/security-testing/setup.php
   ```

4. Access the application:
   ```
   http://localhost/security-testing/
   ```

### Default Credentials
| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | admin |
| john | password | user |
| jane | 123456 | user |
| bob | qwerty | user |
| alice | letmein | moderator |

## 🎯 Testing Scenarios

### 1. Intercept & Inspect Requests (Burp Proxy)

**Pages:** `login.php`, `transfer.php`, `api.php`

- Configure browser to use Burp proxy (127.0.0.1:8080)
- Intercept login requests to view credentials
- Modify parameters before forwarding
- Observe cookies and session tokens
- Analyze response headers

### 2. Application Mapping (Spider/Crawler)

**Pages:** `sitemap.php`, `robots.txt`

- Start crawling from the homepage
- Discover hidden endpoints via robots.txt
- Map all application pages and parameters
- Identify entry points for attacks
- Use content discovery for hidden files

### 3. OWASP Top 10 Vulnerabilities

| Vulnerability | Test Pages | Payloads |
|--------------|------------|----------|
| SQL Injection | `login.php`, `search.php`, `products.php` | `' OR '1'='1' --` |
| XSS (Reflected) | `search.php` | `<script>alert('XSS')</script>` |
| XSS (Stored) | `comments.php` | `<img src=x onerror=alert('XSS')>` |
| CSRF | `transfer.php` | No CSRF tokens |
| IDOR | `profile.php?id=X` | Enumerate user IDs |
| Broken Auth | `admin.php`, `login.php` | Auth bypass, brute force |
| File Upload | `upload.php` | PHP web shell |
| SSRF | `fetch.php` | `http://localhost/` |
| LFI | `fetch.php` | `C:\Windows\win.ini` |
| Security Misconfig | `phpinfo.php`, `config.php` | Information disclosure |

### 4. Intruder Fuzzing

**Scenarios:**
- Brute force login credentials
- Enumerate valid usernames
- Fuzz SQL injection payloads
- Test parameter values
- Content discovery

**Example Attack:**
1. Intercept login request
2. Send to Intruder (Ctrl+I)
3. Set payload positions on username/password
4. Load wordlists
5. Start attack and analyze results

### 5. Repeater Testing

**Scenarios:**
- Refine SQL injection attacks
- Test authentication bypass
- Modify API requests
- Test access control issues

**Example:**
1. Intercept request in Proxy
2. Send to Repeater (Ctrl+R)
3. Modify parameters manually
4. Send and analyze response
5. Iterate until successful

### 6. Decoder Testing

**Page:** `decoder.php`

Sample encoded data:
- Base64 credentials: `YWRtaW46YWRtaW4xMjM=`
- URL encoded XSS: `%3Cscript%3Ealert('XSS')%3C/script%3E`
- JWT tokens
- Hex encoded passwords

**Usage:**
1. Copy encoded value
2. Send to Decoder tab
3. Use Smart Decode or manual decoding
4. Chain multiple decodings

### 7. Comparer Testing

**Page:** `comparer.php`

**Scenarios:**
- Compare valid vs invalid user responses
- Compare success vs failure login responses
- Detect response length differences
- Identify information leakage

**Usage:**
1. Send first response to Comparer
2. Send second response to Comparer
3. Select both and compare (Words/Bytes)
4. Analyze highlighted differences

## 📁 File Structure

```
security-testing/
├── index.php           # Homepage
├── config.php          # Database configuration
├── setup.php           # Database setup script
├── login.php           # SQL Injection, brute force
├── logout.php          # Session management
├── register.php        # Registration vulnerabilities
├── reset.php           # Password reset flaws
├── search.php          # SQL Injection, XSS
├── products.php        # SQL Injection (ORDER BY)
├── profile.php         # IDOR vulnerability
├── comments.php        # Stored XSS
├── upload.php          # File upload vulnerability
├── transfer.php        # CSRF vulnerability
├── admin.php           # Auth bypass, RCE
├── api.php             # API vulnerabilities
├── fetch.php           # SSRF, LFI
├── decoder.php         # Encoding/decoding
├── comparer.php        # Response comparison
├── sitemap.php         # Application mapping
├── export.php          # Data export
├── phpinfo.php         # Information disclosure
├── robots.txt          # Hidden paths
├── .htaccess           # Misconfigurations
├── assets/
│   └── css/
│       └── style.css   # Styling
└── uploads/            # Uploaded files
```

## 🔧 Burp Suite Configuration

1. Configure proxy listener: `127.0.0.1:8080`
2. Set browser proxy settings
3. Add target to scope: `http://localhost/security-testing/*`
4. Enable interception
5. Start testing!

## 📚 Resources

- [OWASP Testing Guide](https://owasp.org/www-project-web-security-testing-guide/)
- [Burp Suite Documentation](https://portswigger.net/burp/documentation)
- [PayloadsAllTheThings](https://github.com/swisskyrepo/PayloadsAllTheThings)
- [HackTricks](https://book.hacktricks.xyz/)

## 📝 License

This project is for educational purposes only. Use responsibly and ethically.

