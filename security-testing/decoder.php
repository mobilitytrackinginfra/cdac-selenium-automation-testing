<?php
require_once 'config.php';

// Sample encoded data for testing
$encoded_samples = [
    'base64' => [
        'label' => 'Base64 Encoded',
        'encoded' => base64_encode('admin:admin123'),
        'decoded' => 'admin:admin123',
        'description' => 'Basic authentication credentials'
    ],
    'base64_json' => [
        'label' => 'Base64 JSON',
        'encoded' => base64_encode('{"user_id":1,"role":"admin","secret":"sk_live_abc123"}'),
        'decoded' => '{"user_id":1,"role":"admin","secret":"sk_live_abc123"}',
        'description' => 'API token with sensitive data'
    ],
    'url' => [
        'label' => 'URL Encoded',
        'encoded' => urlencode("admin' OR '1'='1' --"),
        'decoded' => "admin' OR '1'='1' --",
        'description' => 'SQL injection payload'
    ],
    'double_url' => [
        'label' => 'Double URL Encoded',
        'encoded' => urlencode(urlencode("<script>alert('XSS')</script>")),
        'decoded' => "<script>alert('XSS')</script>",
        'description' => 'XSS payload with double encoding'
    ],
    'hex' => [
        'label' => 'Hex Encoded',
        'encoded' => bin2hex('password123'),
        'decoded' => 'password123',
        'description' => 'Password in hex format'
    ],
    'html' => [
        'label' => 'HTML Encoded',
        'encoded' => '&lt;script&gt;alert(document.cookie)&lt;/script&gt;',
        'decoded' => '<script>alert(document.cookie)</script>',
        'description' => 'XSS payload HTML encoded'
    ],
];

// JWT token sample
$jwt_header = base64_encode(json_encode(['alg' => 'HS256', 'typ' => 'JWT']));
$jwt_payload = base64_encode(json_encode([
    'sub' => '1234567890',
    'name' => 'Admin User',
    'admin' => true,
    'iat' => time(),
    'exp' => time() + 3600
]));
$jwt_signature = base64_encode('fake_signature_for_testing');
$sample_jwt = str_replace(['+', '/', '='], ['-', '_', ''], "$jwt_header.$jwt_payload.$jwt_signature");

