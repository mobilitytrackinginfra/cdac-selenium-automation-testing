<?php
// helpers.php

require_once __DIR__ . '/storage.php';

define('AUTH_TOKEN', 'test-token'); // Use in Authorization: Bearer test-token

/** Send JSON response with status and headers. */
function send_json($data, int $status = 200, array $extraHeaders = [])
{
    if (!headers_sent()) {
        http_response_code($status);
        header('Content-Type: application/json');
        header('Access-Control-Allow-Origin: *');
        header('Access-Control-Allow-Headers: Content-Type, Authorization');
        header('Access-Control-Allow-Methods: GET, POST, PUT, PATCH, DELETE, OPTIONS');
        foreach ($extraHeaders as $name => $value) {
            header($name . ': ' . $value);
        }
    }

    echo json_encode($data, JSON_PRETTY_PRINT);
    exit;
}

/** Read JSON body and return as array. On error -> 400. */
function read_json_body(): array
{
    $raw = file_get_contents('php://input');
    if ($raw === false || $raw === '') {
        return [];
    }

    $data = json_decode($raw, true);
    if (json_last_error() !== JSON_ERROR_NONE) {
        send_json([
            'error' => 'Invalid JSON',
            'details' => json_last_error_msg(),
        ], 400);
    }

    return $data ?? [];
}

/** Get bearer token from Authorization header. */
function get_auth_token(): ?string
{
    $header = $_SERVER['HTTP_AUTHORIZATION'] ?? '';

    // Fallback for some servers
    if (!$header && function_exists('getallheaders')) {
        $headers = getallheaders();
        if (isset($headers['Authorization'])) {
            $header = $headers['Authorization'];
        }
    }

    if (!$header) {
        return null;
    }

    if (stripos($header, 'Bearer ') === 0) {
        return trim(substr($header, 7));
    }

    return null;
}

/** Require valid auth token, otherwise 401/403. */
function require_auth()
{
    $token = get_auth_token();

    if (!$token) {
        send_json([
            'error' => 'Missing Authorization header',
            'hint'  => 'Use: Authorization: Bearer ' . AUTH_TOKEN,
        ], 401);
    }

    if ($token !== AUTH_TOKEN) {
        send_json([
            'error' => 'Invalid token',
        ], 403);
    }
}

/** Validate book data. Returns [bool $ok, array $errors]. */
function validate_book(array $data, bool $partial = false): array
{
    $errors = [];

    // Required fields for full create/update
    if (!$partial) {
        if (!isset($data['title']) || trim($data['title']) === '') {
            $errors['title'] = 'Title is required.';
        }
        if (!isset($data['author']) || trim($data['author']) === '') {
            $errors['author'] = 'Author is required.';
        }
        if (!isset($data['price'])) {
            $errors['price'] = 'Price is required.';
        }
    }

    // If fields are present, validate types/constraints
    if (isset($data['title']) && strlen($data['title']) > 200) {
        $errors['title'] = 'Title is too long (max 200 chars).';
    }

    if (isset($data['price'])) {
        if (!is_numeric($data['price']) || $data['price'] < 0) {
            $errors['price'] = 'Price must be a positive number.';
        }
    }

    if (isset($data['stock'])) {
        if (!is_numeric($data['stock']) || $data['stock'] < 0) {
            $errors['stock'] = 'Stock must be a non-negative integer.';
        }
    }

    return [empty($errors), $errors];
}

/** Helper for 404. */
function not_found($message = 'Not found')
{
    send_json(['error' => $message], 404);
}

/** Helper for 405. */
function method_not_allowed(array $allowed)
{
    $allowHeader = implode(', ', $allowed);
    send_json(
        ['error' => 'Method not allowed', 'allowed' => $allowed],
        405,
        ['Allow' => $allowHeader]
    );
}

/** Simulate random flaky responses for testing retries. */
function flaky_response()
{
    if (rand(1, 3) === 1) { // 1/3 times
        send_json([
            'error' => 'Random internal server error',
        ], 500);
    }

    send_json([
        'status' => 'ok',
        'message' => 'Flaky endpoint succeeded this time.',
    ]);
}

/** Simulate slow response. */
function slow_response()
{
    sleep(3); // 3 seconds delay
    send_json([
        'status'   => 'ok',
        'message'  => 'Slow endpoint finished after delay.',
        'responseTimeSeconds' => 3,
    ]);
}
