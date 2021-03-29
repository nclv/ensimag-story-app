<%-- 
    Document   : add_paragraphe
    Created on : Mar 29, 2021, 5:04:51 PM
    Author     : vincent
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="utils.Path"%>

<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
    <head>
    <link rel="stylesheet" href="${context}/style.css">
    
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>Add Paragraphe</title>
</head>
    <body>
        <header>
            <nav>
                <a href="${context}${Path.REDIRECT_HOME}"><img alt="Logo" src="https://via.placeholder.com/200x70?text=Logo" height="70"></a>
            </nav>
            <h1> add_paragraphe.jsp </h1>
        </header>
    
        <main>
        <form action="${context}${Path.REDIRECT_ADD_PARAGRAPHE}" method="post">
            <strong>Title</strong>:<input type="text" name="title" value="${fn:escapeXml(param.title)}"><br>
            

            <textarea rows = "10" cols = "60" name = "first_paragraphe_content"> Enter paragraphe content here... </textarea>
            <p> Is your paragraphe <strong>final</strong>? </p>
            <input type="radio" id="final" name="is_final" value="final" checked>
            <label for="final">Yes</label>
            <input type="radio" id="non-final" name="is_final" value="non-final">
            <label for="non-final">No</label>

            <input type="submit" name="add" value="Add my paragraphe">
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
