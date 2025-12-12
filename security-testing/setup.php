<?php
/**
 * Database Setup Script
 * Run this once to create the database and tables
 */

$conn = new mysqli('localhost', 'root', '');

// Create database
$sql = "CREATE DATABASE IF NOT EXISTS security_lab";
if ($conn->query($sql) === TRUE) {
    echo "Database created successfully<br>";
} else {
    echo "Error creating database: " . $conn->error . "<br>";
}

$conn->select_db('security_lab');

// Create users table
$sql = "CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) DEFAULT 'user',
    balance DECIMAL(10,2) DEFAULT 1000.00,
    secret_note TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)";
$conn->query($sql);
echo "Users table created<br>";

// Create comments table
$sql = "CREATE TABLE IF NOT EXISTS comments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)";
$conn->query($sql);
echo "Comments table created<br>";

// Create products table
$sql = "CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    price DECIMAL(10,2),
    category VARCHAR(50)
)";
$conn->query($sql);
echo "Products table created<br>";

// Create transactions table
$sql = "CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    from_user INT,
    to_user INT,
    amount DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)";
$conn->query($sql);
echo "Transactions table created<br>";

// Create logs table
$sql = "CREATE TABLE IF NOT EXISTS logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    action VARCHAR(255),
    ip_address VARCHAR(45),
    user_agent TEXT,
    data TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)";
$conn->query($sql);
echo "Logs table created<br>";

// Insert sample users (passwords stored in plain text - vulnerability)
$users = [
    ['admin', 'admin123', 'admin@security-lab.com', 'admin', 5000.00, 'Admin secret: Server password is root123'],
    ['john', 'password', 'john@security-lab.com', 'user', 1500.00, 'My bank PIN is 1234'],
    ['jane', '123456', 'jane@security-lab.com', 'user', 2000.00, 'Secret project codename: Phoenix'],
    ['bob', 'qwerty', 'bob@security-lab.com', 'user', 800.00, 'API key: sk_live_abc123xyz'],
    ['alice', 'letmein', 'alice@security-lab.com', 'moderator', 3000.00, 'Backup codes: 111111, 222222']
];

foreach ($users as $user) {
    $sql = "INSERT INTO users (username, password, email, role, balance, secret_note) 
            VALUES ('{$user[0]}', '{$user[1]}', '{$user[2]}', '{$user[3]}', {$user[4]}, '{$user[5]}')
            ON DUPLICATE KEY UPDATE username=username";
    $conn->query($sql);
}
echo "Sample users inserted<br>";

// Insert sample products
$products = [
    ['Laptop', 'High-performance laptop for professionals', 999.99, 'electronics'],
    ['Smartphone', 'Latest smartphone with advanced features', 699.99, 'electronics'],
    ['Headphones', 'Wireless noise-canceling headphones', 299.99, 'electronics'],
    ['Security Book', 'Web Application Security Guide', 49.99, 'books'],
    ['USB Drive', '256GB encrypted USB drive', 79.99, 'accessories']
];

foreach ($products as $product) {
    $sql = "INSERT INTO products (name, description, price, category) 
            VALUES ('{$product[0]}', '{$product[1]}', {$product[2]}, '{$product[3]}')
            ON DUPLICATE KEY UPDATE name=name";
    $conn->query($sql);
}
echo "Sample products inserted<br>";

// Insert sample comments
$comments = [
    [1, 'Welcome to the security testing lab!'],
    [2, 'This is a great platform for learning.'],
    [3, 'Testing comment functionality.']
];

foreach ($comments as $comment) {
    $sql = "INSERT INTO comments (user_id, comment) VALUES ({$comment[0]}, '{$comment[1]}')";
    $conn->query($sql);
}
echo "Sample comments inserted<br>";

$conn->close();

echo "<br><strong>Setup complete!</strong><br>";
echo "<a href='index.php'>Go to Homepage</a>";
?>

