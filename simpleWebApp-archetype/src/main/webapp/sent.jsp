<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>E-mail odeslán</title></head>
<body>
<jsp:useBean id="emailBean"
             class="javaee.mail.Email"
             scope="session"/>
<h1>E-mail bude odeslán za vámi zvolený čas</h1>
<p>Uživatel: ${emailBean.owner}</p>
<p>Komu: ${emailBean.recipient}</p>
<p>Kopie: ${emailBean.copy}</p>
<p>Skrytá kopie: ${emailBean.hiddenCopy}</p>
<p>Předmět: ${emailBean.subject}</p>
<p>Zpráva: ${emailBean.body}</p>
<form action="sendMail" method="post">
    <input type="hidden" name="action" value="new">
    <input type="submit" value="Nový email">
</form>

</body>
</html>
