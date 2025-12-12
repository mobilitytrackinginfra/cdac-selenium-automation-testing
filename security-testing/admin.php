<?php
require_once 'config.php';

$conn = getConnection();
$error = '';
$success = '';
$is_admin = false;

// VULNERABLE: Multiple authentication bypasses possible

// Check 1: Session-based (can be manipulated)
if (isset($_SESSION['role']) && $_SESSION['role'] === 'admin') {
    $is_admin = true;
}

// Check 2: Cookie-based (easily forged)
if (isset($_COOKIE['admin']) && $_COOKIE['admin'] === 'true') {
    $is_admin = true;
}

// Check 3: GET parameter (ridiculous but for testing)
if (isset($_GET['admin']) && $_GET['admin'] === 'true') {
    $is_admin = true;
}

// Check 4: Header-based (can be added via Burp)
if (isset($_SERVER['HTTP_X_ADMIN']) && $_SERVER['HTTP_X_ADMIN'] === 'true') {
    $is_admin = true;
}

// Check 5: Token-based (hardcoded - can be found in source)
if (isset($_GET['token']) && $_GET['token'] === ADMIN_TOKEN) {
    $is_admin = true;
}

// Handle admin actions
if ($is_admin && $_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    
    switch ($action) {
        case 'delete_user':
            $user_id = $_POST['user_id'] ?? 0;
            $conn->query("DELETE FROM users WHERE id = $user_id");
            $success = "User $user_id deleted";
            break;
            
        case 'reset_password':
            $user_id = $_POST['user_id'] ?? 0;
            $new_pass = $_POST['new_password'] ?? 'password123';
            $conn->query("UPDATE users SET password = '$new_pass' WHERE id = $user_id");
            $success = "Password reset for user $user_id";
            break;
            
        case 'run_query':
            // VULNERABLE: Direct SQL execution
            $query = $_POST['query'] ?? '';
            $result = $conn->query($query);
            if ($result) {
                if ($result instanceof mysqli_result) {
                    $success = "Query executed. Rows: " . $result->num_rows;
                } else {
                    $success = "Query executed successfully";
                }
            } else {
                $error = "Query error: " . $conn->error;
            }
            break;
            
        case 'system_cmd':
            // VULNERABLE: Command injection
            $cmd = $_POST['cmd'] ?? '';
            $output = shell_exec($cmd);
            $success = "Command executed";
            break;
    }
}

// Get all users
$users = [];
$result = $conn->query("SELECT * FROM users ORDER BY id");
if ($result) {
    while ($row = $result->fetch_assoc()) {
        $users[] = $row;
    }
}

// Get system info (information disclosure)
$system_info = [
    'PHP Version' => phpversion(),
    'Server Software' => $_SERVER['SERVER_SOFTWARE'] ?? 'Unknown',
    'Document Root' => $_SERVER['DOCUMENT_ROOT'] ?? 'Unknown',
    'Server IP' => $_SERVER['SERVER_ADDR'] ?? 'Unknown',
    'MySQL Version' => $conn->server_info,
];

$conn->close();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Panel - Security Testing Lab</title>
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
                <a href="admin.php" class="nav-link active">Admin</a>
                <a href="api.php" class="nav-link">API</a>
                <a href="decoder.php" class="nav-link">Decoder</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <?php if (!$is_admin): ?>
            <div class="card">
                <div class="card-header">
                    <div class="card-icon">🔒</div>
                    <div>
                        <div class="card-title">Access Denied</div>
                        <div class="card-subtitle">Admin authentication required</div>
                    </div>
                </div>

                <div class="alert alert-error">
                    ❌ You are not authorized to access this page.
                </div>

                <div style="margin-top: 2rem;">
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🔓 Authentication Bypass Methods</h4>
                    <p style="color: var(--text-secondary); margin-bottom: 1rem;">Try these methods to bypass authentication:</p>
                    
                    <div class="code-block" style="margin-bottom: 1rem;">
