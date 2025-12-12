<?php
require_once 'config.php';

$conn = getConnection();

// Generate different responses for comparison testing
$scenario = $_GET['scenario'] ?? 'default';
$user = $_GET['user'] ?? '';
$password = $_GET['password'] ?? '';

$response_data = [];

switch ($scenario) {
    case 'valid_user':
        // Valid username response
        $response_data = [
            'status' => 'error',
            'message' => 'Invalid password for user admin',
            'code' => 'INVALID_PASSWORD',
            'timestamp' => date('c'),
            'attempts_remaining' => 3
        ];
        http_response_code(401);
        break;
        
    case 'invalid_user':
        // Invalid username response (slightly different)
        $response_data = [
            'status' => 'error',
            'message' => 'User not found',
            'code' => 'USER_NOT_FOUND',
            'timestamp' => date('c')
        ];
        http_response_code(404);
        break;
        
    case 'login_success':
        // Successful login response
        $response_data = [
            'status' => 'success',
            'message' => 'Login successful',
            'code' => 'AUTH_SUCCESS',
            'user' => [
                'id' => 1,
                'username' => 'admin',
                'role' => 'admin',
                'token' => bin2hex(random_bytes(16))
            ],
            'timestamp' => date('c')
        ];
        http_response_code(200);
        break;
        
    case 'auth_check':
        // Check credentials and return appropriate response
        if ($user && $password) {
            $result = $conn->query("SELECT * FROM users WHERE username = '$user'");
            
            if ($result && $result->num_rows > 0) {
                $db_user = $result->fetch_assoc();
                if ($db_user['password'] === $password) {
                    $response_data = [
                        'status' => 'success',
                        'message' => 'Authentication successful',
                        'length' => strlen(json_encode(['status' => 'success']))
                    ];
                } else {
                    // Valid user, wrong password
                    $response_data = [
                        'status' => 'error',
                        'message' => "Invalid password for user: $user",
                        'length' => strlen(json_encode(['status' => 'error', 'extra' => 'data']))
                    ];
                }
            } else {
                // Invalid user
                $response_data = [
                    'status' => 'error',
                    'message' => 'User does not exist',
                    'length' => strlen(json_encode(['status' => 'error']))
                ];
            }
        } else {
            $response_data = [
                'status' => 'error',
                'message' => 'Username and password required'
            ];
        }
        break;
        
    default:
        $response_data = null;
        break;
}

// If API request, return JSON
if (isset($_GET['api'])) {
    header('Content-Type: application/json');
    echo json_encode($response_data, JSON_PRETTY_PRINT);
    exit;
}

$conn->close();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Comparer Test - Security Testing Lab</title>
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
                <a href="comparer.php" class="nav-link active">Comparer</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="card">
            <div class="card-header">
                <div class="card-icon">⚖️</div>
                <div>
                    <div class="card-title">Comparer Testing Lab</div>
                    <div class="card-subtitle">Practice with Burp Suite Comparer</div>
                </div>
            </div>

            <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">
                This page generates different responses for practicing with Burp Suite's Comparer tool.
                Send responses to Comparer to find differences that reveal information about the application.
            </p>

            <div class="grid grid-2">
                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">📊 Response Comparison Scenarios</h4>
                    
                    <div style="background: var(--bg-secondary); border: 1px solid var(--border-color); border-radius: 12px; padding: 1rem; margin-bottom: 1rem;">
                        <strong style="color: var(--accent-secondary);">Username Enumeration</strong>
                        <p style="color: var(--text-muted); font-size: 0.85rem; margin: 0.5rem 0;">
                            Compare responses to detect valid usernames
                        </p>
                        <div style="display: flex; gap: 0.5rem; margin-top: 0.5rem;">
                            <a href="comparer.php?scenario=valid_user&api=1" class="btn btn-secondary" style="font-size: 0.8rem;" target="_blank">Valid User Response</a>
                            <a href="comparer.php?scenario=invalid_user&api=1" class="btn btn-secondary" style="font-size: 0.8rem;" target="_blank">Invalid User Response</a>
                        </div>
                    </div>

                    <div style="background: var(--bg-secondary); border: 1px solid var(--border-color); border-radius: 12px; padding: 1rem; margin-bottom: 1rem;">
                        <strong style="color: var(--accent-secondary);">Authentication Response</strong>
                        <p style="color: var(--text-muted); font-size: 0.85rem; margin: 0.5rem 0;">
                            Compare success vs failure responses
                        </p>
                        <div style="display: flex; gap: 0.5rem; margin-top: 0.5rem;">
                            <a href="comparer.php?scenario=login_success&api=1" class="btn btn-secondary" style="font-size: 0.8rem;" target="_blank">Success Response</a>
                            <a href="comparer.php?scenario=valid_user&api=1" class="btn btn-secondary" style="font-size: 0.8rem;" target="_blank">Failure Response</a>
                        </div>
                    </div>

                    <div style="background: var(--bg-secondary); border: 1px solid var(--border-color); border-radius: 12px; padding: 1rem; margin-bottom: 1rem;">
                        <strong style="color: var(--accent-secondary);">Live Auth Check</strong>
                        <p style="color: var(--text-muted); font-size: 0.85rem; margin: 0.5rem 0;">
                            Test with actual credentials
                        </p>
                        <form action="comparer.php" method="GET" target="_blank">
                            <input type="hidden" name="scenario" value="auth_check">
                            <input type="hidden" name="api" value="1">
                            <div style="display: flex; gap: 0.5rem; margin-top: 0.5rem;">
                                <input type="text" name="user" class="form-input" placeholder="Username" style="padding: 0.5rem;">
                                <input type="text" name="password" class="form-input" placeholder="Password" style="padding: 0.5rem;">
                                <button type="submit" class="btn btn-primary" style="font-size: 0.8rem;">Test</button>
                            </div>
                        </form>
                    </div>
                </div>

                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">🔍 What to Look For</h4>
                    
                    <div class="code-block" style="margin-bottom: 1rem;">
