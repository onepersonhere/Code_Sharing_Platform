<!DOCTYPE html>
<head>
  <title>Code</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="stylesheet"
        target="_blank" href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
  <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
  <script>hljs.initHighlightingOnLoad();</script>
</head>
<body>
        <span id="load_date"
              style="color: rgb(187,62,207);">
            ${date}
        </span>
        <#if views??>
        <span id="views_restriction">
            ${views} more views allowed.
        </span>
        </#if>
        <#if time??>
        <span id="time_restriction">
            The code will be available for ${time} seconds.
        </span>
        </#if>
        <div class="container"
             style="background: rgb(195,195,195);
             border-style: solid";>
            <pre id="code_snippet" >
                <code>${code}</code>
            </pre>
        </div>
</body>
</html>