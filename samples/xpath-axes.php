<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>XPath Axes Demo</title>
    <style>
        body { font-family: Arial, sans-serif; }
        #header, #footer { background-color: #eee; padding: 10px; }
        #content { margin-top: 10px; }
        .section { border: 1px solid #ccc; margin-bottom: 10px; padding: 10px; }
        .nav-item.selected a { font-weight: bold; }
    </style>
</head>
<body>

<div id="page">
    <div id="header">
        <h1>XPath Axes Demo Page</h1>
        <ul id="nav">
            <li class="nav-item"><a href="#" id="homeLink">Home</a></li>
            <li class="nav-item selected"><a href="#" id="productsLink">Products</a></li>
            <li class="nav-item"><a href="#" id="contactLink">Contact</a></li>
        </ul>
    </div>

    <div id="content">
        <div class="section" id="section1" data-type="main">
            <h2>Section 1</h2>
            <p id="p1">First paragraph in section 1</p>
            <p id="p2">Second paragraph in section 1
                <span id="spanInsideP2">Span inside paragraph 2</span>
            </p>
        </div>

        <div class="section" id="section2" data-type="side">
            <h2>Section 2</h2>
            <ul id="itemList">
                <li class="item" data-id="1" id="item1">Item 1</li>
                <li class="item" data-id="2" id="item2">Item 2</li>
                <li class="item special" data-id="3" id="item3">Item 3</li>
            </ul>
        </div>
    </div>

    <div id="footer">
        <p id="footerText">Footer text goes here</p>
    </div>
</div>

</body>
</html>
