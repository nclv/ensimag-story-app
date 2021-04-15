<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="utils.Path"%>

<c:set var="context" value="${pageContext.request.contextPath}" />

<!doctype html>
<html lang="fr">

<head>
    <link rel="icon" href="data:,">
    <link rel="stylesheet" href="${context}/style.css">
    
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>Show Story</title>
</head>

<body>
    <script>0</script>
    <header>
        <nav>
            <a href="${context}${Path.REDIRECT_HOME}"><img alt="Logo" src="${context}/logo.png" height="70"></a>
            <ul>
                <%-- Un utilisateur connecté --%>
                <c:if test="${not empty user}">
                    <li><a href="${context}${Path.REDIRECT_CREATE_STORY}"> Create a Story </a></li>
                    <li><a href="${context}${Path.REDIRECT_SAVE_HISTORY}&story_id=${story.id}"> Save History </a><li>
                    <li><a href="${context}${Path.REDIRECT_LOGOUT}"> Logout </a></li>
                    <li> ${user.name} </li>
                </c:if>
                <%-- On peut lire le paragraphe donc on peut lire la story --%>
                <li><a href="${context}${Path.REDIRECT_SHOW_STORY}&story_id=${story.id}"> Show Story </a><li>
                <li><a href="${context}${Path.REDIRECT_CLEAR_HISTORY}&story_id=${story.id}"> Clear History </a><li>
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
                        <th>Title</th>
                        <th>Open</th>
                        <th>Published</th>
                    </tr>
                </thead>
                <tr>
                    <td><c:out value="${story.title}" /></td>
                    <td><c:out value="${story.open}" /></td>
                    <td><c:out value="${story.published}" /></td>
                </tr>
            </table>
        </section>
        <hr>
        <p> Sélectionner un paragraphe de départ: </p>
        <section>
            <table>
                <thead>
                    <tr>
                        <th>Paragraphe Id</th>
                        <th>Content</th>
                        <th>Is final</th>
                    </tr>
                </thead>
                <c:forEach var="paragraphe" items="${paragraphes}">
                    <tr>
                        <td> <a href="${context}${Path.REDIRECT_SHOW_PARAGRAPHE}&story_id=${story.id}&paragraphe_id=${paragraphe.id}&read=true"> <c:out value="${paragraphe.id}" /></td>
                        <td><c:out value="${paragraphe.content}" /></td>
                        <td><c:out value="${paragraphe.last}" /></td>
                    </tr>
                </c:forEach>
            </table>
        </section>
        <c:if test="${not empty history}">
            <hr>
            <section>
                <p> 
                History: <br>
                <c:forEach var="historic" items="${history}">
                    <a href="${context}${Path.REDIRECT_SHOW_PARAGRAPHE}&story_id=${historic.story_id}&paragraphe_id=${historic.paragraphe_id}&previous_paragraphe_id=${paragraphe.id}&read=${read}"> <c:out value="${historic.paragraphe_id}" /></a> ${!loop.last ? ', ' : ''}
                </c:forEach>
                </p>
            </section>
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
