<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thông báo - TALENT01</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/teacher-notification.css">
</head>
<body>
<!-- Include Header -->
<%@ include file="../View/header.jsp" %>

<div class="main-container">
    <!-- Include Teacher Sidebar -->
    <div class="sidebar-col">
        <%@ include file="../View/teacher-sidebar.jsp" %>
    </div>


<!-- Include Footer -->
<%@ include file="../View/footer.jsp" %>

</body>
</html>
