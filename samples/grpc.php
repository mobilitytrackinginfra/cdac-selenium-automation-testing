<?php
// jsonrpc.php

header('Content-Type: application/json');

$rawBody = file_get_contents("php://input");
$request = json_decode($rawBody, true);

$method = isset($request['method']) ? $request['method'] : null;
$params = isset($request['params']) ? $request['params'] : [];
$id     = isset($request['id']) ? $request['id'] : null;

$response = [
    "jsonrpc" => "2.0",
    "id" => $id
];

if ($method === 'add') {
    $a = isset($params[0]) ? $params[0] : 0;
    $b = isset($params[1]) ? $params[1] : 0;
    $response["result"] = $a + $b;
} elseif ($method === 'getUser') {
    $userId = isset($params['userId']) ? $params['userId'] : null;
    $response["result"] = [
        "id" => $userId,
        "name" => "RPC User",
        "email" => "rpc.user@example.com"
    ];
} else {
    $response["error"] = [
        "code" => -32601,
        "message" => "Method not found"
    ];
}

echo json_encode($response);
