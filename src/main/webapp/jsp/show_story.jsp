<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="utils.Path"%>

<c:set var="context" value="${pageContext.request.contextPath}" />

<!doctype html>
<html lang="fr">

<head>
    <link rel="stylesheet" href="${context}/style.css">
    
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>Show Story</title>
</head>

<body>
    <header>
        <nav>
            <a href="${context}${Path.REDIRECT_HOME}"><img alt="Logo" src="https://via.placeholder.com/200x70?text=Logo" height="70"></a>
            <ul>
                <c:if test="${not empty user}">
                    <li><a href="${context}${Path.PAGE_CREATE_STORY}"> Create a Story </a></li>
                    <li><a href="${context}${Path.PAGE_ADD_PARAGRAPHE}"> Add Paragraphe </a><li>
                    <li><a href="${context}${Path.REDIRECT_LOGOUT}"> Logout </a></li>
                    <li> ${user.name} </li>
                </c:if>
            </ul>
        </nav>
        <h1> show_story.jsp </h1>
    </header>
    <main>
        <hr>
        <section>
            <table>
                <caption><h2>Story</h2></caption>
                <thead>
                    <tr>
                        <th>Story Id</th>
                        <th>User Id</th>
                        <th>Open</th>
                        <th>Published</th>
                    </tr>
                </thead>
                <tr>
                    <td><c:out value="${story.id}" /></td>
                    <td><c:out value="${story.user_id}" /></td>
                    <td><c:out value="${story.open}" /></td>
                    <td><c:out value="${story.published}" /></td>
                </tr>
            </table>
        </section>
    </main>
    <footer>
        <hr>
        <p>
            <small>@story-app</small>
        </p>
    </footer>
</body>

</html>
