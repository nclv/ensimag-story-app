<%-- 
    Document   : edit_paragraphe
    Created on : Mar 29, 2021, 5:04:51 PM
    Author     : vincent
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="utils.Path"%>

<c:set var="context" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${not empty paragraphe_content}">
        <c:set var="paragraphe_content" value="${paragraphe_content}" />
        <c:set var="is_final" value="${is_final}" />
    </c:when>
    <c:when test="${not empty param.paragraphe_content}">
        <c:set var="paragraphe_content" value="${param.paragraphe_content}" />
        <c:set var="is_final" value="${param.is_final}" />
    </c:when>
    <c:otherwise>
        <c:set var="paragraphe_content" value="" />
    </c:otherwise>
</c:choose>

<!DOCTYPE html>
<html>
    <head>
    <link rel="stylesheet" href="${context}/style.css">
    
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>Edit Paragraphe</title>

    <script type="text/javascript" src="${context}/utils.js"></script>
</head>

<body>
    <header>
        <nav>
            <a href="${context}${Path.REDIRECT_HOME}"><img alt="Logo" src="${context}/logo.png" height="70"></a>
        </nav>
        <h1> edit_paragraphe.jsp </h1>
    </header>
    
    <main>
        <section>
            <form action="${context}${Path.REDIRECT_EDIT_PARAGRAPHE}&story_id=${param.story_id}&paragraphe_id=${param.paragraphe_id}" method="post">
                <textarea rows="10" cols="60" name="paragraphe_content" placeholder="Enter your first paragraphe content..." required>${paragraphe_content}</textarea>
                
                <%-- S'il y a des paragraphes enfants on ne peut pas éditer en non final --%>
                <%-- <c:if test="${empty choices}"> --%>
                    <p> Is your paragraphe <strong>final</strong>? </p>
                    <input type="radio" id="final" name="is_final" value="final" required ${is_final eq "final" ? "checked": ""}>
                    <label for="final">Yes</label>
                    <input type="radio" id="non-final" name="is_final" value="non-final" ${is_final eq "non-final" ? "checked": ""}>
                    <label for="non-final">No</label>
                <%-- </c:if> --%>

                <c:if test="${not empty oldChoices}">
                    <p> <strong>Choix actuels possibles</strong> à la fin de ce paragraphe: 
                    <c:forEach var="oldChoice" items="${oldChoices}">
                        <c:out value="${oldChoice}" />, 
                    </c:forEach>
                    </p>
                </c:if>

                <c:if test="${not empty existingParagraphes}">
                    <p> <strong>Converger</strong> sur un ou plusieurs paragraphe déjà existant:</p>
                    <c:forEach var="existingParagraphe" items="${existingParagraphes}">
                        <div>
                            <input type="checkbox" id="${existingParagraphe.id}" name="converge" value="${existingParagraphe.id}">
                            <label for="${existingParagraphe.id}" ><c:out value="${existingParagraphe}" /></label>
                        </div>
                    </c:forEach>
                </c:if>

                <label for="conditionnal">Sélectionner un paragraphe conditionnel:</label>
                <select name="conditionnal" id="conditionnal">
                    <option value=""> NO CONDITIONNAL PARAGRAPHE </option>
                    <c:forEach var="existingParagraphe" items="${existingParagraphes}">
                        <option value="${existingParagraphe}"><c:out value="${existingParagraphe}" /></option>
                    </c:forEach>
                </select>

                
                <%-- Invisible choice text input --%>
                <div id="readroot" style="display: none">
                    <label for="choice">Enter new choice text:</label>
                    <input type="text" id="name" name="choice" size="28">
                    <input type="button" value="Remove choice" onclick="this.parentNode.parentNode.parentNode.removeChild(this.parentNode.parentNode);" />
                </div>

                <section>
                    <%-- Load previous choices input --%>
                    <c:if test="${not empty choices}">
                        <c:forEach var="choice" items="${choices}" varStatus="loop">
                            <aside>
                                <label for="choice">Enter new choice text:</label>
                                <input type="text" id="name" name="choice_${loop.index}" size="28" value="${choice}">
                                <input type="button" value="Remove choice" onclick="this.parentNode.parentNode.removeChild(this.parentNode);" />
                            </aside>
                        </c:forEach>
                    </c:if>
                    <%-- moreFields() add a new text input here --%>
                    <span id="writeroot"></span>
                </section>

                <input type="button" name="addMoreFields" onclick="moreFields()" value="Add a choice." />

                <input type="submit" name="edit_paragraphe" value="Edit my paragraphe">
            </form>
        </section>
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
