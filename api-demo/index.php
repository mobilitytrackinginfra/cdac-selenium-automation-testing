<?php
// index.php

require_once __DIR__ . '/helpers.php';

// Handle CORS preflight quickly
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Headers: Content-Type, Authorization');
    header('Access-Control-Allow-Methods: GET, POST, PUT, PATCH, DELETE, OPTIONS');
    http_response_code(204);
    exit;
}

$method = $_SERVER['REQUEST_METHOD'];
$fullPath = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);

// Detect base path (e.g. "/api-demo") and strip it
$scriptDir = rtrim(dirname($_SERVER['SCRIPT_NAME']), '/');
$path = $fullPath;

if ($scriptDir !== '' && $scriptDir !== '/') {
    if (strpos($fullPath, $scriptDir) === 0) {
        $path = substr($fullPath, strlen($scriptDir));
    }
}

if ($path === '' || $path === false) {
    $path = '/';
}

/**
 * ROUTES
 *
 * We handle a bunch of scenarios:
 * - /api/login              POST      -> Fake login, returns token
 * - /api/books              GET/POST  -> List, create
 * - /api/books/{id}         GET/PUT/PATCH/DELETE
 * - /api/books/reset        POST      -> Reset in-memory data
 * - /api/rate-limited       GET       -> Simulate 429
 * - /api/flaky              GET       -> Random 500 vs 200
 * - /api/slow               GET       -> Slow response (timeout test)
 * - /api/error/{code}       GET       -> Force specific HTTP code
 * - /api/upload             POST      -> File upload simulation
 */

switch (true) {

    // Root help
    case $path === '/' && $method === 'GET':
        send_json([
            'message' => 'In-memory API test server',
            'endpoints' => [
                'POST /api/login',
                'GET  /api/books',
                'POST /api/books',
                'GET  /api/books/{id}',
                'PUT  /api/books/{id}',
                'PATCH /api/books/{id}',
                'DELETE /api/books/{id}',
                'POST /api/books/reset',
                'GET  /api/rate-limited',
                'GET  /api/flaky',
                'GET  /api/slow',
                'GET  /api/error/{code}',
                'POST /api/upload',
            ],
        ]);

    // ------------------ AUTH ------------------ //
    case $path === '/api/login' && $method === 'POST':
        handle_login();

    // ------------------ BOOKS COLLECTION ------------------ //
    case $path === '/api/books' && $method === 'GET':
        require_auth();
        handle_list_books();

    case $path === '/api/books' && $method === 'POST':
        require_auth();
        handle_create_book();

    // ------------------ BOOKS SINGLE ------------------ //
    case preg_match('#^/api/books/(\d+)$#', $path, $m):
        require_auth();
        $id = (int) $m[1];
        handle_single_book($method, $id);

    // ------------------ RESET STORE ------------------ //
    case $path === '/api/books/reset' && $method === 'POST':
        reset_store();
        reset_rate_counter();
        send_json(['status' => 'reset-ok']);

    // ------------------ RATE LIMIT SIM ------------------ //
    case $path === '/api/rate-limited' && $method === 'GET':
        require_auth();
        handle_rate_limited();

    // ------------------ FLAKY ENDPOINT ------------------ //
    case $path === '/api/flaky' && $method === 'GET':
        require_auth();
        flaky_response();

    // ------------------ SLOW ENDPOINT ------------------ //
    case $path === '/api/slow' && $method === 'GET':
        require_auth();
        slow_response();

    // ------------------ ERROR ENDPOINT ------------------ //
    case preg_match('#^/api/error/(\d{3})$#', $path, $m) && $method === 'GET':
        require_auth();
        handle_forced_error((int) $m[1]);

    // ------------------ FILE UPLOAD ------------------ //
    case $path === '/api/upload' && $method === 'POST':
        require_auth();
        handle_upload();

	// ------------------ FILE DOWNLOAD ------------------ //
    case preg_match('#^/api/files/([^/]+)$#', $path, $m) && $method === 'GET':
        require_auth();
        handle_download($m[1]);

    default:
        not_found('Route not found: ' . $path);
}

// --------------- HANDLER FUNCTIONS ---------------- //

