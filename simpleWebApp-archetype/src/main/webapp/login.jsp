<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Login</title></head>
<body>
<h1>Login</h1>

<form action="j_security_check" method="post">
    <label for="user">User:</label><br>
    <input id="user" type="text" name="j_username"/><br>
    <label for="psw">Password:</label><br>
    <input id="psw" type="password" name="j_password"/><br>
    <input type="submit" value="Enter"/>
</form>

</body>
</html>
