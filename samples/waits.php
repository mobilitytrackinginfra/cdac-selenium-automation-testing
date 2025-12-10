<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>ExpectedConditions Demo</title>
    <style>
        .hidden { display: none; }
        .box { padding: 8px; margin: 4px; border: 1px solid #ccc; }
    </style>
    <script>
        function showAlert() {
            alert("Hello from alertIsPresent!");
        }

        function toggleStatus() {
            const el = document.getElementById("status");
            const state = el.getAttribute("data-state");
            if (state === "idle") {
                el.setAttribute("data-state", "ready");
                el.setAttribute("data-dom-attr", "ok");
                el.textContent = "Status: READY";
            } else {
                el.setAttribute("data-state", "idle");
                el.setAttribute("data-dom-attr", "wait");
                el.textContent = "Status: IDLE";
            }
        }

        function makeInvisible() {
            document.getElementById("toHide").classList.add("hidden");
        }

        function changeText() {
            document.getElementById("message").textContent = "New message text";
            document.getElementById("messageInput").value = "New message text in input";
        }

        function removeStaleElement() {
            const el = document.getElementById("staleElement");
            if (el) el.remove();
        }

        function addListItem() {
            const list = document.getElementById("itemsList");
            const li = document.createElement("li");
            li.className = "item";
            li.textContent = "Item " + (list.children.length + 1);
            list.appendChild(li);
        }

        function toggleCheckbox() {
            const cb = document.getElementById("cb2");
            cb.checked = !cb.checked;
        }
    </script>
</head>
<body>

<h1 id="pageTitle">ExpectedConditions Demo Page</h1>

<div id="status"
     class="box"
     data-state="idle"
     data-dom-attr="wait">
    Status: IDLE
</div>

<div id="messageContainer" class="box">
    <span id="message">Initial message text</span>
    <input id="messageInput" type="text" value="Initial message text in input">
</div>

<div id="toHide" class="box">
    <span id="textToHide">I will be hidden</span>
</div>

<div id="staleWrapper" class="box">
    <span id="staleElement">I will become stale (removed from DOM)</span>
</div>

<div id="checkboxes" class="box">
    <label><input type="checkbox" id="cb1" checked> Checkbox 1 (initially selected)</label><br>
    <label><input type="checkbox" id="cb2"> Checkbox 2 (initially not selected)</label>
</div>

<div id="parent" class="box">
    Parent
    <span class="child">Child 1</span>
    <span class="child">Child 2</span>
</div>

<ul id="itemsList" class="box">
    <li class="item">Item 1</li>
    <li class="item">Item 2</li>
</ul>

<div id="nestedParent" class="box">
    <div class="nested-child">Nested Child 1</div>
    <div class="nested-child">Nested Child 2</div>
</div>

<button id="btnAlert" onclick="showAlert()">Show Alert</button>
<button id="btnToggleStatus" onclick="toggleStatus()">Toggle Status</button>
<button id="btnHide" onclick="makeInvisible()">Make Invisible</button>
<button id="btnChangeText" onclick="changeText()">Change Text</button>
<button id="btnRemoveStale" onclick="removeStaleElement()">Remove Stale Element</button>
<button id="btnAddItem" onclick="addListItem()">Add List Item</button>
<button id="btnToggleCheckbox" onclick="toggleCheckbox()">Toggle Checkbox 2</button>

<!-- Frame demo -->
<iframe id="demoFrameByIndex" name="demoFrameByIndex"></iframe>
<iframe id="demoFrameByName" name="demoFrameByName"></iframe>
<iframe id="demoFrameByLocator"></iframe>

<!-- Link to open new window/tab -->
<a id="newWindowLink" href="https://example.com" target="_blank">Open New Window</a>

</body>
</html>
