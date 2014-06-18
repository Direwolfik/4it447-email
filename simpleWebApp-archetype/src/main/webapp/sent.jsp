<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>E-mail odeslán</title></head>
<body>
<jsp:useBean id="email"
             class="javaee.mail.Email"
             scope="session"/>
<h1>E-mail bude odeslán za vámi zvolený čas</h1>
<p>Uživatel: ${email.owner}</p>
<p>Komu: ${email.recipient}</p>
<p>Kopie: ${email.copy}</p>
<p>Skrytá kopie: ${email.hiddenCopy}</p>
<p>Předmět: ${email.subject}</p>
<p>Zpráva: ${email.body}</p>
<form action="sendMail" method="post">
    <input type="hidden" name="action" value="new">
    <input type="submit" value="Nový email">
</form>

</body>
</html>
