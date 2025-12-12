<?php
require_once 'config.php';

$logged_in = isset($_SESSION['user_id']);
$username = $logged_in ? $_SESSION['username'] : '';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Security Testing Lab</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="nav-container">
            <a href="index.php" class="logo">SecLab</a>
            <div class="nav-links">
                <a href="index.php" class="nav-link active">Home</a>
                <a href="login.php" class="nav-link">Login</a>
                <a href="search.php" class="nav-link">Search</a>
                <a href="products.php" class="nav-link">Products</a>
                <a href="profile.php" class="nav-link">Profile</a>
                <a href="comments.php" class="nav-link">Comments</a>
                <a href="upload.php" class="nav-link">Upload</a>
                <a href="transfer.php" class="nav-link">Transfer</a>
                <a href="admin.php" class="nav-link">Admin</a>
                <a href="api.php" class="nav-link">API</a>
                <a href="decoder.php" class="nav-link">Decoder</a>
                <a href="comparer.php" class="nav-link">Comparer</a>
                <?php if ($logged_in): ?>
                    <a href="logout.php" class="nav-link">Logout (<?php echo $username; ?>)</a>
                <?php endif; ?>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="hero">
            <h1>Security Testing Lab</h1>
            <p>A deliberately vulnerable web application for practicing Burp Suite and security testing techniques</p>
            <?php if (!file_exists('setup_complete.txt')): ?>
                <div style="margin-top: 2rem;">
                    <a href="setup.php" class="btn btn-danger">⚠️ Run Setup First</a>
                </div>
            <?php endif; ?>
        </div>

        <h2 style="margin-bottom: 1.5rem; color: var(--accent-primary);">🎯 Testing Modules</h2>
        
        <div class="feature-grid">
            <!-- Intercept & Inspect -->
            <a href="login.php" class="feature-card">
                <div class="card-icon">🔍</div>
                <h3>Intercept & Inspect</h3>
                <p>Login form with interceptable credentials. Test request/response interception with Burp Proxy.</p>
                <div class="vuln-badge">📍 SQL Injection • Credential Capture</div>
            </a>

            <!-- Spider / Mapping -->
            <a href="sitemap.php" class="feature-card">
                <div class="card-icon">🕷️</div>
                <h3>Application Mapping</h3>
                <p>Multiple linked pages for Burp Spider to discover. Test site mapping and crawling.</p>
                <div class="vuln-badge">📍 Hidden Endpoints • Robots.txt</div>
            </a>

            <!-- SQL Injection -->
            <a href="search.php" class="feature-card">
                <div class="card-icon">💉</div>
                <h3>SQL Injection</h3>
                <p>Search functionality vulnerable to SQL injection attacks.</p>
                <div class="vuln-badge">📍 Union-based • Error-based SQLi</div>
            </a>

            <!-- XSS -->
            <a href="comments.php" class="feature-card">
                <div class="card-icon">📝</div>
                <h3>Cross-Site Scripting</h3>
                <p>Comment system with stored and reflected XSS vulnerabilities.</p>
                <div class="vuln-badge">📍 Stored XSS • Reflected XSS</div>
            </a>

            <!-- IDOR -->
            <a href="profile.php?id=1" class="feature-card">
                <div class="card-icon">👤</div>
                <h3>IDOR / Access Control</h3>
                <p>User profiles accessible via predictable IDs. Test for insecure direct object references.</p>
                <div class="vuln-badge">📍 IDOR • Information Disclosure</div>
            </a>

            <!-- CSRF -->
            <a href="transfer.php" class="feature-card">
                <div class="card-icon">💸</div>
                <h3>CSRF Attack</h3>
                <p>Money transfer without CSRF tokens. Test cross-site request forgery.</p>
                <div class="vuln-badge">📍 Missing CSRF Token</div>
            </a>

            <!-- File Upload -->
            <a href="upload.php" class="feature-card">
                <div class="card-icon">📤</div>
                <h3>File Upload</h3>
                <p>Unrestricted file upload allowing potential shell uploads.</p>
                <div class="vuln-badge">📍 Unrestricted Upload • RCE</div>
            </a>

            <!-- Broken Auth -->
            <a href="admin.php" class="feature-card">
                <div class="card-icon">🔓</div>
                <h3>Broken Authentication</h3>
                <p>Admin panel with weak access controls and authentication bypass.</p>
                <div class="vuln-badge">📍 Auth Bypass • Weak Passwords</div>
            </a>

            <!-- API Testing -->
            <a href="api.php" class="feature-card">
                <div class="card-icon">🔌</div>
                <h3>API Endpoints</h3>
                <p>REST API with various HTTP methods for Repeater testing.</p>
                <div class="vuln-badge">📍 API Abuse • Rate Limiting</div>
            </a>

            <!-- Intruder Fuzzing -->
            <a href="login.php" class="feature-card">
                <div class="card-icon">🎯</div>
                <h3>Intruder Fuzzing</h3>
                <p>Login form for brute-force and fuzzing attacks with Burp Intruder.</p>
                <div class="vuln-badge">📍 Brute Force • No Lockout</div>
            </a>

            <!-- Decoder -->
            <a href="decoder.php" class="feature-card">
                <div class="card-icon">🔐</div>
                <h3>Decoder Test</h3>
                <p>Page with various encoded data for testing Burp Decoder functionality.</p>
                <div class="vuln-badge">📍 Base64 • URL Encode • JWT</div>
            </a>

            <!-- Comparer -->
            <a href="comparer.php" class="feature-card">
                <div class="card-icon">⚖️</div>
                <h3>Comparer Test</h3>
                <p>Multiple responses to compare for differences using Burp Comparer.</p>
                <div class="vuln-badge">📍 Response Diff • Error Messages</div>
            </a>
        </div>

        <div class="card" style="margin-top: 3rem;">
            <div class="card-header">
                <div class="card-icon">📋</div>
                <div>
                    <div class="card-title">OWASP Top 10 Coverage</div>
                    <div class="card-subtitle">Vulnerabilities implemented for testing</div>
                </div>
            </div>
            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>OWASP Category</th>
                            <th>Vulnerability</th>
                            <th>Test Page</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>A01:2021 - Broken Access Control</td>
                            <td>IDOR, Missing Function Level Access Control</td>
                            <td><a href="profile.php?id=1" style="color: var(--accent-secondary)">profile.php</a>, <a href="admin.php" style="color: var(--accent-secondary)">admin.php</a></td>
                        </tr>
                        <tr>
                            <td>A02:2021 - Cryptographic Failures</td>
                            <td>Plain-text passwords, Weak encoding</td>
                            <td><a href="decoder.php" style="color: var(--accent-secondary)">decoder.php</a></td>
                        </tr>
                        <tr>
                            <td>A03:2021 - Injection</td>
                            <td>SQL Injection, Command Injection</td>
                            <td><a href="search.php" style="color: var(--accent-secondary)">search.php</a>, <a href="login.php" style="color: var(--accent-secondary)">login.php</a></td>
                        </tr>
                        <tr>
                            <td>A04:2021 - Insecure Design</td>
                            <td>No rate limiting, Predictable resources</td>
                            <td><a href="login.php" style="color: var(--accent-secondary)">login.php</a></td>
                        </tr>
                        <tr>
                            <td>A05:2021 - Security Misconfiguration</td>
                            <td>Verbose errors, Default credentials</td>
                            <td><a href="admin.php" style="color: var(--accent-secondary)">admin.php</a></td>
                        </tr>
                        <tr>
                            <td>A06:2021 - Vulnerable Components</td>
                            <td>Information disclosure</td>
                            <td><a href="phpinfo.php" style="color: var(--accent-secondary)">phpinfo.php</a></td>
                        </tr>
                        <tr>
                            <td>A07:2021 - Auth Failures</td>
                            <td>Weak passwords, No lockout</td>
                            <td><a href="login.php" style="color: var(--accent-secondary)">login.php</a></td>
                        </tr>
                        <tr>
                            <td>A08:2021 - Data Integrity</td>
                            <td>Insecure deserialization hints</td>
                            <td><a href="api.php" style="color: var(--accent-secondary)">api.php</a></td>
                        </tr>
                        <tr>
                            <td>A09:2021 - Logging Failures</td>
                            <td>No logging, Information leakage</td>
                            <td><a href="admin.php" style="color: var(--accent-secondary)">admin.php</a></td>
                        </tr>
                        <tr>
                            <td>A10:2021 - SSRF</td>
                            <td>Server-side request forgery</td>
                            <td><a href="fetch.php" style="color: var(--accent-secondary)">fetch.php</a></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="alert alert-warning" style="margin-top: 2rem;">
            ⚠️ <strong>Warning:</strong> This application is intentionally vulnerable. Do NOT deploy in production or expose to the internet. Use only in isolated testing environments.
        </div>
    </div>

    <footer class="footer">
        <p>Security Testing Lab - For Educational Purposes Only</p>
        <p style="margin-top: 0.5rem;">Default credentials: admin/admin123, john/password, jane/123456</p>
    </footer>
</body>
</html>

