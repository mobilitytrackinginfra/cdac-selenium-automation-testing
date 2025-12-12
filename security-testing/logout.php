<?php
require_once 'config.php';

// Clear session
session_unset();
session_destroy();

// Clear cookies
setcookie('admin', '', time() - 3600, '/');
setcookie('user_data', '', time() - 3600, '/');
setcookie('remember_token', '', time() - 3600, '/');

// Redirect to login
header('Location: login.php');
exit;
?>

