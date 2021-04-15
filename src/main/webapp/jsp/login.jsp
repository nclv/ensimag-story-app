<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="utils.Path"%>

<c:set var="context" value="${pageContext.request.contextPath}" />

<%-- 
    On sauvegarde le paramètre de redirection pour le réutiliser 
    après rechargement de la page login.jsp (erreur de validation) 
--%>
<c:if test="${not empty fn:trim(param.redirect)}">
    <c:set var="redirect" value="${param.redirect}"/>
</c:if>

<!doctype html>
<html lang="fr">

<head>
    <link rel="icon" href="data:,">
    <link rel="stylesheet" href="${context}/style.css">
    
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>Login</title>
</head>

<body>
    <script>0</script>
    <header>
        <nav>
            <a href="${context}${Path.REDIRECT_HOME}"><img alt="Logo" src="${context}/logo.png" height="70"></a>
        </nav>
        <h1> login.jsp </h1>
    </header>
    <main>
        <form action="${context}${Path.REDIRECT_LOGIN}" method="post">
            <strong>Username</strong>:<input type="text" name="username" value="${fn:escapeXml(param.username)}"><br>
            <strong>Password</strong>:<input type="password" name="password" value="${fn:escapeXml(param.password)}"><br>
            <input type="hidden" name="redirect" value="${redirect}">
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
