<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Selenium UI Playground</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(to right, #74ebd5, #ACB6E5);
            margin: 0;
            padding: 40px;
        }
        .container {
            background: #fff;
            border-radius: 10px;
            padding: 30px;
            max-width: 600px;
            margin: auto;
            box-shadow: 0 4px 10px rgba(0,0,0,0.2);
        }
        h1 {
            text-align: center;
            color: #333;
        }
        label {
            font-weight: bold;
            display: block;
            margin-top: 15px;
        }
        input, select, textarea, button {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        button {
            background: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
        button:hover {
            background: #45a049;
        }
        .inline {
            display: inline-block;
            margin-right: 15px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Selenium Elements Playground</h1>

        <!-- Textbox -->
        <label for="username">Username</label>
        <input type="text" id="username" name="username">

        <!-- Password -->
        <label for="password">Password</label>
        <input type="password" id="password" name="password">

        <!-- Radio Buttons -->
        <label>Gender</label>
        <div class="inline">
            <input type="radio" id="male" name="gender" value="male"> Male
        </div>
        <div class="inline">
            <input type="radio" id="female" name="gender" value="female"> Female
        </div>

        <!-- Checkbox -->
        <label>Interests</label>
        <div class="inline">
            <input type="checkbox" id="sports" name="interest" value="sports"> Sports
        </div>
        <div class="inline">
            <input type="checkbox" id="music" name="interest" value="music"> Music
        </div>

        <!-- Dropdown -->
        <label for="country">Country</label>
        <select id="country" name="country">
            <option value="india">India</option>
            <option value="usa">USA</option>
            <option value="uk">UK</option>
        </select>

        <!-- Multi-Select -->
        <label for="skills">Skills (select multiple)</label>
        <select id="skills" name="skills" multiple>
            <option value="java">Java</option>
            <option value="python">Python</option>
            <option value="selenium">Selenium</option>
            <option value="sql">SQL</option>
        </select>

        <!-- Textarea -->
        <label for="comments">Comments</label>
        <textarea id="comments" name="comments"></textarea>

        <!-- File Upload -->
        <label for="fileUpload">Upload File</label>
        <input type="file" id="fileUpload" name="fileUpload">

        <!-- Date Picker -->
        <label for="dob">Date of Birth</label>
        <input type="date" id="dob" name="dob">

        <!-- Button -->
        <button id="submitBtn" name="submitBtn">Submit</button>

        <!-- Link -->
        <p><a id="homeLink" name="homeLink" href="https://example.com">Go to Example</a></p>

        <!-- Image -->
        <img id="demoImage" name="demoImage" src="https://via.placeholder.com/150" alt="Demo Image">

        <!-- Hidden Field -->
        <input type="hidden" id="hiddenField" name="hiddenField" value="hiddenValue">
    </div>
</body>
</html>
