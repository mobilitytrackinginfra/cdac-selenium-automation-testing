<?php
require_once 'config.php';

$error = '';
$success = '';

// Vulnerable login - SQL Injection possible
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'] ?? '';
    $password = $_POST['password'] ?? '';
    
    // Log the attempt (for testing visibility)
    $conn = getConnection();
    
    // VULNERABLE: Direct string concatenation - SQL Injection possible
    // Try: admin' OR '1'='1' -- 
    // Try: ' OR 1=1 --
    $sql = "SELECT * FROM users WHERE username = '$username' AND password = '$password'";
    
    // For debugging (intentional information disclosure)
    if (isset($_GET['debug'])) {
        echo "<pre>Query: $sql</pre>";
    }
    
    $result = $conn->query($sql);
    
    if ($result && $result->num_rows > 0) {
        $user = $result->fetch_assoc();
        $_SESSION['user_id'] = $user['id'];
        $_SESSION['username'] = $user['username'];
        $_SESSION['role'] = $user['role'];
        
        $success = "Login successful! Welcome, " . htmlspecialchars($user['username']);
        
        // Redirect after successful login
        header("refresh:2;url=profile.php?id=" . $user['id']);
    } else {
        // Verbose error message - information disclosure
        $error = "Login failed for user '$username'. Invalid credentials.";
        
        // Additional info leak
        if ($conn->error) {
            $error .= " Database error: " . $conn->error;
        }
    }
    
    $conn->close();
}

$logged_in = isset($_SESSION['user_id']);
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Security Testing Lab</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="nav-container">
            <a href="index.php" class="logo">SecLab</a>
            <div class="nav-links">
                <a href="index.php" class="nav-link">Home</a>
                <a href="login.php" class="nav-link active">Login</a>
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
                    <div class="card-icon">🔐</div>
                    <div>
                        <div class="card-title">User Login</div>
                        <div class="card-subtitle">Authenticate to access your account</div>
                    </div>
                </div>

                <?php if ($error): ?>
                    <div class="alert alert-error">❌ <?php echo $error; ?></div>
                <?php endif; ?>

                <?php if ($success): ?>
                    <div class="alert alert-success">✅ <?php echo $success; ?></div>
                <?php endif; ?>

                <form method="POST" action="login.php">
                    <div class="form-group">
                        <label class="form-label">Username</label>
                        <input type="text" name="username" class="form-input" placeholder="Enter username" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Password</label>
                        <input type="password" name="password" class="form-input" placeholder="Enter password" required>
                    </div>
                    <button type="submit" class="btn btn-primary" style="width: 100%;">Login</button>
                </form>

                <div style="margin-top: 1.5rem; padding-top: 1.5rem; border-top: 1px solid var(--border-color);">
                    <p style="color: var(--text-muted); font-size: 0.85rem;">
                        Forgot password? <a href="reset.php" style="color: var(--accent-secondary);">Reset here</a>
                    </p>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <div class="card-icon">🎯</div>
                    <div>
                        <div class="card-title">Burp Suite Testing</div>
                        <div class="card-subtitle">Testing scenarios for this page</div>
                    </div>
                </div>

                <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🔍 Intercept Testing</h4>
                <ul style="color: var(--text-secondary); margin-left: 1.5rem; margin-bottom: 1.5rem;">
                    <li>Intercept login requests to view credentials</li>
                    <li>Modify parameters in transit</li>
                    <li>Observe response headers and cookies</li>
                </ul>

                <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">💉 SQL Injection Payloads</h4>
                <div class="code-block">
' OR '1'='1' --
' OR 1=1 --
admin' --
' UNION SELECT * FROM users --
' OR ''='
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">🎯 Intruder Fuzzing</h4>
                <ul style="color: var(--text-secondary); margin-left: 1.5rem; margin-bottom: 1.5rem;">
                    <li>Brute force with common password lists</li>
                    <li>Username enumeration</li>
                    <li>No rate limiting or account lockout</li>
                </ul>

                <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">📝 Test Credentials</h4>
                <div class="code-block">
admin / admin123
john / password
jane / 123456
bob / qwerty
alice / letmein
                </div>

                <div class="alert alert-info" style="margin-top: 1.5rem;">
                    💡 <strong>Tip:</strong> Add <code>?debug=1</code> to URL to see SQL query
                </div>
            </div>
        </div>
    </div>
</body>
</html>