1. URL Parameter: ?admin=true
2. URL Token: ?token=admin_token_12345
3. Cookie: admin=true
4. HTTP Header: X-Admin: true
5. Login as admin user first
                    </div>

                    <div style="display: flex; gap: 0.5rem; flex-wrap: wrap;">
                        <a href="admin.php?admin=true" class="btn btn-secondary">Try ?admin=true</a>
                        <a href="admin.php?token=admin_token_12345" class="btn btn-secondary">Try Token</a>
                        <button onclick="document.cookie='admin=true'; location.reload();" class="btn btn-secondary">Set Cookie</button>
                    </div>
                </div>

                <div class="alert alert-info" style="margin-top: 1.5rem;">
                    💡 <strong>Burp Tip:</strong> Intercept the request and add header <code>X-Admin: true</code>
                </div>
            </div>
        <?php else: ?>
            <div class="alert alert-success" style="margin-bottom: 1.5rem;">
                ✅ Admin access granted! Authentication bypassed successfully.
            </div>

            <?php if ($success): ?>
                <div class="alert alert-success">✅ <?php echo htmlspecialchars($success); ?></div>
            <?php endif; ?>

            <?php if ($error): ?>
                <div class="alert alert-error">❌ <?php echo $error; ?></div>
            <?php endif; ?>

            <div class="grid grid-2">
                <div class="card">
                    <div class="card-header">
                        <div class="card-icon">👥</div>
                        <div>
                            <div class="card-title">User Management</div>
                            <div class="card-subtitle">Manage all users</div>
                        </div>
                    </div>

                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Username</th>
                                    <th>Password</th>
                                    <th>Email</th>
                                    <th>Role</th>
                                    <th>Balance</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <?php foreach ($users as $user): ?>
                                    <tr>
                                        <td><?php echo $user['id']; ?></td>
                                        <td><?php echo htmlspecialchars($user['username']); ?></td>
                                        <td><code><?php echo htmlspecialchars($user['password']); ?></code></td>
                                        <td><?php echo htmlspecialchars($user['email']); ?></td>
                                        <td>
                                            <span class="tag <?php echo $user['role'] === 'admin' ? 'tag-vuln' : 'tag-info'; ?>">
                                                <?php echo $user['role']; ?>
                                            </span>
                                        </td>
                                        <td>$<?php echo number_format($user['balance'], 2); ?></td>
                                        <td>
                                            <form method="POST" style="display: inline;">
                                                <input type="hidden" name="action" value="delete_user">
                                                <input type="hidden" name="user_id" value="<?php echo $user['id']; ?>">
                                                <button type="submit" class="btn btn-danger" style="padding: 0.25rem 0.5rem; font-size: 0.75rem;">Delete</button>
                                            </form>
                                        </td>
                                    </tr>
                                <?php endforeach; ?>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <div class="card-icon">⚙️</div>
                        <div>
                            <div class="card-title">System Information</div>
                            <div class="card-subtitle">Server details (information disclosure)</div>
                        </div>
                    </div>

                    <div class="table-container">
                        <table>
                            <?php foreach ($system_info as $key => $value): ?>
                                <tr>
                                    <th><?php echo $key; ?></th>
                                    <td><?php echo htmlspecialchars($value); ?></td>
                                </tr>
                            <?php endforeach; ?>
                        </table>
                    </div>

                    <div style="margin-top: 1.5rem;">
                        <a href="phpinfo.php" class="btn btn-secondary">View Full phpinfo()</a>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <div class="card-icon">🗃️</div>
                        <div>
                            <div class="card-title">SQL Console</div>
                            <div class="card-subtitle">Execute raw SQL queries</div>
                        </div>
                    </div>

                    <form method="POST">
                        <input type="hidden" name="action" value="run_query">
                        <div class="form-group">
                            <label class="form-label">SQL Query</label>
                            <textarea name="query" class="form-textarea" placeholder="SELECT * FROM users WHERE id = 1"><?php echo htmlspecialchars($_POST['query'] ?? ''); ?></textarea>
                        </div>
                        <button type="submit" class="btn btn-danger">Execute Query</button>
                    </form>

                    <div class="code-block" style="margin-top: 1rem;">
Example queries:
SELECT * FROM users
SELECT * FROM information_schema.tables
DROP TABLE users -- (destructive!)
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <div class="card-icon">💻</div>
                        <div>
                            <div class="card-title">System Command</div>
                            <div class="card-subtitle">Execute system commands (RCE)</div>
                        </div>
                    </div>

                    <form method="POST">
                        <input type="hidden" name="action" value="system_cmd">
                        <div class="form-group">
                            <label class="form-label">Command</label>
                            <input type="text" name="cmd" class="form-input" placeholder="whoami">
                        </div>
                        <button type="submit" class="btn btn-danger">Execute Command</button>
                    </form>

                    <div class="code-block" style="margin-top: 1rem;">
Example commands (Windows):
whoami
dir
ipconfig
type C:\xampp\htdocs\security-testing\config.php
                    </div>
                </div>
            </div>
        <?php endif; ?>
    </div>
</body>
</html>

