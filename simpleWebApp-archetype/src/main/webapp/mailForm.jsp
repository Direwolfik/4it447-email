<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>E-mail formulář</title></head>
<body>
<h1>Formulář pro email</h1>
<h2>Uživatel: ${requestScope['user']}</h2>
<form action="logout" method="post">
    <input type="hidden" name="action" value="doLogout"/>
    <input type="submit" value="Logout"/>
</form>
<form action="sendMail" method="post">
    <label for="to">Komu:</label><br/>
    <input id="to" name="to" type="text"/><br/>
    <label for="subject">Předmět:</label><br/>
    <input id="subject" name="subject" type="text"/><br/>
    <label for="message">Zpráva:</label><br/>
    <textarea id="message" name="message" rows="20" cols="60">
    </textarea><br/>
    <input type="submit" value="Odeslat"/>
</form>
</body>
</html>
