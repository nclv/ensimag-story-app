<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="utils.Path"%>

<sql:query var="listStories" dataSource="jdbc/story-app">
    select "story_id", "user_id", "open", "published" from "Story"
</sql:query>

<!doctype html>
<html lang="fr">

<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>User List</title>
</head>

<body>
    <header>
        
        <h1> show_stories.jsp </h1>
    </header>
    <main>
        <hr>
        <section>
            <table>
                <caption><h2>List of stories</h2></caption>
                <thead>
                    <tr>
                        <th>Story Id</th>
                        <th>User Id</th>
                        <th>Open</th>
                        <th>Published</th>
                    </tr>
                </thead>
                <c:forEach var="story" items="${listStories.rows}">
                    <tr>
                        <td><c:out value="${story.story_id}" /></td>
                        <td><c:out value="${story.user_id}" /></td>
                        <td><c:out value="${story.open}" /></td>
                        <td><c:out value="${story.published}" /></td>
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
