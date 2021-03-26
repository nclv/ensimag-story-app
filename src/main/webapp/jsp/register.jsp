<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="utils.Path"%>

<c:set var="context" value="${pageContext.request.contextPath}" />

<!doctype html>
<html lang="fr">

<head>
    <link rel="stylesheet" href="${context}/style.css">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Register</title>
</head>

<body>
    <header>
        <nav>
            <a href="${context}${Path.REDIRECT_HOME}"><img alt="Logo" src="https://via.placeholder.com/200x70?text=Logo" height="70"></a>
        </nav>
        <h1> register.jsp </h1>
    </header>
    <main>
        <form action="${context}${Path.REDIRECT_REGISTER}" method="post">
            <strong>Username</strong>:<input type="text" name="username" value="${fn:escapeXml(param.username)}"><br>
            <strong>Password</strong>:<input type="password" name="password" value="${fn:escapeXml(param.password)}"><br>
            <input type="submit" value="Register">
        </form>
        <c:if test="${not empty error_message}">
            <span style="background-color: #F08080"> ${error_message} </span>
        </c:if>
        <p> Si vous êtes déjà enregistré, pensez à vous <a href="${context}${Path.PAGE_LOGIN}"> identifier </a>. </p>
    </main>
    <footer>
        <hr>
        <p>
            <small>@story-app</small>
        </p>
    </footer>
</body>

</html>
