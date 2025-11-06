<?php
header('Content-Type: text/html; charset=UTF-8');
?><!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Selenium Frame API Playground (Static Pages)</title>
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <style>
    :root { --b:#186bdd22; --bb:#186bdd; --fg:#222; --muted:#666; --ok:#0a7d00; }
    * { box-sizing: border-box; }
    body { font-family: system-ui,Segoe UI,Roboto,Helvetica,Arial,sans-serif; margin: 0; color: var(--fg); }
    header { padding: 16px 20px; background: #f7f9ff; border-bottom: 2px solid var(--bb); }
    header h1 { margin: 0 0 6px; font-size: 20px; }
    header p { margin: 0; color: var(--muted); font-size: 14px; }
    main { padding: 16px; display: grid; gap: 16px; }
    .grid { display: grid; grid-template-columns: 360px 1fr; gap: 16px; align-items: start; }
    .card { border: 1px solid #e3e6ef; border-radius: 12px; background: white; box-shadow: 0 1px 4px rgba(0,0,0,.04); }
    .card h2 { margin: 0; padding: 12px 14px; font-size: 16px; border-bottom: 1px solid #eef1f6; background: #fbfcff; }
    .card .content { padding: 12px 14px; }
    .legend { font-size: 13px; line-height: 1.5; }
    code.k { padding: 2px 6px; border-radius: 6px; background: #f0f2f8; border: 1px solid #e3e6ef; }
    .layout { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; padding: 12px; }
    .frame-wrap { border: 2px dashed var(--bb); border-radius: 12px; background: var(--b); padding: 8px; }
    .frame-label { font-size: 12px; color: var(--bb); margin: 0 0 6px; font-weight: 700; letter-spacing: .02em; }
    iframe { width: 100%; height: 320px; border: 2px solid var(--bb); border-radius: 10px; background: white; }
    .top-controls { display: grid; gap: 8px; }
    .row { display: flex; gap: 8px; flex-wrap: wrap; align-items: center; }
    button, input[type="text"] {
      font: inherit; padding: 8px 10px; border-radius: 10px; border: 1px solid #d5d9e3; background: #fff;
    }
    button { cursor: pointer; border-color: var(--bb); }
    .pill { display: inline-block; padding: 2px 8px; border-radius: 999px; font-size: 12px; border: 1px solid #e2e6f0; background: #fafbff; }
    .hint { color: var(--muted); font-size: 12px; }
    .ok { color: var(--ok); font-weight: 600; }
    .stack { display: grid; gap: 8px; }
    #cheatsheet { position: sticky; top: 12px; }
  </style>
</head>
<body>
  <header>
    <h1>Frame API Playground for Selenium (Static Pages)</h1>
    <p>This version loads each frame from its own PHP file via <code class="k">src</code> URLs.</p>
  </header>

  <main>
    <div class="grid">
      <section class="card" id="cheatsheet">
        <h2>Cheatsheet</h2>
        <div class="content legend">
          <div class="stack">
            <div>
              <div class="pill">Top-level frames & indexes</div>
              <ul>
                <li><strong>index 0</strong> → <code class="k">#frameA</code> (name: <code class="k">frameA</code>)</li>
                <li><strong>index 1</strong> → <code class="k">#frameB</code> (name: <code class="k">frameB</code>)</li>
              </ul>
            </div>
            <div>
              <div class="pill">Nested inside <code class="k">#frameB</code></div>
              <ul>
                <li><strong>index 0</strong> → <code class="k">#childB1</code> (name: <code class="k">childB1</code>)</li>
              </ul>
            </div>
            <div>
              <div class="pill">Targets to interact</div>
              <ul>
                <li><strong>Main page</strong>: <code class="k">#mainCounterBtn</code>, <code class="k">#mainText</code></li>
                <li><strong>frameA</strong>: <code class="k">#aCounterBtn</code>, <code class="k">#aText</code></li>
                <li><strong>frameB</strong>: <code class="k">#bCounterBtn</code>, <code class="k">#bText</code></li>
                <li><strong>childB1</strong>: <code class="k">#b1CounterBtn</code>, <code class="k">#b1Text</code></li>
              </ul>
            </div>
          </div>
        </div>
      </section>

      <section class="card">
        <h2>Live Playground</h2>
        <div class="content">
          <div class="top-controls">
            <div class="row">
              <button id="mainCounterBtn" data-testid="mainCounterBtn">Main: Increment Counter</button>
              <span>Value: <span id="mainCounter" class="ok">0</span></span>
            </div>
            <div class="row">
              <input id="mainText" type="text" placeholder="Type on MAIN page…" data-testid="mainText"/>
              <button id="mainEchoBtn" data-testid="mainEchoBtn">Echo</button>
              <span class="hint" id="mainEchoHint">Echo: —</span>
            </div>
          </div>

          <div class="layout" aria-label="frames">
            <div class="frame-wrap">
              <div class="frame-label">Top frame index <strong>0</strong> • id/name <code class="k">frameA</code></div>
              <iframe id="frameA" name="frameA" title="frameA" src="frameA.php"></iframe>
            </div>

            <div class="frame-wrap">
              <div class="frame-label">Top frame index <strong>1</strong> • id/name <code class="k">frameB</code> (contains nested <code class="k">childB1</code>)</div>
              <iframe id="frameB" name="frameB" title="frameB" src="frameB.php"></iframe>
            </div>
          </div>

          <div class="row" style="margin-top:10px;">
            <button id="resetAll" data-testid="resetAll">Reset all counters & inputs</button>
            <span class="hint" id="resetStatus">Use Selenium to verify resets across contexts.</span>
          </div>
        </div>
      </section>
    </div>
  </main>

  <script>
    let mainCount = 0;
    const mainCounterBtn = document.getElementById('mainCounterBtn');
    const mainCounter = document.getElementById('mainCounter');
    const mainText = document.getElementById('mainText');
    const mainEchoBtn = document.getElementById('mainEchoBtn');
    const mainEchoHint = document.getElementById('mainEchoHint');
    const resetAll = document.getElementById('resetAll');
    const resetStatus = document.getElementById('resetStatus');

    mainCounterBtn.addEventListener('click', () => { mainCount++; mainCounter.textContent = String(mainCount); });
    mainEchoBtn.addEventListener('click', () => { mainEchoHint.textContent = 'Echo: ' + mainText.value; });

    // Reset signals via postMessage to both frames (and child will be handled by frameB.php)
    resetAll.addEventListener('click', () => {
      mainCount = 0; mainCounter.textContent = '0'; mainText.value=''; mainEchoHint.textContent='Echo: —';
      document.getElementById('frameA').contentWindow.postMessage('reset', '*');
      document.getElementById('frameB').contentWindow.postMessage('reset', '*');
      resetStatus.textContent = 'All reset ✓';
      setTimeout(() => resetStatus.textContent = 'Use Selenium to verify resets across contexts.', 1500);
    });
  </script>
</body>
</html>
