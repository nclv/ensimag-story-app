<%@ page import="utils.Path"%>

<!doctype html>
<html lang="fr">

<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Register</title>
</head>

<body>
    <header>
        <p> register.jsp </p>
    </header>
    <main>
        <form action=<%= Path.REDIRECT_REGISTER %> method="post">
            <strong>Username</strong>:<input type="text" name="username"><br>
            <strong>Password</strong>:<input type="password" name="password"><br>
            <input type="submit" value="Register">
        </form>
        <p> Si vous êtes déjà enregistré, pensez à vous <a href="login.jsp"> identifier </a>. </p>
    </main>
    <footer>
        <hr>
        <p>
            <small>@story-app</small>
        </p>
    </footer>
</body>

</html>
