<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Latest</title>
</head>
<body>
  <#list Codes as code>
      <span id="load_date"
            style="color: rgb(187,62,207);">
          ${code.date}
      </span>
        <div class="container" style="background: rgb(195,195,195);border-style: solid">
            <pre>${code.code}</pre>
        </div>
  </#list>
</body>
</html>