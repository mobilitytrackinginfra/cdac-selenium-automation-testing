<?php
require_once 'config.php';

$conn = getConnection();
$format = $_GET['format'] ?? 'json';
$type = $_GET['type'] ?? 'users';

// VULNERABLE: No authorization, exports sensitive data
$data = [];

switch ($type) {
    case 'users':
        $result = $conn->query("SELECT * FROM users");
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }
        break;
    case 'products':
        $result = $conn->query("SELECT * FROM products");
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }
        break;
    case 'transactions':
        $result = $conn->query("SELECT * FROM transactions");
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }
        break;
    case 'logs':
        $result = $conn->query("SELECT * FROM logs");
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }
        break;
    case 'all':
        // Export everything
        $tables = ['users', 'products', 'transactions', 'logs', 'comments'];
        foreach ($tables as $table) {
            $result = $conn->query("SELECT * FROM $table");
            $data[$table] = [];
            while ($row = $result->fetch_assoc()) {
                $data[$table][] = $row;
            }
        }
        break;
}

$conn->close();

// Output based on format
switch ($format) {
    case 'json':
        header('Content-Type: application/json');
        echo json_encode($data, JSON_PRETTY_PRINT);
        break;
        
    case 'csv':
        header('Content-Type: text/csv');
        header('Content-Disposition: attachment; filename="export.csv"');
        if (!empty($data) && isset($data[0])) {
            $fp = fopen('php://output', 'w');
            fputcsv($fp, array_keys($data[0]));
            foreach ($data as $row) {
                fputcsv($fp, $row);
            }
            fclose($fp);
        }
        break;
        
    case 'xml':
        header('Content-Type: application/xml');
        echo '<?xml version="1.0" encoding="UTF-8"?>';
        echo '<data>';
        foreach ($data as $item) {
            echo '<item>';
            foreach ($item as $key => $value) {
                echo "<$key>" . htmlspecialchars($value) . "</$key>";
            }
            echo '</item>';
        }
        echo '</data>';
        break;
        
    default:
        header('Content-Type: text/plain');
        print_r($data);
        break;
}
?>