function handle_login()
{
    $body = read_json_body();

    // Simulate validation errors
    if (empty($body['username']) || empty($body['password'])) {
        send_json([
            'error' => 'Validation failed',
            'fields' => [
                'username' => empty($body['username']) ? 'Required' : null,
                'password' => empty($body['password']) ? 'Required' : null,
            ],
        ], 422);
    }

    // Fake auth – any non-empty username/password works
    send_json([
        'token'    => AUTH_TOKEN,
        'user'     => [
            'username' => $body['username'],
            'roles'    => ['ROLE_USER'],
        ],
        'expiresInSeconds' => 3600,
    ], 200);
}

function handle_list_books()
{
    $books = list_all_books();

    // Filtering via query params
    $author   = $_GET['author']   ?? null;
    $search   = $_GET['search']   ?? null;
    $minPrice = $_GET['minPrice'] ?? null;
    $maxPrice = $_GET['maxPrice'] ?? null;

    $filtered = array_filter($books, function ($book) use ($author, $search, $minPrice, $maxPrice) {
        if ($author && stripos($book['author'], $author) === false) {
            return false;
        }
        if ($search && stripos($book['title'], $search) === false) {
            return false;
        }
        if ($minPrice !== null && $book['price'] < (float) $minPrice) {
            return false;
        }
        if ($maxPrice !== null && $book['price'] > (float) $maxPrice) {
            return false;
        }

        return true;
    });

    // Sorting (optional)
    $sort = $_GET['sort'] ?? null; // e.g. "price" or "-price"
    if ($sort) {
        $desc = false;
        if ($sort[0] === '-') {
            $desc = true;
            $sort = substr($sort, 1);
        }

        usort($filtered, function ($a, $b) use ($sort, $desc) {
            $av = $a[$sort] ?? null;
            $bv = $b[$sort] ?? null;

            if ($av == $bv) return 0;
            $result = ($av < $bv) ? -1 : 1;
            return $desc ? -$result : $result;
        });
    }

    // Pagination
    $page    = max(1, (int) ($_GET['page'] ?? 1));
    $perPage = max(1, min(50, (int) ($_GET['perPage'] ?? 5)));

    $total       = count($filtered);
    $totalPages  = (int) ceil($total / $perPage);
    $offset      = ($page - 1) * $perPage;
    $pagedItems  = array_slice($filtered, $offset, $perPage);

    send_json([
        'data' => array_values($pagedItems),
        'meta' => [
            'total'      => $total,
            'page'       => $page,
            'perPage'    => $perPage,
            'totalPages' => $totalPages,
        ],
    ]);
}

function handle_create_book()
{
    $data = read_json_body();

    // Simulate conflict (e.g. same title)
    foreach (list_all_books() as $b) {
        if (isset($data['title']) && strcmp($b['title'], $data['title']) === 0) {
            send_json([
                'error' => 'Book already exists with same title',
            ], 409);
        }
    }

    [$ok, $errors] = validate_book($data, false);
    if (!$ok) {
        send_json([
            'error'  => 'Validation failed',
            'fields' => $errors,
        ], 422);
    }

    $book = create_book_in_store($data);
    send_json($book, 201);
}

function handle_single_book($method, int $id)
{
    switch ($method) {
        case 'GET':
            $book = find_book($id);
            if (!$book) {
                not_found('Book not found');
            }
            send_json($book);
            break;

        case 'PUT':
            $data = read_json_body();
            [$ok, $errors] = validate_book($data, false);
            if (!$ok) {
                send_json(['error' => 'Validation failed', 'fields' => $errors], 422);
            }

            $book = update_book_in_store($id, $data, false);
            if (!$book) {
                not_found('Book not found');
            }
            send_json($book);
            break;

        case 'PATCH':
            $data = read_json_body();
            [$ok, $errors] = validate_book($data, true);
            if (!$ok) {
                send_json(['error' => 'Validation failed', 'fields' => $errors], 422);
            }

            $book = update_book_in_store($id, $data, true);
            if (!$book) {
                not_found('Book not found');
            }
            send_json($book);
            break;

        case 'DELETE':
            $deleted = delete_book_in_store($id);
            if (!$deleted) {
                not_found('Book not found');
            }
            // 204 No Content
            if (!headers_sent()) {
                http_response_code(204);
            }
            exit;

        default:
            method_not_allowed(['GET', 'PUT', 'PATCH', 'DELETE']);
    }
}

