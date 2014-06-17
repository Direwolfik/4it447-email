<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>E-mail odeslán</title></head>
<body>
<jsp:useBean id="emailBean"
             class="javaee.mail.EmailBean"
             scope="session"/>
<h1>E-mail odeslán</h1>
<p>Uživatel: ${emailBean.owner}</p>
<p>Komu: ${emailBean.recipient}</p>
<p>Kopie: ${emailBean.copy}</p>
<p>Předmět: ${emailBean.subject}</p>
<p>Zpráva: ${emailBean.body}</p>
<form action="sendMail" method="post">
    <input type="hidden" name="action" value="new">
    <input type="submit" value="Nový email">
</form>

</body>
</html>
