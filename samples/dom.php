<!DOCTYPE html>
<html>
<head>
    <title>Default Text Field Test</title>
</head>
<body>

<h2>Default Value Text Field</h2>

<!-- Default value set in HTML attribute -->
<input id="username" type="text" value="Username">

<!-- Button that modifies the value using JavaScript -->
<button onclick="document.getElementById('username').value = 'ChangedByJS'">
    Change Value via JS
</button>

</body>
</html>