// Handle decoding/encoding
$result = '';
$input_text = '';
$operation = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $input_text = $_POST['input'] ?? '';
    $operation = $_POST['operation'] ?? '';
    
    switch ($operation) {
        case 'base64_decode':
            $result = base64_decode($input_text);
            break;
        case 'base64_encode':
            $result = base64_encode($input_text);
            break;
        case 'url_decode':
            $result = urldecode($input_text);
            break;
        case 'url_encode':
            $result = urlencode($input_text);
            break;
        case 'hex_decode':
            $result = hex2bin($input_text);
            break;
        case 'hex_encode':
            $result = bin2hex($input_text);
            break;
        case 'html_decode':
            $result = html_entity_decode($input_text);
            break;
        case 'html_encode':
            $result = htmlspecialchars($input_text);
            break;
        case 'md5':
            $result = md5($input_text);
            break;
        case 'sha1':
            $result = sha1($input_text);
            break;
        case 'sha256':
            $result = hash('sha256', $input_text);
            break;
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Decoder Test - Security Testing Lab</title>
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
                <a href="decoder.php" class="nav-link active">Decoder</a>
                <a href="comparer.php" class="nav-link">Comparer</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="card">
            <div class="card-header">
                <div class="card-icon">🔐</div>
                <div>
                    <div class="card-title">Decoder Testing Lab</div>
                    <div class="card-subtitle">Practice with Burp Suite Decoder</div>
                </div>
            </div>

            <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">
                This page contains various encoded data samples for practicing with Burp Suite's Decoder tool.
                Copy the encoded values to Decoder and try to decode them.
            </p>

            <div class="grid grid-2">
                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">📝 Encoded Samples</h4>
                    
                    <?php foreach ($encoded_samples as $key => $sample): ?>
                        <div style="background: var(--bg-secondary); border: 1px solid var(--border-color); border-radius: 12px; padding: 1rem; margin-bottom: 1rem;">
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem;">
                                <strong style="color: var(--accent-secondary);"><?php echo $sample['label']; ?></strong>
                                <span class="tag tag-info"><?php echo $key; ?></span>
                            </div>
                            <p style="color: var(--text-muted); font-size: 0.85rem; margin-bottom: 0.5rem;">
                                <?php echo $sample['description']; ?>
                            </p>
                            <div class="code-block" style="margin-bottom: 0.5rem;">
                                <?php echo htmlspecialchars($sample['encoded']); ?>
                            </div>
                            <details>
                                <summary style="color: var(--accent-primary); cursor: pointer; font-size: 0.85rem;">Show decoded</summary>
                                <div class="code-block" style="margin-top: 0.5rem; color: var(--accent-primary);">
                                    <?php echo htmlspecialchars($sample['decoded']); ?>
                                </div>
                            </details>
                        </div>
                    <?php endforeach; ?>
                </div>

                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🎟️ JWT Token Sample</h4>
                    <div style="background: var(--bg-secondary); border: 1px solid var(--border-color); border-radius: 12px; padding: 1rem; margin-bottom: 1.5rem;">
                        <p style="color: var(--text-muted); font-size: 0.85rem; margin-bottom: 0.5rem;">
                            JWT with admin privileges - decode in Burp or jwt.io
                        </p>
                        <div class="code-block" style="word-break: break-all;">
                            <?php echo $sample_jwt; ?>
                        </div>
                        <details style="margin-top: 0.5rem;">
                            <summary style="color: var(--accent-primary); cursor: pointer; font-size: 0.85rem;">Show decoded parts</summary>
                            <div style="margin-top: 0.5rem;">
                                <p style="color: var(--text-muted); font-size: 0.8rem;">Header:</p>
                                <div class="code-block"><?php echo json_encode(['alg' => 'HS256', 'typ' => 'JWT'], JSON_PRETTY_PRINT); ?></div>
                                <p style="color: var(--text-muted); font-size: 0.8rem; margin-top: 0.5rem;">Payload:</p>
                                <div class="code-block"><?php echo json_encode([
                                    'sub' => '1234567890',
                                    'name' => 'Admin User',
                                    'admin' => true,
                                    'iat' => time(),
                                    'exp' => time() + 3600
                                ], JSON_PRETTY_PRINT); ?></div>
                            </div>
                        </details>
                    </div>

                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🔄 Online Encoder/Decoder</h4>
                    <form method="POST">
                        <div class="form-group">
                            <label class="form-label">Input Text</label>
                            <textarea name="input" class="form-textarea" placeholder="Enter text to encode/decode..."><?php echo htmlspecialchars($input_text); ?></textarea>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Operation</label>
                            <select name="operation" class="form-select">
                                <option value="base64_decode" <?php echo $operation === 'base64_decode' ? 'selected' : ''; ?>>Base64 Decode</option>
                                <option value="base64_encode" <?php echo $operation === 'base64_encode' ? 'selected' : ''; ?>>Base64 Encode</option>
                                <option value="url_decode" <?php echo $operation === 'url_decode' ? 'selected' : ''; ?>>URL Decode</option>
                                <option value="url_encode" <?php echo $operation === 'url_encode' ? 'selected' : ''; ?>>URL Encode</option>
                                <option value="hex_decode" <?php echo $operation === 'hex_decode' ? 'selected' : ''; ?>>Hex Decode</option>
                                <option value="hex_encode" <?php echo $operation === 'hex_encode' ? 'selected' : ''; ?>>Hex Encode</option>
                                <option value="html_decode" <?php echo $operation === 'html_decode' ? 'selected' : ''; ?>>HTML Decode</option>
                                <option value="html_encode" <?php echo $operation === 'html_encode' ? 'selected' : ''; ?>>HTML Encode</option>
                                <option value="md5" <?php echo $operation === 'md5' ? 'selected' : ''; ?>>MD5 Hash</option>
                                <option value="sha1" <?php echo $operation === 'sha1' ? 'selected' : ''; ?>>SHA1 Hash</option>
                                <option value="sha256" <?php echo $operation === 'sha256' ? 'selected' : ''; ?>>SHA256 Hash</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">Process</button>
                    </form>

                    <?php if ($result !== ''): ?>
                        <div style="margin-top: 1rem;">
                            <label class="form-label">Result</label>
                            <div class="code-block"><?php echo htmlspecialchars($result); ?></div>
                        </div>
                    <?php endif; ?>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <div class="card-icon">🍪</div>
                <div>
                    <div class="card-title">Cookie & Session Data</div>
                    <div class="card-subtitle">Encoded session information</div>
                </div>
            </div>

            <div class="grid grid-2">
                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">Current Session Cookie</h4>
                    <div class="code-block">
PHPSESSID: <?php echo session_id(); ?>
                    </div>
                    
                    <?php 
                    // Set some test cookies
                    $user_cookie = base64_encode(json_encode(['id' => 1, 'username' => 'admin', 'role' => 'admin']));
                    setcookie('user_data', $user_cookie, time() + 3600, '/');
                    setcookie('remember_token', md5('admin' . time()), time() + 3600, '/');
                    ?>
                    
                    <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">Test Cookies Set</h4>
                    <div class="code-block">
user_data: <?php echo $user_cookie; ?>

Decoded: <?php echo json_encode(['id' => 1, 'username' => 'admin', 'role' => 'admin']); ?>
                    </div>
                </div>

                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">Burp Decoder Tips</h4>
                    <ul style="color: var(--text-secondary); margin-left: 1.5rem;">
                        <li>Right-click any value → Send to Decoder</li>
                        <li>Smart Decode auto-detects encoding</li>
                        <li>Chain multiple decodings (URL → Base64 → Plain)</li>
                        <li>Use Encode As to re-encode modified values</li>
                        <li>Copy decoded values to Repeater for testing</li>
                    </ul>

                    <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">Common Encoding Chains</h4>
                    <div class="code-block">
1. URL Encoded Base64:
   %59%57%52%74%61%57%34%3D → YWRtaW4= → admin

2. Double URL Encoded:
   %253Cscript%253E → %3Cscript%3E → &lt;script&gt;

3. Unicode Escape:
   \u003cscript\u003e → &lt;script&gt;
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

