<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>welcome</title>
    <link rel="stylesheet" href='style.css'>
    <script>
        window.onload = function() {
            let jsonString = document.getElementById('token').textContent
            let jsonObj = JSON.parse(jsonString);
            document.getElementById('token').innerHTML = JSON.stringify(jsonObj, null, 100);
        }
    </script>
</head>
<body>
<div class="content">
    <div class="logo">
        <img src="logo.png">
    </div>
    <div class="logo">
        <pre>Hello, ${username!}</pre>
    </div>
    <div class="auth-resp">
        <h3><b>Auth Response:</b></h3>
    </div>
    <div class="success" style="width: 1000px; height: 100%; margin: 0 auto; background-color: #ffffff; font-size: 12px;">
        <pre><code id="token">${token!}</code></pre>
    </div>
</div>
</body>
</html>