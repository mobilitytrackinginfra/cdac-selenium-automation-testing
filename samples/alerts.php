<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Button Alert Example</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      height: 100vh;
      background-color: #f2f2f2;
    }
    button {
      padding: 10px 20px;
      margin: 10px;
      font-size: 16px;
      border: none;
      border-radius: 8px;
      background-color: #186BDD;
      color: white;
      cursor: pointer;
    }
    button:hover {
      background-color: #155bb8;
    }
  </style>
</head>
<body>

  <button id="simpleBtn">Show Alert</button>
  <button id="promptBtn">Show Input Alert</button>

  <script>
    document.getElementById('simpleBtn').addEventListener('click', () => {
      alert('Simple alert');
    });

    document.getElementById('promptBtn').addEventListener('click', () => {
      const val = prompt('Enter something:');
      if (val !== null) {
        alert('You entered: ' + val);
      } else {
        alert('Cancelled.');
      }
    });
  </script>

</body>
</html>
