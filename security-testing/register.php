<?php
require_once 'config.php';

$conn = getConnection();
$error = '';
$success = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'] ?? '';
    $password = $_POST['password'] ?? '';
    $email = $_POST['email'] ?? '';
    $role = $_POST['role'] ?? 'user'; // VULNERABLE: Role can be set by user
    
    // Check if username exists (SQL Injection possible)
    $check = $conn->query("SELECT * FROM users WHERE username = '$username'");
    
    if ($check && $check->num_rows > 0) {
        $error = "Username '$username' already exists";
    } else {
        // VULNERABLE: SQL Injection, plain text password, role escalation
        $sql = "INSERT INTO users (username, password, email, role) VALUES ('$username', '$password', '$email', '$role')";
        
        if ($conn->query($sql)) {
            $success = "Registration successful! You can now <a href='login.php' style='color: var(--accent-secondary);'>login</a>.";
        } else {
            $error = "Registration failed: " . $conn->error;
        }
    }
}

$conn->close();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Security Testing Lab</title>
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
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="grid grid-2">
            <div class="card">
                <div class="card-header">
                    <div class="card-icon">📝</div>
                    <div>
                        <div class="card-title">User Registration</div>
                        <div class="card-subtitle">Create a new account</div>
                    </div>
                </div>

                <?php if ($error): ?>
                    <div class="alert alert-error">❌ <?php echo $error; ?></div>
                <?php endif; ?>

                <?php if ($success): ?>
                    <div class="alert alert-success">✅ <?php echo $success; ?></div>
                <?php endif; ?>

                <form method="POST">
                    <div class="form-group">
                        <label class="form-label">Username</label>
                        <input type="text" name="username" class="form-input" placeholder="Choose a username" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Email</label>
                        <input type="email" name="email" class="form-input" placeholder="Enter your email" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Password</label>
                        <input type="password" name="password" class="form-input" placeholder="Choose a password" required>
                    </div>
                    <!-- VULNERABLE: Hidden role field that can be modified -->
                    <input type="hidden" name="role" value="user">
                    <button type="submit" class="btn btn-primary" style="width: 100%;">Register</button>
                </form>

                <div style="margin-top: 1.5rem; padding-top: 1.5rem; border-top: 1px solid var(--border-color);">
                    <p style="color: var(--text-muted); font-size: 0.85rem;">
                        Already have an account? <a href="login.php" style="color: var(--accent-secondary);">Login here</a>
                    </p>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <div class="card-icon">⚠️</div>
                    <div>
                        <div class="card-title">Vulnerabilities</div>
                        <div class="card-subtitle">Registration testing</div>
                    </div>
                </div>

                <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🔓 Security Issues</h4>
                <ul style="color: var(--text-secondary); margin-left: 1.5rem; margin-bottom: 1.5rem;">
                    <li>SQL Injection in all fields</li>
                    <li>Privilege escalation via hidden role field</li>
                    <li>Username enumeration</li>
                    <li>No password strength requirements</li>
                    <li>No CAPTCHA (bot registration)</li>
                    <li>No email verification</li>
                </ul>

                <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🎯 Privilege Escalation</h4>
                <p style="color: var(--text-secondary); margin-bottom: 0.5rem;">Modify the hidden role field in Burp:</p>
                <div class="code-block">
username=hacker&password=test&email=hack@test.com&role=admin
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">💉 SQL Injection</h4>
                <div class="code-block">
Username: admin'--
Username: ' OR '1'='1
Email: test@test.com', 'admin')--
                </div>

                <div class="alert alert-info" style="margin-top: 1.5rem;">
                    💡 <strong>Tip:</strong> Intercept the registration request in Burp and change the role parameter from "user" to "admin".
                </div>
            </div>
        </div>
    </div>
</body>
</html>

