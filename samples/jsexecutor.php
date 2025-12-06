<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>JSExecutor Test Page</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 20px; }
    #log { border: 1px solid #ccc; padding: 10px; height: 120px; overflow:auto; }
    .big { height: 1000px; background: linear-gradient(#f8f8f8,#e8e8ff); }
    .hidden { display:none; }
    .red { color: red; }
    .box { padding: 10px; margin: 6px 0; border: 1px dashed #aaa; }
  </style>
</head>
<body>
  <h1>JavascriptExecutor Demo</h1>

  <div class="box">
    <label for="textInput">Text input:</label>
    <input id="textInput" type="text" value="initial" />
    <button id="btnChange">Change text to "changed"</button>
  </div>

  <div class="box">
    <button id="btnHidden" class="hidden">Hidden button</button>
    <button id="btnShow">Show hidden button</button>
  </div>

  <div class="box">
    <a id="externalLink" href="https://example.com" target="_blank">Open Example.com</a>
  </div>

  <div class="box">
    <div id="scrollTarget" style="margin-top:800px; padding:10px; border:1px solid #222;">
      Scroll target (far below)
    </div>
    <div class="big"></div>
  </div>

  <div class="box">
    <ul id="items">
      <li data-id="1">One</li>
      <li data-id="2">Two</li>
      <li data-id="3">Three</li>
    </ul>
  </div>

  <div class="box">
    <button id="btnEvent">Click me (event will listen)</button>
  </div>

  <div class="box">
    <div id="promiseResult">Promise result: <span id="promiseVal">none</span></div>
  </div>

  <h3>Console log panel</h3>
  <div id="log"></div>

  <script>
    // helper to append to #log
    function log(msg) {
      const l = document.getElementById('log');
      l.innerHTML += (new Date()).toLocaleTimeString() + ' — ' + msg + '<br>';
      l.scrollTop = l.scrollHeight;
      console.log(msg);
    }

    document.getElementById('btnChange').addEventListener('click', function() {
      document.getElementById('textInput').value = 'changed';
      log('btnChange clicked — input changed');
    });

    document.getElementById('btnShow').addEventListener('click', function() {
      const h = document.getElementById('btnHidden');
      h.classList.remove('hidden');
      log('btnShow clicked — hidden button shown');
    });

    document.getElementById('btnEvent').addEventListener('click', function() {
      log('btnEvent clicked — event fired normally');
    });

    // Example: a function that returns a Promise (simulate network)
    function delayedValue(ms, value) {
      return new Promise(resolve => {
        setTimeout(() => {
          resolve(value);
        }, ms);
      });
    }
  </script>
</body>
</html>
