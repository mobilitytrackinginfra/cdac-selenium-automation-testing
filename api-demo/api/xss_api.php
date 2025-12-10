<?php
// xss_api.php
// Demo API for XSS testing: reflected + stored (vulnerable + secure modes)

header('Content-Type: application/json; charset=utf-8');

$type = isset($_GET['type']) ? $_GET['type'] : 'reflect';   // reflect | store
$mode = isset($_GET['mode']) ? $_GET['mode'] : 'vulnerable'; // vulnerable | secure
$method = $_SERVER['REQUEST_METHOD'];

// Helper: read JSON body
$rawBody = file_get_contents('php://input');
$data = json_decode($rawBody, true);

// ---------- 1. REFLECTED XSS DEMO ----------
if ($type === 'reflect') {

    // Input can come from JSON body or query param
    $input = '';
    if (is_array($data) && isset($data['input'])) {
        $input = $data['input'];
    } elseif (isset($_GET['input'])) {
        $input = $_GET['input'];
    }

    if ($input === '') {
        http_response_code(400);
        echo json_encode([
            'error'   => true,
            'message' => 'input is required',
            'type'    => 'reflect',
            'mode'    => $mode
        ], JSON_PRETTY_PRINT);
        exit;
    }

    if ($mode === 'vulnerable') {
        // ❌ VULNERABLE: raw echo back (as if this were HTML somewhere)
        $output = $input;
        $xssPossible = true;
    } else {
        // ✅ SECURE: encode dangerous characters
        $output = htmlspecialchars($input, ENT_QUOTES, 'UTF-8');
        $xssPossible = false;
    }

    http_response_code(200);
    echo json_encode([
        'error'        => false,
        'type'         => 'reflect',
        'mode'         => $mode,
        'input'        => $input,
        'output'       => $output,
        'xss_possible' => $xssPossible
    ], JSON_PRETTY_PRINT);
    exit;
}

// ---------- 2. STORED XSS DEMO (comments) ----------

/**
 * We use SQLite file xss_demo.db in the same directory.
 * VULNERABLE MODE: returns raw comment text (could be <script>).
 * SECURE MODE: escapes comment text before returning.
 */

try {
    $pdo = new PDO('sqlite:' . __DIR__ . '/xss_demo.db');
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $pdo->exec("
        CREATE TABLE IF NOT EXISTS comments (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            text TEXT NOT NULL,
            created_at TEXT NOT NULL
        )
    ");
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode([
        'error'   => true,
        'type'    => 'store',
        'mode'    => $mode,
        'message' => 'Database initialization error',
        'details' => $e->getMessage()
    ], JSON_PRETTY_PRINT);
    exit;
}

if ($type === 'store') {

    if ($method === 'POST') {
        // Create comment (store raw text)
        $comment = is_array($data) && isset($data['comment']) ? $data['comment'] : '';

        if ($comment === '') {
            http_response_code(400);
            echo json_encode([
                'error'   => true,
                'type'    => 'store',
                'mode'    => $mode,
                'message' => 'comment is required'
            ], JSON_PRETTY_PRINT);
            exit;
        }

        try {
            $stmt = $pdo->prepare("INSERT INTO comments (text, created_at) VALUES (:text, :created_at)");
            $stmt->execute([
                ':text'       => $comment,                 // stored raw
                ':created_at' => date('Y-m-d H:i:s')
            ]);

            $id = $pdo->lastInsertId();

            http_response_code(201);
            echo json_encode([
                'error'   => false,
                'type'    => 'store',
                'mode'    => $mode,
                'action'  => 'create',
                'id'      => (int)$id,
                'comment' => $comment
            ], JSON_PRETTY_PRINT);
            exit;

        } catch (PDOException $e) {
            http_response_code(500);
            echo json_encode([
                'error'   => true,
                'type'    => 'store',
                'mode'    => $mode,
                'message' => 'Insert failed',
                'details' => $e->getMessage()
            ], JSON_PRETTY_PRINT);
            exit;
        }
    }

    if ($method === 'GET') {
        // List all comments
        try {
            $stmt = $pdo->query("SELECT id, text, created_at FROM comments ORDER BY id ASC");
            $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

            $comments = [];

            foreach ($rows as $row) {
                if ($mode === 'vulnerable') {
                    // ❌ VULNERABLE: returning raw text
                    $comments[] = [
                        'id'         => (int)$row['id'],
                        'text'       => $row['text'],          // raw -> XSS in UI
                        'created_at' => $row['created_at']
                    ];
                } else {
                    // ✅ SECURE: encoded text
                    $comments[] = [
                        'id'         => (int)$row['id'],
                        'text'       => htmlspecialchars($row['text'], ENT_QUOTES, 'UTF-8'),
                        'created_at' => $row['created_at']
                    ];
                }
            }

            http_response_code(200);
            echo json_encode([
                'error'    => false,
                'type'     => 'store',
                'mode'     => $mode,
                'action'   => 'list',
                'count'    => count($comments),
                'comments' => $comments
            ], JSON_PRETTY_PRINT);
            exit;

        } catch (PDOException $e) {
            http_response_code(500);
            echo json_encode([
                'error'   => true,
                'type'    => 'store',
                'mode'    => $mode,
                'message' => 'Select failed',
                'details' => $e->getMessage()
            ], JSON_PRETTY_PRINT);
            exit;
        }
    }

    // Unsupported method
    http_response_code(405);
    echo json_encode([
        'error'   => true,
        'type'    => 'store',
        'mode'    => $mode,
        'message' => 'Method not allowed'
    ], JSON_PRETTY_PRINT);
    exit;
}

// If type is unknown
http_response_code(400);
echo json_encode([
    'error'   => true,
    'message' => 'Invalid type. Use type=reflect or type=store'
], JSON_PRETTY_PRINT);
