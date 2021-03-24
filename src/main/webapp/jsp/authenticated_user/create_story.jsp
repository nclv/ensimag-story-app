<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="utils.Path"%>

<!doctype html>
<html lang="fr">

<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
    
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>Create Story</title>
</head>

<body>
    <header>
         <p> create_story.jsp </p>
    </header>
    <main>
        <form action=<%= Path.REDIRECT_CREATE_STORY %> method="post">
            <strong>Title</strong>:<input type="text" name="title" value="${fn:escapeXml(param.title)}"><br>
            <input type="submit" value="CreateStory">
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
