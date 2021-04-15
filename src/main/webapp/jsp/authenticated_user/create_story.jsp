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

    <script type="text/javascript" src="${context}/utils.js"></script>
</head>

<body>
    <script>0</script>
    <header>
        <nav>
            <a href="${context}${Path.REDIRECT_HOME}"><img alt="Logo" src="${context}/logo.png" height="70"></a>
        </nav>
        <h1> create_story.jsp </h1>
    </header>
    <main>
        <section>
            <form action="${context}${Path.REDIRECT_CREATE_STORY}" method="post">
                <strong>Story Title</strong>:<input type="text" name="story_title" value="${param.story_title}" required><br>
                
                <p> Is your story <strong>open</strong> or <strong>private</strong>? </p>
                <input type="radio" id="open" name="open" value="open" required ${param.open eq "open" ? "checked": ""}>
                <label for="open">Open</label>
                <input type="radio" id="private" name="open" value="private" ${param.open eq "private" ? "checked": ""}>
                <label for="private">Private</label>

                <textarea rows="10" cols="60" name="paragraphe_content" placeholder="Enter your first paragraphe content..." required>${not empty param.paragraphe_content ? param.paragraphe_content: ""}</textarea>
                
                <p> Is your paragraphe <strong>final</strong>? </p>
                <input type="radio" id="final" name="is_final" value="final" required ${param.is_final eq "final" ? "checked": ""}>
                <label for="final">Yes</label>
                <input type="radio" id="non-final" name="is_final" value="non-final" ${param.is_final eq "non-final" ? "checked": ""}>
                <label for="non-final">No</label>

                <%-- Invisible choice text input --%>
                <div id="readroot" style="display: none">
                    <label for="choice">Enter choice text:</label>
                    <input type="text" id="name" name="choice" size="28">
                    <input type="button" value="Remove choice" onclick="this.parentNode.parentNode.parentNode.removeChild(this.parentNode.parentNode);" />
                </div>

                <section>
                    <%-- Load previous choices input --%>
                    <c:if test="${not empty choices}">
                        <c:forEach var="choice" items="${choices}" varStatus="loop">
                            <aside>
                                <label for="choice">Enter choice text:</label>
                                <input type="text" id="name" name="choice_${loop.index}" size="28" value="${choice}">
                                <input type="button" value="Remove choice" onclick="this.parentNode.parentNode.removeChild(this.parentNode);" />
                            </aside>
                        </c:forEach>
                    </c:if>
                    <%-- moreFields() add a new text input here --%>
                    <span id="writeroot"></span>
                </section>

                <input type="button" name="addMoreFields" onclick="moreFields()" value="Add a choice." />

                <input type="submit" name="create" value="Create my Story">
                <input type="submit" name="create_and_publish" value="Create and Publish my Story">
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
