<?php
require_once 'config.php';

$user = null;
$error = '';

// VULNERABLE: IDOR - Any user ID can be accessed
// No authorization check - just authentication
$user_id = $_GET['id'] ?? ($_SESSION['user_id'] ?? 1);

$conn = getConnection();

// Direct object reference without authorization
$sql = "SELECT * FROM users WHERE id = $user_id";
$result = $conn->query($sql);

if ($result && $result->num_rows > 0) {
    $user = $result->fetch_assoc();
} else {
    $error = "User not found with ID: $user_id";
}

// Handle profile update (also vulnerable)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $new_email = $_POST['email'] ?? '';
    $new_note = $_POST['secret_note'] ?? '';
    
    // VULNERABLE: No CSRF token, no authorization check
    $update_sql = "UPDATE users SET email = '$new_email', secret_note = '$new_note' WHERE id = $user_id";
    
    if ($conn->query($update_sql)) {
        $success = "Profile updated successfully!";
        // Refresh user data
        $result = $conn->query($sql);
        $user = $result->fetch_assoc();
    } else {
        $error = "Update failed: " . $conn->error;
    }
}

$conn->close();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile - Security Testing Lab</title>
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
                <a href="profile.php" class="nav-link active">Profile</a>
                <a href="comments.php" class="nav-link">Comments</a>
                <a href="upload.php" class="nav-link">Upload</a>
                <a href="admin.php" class="nav-link">Admin</a>
                <a href="api.php" class="nav-link">API</a>
                <a href="decoder.php" class="nav-link">Decoder</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <?php if ($error): ?>
            <div class="alert alert-error">❌ <?php echo $error; ?></div>
        <?php endif; ?>

        <?php if (isset($success)): ?>
            <div class="alert alert-success">✅ <?php echo $success; ?></div>
        <?php endif; ?>

        <?php if ($user): ?>
            <div class="grid grid-2">
                <div class="card">
                    <div class="profile-header">
                        <div class="avatar"><?php echo strtoupper(substr($user['username'], 0, 1)); ?></div>
                        <div>
                            <h2><?php echo htmlspecialchars($user['username']); ?></h2>
                            <span class="tag <?php echo $user['role'] === 'admin' ? 'tag-vuln' : 'tag-info'; ?>">
                                <?php echo htmlspecialchars($user['role']); ?>
                            </span>
                        </div>
                    </div>

                    <div class="table-container">
                        <table>
                            <tr>
                                <th>User ID</th>
                                <td><?php echo $user['id']; ?></td>
                            </tr>
                            <tr>
                                <th>Email</th>
                                <td><?php echo htmlspecialchars($user['email']); ?></td>
                            </tr>
                            <tr>
                                <th>Balance</th>
                                <td>$<?php echo number_format($user['balance'], 2); ?></td>
                            </tr>
                            <tr>
                                <th>Role</th>
                                <td><?php echo htmlspecialchars($user['role']); ?></td>
                            </tr>
                            <tr>
                                <th>Password</th>
                                <!-- VULNERABLE: Password exposed in response -->
                                <td><code><?php echo htmlspecialchars($user['password']); ?></code></td>
                            </tr>
                            <tr>
                                <th>Secret Note</th>
                                <!-- VULNERABLE: Sensitive data exposure -->
                                <td><?php echo htmlspecialchars($user['secret_note']); ?></td>
                            </tr>
                            <tr>
                                <th>Created</th>
                                <td><?php echo $user['created_at']; ?></td>
                            </tr>
                        </table>
                    </div>

                    <form method="POST" style="margin-top: 2rem;">
                        <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">Edit Profile</h4>
                        <div class="form-group">
                            <label class="form-label">Email</label>
                            <input type="email" name="email" class="form-input" value="<?php echo htmlspecialchars($user['email']); ?>">
                        </div>
                        <div class="form-group">
                            <label class="form-label">Secret Note</label>
                            <textarea name="secret_note" class="form-textarea"><?php echo htmlspecialchars($user['secret_note']); ?></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Update Profile</button>
                    </form>
                </div>

                <div class="card">
                    <div class="card-header">
                        <div class="card-icon">🔓</div>
                        <div>
                            <div class="card-title">IDOR Testing</div>
                            <div class="card-subtitle">Insecure Direct Object Reference</div>
                        </div>
                    </div>

                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🎯 Test Different User IDs</h4>
                    <p style="color: var(--text-secondary); margin-bottom: 1rem;">
                        Change the <code>id</code> parameter in the URL to access other users' profiles:
                    </p>
                    
                    <div style="display: flex; gap: 0.5rem; flex-wrap: wrap; margin-bottom: 1.5rem;">
                        <a href="profile.php?id=1" class="btn btn-secondary">User 1 (Admin)</a>
                        <a href="profile.php?id=2" class="btn btn-secondary">User 2</a>
                        <a href="profile.php?id=3" class="btn btn-secondary">User 3</a>
                        <a href="profile.php?id=4" class="btn btn-secondary">User 4</a>
                        <a href="profile.php?id=5" class="btn btn-secondary">User 5</a>
                    </div>

                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🔍 Intruder Enumeration</h4>
                    <p style="color: var(--text-secondary); margin-bottom: 1rem;">
                        Use Burp Intruder to enumerate all user IDs:
                    </p>
                    <div class="code-block">
GET /profile.php?id=§1§ HTTP/1.1
Host: localhost

Payload: Numbers 1-100
                    </div>

                    <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">⚠️ Vulnerabilities Present</h4>
                    <ul style="color: var(--text-secondary); margin-left: 1.5rem;">
                        <li>No authorization check (IDOR)</li>
                        <li>Password displayed in plain text</li>
                        <li>Sensitive data exposure (secret notes)</li>
                        <li>No CSRF token on update form</li>
                        <li>SQL Injection in update query</li>
                    </ul>

                    <div class="alert alert-warning" style="margin-top: 1.5rem;">
                        💡 <strong>Tip:</strong> In Burp Repeater, modify the ID and observe how you can access any user's data without authentication.
                    </div>
                </div>
            </div>
        <?php endif; ?>
    </div>
</body>
</html>

