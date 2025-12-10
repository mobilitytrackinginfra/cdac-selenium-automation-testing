<?php
/**
 * api-response-tester.php
 *
 * PHP script to test an API and print details similar to Postman pm.response:
 *
 *  - pm.response.code
 *  - pm.response.status
 *  - pm.response.headers
 *  - pm.response.responseTime
 *  - pm.response.responseSize
 *  - pm.response.text()
 *  - pm.response.json()
 */

// ====== CONFIGURATION ======
$method = 'GET';
$url    = 'http://localhost:80/api-demo/api/books/1'; // <--- change your API URL here
$headers = [
    'Authorization: Bearer test-token',
    'Accept: application/json'
];
$body = null; // for POST/PUT/PATCH set JSON or other payload here

// ====== CURL SETUP ======

// Track start time for responseTime (similar to pm.response.responseTime)
$startTime = microtime(true);

$ch = curl_init();

// We want both headers + body to parse status text, headers, etc.
curl_setopt_array($ch, [
    CURLOPT_URL            => $url,
    CURLOPT_CUSTOMREQUEST  => $method,
    CURLOPT_RETURNTRANSFER => true,
    CURLOPT_HEADER         => true, // important: include headers in the output
    CURLOPT_HTTPHEADER     => $headers,
]);

if ($body !== null) {
    curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
}

$responseRaw = curl_exec($ch);

// Time in ms (similar to pm.response.responseTime)
$endTime = microtime(true);
$responseTimeMs = ($endTime - $startTime) * 1000;

// curl_getinfo gives many values we need
$info = curl_getinfo($ch);
$error = curl_error($ch);

curl_close($ch);

if ($responseRaw === false) {
    header('Content-Type: text/plain; charset=utf-8');
    echo "cURL error: " . $error;
    exit;
}

// ====== SPLIT HEADERS + BODY ======
$headerSize = $info['header_size'];
$headerString = substr($responseRaw, 0, $headerSize);
$bodyString   = substr($responseRaw, $headerSize);

// pm.response.text()  ==> raw body string
$pm_response_text = $bodyString;

// Try to simulate pm.response.json()
$pm_response_json = null;
$decoded = json_decode($bodyString, true);
if (json_last_error() === JSON_ERROR_NONE) {
    $pm_response_json = $decoded;
}

// Parse headers into associative array (simulating pm.response.headers)
$headerLines = preg_split("/\r\n|\n|\r/", trim($headerString));
$statusLine  = array_shift($headerLines); // e.g. HTTP/1.1 200 OK

$headersAssoc = [];
foreach ($headerLines as $line) {
    if (strpos($line, ':') !== false) {
        list($name, $value) = explode(':', $line, 2);
        $name  = trim($name);
        $value = trim($value);
        if (!isset($headersAssoc[$name])) {
            $headersAssoc[$name] = $value;
        } else {
            // handle multiple headers with same name
            if (!is_array($headersAssoc[$name])) {
                $headersAssoc[$name] = [$headersAssoc[$name]];
            }
            $headersAssoc[$name][] = $value;
        }
    }
}

// Extract status code + text from status line
// status line example: HTTP/1.1 200 OK
$statusCode = $info['http_code']; // pm.response.code
$statusText = '';
if (preg_match('#^HTTP/\S+\s+(\d+)\s+(.*)$#i', $statusLine, $m)) {
    $statusCode = (int)$m[1];
    $statusText = $m[2]; // pm.response.status
}

// Response size similar to pm.response.responseSize
// curl_getinfo size_download is body only; header_size we already have
$responseSizeBytes = $info['size_download'] + $info['header_size'];

// ====== OUTPUT IN HTML ======
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Postman pm.response PHP Tester</title>
    <style>
        body      { font-family: Arial, sans-serif; margin: 20px; }
        h1, h2    { margin-bottom: 0.3rem; }
        pre       { background: #f4f4f4; padding: 10px; border-radius: 4px; overflow: auto; }
        table     { border-collapse: collapse; width: 100%; margin-bottom: 20px; }
        th, td    { border: 1px solid #ccc; padding: 6px 8px; text-align: left; font-size: 14px; }
        th        { background: #eee; }
        .label    { font-weight: bold; width: 260px; }
        .value    { font-family: Consolas, monospace; }
        .ok       { color: green; }
        .fail     { color: red; }
    </style>
</head>
<body>
<h1>Postman <code>pm.response</code> – PHP API Tester</h1>

<h2>Request</h2>
<table>
    <tr>
        <td class="label">HTTP Method</td>
        <td class="value"><?= htmlspecialchars($method) ?></td>
    </tr>
    <tr>
        <td class="label">URL</td>
        <td class="value"><?= htmlspecialchars($url) ?></td>
    </tr>
    <tr>
        <td class="label">Request Headers</td>
        <td class="value"><pre><?php print_r($headers); ?></pre></td>
    </tr>
    <tr>
        <td class="label">Request Body</td>
        <td class="value"><pre><?php echo $body === null ? '(null)' : htmlspecialchars($body); ?></pre></td>
    </tr>
</table>

<h2>pm.response properties (simulated)</h2>
<table>
    <tr>
        <th>pm.response.*</th>
        <th>Value from PHP</th>
    </tr>
    <tr>
        <td class="label"><code>pm.response.code</code></td>
        <td class="value"><?= htmlspecialchars((string)$statusCode) ?></td>
    </tr>
    <tr>
        <td class="label"><code>pm.response.status</code></td>
        <td class="value"><?= htmlspecialchars($statusText) ?></td>
    </tr>
    <tr>
        <td class="label"><code>pm.response.responseTime</code> (ms)</td>
        <td class="value"><?= number_format($responseTimeMs, 2) ?></td>
    </tr>
    <tr>
        <td class="label"><code>pm.response.responseSize</code> (bytes)</td>
        <td class="value"><?= htmlspecialchars((string)$responseSizeBytes) ?></td>
    </tr>
    <tr>
        <td class="label"><code>pm.response.headers</code></td>
        <td class="value"><pre><?php print_r($headersAssoc); ?></pre></td>
    </tr>
</table>

<h2>pm.response.text()</h2>
<p>This is equivalent to <code>pm.response.text()</code> in Postman (raw body string).</p>
<pre><?php echo htmlspecialchars($pm_response_text); ?></pre>

<h2>pm.response.json()</h2>
<p>
    Below we try to decode the response as JSON (similar to <code>pm.response.json()</code>).
    If the body is not valid JSON, you'll see <code>null</code>.
</p>
<pre><?php
if ($pm_response_json === null) {
    echo "null (body is not valid JSON or empty)";
} else {
    print_r($pm_response_json);
}
?></pre>

<h2>Raw response (headers + body)</h2>
<pre><?php echo htmlspecialchars($responseRaw); ?></pre>

</body>
</html>
