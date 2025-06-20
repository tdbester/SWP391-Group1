<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/12/2025
  Time: 10:08 PM
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">
    <!-- Kế thừa hoặc nhúng lại CSS từ trang chính -->
    <style>
        /* Override để căn giữa content */
        .content-area {
            background-color: white;
            padding: 40px 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            text-align: center;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            min-height: 70vh;
        }

        .content-area h1 {
            margin-bottom: 20px;
            color: #333;
            font-size: 2.5rem;
        }

        .content-area p {
            color: #666;
            font-size: 18px;
            max-width: 600px;
            line-height: 1.6;
        }

        /* Đảm bảo container layout đúng */
        .container {
            display: flex;
            min-height: 100vh;
        }

        .main-content {
            flex: 1;
            padding: 30px;
        }

        /* Header styles */
        .dashboard-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 20px 0;
            border-bottom: 1px solid #eee;
            margin-bottom: 30px;
        }

        .header-left {
            display: flex;
            align-items: center;
        }

        .header-left h1 {
            margin: 0 20px;
            font-size: 1.8rem;
            color: #333;
            font-weight: 600;
        }

        .sidebar-toggle-main {
            background: #8d78e4;
            border: none;
            color: white;
            padding: 10px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 1rem;
            margin-right: 15px;
            transition: all 0.3s ease;
        }

        .sidebar-toggle-main:hover {
            background: #7a6ad8;
        }

        .header-right {
            display: flex;
            align-items: center;
        }

        .user-info-header {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .user-details-header {
            text-align: right;
        }

        .user-name-header {
            font-size: 0.9rem;
            font-weight: 600;
            color: #333;
            margin-bottom: 2px;
        }

        .user-role-header {
            font-size: 0.75rem;
            color: #666;
        }

        .user-avatar-header {
            width: 35px;
            height: 35px;
            border-radius: 50%;
            background: linear-gradient(135deg, #8d78e4, #7a6ad8);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 0.9rem;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .main-content {
                margin-left: 0;
            }

            .user-details-header {
                display: none;
            }
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />

<div class="container">
    <jsp:include page="student-sidebar.jsp" />
    <div class="main-content">
        <div class="dashboard-header">
            <div class="header-left">
                <button class="sidebar-toggle-main" onclick="toggleSidebar()">
                    <i class="fas fa-bars"></i>
                </button>
                <h1>Student Dashboard</h1>
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
            <h1>Chào mừng đến Dashboard học sinh</h1>
            <p>Xem thông tin lịch học, khoá học,... tại đây</p>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp" />
</body>
</html>