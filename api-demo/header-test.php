<?php
// header-tester.php
// Simple API to inspect which headers actually reached the server
// Use this from Postman to verify pm.request.headers.add / remove / upsert

// Always return JSON
header('Content-Type: application/json');

/**
 * Get request headers in a portable way
 */
function getRequestHeaders(): array {
    if (function_exists('getallheaders')) {
        $headers = getallheaders();
    } else {
        // Fallback for servers without getallheaders (e.g. some PHP-FPM setups)
        $headers = [];
        foreach ($_SERVER as $name => $value) {
            if (strpos($name, 'HTTP_') === 0) {
                $key = str_replace(' ', '-', ucwords(strtolower(str_replace('_', ' ', substr($name, 5)))));
                $headers[$key] = $value;
            }
        }
    }
    return $headers;
}

$headers = getRequestHeaders();

// Build a lowercase map for easy lookups
$normalized = [];
foreach ($headers as $key => $value) {
    $normalized[strtolower($key)] = $value;
}

// These are special headers we’ll use in Postman to test add/upsert/remove:
//   X-Pm-Add    -> for testing pm.request.headers.add
//   X-Pm-Upsert -> for testing pm.request.headers.upsert
//   X-Pm-Remove -> for testing pm.request.headers.remove
$response = [
    'request' => [
        'method' => $_SERVER['REQUEST_METHOD'] ?? null,
        'uri'    => $_SERVER['REQUEST_URI'] ?? null,
    ],
    'receivedHeaders' => $headers,          // Raw headers as PHP sees them
    'checks' => [
        'has_x_pm_add'           => array_key_exists('x-pm-add', $normalized),
        'x_pm_add_value'         => $normalized['x-pm-add'] ?? null,

        'has_x_pm_upsert'        => array_key_exists('x-pm-upsert', $normalized),
        'x_pm_upsert_value'      => $normalized['x-pm-upsert'] ?? null,

        'has_x_pm_remove'        => array_key_exists('x-pm-remove', $normalized),
        'x_pm_remove_value'      => $normalized['x-pm-remove'] ?? null,
    ],
    'serverTime' => date('c'),
];

http_response_code(200);
echo json_encode($response, JSON_PRETTY_PRINT);
