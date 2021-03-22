<%@ page import="utils.Path"%>
<!doctype html>
<html lang="fr">

<head>
    <meta charset="utf-8">
    <link rel="stylesheet" href="style.css">
    <title>Register</title>
</head>

<body>
    <p> register.jsp </p>
    <form action=<%= Path.REDIRECT_REGISTER %> method="post">
        <strong>Username</strong>:<input type="text" name="username"><br>
        <strong>Password</strong>:<input type="password" name="password"><br>
        <input type="submit" value="Register">
    </form>
    <p> Si vous êtes déjà enregistré, pensez à vous <a href="login.jsp"> identifier </a>. </p>
</body>

</html>
