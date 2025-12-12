<?php
require_once 'config.php';

// Set JSON content type for API responses
header('Content-Type: application/json');

// VULNERABLE: CORS misconfiguration - allows any origin
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
header('Access-Control-Allow-Headers: *');

// Handle preflight
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

$conn = getConnection();
$response = ['success' => false, 'message' => '', 'data' => null];

// Get request method and endpoint
$method = $_SERVER['REQUEST_METHOD'];
$endpoint = $_GET['endpoint'] ?? '';
$action = $_GET['action'] ?? '';

// Parse JSON body for POST/PUT requests
$input = json_decode(file_get_contents('php://input'), true) ?? $_POST;

try {
    switch ($endpoint) {
        case 'users':
            switch ($method) {
                case 'GET':
                    $id = $_GET['id'] ?? null;
                    if ($id) {
                        // VULNERABLE: SQL Injection
                        $result = $conn->query("SELECT * FROM users WHERE id = $id");
                        $response['data'] = $result->fetch_assoc();
                    } else {
                        $result = $conn->query("SELECT id, username, email, role, balance FROM users");
                        $response['data'] = $result->fetch_all(MYSQLI_ASSOC);
                    }
                    $response['success'] = true;
                    break;
                    
                case 'POST':
                    // Create user - no validation
                    $username = $input['username'] ?? '';
                    $password = $input['password'] ?? '';
                    $email = $input['email'] ?? '';
                    
                    $sql = "INSERT INTO users (username, password, email) VALUES ('$username', '$password', '$email')";
                    if ($conn->query($sql)) {
                        $response['success'] = true;
                        $response['data'] = ['id' => $conn->insert_id];
                    } else {
                        $response['message'] = $conn->error;
                    }
                    break;
                    
                case 'PUT':
                    $id = $_GET['id'] ?? 0;
                    $sets = [];
                    foreach ($input as $key => $value) {
                        // VULNERABLE: Mass assignment
                        $sets[] = "$key = '$value'";
                    }
                    $sql = "UPDATE users SET " . implode(', ', $sets) . " WHERE id = $id";
                    $conn->query($sql);
                    $response['success'] = true;
                    $response['message'] = "User updated";
                    break;
                    
                case 'DELETE':
                    $id = $_GET['id'] ?? 0;
                    $conn->query("DELETE FROM users WHERE id = $id");
                    $response['success'] = true;
                    $response['message'] = "User deleted";
                    break;
            }
            break;
            
        case 'products':
            switch ($method) {
                case 'GET':
                    $search = $_GET['search'] ?? '';
                    if ($search) {
                        // VULNERABLE: SQL Injection
                        $sql = "SELECT * FROM products WHERE name LIKE '%$search%'";
                    } else {
                        $sql = "SELECT * FROM products";
                    }
                    $result = $conn->query($sql);
                    $response['data'] = $result ? $result->fetch_all(MYSQLI_ASSOC) : [];
                    $response['success'] = true;
                    break;
            }
            break;
            
        case 'auth':
            if ($method === 'POST') {
                $username = $input['username'] ?? '';
                $password = $input['password'] ?? '';
                
                // VULNERABLE: SQL Injection in authentication
                $sql = "SELECT * FROM users WHERE username = '$username' AND password = '$password'";
                $result = $conn->query($sql);
                
                if ($result && $result->num_rows > 0) {
                    $user = $result->fetch_assoc();
                    // VULNERABLE: Sensitive data in response
                    $response['success'] = true;
                    $response['data'] = $user;
                    $response['token'] = base64_encode(json_encode(['user_id' => $user['id'], 'role' => $user['role']]));
                } else {
                    $response['message'] = "Authentication failed for user: $username";
                }
            }
            break;
            
        case 'debug':
            // VULNERABLE: Debug endpoint exposed
            $response['success'] = true;
            $response['data'] = [
                'server' => $_SERVER,
                'session' => $_SESSION ?? [],
                'cookies' => $_COOKIE,
                'env' => getenv()
            ];
            break;
            
        case 'file':
            // VULNERABLE: Arbitrary file read (LFI)
            $file = $_GET['path'] ?? '';
            if ($file && file_exists($file)) {
                $response['success'] = true;
                $response['data'] = file_get_contents($file);
            } else {
                $response['message'] = "File not found: $file";
            }
            break;
            
        case 'ping':
            // VULNERABLE: Command injection
            $host = $_GET['host'] ?? 'localhost';
            $output = shell_exec("ping -n 1 $host");
            $response['success'] = true;
            $response['data'] = $output;
            break;
            
        default:
            // API documentation
            $response['success'] = true;
            $response['message'] = 'Security Testing Lab API';
            $response['endpoints'] = [
                'GET /api.php?endpoint=users' => 'List all users',
                'GET /api.php?endpoint=users&id=1' => 'Get user by ID',
                'POST /api.php?endpoint=users' => 'Create user',
                'PUT /api.php?endpoint=users&id=1' => 'Update user',
                'DELETE /api.php?endpoint=users&id=1' => 'Delete user',
                'GET /api.php?endpoint=products' => 'List products',
                'GET /api.php?endpoint=products&search=term' => 'Search products',
                'POST /api.php?endpoint=auth' => 'Authenticate user',
                'GET /api.php?endpoint=debug' => 'Debug info (vulnerable)',
                'GET /api.php?endpoint=file&path=/etc/passwd' => 'Read file (LFI)',
                'GET /api.php?endpoint=ping&host=localhost' => 'Ping host (RCE)',
            ];
            break;
    }
} catch (Exception $e) {
    $response['message'] = "Error: " . $e->getMessage();
}

$conn->close();

// Output JSON response
echo json_encode($response, JSON_PRETTY_PRINT);
?>

