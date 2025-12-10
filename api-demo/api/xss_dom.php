<?php
// xss_dom.php
// DOM-Based XSS demo: vulnerable vs secure based on ?mode=

$mode = isset($_GET['mode']) ? $_GET['mode'] : 'vulnerable';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>DOM XSS Demo (<?php echo htmlspecialchars($mode, ENT_QUOTES, 'UTF-8'); ?>)</title>
</head>
<body>
    <h1>DOM XSS Demo (Mode: <?php echo htmlspecialchars($mode, ENT_QUOTES, 'UTF-8'); ?>)</h1>

    <p>
        This page reads the <code>#</code> fragment from URL (location.hash)
        and shows it below as a greeting.
    </p>

    <p>Try URL like:</p>
    <pre>
        xss_dom.php?mode=vulnerable#&lt;script&gt;alert('DOM XSS')&lt;/script&gt;
    </pre>

    <h2>Hello, <span id="greeting"></span></h2>

    <script>
        var hash = window.location.hash;          // e.g. "#Swapnil"
        var name = hash ? hash.substring(1) : "Guest";

        <?php if ($mode === 'vulnerable'): ?>
        // ❌ VULNERABLE: innerHTML with untrusted data
        document.getElementById("greeting").innerHTML = name;
        <?php else: ?>
        // ✅ SECURE: textContent, does not interpret HTML
        document.getElementById("greeting").textContent = name;
        <?php endif; ?>
    </script>
</body>
</html>
