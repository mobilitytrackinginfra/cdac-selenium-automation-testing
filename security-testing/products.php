<?php
require_once 'config.php';

$conn = getConnection();

// Get products
$category = $_GET['category'] ?? '';
$sort = $_GET['sort'] ?? 'id';
$order = $_GET['order'] ?? 'ASC';

// VULNERABLE: SQL Injection via sort/order parameters
if ($category) {
    $sql = "SELECT * FROM products WHERE category = '$category' ORDER BY $sort $order";
} else {
    $sql = "SELECT * FROM products ORDER BY $sort $order";
}

if (isset($_GET['debug'])) {
    echo "<pre style='background:#1a1f2e;color:#00ff88;padding:1rem;border-radius:8px;margin:1rem;'>Query: $sql</pre>";
}

$products = [];
$result = $conn->query($sql);
if ($result) {
    while ($row = $result->fetch_assoc()) {
        $products[] = $row;
    }
}

// Handle product view (IDOR)
$product_detail = null;
if (isset($_GET['id'])) {
    $product_id = $_GET['id'];
    // VULNERABLE: SQL Injection
    $result = $conn->query("SELECT * FROM products WHERE id = $product_id");
    $product_detail = $result ? $result->fetch_assoc() : null;
}

$conn->close();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products - Security Testing Lab</title>
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
                <a href="products.php" class="nav-link active">Products</a>
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
        <?php if ($product_detail): ?>
            <div class="card">
                <a href="products.php" style="color: var(--accent-secondary);">← Back to Products</a>
                <h2 style="margin: 1rem 0;"><?php echo htmlspecialchars($product_detail['name']); ?></h2>
                <p style="color: var(--text-secondary); margin-bottom: 1rem;">
                    <?php echo htmlspecialchars($product_detail['description']); ?>
                </p>
                <p style="font-size: 1.5rem; color: var(--accent-primary); font-weight: bold;">
                    $<?php echo number_format($product_detail['price'], 2); ?>
                </p>
                <span class="tag tag-info"><?php echo htmlspecialchars($product_detail['category']); ?></span>
            </div>
        <?php endif; ?>

        <div class="card">
            <div class="card-header">
                <div class="card-icon">🛒</div>
                <div>
                    <div class="card-title">Product Catalog</div>
                    <div class="card-subtitle">Browse our products</div>
                </div>
            </div>

            <div style="display: flex; gap: 1rem; margin-bottom: 1.5rem; flex-wrap: wrap;">
                <div>
                    <label class="form-label">Category</label>
                    <select class="form-select" onchange="location.href='products.php?category='+this.value">
                        <option value="">All Categories</option>
                        <option value="electronics" <?php echo $category === 'electronics' ? 'selected' : ''; ?>>Electronics</option>
                        <option value="books" <?php echo $category === 'books' ? 'selected' : ''; ?>>Books</option>
                        <option value="accessories" <?php echo $category === 'accessories' ? 'selected' : ''; ?>>Accessories</option>
                    </select>
                </div>
                <div>
                    <label class="form-label">Sort By</label>
                    <select class="form-select" onchange="location.href='products.php?sort='+this.value+'&order=<?php echo $order; ?>'">
                        <option value="id" <?php echo $sort === 'id' ? 'selected' : ''; ?>>ID</option>
                        <option value="name" <?php echo $sort === 'name' ? 'selected' : ''; ?>>Name</option>
                        <option value="price" <?php echo $sort === 'price' ? 'selected' : ''; ?>>Price</option>
                    </select>
                </div>
                <div>
                    <label class="form-label">Order</label>
                    <select class="form-select" onchange="location.href='products.php?sort=<?php echo $sort; ?>&order='+this.value">
                        <option value="ASC" <?php echo $order === 'ASC' ? 'selected' : ''; ?>>Ascending</option>
                        <option value="DESC" <?php echo $order === 'DESC' ? 'selected' : ''; ?>>Descending</option>
                    </select>
                </div>
                <div style="align-self: flex-end;">
                    <a href="products.php?debug=1" class="btn btn-secondary">Debug Mode</a>
                </div>
            </div>

            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Price</th>
                            <th>Category</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($products as $product): ?>
                            <tr>
                                <td><?php echo $product['id']; ?></td>
                                <td><?php echo htmlspecialchars($product['name']); ?></td>
                                <td><?php echo htmlspecialchars(substr($product['description'], 0, 50)) . '...'; ?></td>
                                <td>$<?php echo number_format($product['price'], 2); ?></td>
                                <td><span class="tag tag-info"><?php echo htmlspecialchars($product['category']); ?></span></td>
                                <td>
                                    <a href="products.php?id=<?php echo $product['id']; ?>" class="btn btn-secondary" style="padding: 0.25rem 0.75rem; font-size: 0.8rem;">View</a>
                                </td>
                            </tr>
                        <?php endforeach; ?>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <div class="card-icon">💉</div>
                <div>
                    <div class="card-title">SQL Injection Testing</div>
                    <div class="card-subtitle">ORDER BY injection</div>
                </div>
            </div>

            <p style="color: var(--text-secondary); margin-bottom: 1rem;">
                The <code>sort</code> and <code>order</code> parameters are vulnerable to SQL injection.
            </p>

            <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">Test Payloads</h4>
            <div class="code-block">
# Error-based injection via sort parameter:
products.php?sort=id;SELECT * FROM users--

# Boolean-based:
products.php?sort=(SELECT CASE WHEN (1=1) THEN id ELSE name END)

# Time-based blind:
products.php?sort=IF(1=1,SLEEP(5),id)

# UNION with ORDER BY:
products.php?sort=1 UNION SELECT 1,2,3,4,5--
            </div>

            <div class="alert alert-info" style="margin-top: 1rem;">
                💡 <strong>Tip:</strong> ORDER BY injection is often overlooked. Use Burp Intruder to fuzz these parameters.
            </div>
        </div>
    </div>
</body>
</html>

