<?php
header('Content-Type: text/html; charset=UTF-8');
?><!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Frame A (index 0, id/name=frameA)</title>
  <style>
    :root { --bb:#186bdd; --muted:#666; }
    body { margin: 0; font-family: system-ui,Segoe UI,Roboto,Helvetica,Arial,sans-serif; }
    header { padding: 10px 12px; background: #f7faff; border-bottom: 2px solid var(--bb); }
    h1 { margin:0; font-size: 16px; }
    main { padding: 10px 12px; display: grid; gap: 8px; }
    .row { display: flex; gap: 8px; align-items: center; flex-wrap: wrap; }
    button, input[type="text"] { font: inherit; padding: 7px 9px; border-radius: 10px; border: 1px solid #d5d9e3; }
    button { border-color: var(--bb); cursor: pointer; }
    .muted { color: var(--muted); font-size: 12px; }
    .ok { color: #0a7d00; font-weight: 600; }
  </style>
</head>
<body>
  <header><h1>Frame A</h1></header>
  <main>
    <div class="row">
      <button id="aCounterBtn" data-testid="aCounterBtn">A: Increment Counter</button>
      <span>Value: <span id="aCounter" class="ok">0</span></span>
    </div>
    <div class="row">
      <input id="aText" type="text" placeholder="Type inside Frame A…" data-testid="aText"/>
      <button id="aEchoBtn" data-testid="aEchoBtn">Echo</button>
      <span class="muted" id="aEchoHint">Echo: —</span>
    </div>
    <div class="row">
      <button id="aAlertBtn" data-testid="aAlertBtn">Alert A</button>
      <span class="muted">Use Selenium to click &amp; accept alert.</span>
    </div>
  </main>
  <script>
    let aCount = 0;
    const aCounterBtn = document.getElementById('aCounterBtn');
    const aCounter = document.getElementById('aCounter');
    const aText = document.getElementById('aText');
    const aEchoBtn = document.getElementById('aEchoBtn');
    const aEchoHint = document.getElementById('aEchoHint');
    const aAlertBtn = document.getElementById('aAlertBtn');

    aCounterBtn.addEventListener('click', () => { aCount++; aCounter.textContent = String(aCount); });
    aEchoBtn.addEventListener('click', () => { aEchoHint.textContent = 'Echo: ' + aText.value; });
    aAlertBtn.addEventListener('click', () => { alert('Hello from Frame A'); });

    window.addEventListener('message', (e) => {
      if (e.data === 'reset') {
        aCount = 0; aCounter.textContent = '0'; aText.value=''; aEchoHint.textContent='Echo: —';
      }
    });
  </script>
</body>
</html>
