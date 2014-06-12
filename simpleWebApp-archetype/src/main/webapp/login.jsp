<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Login</title></head>
<body>
<h1>Login</h1>

<form action="login" method="post">
    <label for="nick">Nickname:</label><br/>
    <input id="nick" name="nick" type="text"/><br/>
    <input name="action" type="hidden" value="doLogin"/><br/>
    <input type="submit" value="Log in"/>
</form>
</body>
</html>
