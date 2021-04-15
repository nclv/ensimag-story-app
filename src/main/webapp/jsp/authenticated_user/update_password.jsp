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
    
    <title>Create Story</title>
</head>

<body>
    <script>0</script>
    <header>
        <nav>
            <a href="${context}${Path.REDIRECT_HOME}"><img alt="Logo" src="${context}/logo.png" height="70"></a>
            <ul>
                <c:if test="${not empty user}">
                    <li><a href="${context}${Path.REDIRECT_LOGOUT}"> Logout </a></li>
                    <li> ${user.name} </li>
                </c:if>
            </ul>
        </nav>
        <h1> update_password.jsp </h1>
    </header>
    <main>
        <form action="${context}${Path.REDIRECT_UPDATE_PASSWORD}" method="post">
            <strong>New password</strong>:<input type="password" name="new_password" value="${fn:escapeXml(param.new_password)}"><br>
            <strong>Retype new password</strong>:<input type="password" name="new_password_confirmation" value="${fn:escapeXml(param.new_password_confirmation)}"><br>
            <input type="submit" value="Update Password">
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
