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
    
    <title>Create Story</title>
</head>

<body>
    <header>
         <h1> create_story.jsp </h1>
    </header>
    <main>
        <form action="${context}${Path.REDIRECT_CREATE_STORY}" method="post">
            <strong>Title</strong>:<input type="text" name="title" value="${fn:escapeXml(param.title)}"><br>
            
            <p> Is your story <strong>open</strong> or <strong>private</strong>? </p>
            <input type="radio" id="open" name="open" value="open" checked>
            <label for="open">Open</label>
            <input type="radio" id="private" name="open" value="private">
            <label for="private">Private</label>

            <textarea rows = "10" cols = "60" name = "first_paragraphe_content"> Enter first paragraphe content here... </textarea>
            <p> Is your paragraphe <strong>final</strong>? </p>
            <input type="radio" id="final" name="is_final" value="final" checked>
            <label for="final">Yes</label>
            <input type="radio" id="non-final" name="is_final" value="non-final">
            <label for="non-final">No</label>

            <input type="submit" name="create" value="Create my Story">
            <input type="submit" name="create_and_publish" value="Create and Publish my Story">
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
