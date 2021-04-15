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
                    <li><a href="${context}${Path.REDIRECT_SHOW_USER_STORIES}"> My stories </a></li>
                    <%-- Si on est l'auteur de l'histoire (not open) on peut inviter d'autres utilisateurs --%>
                    <c:if test="${not empty canInvite}">
                        <li><a href="${context}${Path.REDIRECT_INVITE_USERS}&story_id=${story.id}"> Invite Users </a><li>
                    </c:if>
                    <%-- Si on est l'auteur de la story on peut la publier --%>
                    <c:if test="${story.user_id == user.id}">
                        <li><a href="${context}${Path.REDIRECT_PUBLISH_STORY}&story_id=${story.id}"> Publish/Unpublish story </a><li>
                    </c:if>
                    <li><a href="${context}${Path.REDIRECT_LOGOUT}"> Logout </a></li>
                    <li> ${user.name} </li>
                </c:if>
                <c:if test="${not empty canRead}">
                    <li><a href="${context}${Path.REDIRECT_READ_STORY}&story_id=${story.id}"> Read Story </a><li>
                </c:if>
            </ul>
        </nav>
        <h1> show_story.jsp </h1>
    </header>
    <main>
        <hr>
        <section>
        <p> L'auteur de cette histoire est <c:out value="${author.name}" />. </p>
        </section>
        <hr>
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
        <c:if test="${not empty invitedUsers}">
            <hr>
            <h2>Invited users</h2>
            <section>
                <table>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th></th>
                        </tr>
                    </thead>
                    <c:forEach var="invitedUser" items="${invitedUsers}">
                        <tr>
                            <td><c:out value="${invitedUser.name}" /></td>
                            <c:if test="${not empty canInvite}">
                                <td><a href="${context}${Path.REDIRECT_REMOVE_INVITED}&user_id=${invitedUser.id}&story_id=${story.id}"> Remove </a></td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </table>
            </section>
        </c:if>
        <hr>
        <h2>Paragraphes: </h2>
        <section>
            <table>
                <thead>
                    <tr>
                        <th>Paragraphe Id</th>
                        <th>Content</th>
                        <th>Is final</th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <c:forEach var="paragraphe" items="${paragraphes}">
                    <tr>
                        <td> 
                            <c:choose>
                                <c:when test="${paragraphe.validated}">
                                    <a href="${context}${Path.REDIRECT_SHOW_PARAGRAPHE}&story_id=${story.id}&paragraphe_id=${paragraphe.id}"> <c:out value="${paragraphe.id}" />
                                </c:when>
                                <c:when test="${my_invalidated_paragraphe_id == paragraphe.id}">
                                    <span style="background-color: #00FF00">
                                        <a href="${context}${Path.REDIRECT_SHOW_PARAGRAPHE}&story_id=${story.id}&paragraphe_id=${paragraphe.id}"> <c:out value="${paragraphe.id}" />
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span style="background-color: #F08080">
                                        <a href="${context}${Path.REDIRECT_SHOW_PARAGRAPHE}&story_id=${story.id}&paragraphe_id=${paragraphe.id}"> <c:out value="${paragraphe.id}" />
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${paragraphe.content}" /></td>
                        <td><c:out value="${paragraphe.last}" /></td>
                        <c:if test="${paragraphe.user_id == user.id}">
                            <td><a href="${context}${Path.REDIRECT_REMOVE_PARAGRAPHE}&story_id=${story.id}&paragraphe_id=${paragraphe.id}"> Remove </a></td>
                            <td><a href="${context}${Path.REDIRECT_UNLOCK_PARAGRAPHE}&story_id=${story.id}&paragraphe_id=${paragraphe.id}"> Unlock </a></td>
                        </c:if>
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
