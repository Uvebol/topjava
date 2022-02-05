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
    <a href="">Add Meal</a>
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
        <tr>
            <c:choose>
                <c:when test="${mealToList.get(status.index).isExcess()==true}">
                    <td><font color="red"><javatime:format
                            value="${mealToList.get(status.index).getDateTime()}"
                            pattern="yyyy-MM-dd HH:mm"/>
                    </font></td>
                    <td><font color="red">${mealToList.get(status.index).getDescription()}</font></td>
                    <td><font color="red">${mealToList.get(status.index).getCalories()}</font></td>
                </c:when>
                <c:otherwise>
                    <td><font color="green"><javatime:format
                            value="${mealToList.get(status.index).getDateTime()}"
                            pattern="yyyy-MM-dd HH:mm"/>
                    </font></td>
                    <td><font color="green">${mealToList.get(status.index).getDescription()}</font></td>
                    <td><font color="green">${mealToList.get(status.index).getCalories()}</font></td>
                </c:otherwise>
            </c:choose>
            <td><a href="">Update</a></td>
            <td><a href="">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