function handle_rate_limited()
{
    $count = increment_rate_counter();
    $limit = 5;

    if ($count > $limit) {
        send_json(
            [
                'error'      => 'Too Many Requests',
                'limit'      => $limit,
                'used'       => $count,
                'resetHint'  => 'POST /api/books/reset',
            ],
            429,
            ['Retry-After' => 10]
        );
    }

    send_json([
        'status' => 'ok',
        'used'   => $count,
        'limit'  => $limit,
    ]);
}

function handle_forced_error(int $code)
{
    $allowed = [400, 401, 403, 404, 409, 500];
    if (!in_array($code, $allowed, true)) {
        send_json([
            'error'  => 'Unsupported error code in demo',
            'allowedCodes' => $allowed,
        ], 400);
    }

    $messages = [
        400 => 'Bad Request demo',
        401 => 'Unauthorized demo',
        403 => 'Forbidden demo',
        404 => 'Not Found demo',
        409 => 'Conflict demo',
        500 => 'Internal Server Error demo',
    ];

    send_json([
        'error'   => $messages[$code],
        'code'    => $code,
        'demo'    => true,
    ], $code);
}

function handle_upload()
{
    if (empty($_FILES['file'])) {
        send_json([
            'error' => 'No file uploaded. Use key "file".',
        ], 400);
    }

    $file = $_FILES['file'];

    if ($file['error'] !== UPLOAD_ERR_OK) {
        send_json([
            'error' => 'Upload failed',
            'code'  => $file['error'],
        ], 400);
    }

    // Create uploads directory if not exists
    $uploadDir = __DIR__ . '/uploads';
    if (!is_dir($uploadDir)) {
        mkdir($uploadDir, 0777, true);
    }

    // Generate safe filename
    $baseName = basename($file['name']); // strip any path
    $targetPath = $uploadDir . '/' . $baseName;

    // If file exists, add a suffix
    $i = 1;
    $pathInfo = pathinfo($baseName);
    $namePart = $pathInfo['filename'] ?? $baseName;
    $extPart  = isset($pathInfo['extension']) && $pathInfo['extension'] !== ''
        ? '.' . $pathInfo['extension']
        : '';

    while (file_exists($targetPath)) {
        $newName   = $namePart . "_{$i}" . $extPart;
        $targetPath = $uploadDir . '/' . $newName;
        $i++;
    }

    // Move from temp to uploads
    if (!move_uploaded_file($file['tmp_name'], $targetPath)) {
        send_json([
            'error' => 'Failed to move uploaded file',
        ], 500);
    }

    send_json([
        'status'      => 'ok',
        'originalName'=> $file['name'],
        'savedAs'     => basename($targetPath),
        'size'        => $file['size'],
        'type'        => $file['type'],
        'path'        => $targetPath,
        'downloadUrl' => '/api/files/' . rawurlencode(basename($targetPath)),
    ], 201);
}


function handle_download(string $filename)
{
    // Basic safety: prevent ../ attacks
    $safeName = basename($filename);

    $filePath = __DIR__ . '/uploads/' . $safeName;

    if (!file_exists($filePath) || !is_file($filePath)) {
        not_found('File not found');
    }

    // Try to guess MIME type
    $mimeType = 'application/octet-stream';
    if (function_exists('mime_content_type')) {
        $detected = mime_content_type($filePath);
        if ($detected !== false) {
            $mimeType = $detected;
        }
    }

    // Clear any previous output buffer
    if (ob_get_level()) {
        ob_end_clean();
    }

    // Send download headers
    header('Content-Description: File Transfer');
    header('Content-Type: ' . $mimeType);
    header('Content-Length: ' . filesize($filePath));
    header('Content-Disposition: attachment; filename="' . rawbasename($safeName) . '"');
    header('Cache-Control: no-cache, must-revalidate');
    header('Pragma: public');

    // Output file contents
    readfile($filePath);
    exit;
}

/**
 * rawbasename — like basename but avoids messing with multibyte characters.
 */
function rawbasename(string $name): string
{
    // Very simple wrapper; you can just use basename($name) if you want
    return basename($name);
}
