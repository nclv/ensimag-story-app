<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="utils.Path"%>

<c:set var="context" value="${pageContext.request.contextPath}" />

<c:if test="${not empty fn:trim(param.story_id)}">
    <c:set var="story_id" value="${param.story_id}"/>
</c:if>

<!doctype html>
<html lang="fr">

<head>
    <link rel="stylesheet" href="${context}/style.css">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>User List</title>
</head>

<body>
    <header>
        <nav>
            <a href="${context}${Path.REDIRECT_HOME}"><img alt="Logo" src="https://via.placeholder.com/200x70?text=Logo" height="70"></a>
            <ul>
                <c:if test="${not empty user}">
                    <li><a href="${context}${Path.REDIRECT_LOGOUT}"> Logout </a></li>
                </c:if>
            </ul>
        </nav>
        <h1> invite_users.jsp </h1>
    </header>
    <main>
        <hr>
        <p> Invite users to story <b>${story_id}</b> :</p>
        <form action="${context}${Path.REDIRECT_INVITE_USERS}&story_id=${story_id}" method="post">
            <c:forEach var="user" items="${users}">
                <div>
                    <input type="checkbox" id="${user.name}" name="selected" value="${user.id}">
                    <label for="${user.name}" ><c:out value="${user.name}" /></label>
                </div>
            </c:forEach>
            <input type="submit" name="invite" value="Invite selected Users to collaborate">
        </form>
        <c:if test="${not empty param.error_message}">
            <span style="background-color: #F08080"> ${param.error_message} </span>
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
