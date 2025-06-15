<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 5/31/2025
  Time: 11:46 PM
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">
    <!-- Kế thừa hoặc nhúng lại CSS từ trang chính --> </head>
<body>
<jsp:include page="header.jsp"/> <!-- SIDEBAR --> <!-- Tách sidebar thành file riêng để tái sử dụng -->
<!-- dashboard -->
<div class="container">
    <jsp:include page="sale-sidebar.jsp" />
    <div class="main-content">
        <div class="dashboard-header">
            <div class="header-left">
                <button class="sidebar-toggle-main" onclick="toggleSidebar()">
                    <i class="fas fa-bars"></i>
                </button>
                <h1>Sale Dashboard</h1>
            </div>
            <div class="header-right">
                <div class="user-info-header">
                    <div class="user-details-header">
                        <div class="user-name-header">Xin chào, Nguyễn Văn A</div>
                    </div>
                    <div class="user-avatar-header">
                        <i class="fas fa-user"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="content-area">
            <h1>Chào mừng đến Sale Dashboard</h1>
            <p>Quản lý công việc sale tại đây</p>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>