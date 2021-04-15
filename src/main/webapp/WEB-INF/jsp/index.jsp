<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="utils.Path"%>

<sql:query var="listUsers" dataSource="jdbc/story-app">
    select "user_id", "username", "password" from "User"
</sql:query>

<c:set var="context" value="${pageContext.request.contextPath}" />

<!doctype html>
<html lang="fr">

<head>
    <link rel="icon" href="data:,">
    <link rel="stylesheet" href="${context}/style.css">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>User List</title>
</head>

<body>
    <script>0</script>
    <header>
        <nav>
            <a href="${context}${Path.REDIRECT_HOME}"><img alt="Logo" src="${context}/logo.png" height="70"></a>
            <ul>
                <c:if test="${not empty user}">
                    <li><a href="${context}${Path.REDIRECT_CREATE_STORY}"> Create a Story </a></li>
                    <li><a href="${context}${Path.REDIRECT_SHOW_USER_STORIES}"> My stories </a></li>
                    <li><a href="${context}${Path.REDIRECT_LOGOUT}"> Logout </a></li>
                    <li><a href="${context}${Path.PAGE_UPDATE_PASSWORD}"> ${user.name} </a></li>
                </c:if>
            </ul>
        </nav>
        <h1> index.jsp </h1>
    </header>
    <main>
        <hr>
        <h2>Open and published stories</h2>
        <section>
            <table>
                <thead>
                    <tr>
                        <th>Story title</th>
                        <th>Open</th>
                        <th>Published</th>
                    </tr>
                </thead>
                <c:forEach var="story" items="${stories}">
                    <tr>
                        <td>
                            <a href="${context}${Path.REDIRECT_SHOW_STORY}&story_id=${story.id}"> <c:out value="${story.title}" /></a>
                        </td>
                        <td><c:out value="${story.open}" /></td>
                        <td><c:out value="${story.published}" /></td>
                    </tr>
                </c:forEach>
            </table>
        </section>
        <hr>
        <h2>Published stories</h2>
        <section>
            <table>
                <thead>
                    <tr>
                        <th>Story Title</th>
                        <th>Open</th>
                        <th>Published</th>
                    </tr>
                </thead>
                <c:forEach var="story" items="${published_stories}">
                    <tr>
                        <td>
                            <a href="${context}${Path.REDIRECT_SHOW_STORY}&story_id=${story.id}"> <c:out value="${story.title}" /></a>
                        </td>
                        <td><c:out value="${story.open}" /></td>
                        <td><c:out value="${story.published}" /></td>
                    </tr>
                </c:forEach>
            </table>
        </section>
        <p> Pensez à vous <a href="${context}${Path.REDIRECT_REGISTER}"> enregistrer </a>. </p>
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
