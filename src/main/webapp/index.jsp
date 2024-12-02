<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quest Game</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
</head>
<body>
<div class="container">
    <h2>${currentSituation.getDescription()}</h2>
    <div class="button-container">
        <c:forEach var="choice" items="${currentSituation.getChoices()}" varStatus="status">
            <button onclick="window.location='/story?index=${status.index}'">${choice.getDescription()}</button>
        </c:forEach>
        <button onclick="window.location='/story?index=-1'">go Back</button>
    </div>
</div>
</body>
</html>