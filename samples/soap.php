<?php
// soap.php

// Always respond as XML
header('Content-Type: text/xml; charset=utf-8');

// Read raw POST body
$rawPost = file_get_contents("php://input");

// Very simple check: does request contain <GetUser> ?
if (strpos($rawPost, '<GetUser') !== false) {
    // Just return fixed SOAP response
    $response = '<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <GetUserResponse xmlns="http://example.com/soap">
      <User>
        <Id>10</Id>
        <Name>John Soap</Name>
        <Email>john.soap@example.com</Email>
      </User>
    </GetUserResponse>
  </soap:Body>
</soap:Envelope>';
} else {
    $response = '<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <Error>Unknown SOAP action</Error>
  </soap:Body>
</soap:Envelope>';
}

echo $response;
