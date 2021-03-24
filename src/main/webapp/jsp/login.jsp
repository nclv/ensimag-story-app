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
    
    <title>Login</title>
</head>

<body>
    <header>
         <p> login.jsp </p>
    </header>
    <main>
        <form action="${context}${Path.REDIRECT_LOGIN}" method="post">
            <strong>Username</strong>:<input type="text" name="username" value="${fn:escapeXml(param.username)}"><br>
            <strong>Password</strong>:<input type="password" name="password" value="${fn:escapeXml(param.password)}"><br>
            <input type="submit" value="Login">
        </form>
        <c:if test="${not empty error_message}">
            <span style="background-color: #F08080"> ${error_message} </span>
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
