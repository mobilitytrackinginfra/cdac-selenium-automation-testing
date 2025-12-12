<?php
require_once 'config.php';

$conn = getConnection();
$error = '';
$success = '';
$step = 1;

// VULNERABLE: Insecure password reset flow
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['email'])) {
        $email = $_POST['email'];
        
        // Check if user exists (information disclosure)
        $result = $conn->query("SELECT * FROM users WHERE email = '$email'");
        
        if ($result && $result->num_rows > 0) {
            $user = $result->fetch_assoc();
            
            // VULNERABLE: Predictable reset token
            $token = md5($user['email'] . 'reset' . date('Y-m-d'));
            
            $success = "Password reset link sent to: $email<br>";
            $success .= "Token: <code>$token</code><br>";
            $success .= "<a href='reset.php?token=$token&user_id={$user['id']}'>Click here to reset</a>";
            
            $step = 2;
        } else {
            // Information disclosure - reveals if email exists
            $error = "No account found with email: $email";
        }
    }
    
    if (isset($_POST['new_password']) && isset($_POST['token']) && isset($_POST['user_id'])) {
        $new_password = $_POST['new_password'];
        $token = $_POST['token'];
        $user_id = $_POST['user_id'];
        
        // VULNERABLE: No proper token validation, password stored in plain text
        $conn->query("UPDATE users SET password = '$new_password' WHERE id = $user_id");
        $success = "Password updated successfully! You can now <a href='login.php' style='color: var(--accent-secondary);'>login</a>.";
        $step = 3;
    }
}

// Handle token-based reset
if (isset($_GET['token']) && isset($_GET['user_id'])) {
    $step = 2;
}

$conn->close();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Password Reset - Security Testing Lab</title>
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
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="grid grid-2">
            <div class="card">
                <div class="card-header">
                    <div class="card-icon">🔑</div>
                    <div>
                        <div class="card-title">Password Reset</div>
                        <div class="card-subtitle">Reset your password</div>
                    </div>
                </div>

                <?php if ($error): ?>
                    <div class="alert alert-error">❌ <?php echo $error; ?></div>
                <?php endif; ?>

                <?php if ($success): ?>
                    <div class="alert alert-success">✅ <?php echo $success; ?></div>
                <?php endif; ?>

                <?php if ($step === 1): ?>
                    <form method="POST">
                        <div class="form-group">
                            <label class="form-label">Email Address</label>
                            <input type="email" name="email" class="form-input" placeholder="Enter your email" required>
                        </div>
                        <button type="submit" class="btn btn-primary" style="width: 100%;">Request Reset Link</button>
                    </form>
                <?php endif; ?>

                <?php if ($step === 2 && isset($_GET['token'])): ?>
                    <form method="POST">
                        <input type="hidden" name="token" value="<?php echo htmlspecialchars($_GET['token']); ?>">
                        <input type="hidden" name="user_id" value="<?php echo htmlspecialchars($_GET['user_id']); ?>">
                        <div class="form-group">
                            <label class="form-label">New Password</label>
                            <input type="password" name="new_password" class="form-input" placeholder="Enter new password" required>
                        </div>
                        <button type="submit" class="btn btn-primary" style="width: 100%;">Reset Password</button>
                    </form>
                <?php endif; ?>

                <div style="margin-top: 1.5rem; padding-top: 1.5rem; border-top: 1px solid var(--border-color);">
                    <p style="color: var(--text-muted); font-size: 0.85rem;">
                        Remember your password? <a href="login.php" style="color: var(--accent-secondary);">Login here</a>
                    </p>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <div class="card-icon">⚠️</div>
                    <div>
                        <div class="card-title">Vulnerabilities</div>
                        <div class="card-subtitle">Broken authentication testing</div>
                    </div>
                </div>

                <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🔓 Security Issues</h4>
                <ul style="color: var(--text-secondary); margin-left: 1.5rem; margin-bottom: 1.5rem;">
                    <li>Email enumeration via different error messages</li>
                    <li>Predictable reset token (MD5 of email + date)</li>
                    <li>Token displayed in response (should be emailed)</li>
                    <li>No token expiration</li>
                    <li>IDOR - can reset any user's password with user_id</li>
                    <li>Password stored in plain text</li>
                    <li>SQL Injection in email field</li>
                </ul>

                <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🎯 Attack Vectors</h4>
                <div class="code-block">
# Email enumeration:
POST /reset.php
email=admin@security-lab.com (exists)
email=fake@test.com (doesn't exist)

# Token prediction:
md5("admin@security-lab.com" + "reset" + "2024-01-15")

# IDOR - reset any user:
/reset.php?token=any&user_id=1
                </div>

                <div class="alert alert-info" style="margin-top: 1.5rem;">
                    💡 <strong>Tip:</strong> Use Burp Comparer to compare responses for email enumeration.
                </div>
            </div>
        </div>
    </div>
</body>
</html>

