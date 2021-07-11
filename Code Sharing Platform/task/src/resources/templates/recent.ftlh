<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Latest</title>
    <link rel="stylesheet"
          target="_blank" href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
</head>
<body>
  <#list Codes as code>
      <span id="load_date"
            style="color: rgb(187,62,207);">
          ${code.date}
      </span>
        <div class="container" style="background: rgb(195,195,195);border-style: solid">
            <pre><code>${code.code}</code></pre>
        </div>
  </#list>
</body>
</html>