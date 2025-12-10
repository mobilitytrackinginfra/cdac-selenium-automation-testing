<?php
// rest.php

header('Content-Type: application/json');

// Allow CORS for testing (optional)
header('Access-Control-Allow-Origin: *');

// Get HTTP method
$method = $_SERVER['REQUEST_METHOD'];

if ($method === 'GET') {
    // Example: http://localhost/rest.php?userId=10
    $userId = isset($_GET['userId']) ? $_GET['userId'] : null;

    echo json_encode([
        "status" => "success",
        "type" => "REST",
        "method" => "GET",
        "message" => "Fetched user",
        "user" => [
            "id" => $userId,
            "name" => "John Doe",
            "email" => "john@example.com"
        ]
    ]);
} elseif ($method === 'POST') {
    // Read JSON body
    $rawBody = file_get_contents("php://input");
    $data = json_decode($rawBody, true);

    echo json_encode([
        "status" => "success",
        "type" => "REST",
        "method" => "POST",
        "message" => "User created",
        "receivedData" => $data
    ]);
} else {
    // Other methods
    http_response_code(405);
    echo json_encode([
        "status" => "error",
        "message" => "Method not allowed"
    ]);
}
