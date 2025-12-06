<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>Selenium Timeout Test Page</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <style>
    :root { font-family: system-ui, -apple-system, Segoe UI, Roboto, Ubuntu, Cantarell, Noto Sans, sans-serif; }
    body { margin: 2rem; line-height: 1.5; }
    h1 { margin-bottom: 0.25rem; }
    .note { font-size: 0.95rem; opacity: 0.8; }
    .row { display: grid; grid-template-columns: 1fr 1fr; gap: 1.25rem; align-items: start; }
    code, pre { background: #f6f8fa; border: 1px solid #e5e7eb; border-radius: 8px; padding: 0.5rem 0.75rem; }
    button { padding: 0.6rem 0.9rem; border-radius: 10px; border: 1px solid #d1d5db; background: #fff; cursor: pointer; }
    button:hover { background: #f3f4f6; }
    .card { border: 1px solid #e5e7eb; border-radius: 12px; padding: 1rem; }
    .ok { color: #047857; }
    .warn { color: #b45309; }
    .bad { color: #b91c1c; }
    .mono { font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace; }
    .small { font-size: 0.9rem; }
  </style>
</head>
<body>
  <h1>Selenium Timeout Test Page</h1>
  <p class="note">Use this page to simulate <b>page load timeout</b> and <b>script timeout</b> behaviors in Selenium.</p>

  <div class="card">
    <h2>1) Page Load Timeout scenarios</h2>
    <p>
      Load this page with a query parameter to control slow resources:
    </p>
    <ul>
      <li><a class="mono" href="?mode=fast">?mode=fast</a> &rarr; No slow resources (should load quickly).</li>
      <li><a class="mono" href="?mode=slow">?mode=slow</a> &rarr; Adds subresources pointing to a non-routable IP (<span class="mono">10.255.255.1</span>) so the <code>load</code> event never fires. Ideal to trigger <b>pageLoadTimeout</b>.</li>
    </ul>
    <p class="small">Tip: In Selenium, set a small <code>pageLoadTimeout</code> and then <code>driver.get()</code> the <span class="mono">?mode=slow</span> URL.</p>
    <div id="slow-area"></div>
  </div>

  <div class="card" style="margin-top:1rem;">
    <h2>2) Script Timeout scenarios (executeAsyncScript)</h2>
    <p>
      Selenium's script timeout applies to <code>executeAsyncScript</code>. This page exposes helpers under <code>window.TimeoutDemo</code>:
    </p>
    <ul>
      <li><code>TimeoutDemo.resolveAfter(ms)</code> &rarr; Returns a Promise that resolves after <span class="mono">ms</span> milliseconds.</li>
      <li><code>TimeoutDemo.blockMainThread(ms)</code> &rarr; Busy-loops for <span class="mono">ms</span> milliseconds (blocks the page).</li>
    </ul>

    <div class="row">
      <div>
        <h3 class="warn">Manual buttons (for visual confirmation)</h3>
        <button id="btn-resolve-10s">Resolve after 10s</button>
        <button id="btn-block-10s">Block main thread 10s</button>
        <p id="script-status" class="small"></p>
      </div>
      <div>
        <h3 class="ok">Selenium snippets</h3>
        <p class="small">Java examples (adjust timeouts to see failures):</p>
<pre><code>// Page Load Timeout
driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
driver.get("file:///PATH/selenium-timeout-test.html?mode=slow"); // expect timeout

// Script Timeout (async script)
driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
Object result = ((JavascriptExecutor)driver).executeAsyncScript(
  "const done = arguments[arguments.length - 1];" +
  "window.TimeoutDemo.resolveAfter(10000).then(() =&gt; done('ok'));"
); // expect timeout before 10s

// Script Timeout (busy block - NOT async, will hang call)
((JavascriptExecutor)driver).executeScript(
  "window.TimeoutDemo.blockMainThread(10000);"
); // will block driver for ~10s (no async callback involved)
</code></pre>
      </div>
    </div>
  </div>

  <div class="card" style="margin-top:1rem;">
    <h2>3) Current Mode</h2>
    <p>Query: <span id="q" class="mono"></span></p>
    <ul id="mode-report"></ul>
  </div>

  <script>
    (function(){
      // Helpers exported for Selenium
      window.TimeoutDemo = {
        resolveAfter(ms) {
          return new Promise(res => setTimeout(res, ms));
        },
        blockMainThread(ms) {
          const start = Date.now();
          while (Date.now() - start < ms) { /* busy loop */ }
        }
      };

      // UI helpers
      const params = new URLSearchParams(location.search);
      const mode = params.get("mode") || "fast";
      document.getElementById("q").textContent = location.search || "(none)";
      const report = document.getElementById("mode-report");

      function li(text) {
        const el = document.createElement("li"); el.textContent = text; return el;
      }

      if (mode === "slow") {
        report.appendChild(li("Slow mode enabled: injecting never-ending subresources to delay window 'load'."));

        // Inject subresources that will never load (non-routable IP). This prevents the load event from firing.
        const slowImg = document.createElement("img");
        slowImg.src = "http://10.255.255.1/never.png";
        slowImg.alt = "slow image";

        const slowScript = document.createElement("script");
        slowScript.src = "http://10.255.255.1/never.js";

        const slowLink = document.createElement("link");
        slowLink.rel = "stylesheet";
        slowLink.href = "http://10.255.255.1/never.css";

        const slowIframe = document.createElement("iframe");
        slowIframe.src = "http://10.255.255.1/never.html";
        slowIframe.width = "300";
        slowIframe.height = "100";
        slowIframe.title = "Slow iframe";

        const area = document.getElementById("slow-area");
        area.appendChild(document.createElement("hr"));
        area.appendChild(document.createTextNode("Injected slow resources (image, script, stylesheet, iframe)."));
        area.appendChild(document.createElement("br"));
        area.appendChild(slowImg);
        area.appendChild(document.createElement("br"));
        area.appendChild(slowIframe);

        // Append script & stylesheet late to mimic late-discovered resources
        document.head.appendChild(slowLink);
        document.head.appendChild(slowScript);

      } else {
        report.appendChild(li("Fast mode: no slow resources injected. The page should reach 'load' normally."));
      }

      // Manual button wiring
      const status = document.getElementById("script-status");
      const btnResolve = document.getElementById("btn-resolve-10s");
      const btnBlock = document.getElementById("btn-block-10s");

      btnResolve.addEventListener("click", async () => {
        status.textContent = "Promise started (will resolve in ~10s)...";
        await window.TimeoutDemo.resolveAfter(10000);
        status.textContent = "Promise resolved after ~10s.";
      });

      btnBlock.addEventListener("click", () => {
        status.textContent = "Blocking main thread for ~10s...";
        window.TimeoutDemo.blockMainThread(10000);
        status.textContent = "Block finished.";
      });
    })();
  </script>
</body>
</html>
