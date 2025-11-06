<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>HTML Select: Single & Multiple — Teaching Demo</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <style>
    :root { --bg:#f7f7fb; --ink:#222; --muted:#666; --brand:#186BDD; --card:#fff; --border:#e5e7eb; }
    html,body { margin:0; font:16px/1.5 system-ui, -apple-system, Segoe UI, Roboto, sans-serif; color:var(--ink); background:var(--bg); }
    .wrap { max-width: 980px; margin: 2rem auto; padding: 0 1rem; }
    h1 { margin: 0 0 1rem; }
    .grid { display:grid; gap:1rem; grid-template-columns: repeat(auto-fit, minmax(300px,1fr)); }
    .card { background:var(--card); border:1px solid var(--border); border-radius:12px; padding:1rem 1.25rem; box-shadow:0 1px 2px rgba(0,0,0,.04) }
    label { font-weight:600; display:block; margin-bottom:.35rem; }
    select, button, input[type=text] {
      width:100%; box-sizing:border-box; padding:.6rem .7rem; border:1px solid var(--border); border-radius:10px;
      background:#fff; font:inherit;
    }
    select[multiple] { min-height: 9rem; }
    small { color:var(--muted); display:block; margin-top:.35rem; }
    .row { display:flex; gap:.6rem; margin-top:.6rem; flex-wrap:wrap; }
    button {
      width:auto; cursor:pointer; background:var(--brand); color:#fff; border:none;
      padding:.55rem .8rem; border-radius:999px;
    }
    button.secondary { background:#fff; color:var(--ink); border:1px solid var(--border); }
    pre {
      background:#0b1020; color:#e6edf3; border-radius:10px; padding:1rem; overflow:auto; font-size:.9rem;
    }
    code { font-family: ui-monospace, SFMono-Regular, Menlo, Consolas, monospace; }
    .out { background:#0b1020; color:#a3e635; padding:.6rem .75rem; border-radius:10px; font-family: ui-monospace, SFMono-Regular, Menlo, Consolas, monospace; }
    fieldset { border:1px dashed var(--border); border-radius:12px; padding:1rem; }
    legend { padding:0 .4rem; color:var(--muted); }
    .tip { font-size:.9rem; color:#444; background:#fffbea; border:1px solid #f6e05e; padding:.6rem .75rem; border-radius:10px; }
    .muted { color:var(--muted); }
  </style>
</head>
<body>
  <div class="wrap">
    <h1>Teaching <code>&lt;select&gt;</code>: Single vs Multiple</h1>
    <p class="muted">This page shows best practices for single-select and multi-select, including placeholders, groups, validation, and event handling. Try changing values and see the live output.</p>

    <div class="grid">
      <!-- Single Select -->
      <div class="card">
        <fieldset>
          <legend>Example 1 — Single Selection</legend>
          <label for="country">Choose a country</label>
          <select id="country" name="country" required aria-describedby="countryHelp">
            <option value="" disabled selected hidden>— Select one —</option>
            <optgroup label="Asia">
              <option value="IN">India</option>
              <option value="JP">Japan</option>
            </optgroup>
            <optgroup label="Europe">
              <option value="DE">Germany</option>
              <option value="FR">France</option>
            </optgroup>
          </select>
          <small id="countryHelp">A normal dropdown (single value). Uses a disabled, hidden placeholder and <code>required</code>.</small>

          <div class="row">
            <button id="readSingle">Read value</button>
            <button class="secondary" id="setSingle">Set to India</button>
            <button class="secondary" id="clearSingle">Clear</button>
          </div>
          <div class="out" id="outSingle" style="margin-top:.6rem;">value: (none)</div>
        </fieldset>
      </div>

      <!-- Multi Select -->
      <div class="card">
        <fieldset>
          <legend>Example 2 — Multiple Selection</legend>
          <label for="langs">Select programming languages</label>
          <select id="langs" name="languages" multiple size="8" aria-describedby="langHelp">
            <optgroup label="Frontend">
              <option>HTML</option>
              <option>CSS</option>
              <option>JavaScript</option>
              <option>TypeScript</option>
            </optgroup>
            <optgroup label="Backend">
              <option>Java</option>
              <option>Go</option>
              <option>Python</option>
              <option>Rust</option>
              <option>Kotlin</option>
            </optgroup>
          </select>
          <small id="langHelp">Hold <kbd>Ctrl</kbd>/<kbd>Cmd</kbd> (desktop) to select multiple; on mobile, it acts as a picker. <code>size</code> makes it a listbox.</small>

          <div class="row">
            <button id="readMulti">Read selections</button>
            <button class="secondary" id="selectAll">Select all</button>
            <button class="secondary" id="selectNone">Select none</button>
            <button class="secondary" id="setPreset">Set preset (JS, TS, Go)</button>
          </div>
          <div class="out" id="outMulti" style="margin-top:.6rem;">values: []</div>
        </fieldset>
      </div>

      <!-- Validation & Disabled -->
      <div class="card">
        <fieldset>
          <legend>Example 3 — Validation & Disabled</legend>
          <label for="meal">Meal preference (required)</label>
          <select id="meal" required>
            <option value="" disabled selected hidden>— Select one —</option>
            <option>Vegetarian</option>
            <option>Non-Vegetarian</option>
            <option>Vegan</option>
            <option>Jain</option>
          </select>

          <label for="seat" style="margin-top:.8rem;">Seat type (disabled)</label>
          <select id="seat" disabled>
            <option>Window</option>
            <option>Aisle</option>
          </select>
          <small>Use <code>required</code> for form validation; <code>disabled</code> to prevent interaction.</small>

          <div class="row">
            <button id="trySubmit">Try submit</button>
          </div>
          <div class="out" id="outVal" style="margin-top:.6rem;">Submit status: (not tried)</div>
        </fieldset>
      </div>

      <!-- Code Snippets -->
      <div class="card">
        <h3>Useful Snippets</h3>
        <p class="tip"><strong>Keyboard:</strong> Up/Down arrows to navigate, type letters to jump, <kbd>Alt</kbd>+<kbd>↓</kbd> to open on Windows, <kbd>Space</kbd> on macOS (varies by browser).</p>
        <pre><code>&lt;!-- Single select with placeholder --&gt;
&lt;label for="city"&gt;City&lt;/label&gt;
&lt;select id="city" required&gt;
  &lt;option value="" disabled selected hidden&gt;— Select one —&lt;/option&gt;
  &lt;option value="Pune"&gt;Pune&lt;/option&gt;
  &lt;option value="Mumbai"&gt;Mumbai&lt;/option&gt;
&lt;/select&gt;

&lt;!-- Multi select listbox --&gt;
&lt;label for="skills"&gt;Skills&lt;/label&gt;
&lt;select id="skills" multiple size="6"&gt;
  &lt;optgroup label="Languages"&gt;
    &lt;option&gt;Java&lt;/option&gt;
    &lt;option&gt;Go&lt;/option&gt;
    &lt;option&gt;Python&lt;/option&gt;
  &lt;/optgroup&gt;
&lt;/select&gt;

&lt;!-- Read selections in JS --&gt;
const single = document.getElementById('city').value;
const multi = [...document.getElementById('skills').selectedOptions].map(o =&gt; o.value);</code></pre>
      </div>
    </div>

    <!-- Live change event demo -->
    <div class="card" style="margin-top:1rem;">
      <h3>Change Events (Live)</h3>
      <p class="muted">Open your console to see events firing as you interact.</p>
      <div class="row">
        <button class="secondary" id="logToggle">Toggle console logging (on)</button>
      </div>
    </div>
  </div>

  <script>
    const $ = (id) => document.getElementById(id);

    // Single select handlers
    $('readSingle').addEventListener('click', () => {
      $('outSingle').textContent = 'value: ' + ($('country').value || '(none)');
    });
    $('setSingle').addEventListener('click', () => {
      $('country').value = 'IN';
      $('country').dispatchEvent(new Event('change', { bubbles: true }));
      $('outSingle').textContent = 'value: ' + $('country').value;
    });
    $('clearSingle').addEventListener('click', () => {
      $('country').selectedIndex = 0; // back to placeholder
      $('country').dispatchEvent(new Event('change', { bubbles: true }));
      $('outSingle').textContent = 'value: (none)';
    });

    // Multi-select helpers
    function readMultiValues() {
      return [...$('langs').selectedOptions].map(o => o.value || o.textContent);
    }
    $('readMulti').addEventListener('click', () => {
      $('outMulti').textContent = 'values: ' + JSON.stringify(readMultiValues());
    });
    $('selectAll').addEventListener('click', () => {
      [...$('langs').options].forEach(o => o.selected = true);
      $('langs').dispatchEvent(new Event('change', { bubbles: true }));
      $('outMulti').textContent = 'values: ' + JSON.stringify(readMultiValues());
    });
    $('selectNone').addEventListener('click', () => {
      [...$('langs').options].forEach(o => o.selected = false);
      $('langs').dispatchEvent(new Event('change', { bubbles: true }));
      $('outMulti').textContent = 'values: []';
    });
    $('setPreset').addEventListener('click', () => {
      const preset = new Set(['JavaScript','TypeScript','Go']);
      [...$('langs').options].forEach(o => o.selected = preset.has(o.value || o.textContent));
      $('langs').dispatchEvent(new Event('change', { bubbles: true }));
      $('outMulti').textContent = 'values: ' + JSON.stringify(readMultiValues());
    });

    // Validation demo
    $('trySubmit').addEventListener('click', () => {
      const meal = $('meal');
      if (!meal.checkValidity()) {
        meal.reportValidity(); // show native tooltip
        $('outVal').textContent = 'Submit status: blocked (meal is required)';
      } else {
        $('outVal').textContent = 'Submit status: ok (meal=' + meal.value + ')';
      }
    });

    // Change event logging (teaching)
    let logOn = true;
    const onChange = (e) => {
      if (!logOn) return;
      const target = e.target;
      if (target.id === 'langs') {
        console.log('[change]', target.id, readMultiValues());
      } else {
        console.log('[change]', target.id, target.value);
      }
    };
    ['country','langs','meal'].forEach(id => $(id).addEventListener('change', onChange));
    $('logToggle').addEventListener('click', () => {
      logOn = !logOn;
      $('logToggle').textContent = `Toggle console logging (${logOn ? 'on' : 'off'})`;
    });
  </script>
</body>
</html>
