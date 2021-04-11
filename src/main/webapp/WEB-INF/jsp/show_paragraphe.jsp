<%-- 
    Document   : show_paragraphe
    Created on : Apr 5, 2021, 4:45:48 PM
    Author     : vincent
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="utils.Path"%>

<c:set var="context" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
    <head>
         <link rel="stylesheet" href="${context}/style.css">
    
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Show Paragraphe</title>
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
                    <c:if test="${not empty canEditStory}">
                        <li><a href="${context}${Path.REDIRECT_EDIT_PARAGRAPHE}&story_id=${story.id}&paragraphe_id=${paragraphe.id}"> Edit Paragraphe </a><li>
                    </c:if>
                    <%-- Si on est l'auteur de l'histoire (not open) on peut inviter d'autres utilisateurs --%>
                    <c:if test="${not empty canInvite}">
                        <li><a href="${context}${Path.REDIRECT_INVITE_USERS}&story_id=${story.id}"> Invite Users </a><li>
                    </c:if>
                    <li><a href="${context}${Path.REDIRECT_LOGOUT}"> Logout </a></li>
                    <li> ${user.name} </li>
                </c:if>
                <%-- On peut lire le paragraphe donc on peut lire la story --%>
                <li><a href="${context}${Path.REDIRECT_READ_STORY}&story_id=${story.id}"> Read Story </a><li>
            </ul>
        </nav>
        <h1>show_paragraphe.jsp</h1>
        <c:out value="${paragraphe.title}" />
        <section>
            <table>
                <caption><h2>Choices</h2></caption>
                <thead>
                    <tr>
                        <th>Paragraphe Id</th>
                        <th>User Id</th>
                        <th>Title</th>
                    </tr>
                </thead>
                <%-- Test que l'historique contient le paragraphe actuel --%>
                <c:set var="contain_current" value="false" />
                <c:forEach var="paragraphe_id" items="${historic_paragraphes_ids}">
                    <c:if test="${paragraphe_id eq paragraphe.id}">
                        <c:set var="contain_current" value="true" />
                    </c:if>
                </c:forEach>

                <c:forEach var="child" items="${children}">
                    <%-- Test que l'historique contient le paragraphe child. On est intéressé par sa négation. --%>
                    <c:set var="contain_child" value="false" />
                    <c:forEach var="paragraphe_id" items="${historic_paragraphes_ids}">
                        <c:if test="${paragraphe_id eq child.id}">
                            <c:set var="contain_child" value="true" />
                        </c:if>
                    </c:forEach>
                    <tr>
                        <td>
                            <%-- Choix de l'affichage de la popup --%>
                            <c:set var="child_url" value="${context}${Path.REDIRECT_SHOW_PARAGRAPHE}&story_id=${child.story_id}&paragraphe_id=${child.id}&previous_paragraphe_id=${paragraphe.id}" />
                            <c:choose>
                                <c:when test="${contain_current eq 'true' && contain_child eq 'false'}">
                                    <a href="${child_url}" onclick="return confirm('You will modify your history.')"> <c:out value="${child.id}" /></a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${child_url}"> <c:out value="${child.id}" /></a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${child.user_id}" /></td>
                        <td><c:out value="${child.content}" /></td>
                    </tr>
                </c:forEach>
            </table>
        </section>
        <section>
            <c:if test="${not empty history}">
                <c:forEach var="historic" items="${history}">
                    <tr>
                        <td>
                            <a href="${context}${Path.REDIRECT_SHOW_PARAGRAPHE}&story_id=${historic.story_id}&paragraphe_id=${historic.paragraphe_id}&previous_paragraphe_id=${paragraphe.id}"> <c:out value="${historic.paragraphe_id}" /></a> ${!loop.last ? ', ' : ''}
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
        </section>

    </body>
    
    
</html>
