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
                <%-- Un utilisateur connecté --%>
                <c:if test="${not empty user}">
                    <li><a href="${context}${Path.REDIRECT_CREATE_STORY}"> Create a Story </a></li>
                    <%-- Story éditable (voir conditions dans l'action) --%>
                    <%-- <c:if test="${not empty canEditStory}">
                        <li><a href="${context}${Path.REDIRECT_EDIT_PARAGRAPHE}&story_id=${story.id}"> Edit Paragraphe </a><li>
                    </c:if> --%>
                    <li><a href="${context}${Path.REDIRECT_LOGOUT}"> Logout </a></li>
                    <li> ${user.name} </li>
                </c:if>
            </ul>
        </nav>
        <h1> read_story.jsp </h1>
    </header>
    <main>
        <hr>
        <section>
        <p> 
        Le créateur de cette histoire est <c:out value="${author.name}" />. <br>
        Les auteurs de cette histoire sont: 
        <c:forEach var="redactor" items="${redactors}">
            <c:out value="${redactor.name}" /> ${!loop.last ? ',' : ''}
        </c:forEach>
        </p>
        </section>
        <section>
            <table>
                <caption><h2>Story</h2></caption>
                <thead>
                    <tr>
                        <th>Story Id</th>
                        <th>Title</th>
                        <th>User Id</th>
                        <th>Open</th>
                        <th>Published</th>
                    </tr>
                </thead>
                <tr>
                    <td><c:out value="${story.id}" /></td>
                    <td><c:out value="${story.title}" /></td>
                    <td><c:out value="${story.user_id}" /></td>
                    <td><c:out value="${story.open}" /></td>
                    <td><c:out value="${story.published}" /></td>
                </tr>
            </table>
        </section>
        <p> Sélectionner un paragraphe de départ: </p>
        <section>
            <table>
                <caption><h2>List of paragraphes</h2></caption>
                <thead>
                    <tr>
                        <th>Paragraphe Id</th>
                        <th>Content</th>
                        <th>Is final</th>
                    </tr>
                </thead>
                <c:forEach var="paragraphe" items="${paragraphes}">
                    <tr>
                        <td> <a href="${context}${Path.REDIRECT_SHOW_PARAGRAPHE}&story_id=${story.id}&paragraphe_id=${paragraphe.id}"> <c:out value="${paragraphe.id}" /></td>
                        <td><c:out value="${paragraphe.content}" /></td>
                        <td><c:out value="${paragraphe.last}" /></td>
                    </tr>
                </c:forEach>
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
