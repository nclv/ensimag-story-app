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
                    <li><a href="${context}${Path.PAGE_CREATE_STORY}"> Create a Story </a></li>
                    <%-- Story éditable (voir conditions dans l'action) --%>
                    <c:if test="${not empty canEditStory}">
                        <li><a href="${context}${Path.PAGE_ADD_PARAGRAPHE}?story_id=${story.id}"> Add Paragraphe </a><li>
                    </c:if>
                    <%-- Si on est l'auteur de l'histoire (not open) on peut inviter d'autres utilisateurs --%>
                    <c:if test="${not empty canInvite}">
                        <li><a href="${context}${Path.REDIRECT_INVITE_USERS}&story_id=${story.id}"> Invite Users </a><li>
                    </c:if>
                    <c:if test="${not empty canRead}">
                        <li><a href="${context}${Path.REDIRECT_READ_STORY}&story_id=${story.id}"> Read Story </a><li>
                    </c:if>
                    <li><a href="${context}${Path.REDIRECT_LOGOUT}"> Logout </a></li>
                    <li> ${user.name} </li>
                </c:if>
            </ul>
        </nav>
        <h1>show_paragraphe.jsp</h1>
        <c:out value="${paragraphe.content}" />
    </body>
    
    
</html>