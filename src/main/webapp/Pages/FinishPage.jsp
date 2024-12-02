<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Finish</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
</head>
<body>
    <div class="container">
       <c:if test="${finishType == 'victory'}">
           <h2>Congrats, you won!</h2>
       </c:if>
        <c:if test="${finishType == 'fail'}">
            <h2>You lost</h2>
        </c:if>
        <p>${currentSituation.getDescription()}</p>
        <button onclick="window.location='/restart'">Start Again</button>
        <button onclick="window.location='/restartStory'">Start again with this story</button>
        <button onclick="window.location='/download'">Download this story</button>
    </div>
</body>
</html>
