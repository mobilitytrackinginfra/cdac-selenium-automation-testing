<?php
/**
 * Database Configuration
 * Security Testing Lab - Intentionally Vulnerable
 */

// Database credentials - intentionally exposed for testing
define('DB_HOST', 'localhost');
define('DB_USER', 'root');
define('DB_PASS', '');
define('DB_NAME', 'security_lab');

// Create database connection
function getConnection() {
    $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }
    return $conn;
}

// Session configuration - weak for testing
session_start();

// Error reporting - enabled for testing (security misconfiguration)
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Secret key - hardcoded (vulnerability)
define('SECRET_KEY', 'supersecretkey123');
define('ADMIN_TOKEN', 'admin_token_12345');
?>

