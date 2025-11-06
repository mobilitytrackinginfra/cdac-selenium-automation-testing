<?php
header('Content-Type: text/html; charset=UTF-8');
?><!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Child of B (index 0, id/name=childB1)</title>
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
  <header><h1>Child frame: childB1</h1></header>
  <main>
    <div class="row">
      <button id="b1CounterBtn" data-testid="b1CounterBtn">B1: Increment Counter</button>
      <span>Value: <span id="b1Counter" class="ok">0</span></span>
    </div>
    <div class="row">
      <input id="b1Text" type="text" placeholder="Type inside childB1…" data-testid="b1Text"/>
      <button id="b1EchoBtn" data-testid="b1EchoBtn">Echo</button>
      <span class="muted" id="b1EchoHint">Echo: —</span>
    </div>
    <div class="row">
      <button id="b1AlertBtn" data-testid="b1AlertBtn">Alert B1</button>
      <span class="muted">Use Selenium to click &amp; accept alert.</span>
    </div>
    <div class="row">
      <span class="muted">Use <code>parentFrame()</code> to return to Frame B, or <code>defaultContent()</code> to go to Main.</span>
    </div>
  </main>
  <script>
    let c = 0;
    const btn = document.getElementById('b1CounterBtn');
    const val = document.getElementById('b1Counter');
    const t   = document.getElementById('b1Text');
    const ebtn = document.getElementById('b1EchoBtn');
    const ehint= document.getElementById('b1EchoHint');
    const abtn = document.getElementById('b1AlertBtn');

    btn.addEventListener('click', () => { c++; val.textContent = String(c); });
    ebtn.addEventListener('click', () => { ehint.textContent = 'Echo: ' + t.value; });
    abtn.addEventListener('click', () => { alert('Hello from childB1'); });

    window.addEventListener('message', (e) => {
      if (e.data === 'reset') {
        c = 0; val.textContent = '0'; t.value=''; ehint.textContent='Echo: —';
      }
    });
  </script>
</body>
</html>
