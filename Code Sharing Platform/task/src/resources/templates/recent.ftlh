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
        <div class="container" style="background: rgb(195,195,195);">
          ${code.code}
        </div>
  </#list>
</body>
</html>