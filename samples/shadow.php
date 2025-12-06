<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Shadow DOM Test Page</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 40px;
    }
    #message {
      margin-top: 20px;
      font-size: 18px;
      color: green;
      font-weight: bold;
    }
  </style>
</head>
<body>
  <h2>Shadow DOM Example for Selenium</h2>

  <!-- Shadow host -->
  <div id="shadow-host"></div>

  <!-- Message area -->
  <div id="message"></div>

  <script>
    // Attach shadow root to the host
    const host = document.querySelector("#shadow-host");
    const shadowRoot = host.attachShadow({ mode: "open" });

    // Add content inside shadow DOM
    shadowRoot.innerHTML = `
      <style>
        p {
          color: darkblue;
          font-weight: bold;
        }
        button {
          background-color: orange;
          color: black;
          font-size: 16px;
          padding: 8px 16px;
          border: none;
          cursor: pointer;
        }
      </style>
      <p>Hello from Shadow DOM!</p>
      <button id="shadow-btn">Click Me</button>
    `;

    // Add event listener to button inside shadow DOM
    const shadowButton = shadowRoot.querySelector("#shadow-btn");
    shadowButton.addEventListener("click", () => {
      document.getElementById("message").textContent = 
        "✅ You clicked the Shadow DOM button!";
    });
  </script>
</body>
</html>
