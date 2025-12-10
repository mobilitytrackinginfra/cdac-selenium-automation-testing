<?php
// graphql.php

header('Content-Type: application/json');

$rawBody = file_get_contents("php://input");
$input = json_decode($rawBody, true);

$query = isset($input['query']) ? $input['query'] : '';

$resultData = [];

// Super simple "parser": we just check if query contains certain words
if (strpos($query, 'user') !== false) {
    $resultData['user'] = [
        "id" => 1,
        "name" => "GraphQL User",
        "email" => "graphql.user@example.com"
    ];
} elseif (strpos($query, 'product') !== false) {
    $resultData['product'] = [
        "id" => 100,
        "name" => "Demo Product",
        "price" => 999
    ];
} else {
    $resultData['message'] = "Unknown query";
}

echo json_encode([
    "data" => $resultData
]);
