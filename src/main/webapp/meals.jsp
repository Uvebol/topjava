<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<h4>
    <a href="mealsCreUpd.jsp">Add Meal</a>
</h4>
<table border="2" width="900">
    <thead bgcolor="aqua">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="meal" items="${mealToList}" varStatus="status">
        <tr style="color:${meal.excess ? 'red' : 'green'}">
            <td><javatime:format value="${meal.dateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=update&mealId=<c:out value="${meal.id}"></c:out>">Update</a></td>
            <td><a href="meals?action=delete&mealId=<c:out value="${meal.id}"></c:out>">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
