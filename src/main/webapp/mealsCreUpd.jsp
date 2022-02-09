<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.javawebinar.topjava.model.Meal" %>
<html>
<head>
    <title>Meal</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<form method="post" action="meals">
    <table>
        <tr style="background-color: lightgrey">
            <td>DateTime:</td>
            <td><input name="datetime" id="datetime" type="datetime-local" value="${oldMeal.dateTime}"></td>
        </tr>
        <tr style="background-color: lightgrey">
            <td>Description:</td>
            <td><input name="description" id="description" type="text" size="40%" value="${oldMeal.description}"></td>
        </tr>
        <tr style="background-color: lightgrey">
            <td>Calories:</td>
            <td><input name="calories" id="calories" type="text" value="${oldMeal.calories}"></td>
        </tr>
    </table>
    <br>
<a href="meals">
    <button name="save" value="${oldMeal.id}">Save</button>
</a>
<a href="meals">
    <button name="cancel" value="true">Cancel</button>
</a>
</form>
</body>
</html>
