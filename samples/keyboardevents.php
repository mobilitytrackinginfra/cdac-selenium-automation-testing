<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Keyboard Events Demo — Move the Box</title>
<style>
  :root {
    --bg: #0f1220;
    --panel: #161a2b;
    --accent: #4da3ff;
    --accent-2: #00d38d;
    --text: #e8ecf4;
    --muted: #9aa5b1;
  }

  * { box-sizing: border-box; }
  html, body { height: 100%; }
  body {
    margin: 0;
    font-family: system-ui, -apple-system, Segoe UI, Roboto, "Helvetica Neue", Arial, "Noto Sans", "Apple Color Emoji", "Segoe UI Emoji";
    background: radial-gradient(1200px 800px at 10% 10%, #151933, var(--bg));
    color: var(--text);
    display: grid;
    place-items: center;
  }

  .app {
    width: min(1100px, 96vw);
    height: min(660px, 92vh);
    display: grid;
    grid-template-columns: 2fr 1fr;
    gap: 14px;
  }

  .card {
    background: linear-gradient(180deg, rgba(255,255,255,0.04), rgba(255,255,255,0.02));
    border: 1px solid rgba(255,255,255,0.08);
    border-radius: 16px;
    box-shadow: 0 8px 30px rgba(0,0,0,0.35), inset 0 1px 0 rgba(255,255,255,0.05);
    overflow: hidden;
  }

  .header {
    padding: 12px 16px;
    border-bottom: 1px solid rgba(255,255,255,0.08);
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: linear-gradient(180deg, rgba(255,255,255,0.06), rgba(255,255,255,0.02));
  }
  .title {
    font-weight: 700;
    letter-spacing: 0.2px;
  }
  .muted { color: var(--muted); }

  /* Playfield */
  .field {
    position: relative;
    height: calc(100% - 56px);
    padding: 16px;
    display: grid;
    grid-template-rows: auto 1fr;
    gap: 12px;
  }

  .arena {
    position: relative;
    border-radius: 12px;
    border: 1px dashed rgba(255,255,255,0.14);
    background:
      linear-gradient(90deg, rgba(255,255,255,0.03) 1px, transparent 1px) 0 0/40px 40px,
      linear-gradient(0deg, rgba(255,255,255,0.03) 1px, transparent 1px) 0 0/40px 40px,
      rgba(255,255,255,0.02);
    height: 100%;
    overflow: hidden;
  }

  .player {
    position: absolute;
    width: 36px;
    height: 36px;
    border-radius: 8px;
    background: conic-gradient(from 200deg, var(--accent), #9ae6ff 30%, var(--accent) 60%, #9ae6ff);
    box-shadow: 0 6px 18px rgba(77,163,255,0.45), inset 0 0 10px rgba(255,255,255,0.25);
    outline: 2px solid rgba(255,255,255,0.35);
    outline-offset: -3px;
    transform: translate3d(0,0,0);
  }

  .hud {
    display: flex;
    gap: 10px;
    flex-wrap: wrap;
  }
  .pill {
    font: 600 12px/1 system-ui;
    padding: 8px 10px;
    border-radius: 999px;
    background: rgba(255,255,255,0.06);
    border: 1px solid rgba(255,255,255,0.12);
  }
  .pill code { color: #b6cffd; }

  .controls {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  .controls input[type="range"] { width: 180px; }

  /* Log panel */
  .log-wrap {
    height: calc(100% - 56px);
    display: grid;
    grid-template-rows: auto auto 1fr;
  }
  .legend { padding: 12px 16px; border-bottom: 1px solid rgba(255,255,255,0.08); }
  .legend kbd {
    background: #1e233a; border: 1px solid rgba(255,255,255,0.12); border-bottom-width: 2px;
    padding: 2px 6px; border-radius: 6px; font: 600 12px/1.6 ui-monospace, SFMono-Regular, Menlo, Consolas, "Liberation Mono", monospace;
  }
  .log {
    padding: 12px 0 8px 0; overflow: auto; font: 12px/1.45 ui-monospace, SFMono-Regular, Menlo, Consolas, monospace;
  }
  .row {
    display: grid;
    grid-template-columns: 84px 1fr 72px;
    gap: 8px;
    padding: 6px 16px;
    border-bottom: 1px dashed rgba(255,255,255,0.06);
  }
  .row .evt { font-weight: 700; }
  .row .down { color: #9fe4ff; }
  .row .up { color: #9affc8; }
  .row .iso { color: var(--muted); }

  .footer {
    padding: 10px 16px;
    border-top: 1px solid rgba(255,255,255,0.08);
    display: flex; justify-content: space-between; align-items: center;
  }

  .btn {
    background: linear-gradient(180deg, #2a3257, #1a1f36);
    color: var(--text);
    border: 1px solid rgba(255,255,255,0.14);
    padding: 8px 12px;
    border-radius: 10px;
    cursor: pointer;
    font-weight: 700;
  }
  .btn:hover { filter: brightness(1.08); }
  .status-dot {
    width: 8px; height: 8px; border-radius: 50%;
    background: var(--accent-2);
    box-shadow: 0 0 12px var(--accent-2);
    display: inline-block; margin-right: 8px;
  }
</style>
</head>
<body>
  <div class="app">
    <!-- Left: Playfield -->
    <div class="card">
      <div class="header">
        <div class="title">Move the Box — <span class="muted">Keyboard Events Demo</span></div>
        <div class="controls">
          <label class="muted">Speed:
            <input id="speedRange" type="range" min="50" max="600" value="280" />
          </label>
          <button id="resetBtn" class="btn" title="R">Reset</button>
          <button id="pauseBtn" class="btn" title="Space">Pause</button>
        </div>
      </div>

      <div class="field">
        <div class="hud">
          <div class="pill">Pressed: <code id="pressedKeys">(none)</code></div>
          <div class="pill">Last: <code id="lastEvent">—</code></div>
          <div class="pill">Pos: <code id="pos">0, 0</code></div>
          <div class="pill">Tip: Hold <b>Shift</b> to boost</div>
        </div>

        <div id="arena" class="arena" tabindex="0" aria-label="Playfield (focus here to use keys)">
          <div id="player" class="player" style="left: 36px; top: 36px;"></div>
        </div>
      </div>

      <div class="footer">
        <div><span class="status-dot" id="runDot"></span><span id="runState">Running</span></div>
        <div class="muted">Controls: Arrow Keys / WASD • Shift = Boost • Space = Pause/Resume • R = Reset</div>
      </div>
    </div>

    <!-- Right: Event log -->
    <div class="card">
      <div class="header">
        <div class="title">Key Events</div>
        <span class="muted">keydown / keyup logger</span>
      </div>
      <div class="log-wrap">
        <div class="legend">
          <div class="muted">Focus the playfield (click inside box) then use keys.</div>
          <div class="muted" style="margin-top:6px">Examples:
            <kbd>ArrowUp</kbd> <kbd>ArrowLeft</kbd> <kbd>W</kbd> <kbd>A</kbd> <kbd>Shift</kbd> <kbd>Space</kbd> <kbd>R</kbd>
          </div>
        </div>
        <div id="log" class="log" aria-live="polite"></div>
        <div class="footer">
          <button id="clearLog" class="btn">Clear Log</button>
          <span class="muted">Newest on top</span>
        </div>
      </div>
    </div>
  </div>

<script>
(() => {
  const arena = document.getElementById('arena');
  const player = document.getElementById('player');
  const pressedEl = document.getElementById('pressedKeys');
  const lastEventEl = document.getElementById('lastEvent');
  const posEl = document.getElementById('pos');
  const logEl = document.getElementById('log');
  const speedRange = document.getElementById('speedRange');
  const resetBtn = document.getElementById('resetBtn');
  const pauseBtn = document.getElementById('pauseBtn');
  const runState = document.getElementById('runState');
  const runDot = document.getElementById('runDot');
  const clearLogBtn = document.getElementById('clearLog');

  // Keep the page from scrolling on arrow keys when focused in the arena.
  // We also set tabindex on the arena so it can receive focus.
  arena.addEventListener('keydown', (e) => {
    const scrollKeys = ['ArrowUp','ArrowDown','ArrowLeft','ArrowRight',' '];
    if (scrollKeys.includes(e.key)) e.preventDefault();
  });

  // State
  const pressed = new Set();
  const keyAliases = {
    w: 'ArrowUp',
    a: 'ArrowLeft',
    s: 'ArrowDown',
    d: 'ArrowRight'
  };
  const normalizeKey = (key) => {
    const lower = key.length === 1 ? key.toLowerCase() : key;
    return keyAliases[lower] || key;
  };

  let running = true;
  let pos = { x: 36, y: 36 };
  let lastTs = performance.now();
  let baseSpeed = +speedRange.value; // px/sec

  // Helpers
  function setRunning(state) {
    running = state;
    runState.textContent = running ? 'Running' : 'Paused';
    runDot.style.background = running ? '#00d38d' : '#f59f00';
    runDot.style.boxShadow = running ? '0 0 12px #00d38d' : '0 0 12px #f59f00';
    pauseBtn.textContent = running ? 'Pause' : 'Resume';
  }

  function updatePressedLabel() {
    if (!pressed.size) {
      pressedEl.textContent = '(none)';
      return;
    }
    pressedEl.textContent = Array.from(pressed).join(', ');
  }

  function logEvent(type, key, code) {
    const row = document.createElement('div');
    row.className = 'row';

    const evtCell = document.createElement('div');
    evtCell.className = 'evt ' + (type === 'keydown' ? 'down' : 'up');
    evtCell.textContent = type;

    const keyCell = document.createElement('div');
    keyCell.textContent = `${key}  (${code})`;

    const timeCell = document.createElement('div');
    timeCell.className = 'iso';
    timeCell.textContent = new Date().toLocaleTimeString();

    row.append(evtCell, keyCell, timeCell);
    // newest on top
    logEl.prepend(row);
  }

  function clamp(v, min, max) { return Math.max(min, Math.min(max, v)); }

  function setPos(x, y) {
    pos.x = x; pos.y = y;
    player.style.left = `${pos.x}px`;
    player.style.top = `${pos.y}px`;
    posEl.textContent = `${Math.round(pos.x)}, ${Math.round(pos.y)}`;
  }

  // Keyboard handlers
  function onKeyDown(e) {
    const key = normalizeKey(e.key);
    lastEventEl.textContent = `keydown: ${key}`;
    logEvent('keydown', key, e.code);

    if (key === ' ' || key === 'Spacebar' || e.code === 'Space') {
      setRunning(!running);
      return;
    }
    if (key.toLowerCase() === 'r') {
      resetPosition();
      return;
    }

    // Track pressed movement keys
    if (['ArrowUp','ArrowDown','ArrowLeft','ArrowRight','Shift'].includes(key)) {
      pressed.add(key);
      updatePressedLabel();
    }
  }

  function onKeyUp(e) {
    const key = normalizeKey(e.key);
    lastEventEl.textContent = `keyup: ${key}`;
    logEvent('keyup', key, e.code);

    if (pressed.has(key)) {
      pressed.delete(key);
      updatePressedLabel();
    }
  }

  function resetPosition() {
    setPos(36, 36);
  }

  // Game loop
  function loop(now) {
    const dt = (now - lastTs) / 1000; // seconds
    lastTs = now;

    // Compute velocity from pressed keys
    let vx = 0, vy = 0;
    if (pressed.has('ArrowLeft'))  vx -= 1;
    if (pressed.has('ArrowRight')) vx += 1;
    if (pressed.has('ArrowUp'))    vy -= 1;
    if (pressed.has('ArrowDown'))  vy += 1;

    // Normalize for consistent diagonal speed
    let mag = Math.hypot(vx, vy);
    if (mag > 0 && running) {
      vx /= mag; vy /= mag;
      let speed = baseSpeed;
      if (pressed.has('Shift')) speed *= 1.7; // boost
      pos.x += vx * speed * dt;
      pos.y += vy * speed * dt;

      // Keep inside arena bounds
      const rect = arena.getBoundingClientRect();
      const pr = player.getBoundingClientRect();
      // Rects are relative to viewport; convert to inner-box bounds:
      const maxX = rect.width - pr.width - 8;  // some inner padding from border
      const maxY = rect.height - pr.height - 8;
      const minX = 8, minY = 8;

      setPos(clamp(pos.x, minX, maxX), clamp(pos.y, minY, maxY));
    }

    requestAnimationFrame(loop);
  }

  // Wire up
  arena.addEventListener('keydown', onKeyDown);
  arena.addEventListener('keyup', onKeyUp);
  // Also listen on window to catch keyup even if focus slips
  window.addEventListener('keyup', onKeyUp);

  speedRange.addEventListener('input', () => {
    baseSpeed = +speedRange.value;
  });
  resetBtn.addEventListener('click', resetPosition);
  pauseBtn.addEventListener('click', () => setRunning(!running));
  clearLogBtn.addEventListener('click', () => logEl.innerHTML = '');

  // Start
  arena.focus();
  setPos(pos.x, pos.y);
  requestAnimationFrame((t) => { lastTs = t; loop(t); });
})();
</script>
</body>
</html>
