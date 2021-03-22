<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="utils.Path"%>

<!doctype html>
<html lang="fr">

<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
    
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>Login</title>
</head>

<body>
    <header>
         <p> login.jsp </p>
    </header>
    <main>
        <form action=<%= Path.REDIRECT_LOGIN %> method="post">
            <strong>Username</strong>:<input type="text" name="username"><br>
            <strong>Password</strong>:<input type="password" name="password"><br>
            <input type="submit" value="Login">
        </form>
        <c:if test="${not empty error_message}">
            <p> ${error_message} </p>
        </c:if>
    </main>
    <footer>
        <hr>
        <p>
            <small>@story-app</small>
        </p>
    </footer>
</body>

</html>
