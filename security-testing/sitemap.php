<?php
require_once 'config.php';

// This page helps with application mapping / spidering
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sitemap - Security Testing Lab</title>
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
                <a href="decoder.php" class="nav-link">Decoder</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="card">
            <div class="card-header">
                <div class="card-icon">🕷️</div>
                <div>
                    <div class="card-title">Application Sitemap</div>
                    <div class="card-subtitle">For Burp Spider / Crawler testing</div>
                </div>
            </div>

            <p style="color: var(--text-secondary); margin-bottom: 2rem;">
                This page contains all the links in the application for practicing with Burp Suite's Spider/Crawler functionality.
                Start spidering from here to map the entire application.
            </p>

            <div class="grid grid-3">
                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">📄 Main Pages</h4>
                    <ul style="list-style: none;">
                        <li style="margin-bottom: 0.5rem;"><a href="index.php" style="color: var(--accent-secondary);">Homepage</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="login.php" style="color: var(--accent-secondary);">Login Page</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="logout.php" style="color: var(--accent-secondary);">Logout</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="reset.php" style="color: var(--accent-secondary);">Password Reset</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="register.php" style="color: var(--accent-secondary);">Register (Hidden)</a></li>
                    </ul>
                </div>

                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🛒 Products</h4>
                    <ul style="list-style: none;">
                        <li style="margin-bottom: 0.5rem;"><a href="products.php" style="color: var(--accent-secondary);">All Products</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="products.php?category=electronics" style="color: var(--accent-secondary);">Electronics</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="products.php?category=books" style="color: var(--accent-secondary);">Books</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="products.php?category=accessories" style="color: var(--accent-secondary);">Accessories</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="products.php?id=1" style="color: var(--accent-secondary);">Product Detail</a></li>
                    </ul>
                </div>

                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">👤 User Pages</h4>
                    <ul style="list-style: none;">
                        <li style="margin-bottom: 0.5rem;"><a href="profile.php?id=1" style="color: var(--accent-secondary);">User Profile 1</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="profile.php?id=2" style="color: var(--accent-secondary);">User Profile 2</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="profile.php?id=3" style="color: var(--accent-secondary);">User Profile 3</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="transfer.php" style="color: var(--accent-secondary);">Money Transfer</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="settings.php" style="color: var(--accent-secondary);">Settings (Hidden)</a></li>
                    </ul>
                </div>

                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🔧 Features</h4>
                    <ul style="list-style: none;">
                        <li style="margin-bottom: 0.5rem;"><a href="search.php" style="color: var(--accent-secondary);">Search</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="comments.php" style="color: var(--accent-secondary);">Comments</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="upload.php" style="color: var(--accent-secondary);">File Upload</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="fetch.php" style="color: var(--accent-secondary);">URL Fetch (SSRF)</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="export.php" style="color: var(--accent-secondary);">Data Export</a></li>
                    </ul>
                </div>

                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">⚙️ Admin</h4>
                    <ul style="list-style: none;">
                        <li style="margin-bottom: 0.5rem;"><a href="admin.php" style="color: var(--accent-secondary);">Admin Panel</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="admin.php?admin=true" style="color: var(--accent-secondary);">Admin (Bypass)</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="phpinfo.php" style="color: var(--accent-secondary);">PHP Info</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="config.php" style="color: var(--accent-secondary);">Config (Sensitive)</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="backup.php" style="color: var(--accent-secondary);">Backup (Hidden)</a></li>
                    </ul>
                </div>

                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🔌 API</h4>
                    <ul style="list-style: none;">
                        <li style="margin-bottom: 0.5rem;"><a href="api.php" style="color: var(--accent-secondary);">API Documentation</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="api.php?endpoint=users" style="color: var(--accent-secondary);">Users API</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="api.php?endpoint=products" style="color: var(--accent-secondary);">Products API</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="api.php?endpoint=debug" style="color: var(--accent-secondary);">Debug API</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="api/v1/users" style="color: var(--accent-secondary);">API v1 (404)</a></li>
                    </ul>
                </div>

                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🧪 Testing</h4>
                    <ul style="list-style: none;">
                        <li style="margin-bottom: 0.5rem;"><a href="decoder.php" style="color: var(--accent-secondary);">Decoder Test</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="comparer.php" style="color: var(--accent-secondary);">Comparer Test</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="setup.php" style="color: var(--accent-secondary);">Database Setup</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="test.php" style="color: var(--accent-secondary);">Test Page (Hidden)</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="debug.php" style="color: var(--accent-secondary);">Debug (Hidden)</a></li>
                    </ul>
                </div>

                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">📁 Directories</h4>
                    <ul style="list-style: none;">
                        <li style="margin-bottom: 0.5rem;"><a href="uploads/" style="color: var(--accent-secondary);">Uploads Directory</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="assets/" style="color: var(--accent-secondary);">Assets Directory</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="backup/" style="color: var(--accent-secondary);">Backup (Hidden)</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="logs/" style="color: var(--accent-secondary);">Logs (Hidden)</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href=".git/" style="color: var(--accent-secondary);">.git (Exposed?)</a></li>
                    </ul>
                </div>

                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">📋 Files</h4>
                    <ul style="list-style: none;">
                        <li style="margin-bottom: 0.5rem;"><a href="robots.txt" style="color: var(--accent-secondary);">robots.txt</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="sitemap.xml" style="color: var(--accent-secondary);">sitemap.xml</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href=".htaccess" style="color: var(--accent-secondary);">.htaccess</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="web.config" style="color: var(--accent-secondary);">web.config</a></li>
                        <li style="margin-bottom: 0.5rem;"><a href="readme.txt" style="color: var(--accent-secondary);">readme.txt</a></li>
                    </ul>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <div class="card-icon">🔍</div>
                <div>
                    <div class="card-title">Burp Spider Tips</div>
                    <div class="card-subtitle">Application mapping techniques</div>
                </div>
            </div>

            <div class="grid grid-2">
                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">Spider Configuration</h4>
                    <ol style="color: var(--text-secondary); margin-left: 1.5rem;">
                        <li>Go to Target → Site map</li>
                        <li>Right-click the host → Spider this host</li>
                        <li>Configure spider settings for depth and scope</li>
                        <li>Enable "Submit forms" for better coverage</li>
                        <li>Review discovered content in Site map</li>
                    </ol>
                </div>
                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">Content Discovery</h4>
                    <ol style="color: var(--text-secondary); margin-left: 1.5rem;">
                        <li>Use Engagement tools → Discover content</li>
                        <li>Configure wordlists (directory, file names)</li>
                        <li>Look for backup files (.bak, .old, ~)</li>
                        <li>Check for common files (robots.txt, sitemap.xml)</li>
                        <li>Test for directory listing vulnerabilities</li>
                    </ol>
                </div>
            </div>

            <div class="alert alert-info" style="margin-top: 1.5rem;">
                💡 <strong>Note:</strong> Some links above intentionally return 404 to simulate hidden/deprecated endpoints. Use content discovery tools to find them!
            </div>
        </div>
    </div>
</body>
</html>