<strong>Response Length Differences:</strong>
- Valid user: 187 bytes
- Invalid user: 142 bytes

<strong>Message Differences:</strong>
- "Invalid password for user admin"
- "User not found"

<strong>HTTP Status Codes:</strong>
- Valid user wrong pass: 401
- Invalid user: 404
- Success: 200

<strong>Extra Fields:</strong>
- Valid user has "attempts_remaining"
- Invalid user missing that field
                    </div>

                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">📋 Burp Comparer Steps</h4>
                    <ol style="color: var(--text-secondary); margin-left: 1.5rem;">
                        <li>Send first response to Comparer (Right-click → Send to Comparer)</li>
                        <li>Send second response to Comparer</li>
                        <li>Go to Comparer tab</li>
                        <li>Select both items and click "Words" or "Bytes"</li>
                        <li>Analyze highlighted differences</li>
                    </ol>

                    <div class="alert alert-info" style="margin-top: 1.5rem;">
                        💡 <strong>Tip:</strong> Use Intruder with username wordlist, then compare responses with different lengths to enumerate valid users.
                    </div>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <div class="card-icon">📝</div>
                <div>
                    <div class="card-title">Sample Response Comparison</div>
                    <div class="card-subtitle">Side-by-side response differences</div>
                </div>
            </div>

            <div class="grid grid-2">
                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">Valid Username Response</h4>
                    <div class="code-block">
HTTP/1.1 401 Unauthorized
Content-Type: application/json

{
    "status": "error",
    "message": "Invalid password for user admin",
    "code": "INVALID_PASSWORD",
    "timestamp": "2024-01-15T10:30:00Z",
    <span style="color: var(--accent-danger);">"attempts_remaining": 3</span>
}
                    </div>
                </div>
                <div>
                    <h4 style="color: var(--accent-primary); margin-bottom: 1rem;">Invalid Username Response</h4>
                    <div class="code-block">
HTTP/1.1 <span style="color: var(--accent-danger);">404 Not Found</span>
Content-Type: application/json

{
    "status": "error",
    "message": "<span style="color: var(--accent-danger);">User not found</span>",
    "code": "<span style="color: var(--accent-danger);">USER_NOT_FOUND</span>",
    "timestamp": "2024-01-15T10:30:00Z"
}
                    </div>
                </div>
            </div>

            <div class="alert alert-warning" style="margin-top: 1.5rem;">
                ⚠️ <strong>Vulnerability:</strong> The different responses reveal whether a username exists in the system (Username Enumeration). A secure application should return identical responses regardless of whether the user exists.
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <div class="card-icon">🎯</div>
                <div>
                    <div class="card-title">Intruder + Comparer Workflow</div>
                    <div class="card-subtitle">Automated username enumeration</div>
                </div>
            </div>

            <ol style="color: var(--text-secondary); margin-left: 1.5rem; line-height: 2;">
                <li>Intercept a login request in Burp Proxy</li>
                <li>Send to Intruder (Ctrl+I)</li>
                <li>Set payload position on username parameter</li>
                <li>Load username wordlist (e.g., names.txt)</li>
                <li>Start attack and observe response lengths</li>
                <li>Responses with different lengths indicate valid usernames</li>
                <li>Send different responses to Comparer to confirm</li>
            </ol>

            <div class="code-block" style="margin-top: 1rem;">
Example Intruder Request:
POST /comparer.php?scenario=auth_check&api=1 HTTP/1.1
Host: localhost
Content-Type: application/x-www-form-urlencoded

user=§admin§&password=wrongpassword

Payload: admin, john, jane, bob, alice, root, test, guest...
            </div>
        </div>
    </div>
</body>
</html>

