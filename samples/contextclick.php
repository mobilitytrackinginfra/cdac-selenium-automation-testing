<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1" />
<title>Custom Right-Click Context Menu</title>
<style>
  :root {
    --menu-bg: #fff;
    --menu-border: #d7d7d7;
    --menu-shadow: 0 8px 24px rgba(0,0,0,0.12);
    --menu-item-hover: #f3f4f6;
    --menu-item-active: #e5e7eb;
    --menu-text: #111827;
    --menu-sep: #e5e7eb;
    --radius: 12px;
  }

  body {
    font-family: system-ui, -apple-system, Segoe UI, Roboto, "Helvetica Neue", Arial, Noto Sans, "Apple Color Emoji", "Segoe UI Emoji";
    margin: 0;
    min-height: 100dvh;
    background: #fafafa;
    color: #111827;
  }

  .playground {
    display: grid;
    place-items: center;
    min-height: 100dvh;
    padding: 32px;
  }

  .card {
    width: min(720px, 90vw);
    background: white;
    box-shadow: 0 10px 30px rgba(0,0,0,.08);
    border-radius: 16px;
    padding: 28px;
    border: 1px solid #eee;
  }

  .card h1 { margin: 0 0 8px; font-size: 22px; }
  .card p { margin: 0 0 16px; color: #4b5563; }
  .box {
    border: 2px dashed #c7d2fe;
    background: #eef2ff;
    border-radius: 14px;
    padding: 32px;
    text-align: center;
    color: #3730a3;
    user-select: none;
  }
  .hint {
    margin-top: 8px;
    font-size: 13px;
    color: #6b7280;
  }

  /* Context menu */
  .ctxmenu {
    position: fixed;
    z-index: 9999;
    min-width: 220px;
    background: var(--menu-bg);
    border: 1px solid var(--menu-border);
    border-radius: var(--radius);
    box-shadow: var(--menu-shadow);
    padding: 8px;
    display: none;
  }

  .ctxmenu[aria-hidden="false"] { display: block; }

  .ctxmenu .sep {
    height: 1px;
    background: var(--menu-sep);
    margin: 6px 6px;
  }

  .ctxmenu button {
    width: 100%;
    display: flex;
    gap: 10px;
    align-items: center;
    text-align: left;
    padding: 10px 12px;
    border: 0;
    background: transparent;
    border-radius: 8px;
    color: var(--menu-text);
    font: inherit;
    cursor: pointer;
  }

  .ctxmenu button:hover { background: var(--menu-item-hover); }
  .ctxmenu button:active { background: var(--menu-item-active); }
  .ctxmenu kbd {
    margin-left: auto;
    font-size: 11px;
    background: #f3f4f6;
    border: 1px solid #e5e7eb;
    padding: 2px 6px;
    border-radius: 6px;
    color: #6b7280;
  }
</style>
</head>
<body>
  <div class="playground" id="playground">
    <div class="card" tabindex="0" aria-label="Right-click demo area">
      <h1>Right-click anywhere inside this card</h1>
      <p>This demo shows a custom context menu. Try right-clicking or using the keyboard Context Menu key (or Shift+F10).</p>
      <div class="box">Right-click here ⤵</div>
      <div class="hint">Tip: The menu will never overflow off-screen.</div>
    </div>
  </div>

  <!-- Context Menu -->
  <div class="ctxmenu" id="ctxmenu" role="menu" aria-hidden="true">
    <button type="button" role="menuitem" data-action="refresh">
      🔄 Refresh
      <kbd>F5</kbd>
    </button>
    <button type="button" role="menuitem" data-action="copy">
      📋 Copy selection
      <kbd>Ctrl+C</kbd>
    </button>
    <button type="button" role="menuitem" data-action="inspect">
      🔍 Inspect element
    </button>
    <div class="sep"></div>
    <button type="button" role="menuitem" data-action="coords">
      📌 Copy click coordinates
    </button>
  </div>

<script>
(function () {
  const menu = document.getElementById('ctxmenu');
  const host = document.getElementById('playground'); // area to listen for contextmenu
  let lastClick = { x: 0, y: 0 };

  function openMenu(x, y) {
    // Place menu, then adjust to keep inside viewport
    menu.style.left = x + 'px';
    menu.style.top  = y + 'px';
    menu.setAttribute('aria-hidden', 'false');

    // After showing, measure and correct position if overflowing
    const rect = menu.getBoundingClientRect();
    const vw = window.innerWidth;
    const vh = window.innerHeight;

    let left = rect.left;
    let top  = rect.top;

    if (rect.right > vw) left = Math.max(8, vw - rect.width - 8);
    if (rect.bottom > vh) top = Math.max(8, vh - rect.height - 8);

    menu.style.left = left + 'px';
    menu.style.top  = top + 'px';

    // Move focus to first item for accessibility
    const firstBtn = menu.querySelector('button[role="menuitem"]');
    firstBtn && firstBtn.focus();
  }

  function closeMenu() {
    if (menu.getAttribute('aria-hidden') === 'false') {
      menu.setAttribute('aria-hidden', 'true');
    }
  }

  // Right-click handler
  host.addEventListener('contextmenu', (e) => {
    // Only trigger inside the card area
    const card = e.target.closest('.card');
    if (!card) return;

    e.preventDefault();
    lastClick = { x: e.clientX, y: e.clientY };
    openMenu(e.clientX, e.clientY);
  });

  // Keyboard: open at center of focused element on Shift+F10 or ContextMenu key
  document.addEventListener('keydown', (e) => {
    if ((e.shiftKey && e.key === 'F10') || e.key === 'ContextMenu') {
      e.preventDefault();
      const focusEl = document.activeElement || document.body;
      const rect = focusEl.getBoundingClientRect();
      const x = rect.left + rect.width / 2;
      const y = rect.top + rect.height / 2;
      lastClick = { x, y };
      openMenu(x, y);
    } else if (e.key === 'Escape') {
      closeMenu();
    }
  });

  // Click outside closes
  document.addEventListener('click', (e) => {
    if (!menu.contains(e.target)) closeMenu();
  });

  // Hide on scroll or resize
  ['scroll', 'resize'].forEach(evt => window.addEventListener(evt, closeMenu, { passive: true }));

  // Handle menu actions
  menu.addEventListener('click', async (e) => {
    const btn = e.target.closest('button[data-action]');
    if (!btn) return;
    const action = btn.dataset.action;

    switch (action) {
      case 'refresh':
        alert('Pretend refresh: window.location.reload()');
        break;
      case 'copy': {
        const sel = String(window.getSelection());
        const text = sel || 'No selection';
        try {
          await navigator.clipboard.writeText(text);
          alert('Copied: ' + (text.length > 80 ? text.slice(0,80) + '…' : text));
        } catch {
          alert('Clipboard not available.');
        }
        break;
      }
      case 'inspect':
        alert('Open devtools manually (F12). This is just a demo.');
        break;
      case 'coords': {
        const msg = `Click at x:${Math.round(lastClick.x)}, y:${Math.round(lastClick.y)}`;
        try {
          await navigator.clipboard.writeText(msg);
          alert('Copied: ' + msg);
        } catch {
          alert(msg);
        }
        break;
      }
    }
    closeMenu();
  });
})();
</script>
</body>
</html>
