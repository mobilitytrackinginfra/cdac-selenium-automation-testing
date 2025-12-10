<?php
// sql_test.php
// Demo API for SQL Injection testing (vulnerable + secure modes)

header('Content-Type: application/json; charset=utf-8');

// ---- 1. Initialize SQLite in local file ----
try {
    $pdo = new PDO('sqlite:' . __DIR__ . '/sql_demo.db');
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Create users table if not exists
    $pdo->exec("
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT NOT NULL,
            password TEXT NOT NULL
        )
    ");

    // Seed data only if table is empty
    $stmt = $pdo->query("SELECT COUNT(*) AS cnt FROM users");
    $count = (int)$stmt->fetch(PDO::FETCH_ASSOC)['cnt'];

    if ($count === 0) {
        $pdo->exec("
            INSERT INTO users (username, password) VALUES
            ('admin', 'admin123'),
            ('test', 'test123'),
            ('swapnil', 'password')
        ");
    }
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode([
        'error' => true,
        'message' => 'Database initialization error',
        'details' => $e->getMessage()
    ], JSON_PRETTY_PRINT);
    exit;
}

// ---- 2. Read mode ----
// mode=vulnerable  -> raw string concatenation (SQL injection possible)
// mode=secure      -> prepared statements (safe)
$mode = isset($_GET['mode']) ? $_GET['mode'] : 'vulnerable';

// ---- 3. Read JSON body ----
$rawBody = file_get_contents('php://input');
$input = json_decode($rawBody, true);

$username = isset($input['username']) ? $input['username'] : '';
$password = isset($input['password']) ? $input['password'] : '';

if ($username === '' || $password === '') {
    http_response_code(400);
    echo json_encode([
        'error'   => true,
        'message' => 'username and password are required',
        'mode'    => $mode
    ], JSON_PRETTY_PRINT);
    exit;
}

// ---- 4. Build and execute query depending on mode ----
$response = [
    'mode'     => $mode,
    'username' => $username,
    'password' => $password
];

try {
    if ($mode === 'vulnerable') {
        // 🚨 VULNERABLE CODE: direct string concatenation
        $sql = "SELECT * FROM users WHERE username = '" . $username . "' AND password = '" . $password . "'";
        $response['constructed_sql'] = $sql;

        $stmt = $pdo->query($sql);
        $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);
    } elseif ($mode === 'secure') {
        // ✅ SECURE CODE: prepared statements
        $sql = "SELECT * FROM users WHERE username = :username AND password = :password";
        $response['prepared_sql'] = $sql;

        $stmt = $pdo->prepare($sql);
        $stmt->execute([
            ':username' => $username,
            ':password' => $password
        ]);
        $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);
    } else {
        http_response_code(400);
        echo json_encode([
            'error'   => true,
            'message' => 'Invalid mode. Use mode=vulnerable or mode=secure'
        ], JSON_PRETTY_PRINT);
        exit;
    }

    $rowCount = count($rows);
    $response['row_count'] = $rowCount;
    $response['rows']      = $rows;

    if ($rowCount > 0) {
        // Login success
        http_response_code(200);
        $response['login']   = true;
        $response['message'] = 'Login successful';
    } else {
        // Login failed
        http_response_code(401);
        $response['login']   = false;
        $response['message'] = 'Invalid credentials';
    }

    echo json_encode($response, JSON_PRETTY_PRINT);

} catch (PDOException $e) {
    // In vulnerable mode, we intentionally leak DB error (for demo)
    if ($mode === 'vulnerable') {
        http_response_code(500);
        echo json_encode([
            'error'   => true,
            'mode'    => $mode,
            'message' => 'Database error (intentionally leaked in vulnerable mode)',
            'details' => $e->getMessage()
        ], JSON_PRETTY_PRINT);
    } else {
        // In secure mode, return generic message
        http_response_code(500);
        echo json_encode([
            'error'   => true,
            'mode'    => $mode,
            'message' => 'Internal server error'
        ], JSON_PRETTY_PRINT);
    }
    exit;
}
