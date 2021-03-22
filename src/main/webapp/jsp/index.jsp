<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
     
<sql:query var="listUsers" dataSource="jdbc/story-app">
    select "username", "password" from "User"
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
         <p> index.jsp </p>
    </header>
    <main>
        <hr>
        <section>
            <table>
                <caption><h2>List of users</h2></caption>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Password</th>
                    </tr>
                </thead>
                <c:forEach var="user" items="${listUsers.rows}">
                    <tr>
                        <td><c:out value="${user.username}" /></td>
                        <td><c:out value="${user.password}" /></td>
                    </tr>
                </c:forEach>
            </table>
        </section>
        <p> Pensez à vous <a href="jsp/register.jsp"> enregistrer </a>. </p>
    </main>
    <footer>
        <hr>
        <p>
            <small>@story-app</small>
        </p>
    </footer>
</body>

</html>
