<?php
header('Content-Type: text/html; charset=UTF-8');
?><!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Frame B (index 1, id/name=frameB)</title>
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
    iframe { width: 100%; height: 200px; border: 2px solid #186bdd; border-radius: 10px; }
  </style>
</head>
<body>
  <header><h1>Frame B (contains child)</h1></header>
  <main>
    <div class="row">
      <button id="bCounterBtn" data-testid="bCounterBtn">B: Increment Counter</button>
      <span>Value: <span id="bCounter" class="ok">0</span></span>
    </div>
    <div class="row">
      <input id="bText" type="text" placeholder="Type inside Frame B…" data-testid="bText"/>
      <button id="bEchoBtn" data-testid="bEchoBtn">Echo</button>
      <span class="muted" id="bEchoHint">Echo: —</span>
    </div>
    <hr />
    <div class="row"><span class="muted">Nested frame inside B (index 0):</span></div>
    <iframe id="childB1" name="childB1" title="childB1" src="childB1.php"></iframe>
  </main>
  <script>
    let bCount = 0;
    const bCounterBtn = document.getElementById('bCounterBtn');
    const bCounter = document.getElementById('bCounter');
    const bText = document.getElementById('bText');
    const bEchoBtn = document.getElementById('bEchoBtn');
    const bEchoHint = document.getElementById('bEchoHint');
    const child = document.getElementById('childB1');

    bCounterBtn.addEventListener('click', () => { bCount++; bCounter.textContent = String(bCount); });
    bEchoBtn.addEventListener('click', () => { bEchoHint.textContent = 'Echo: ' + bText.value; });

    // Forward reset to child
    window.addEventListener('message', (e) => {
      if (e.data === 'reset') {
        bCount = 0; bCounter.textContent = '0'; bText.value=''; bEchoHint.textContent='Echo: —';
        try { child.contentWindow.postMessage('reset', '*'); } catch (err) {}
      }
    });
  </script>
</body>
</html>
