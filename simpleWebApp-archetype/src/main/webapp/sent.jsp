<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>E-mail odeslán</title></head>
<body>
<h1>E-mail odeslán</h1>
<p>Uživatel: ${requestScope['user']}</p>
<p>Komu: ${requestScope['to']}</p>
<p>Předmět: ${requestScope['subject']}</p>
<p>Zpráva: ${requestScope['message']}</p>
<p>Datum a čas odeslání: ${requestScope['now']}</p>
</body>
</html>
