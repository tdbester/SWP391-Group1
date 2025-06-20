<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thời Khóa Biểu Tuần - TALENT01</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/schedule.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/teacher-sidebar.css">
</head>
<body>
<!-- Header -->
<div class="header">
    <div class="logo">TALENT01</div>
    <div class="nav">
        <a href="${pageContext.request.contextPath}/View/home.jsp">Trang Chủ</a>
        <a href="#">Dịch Vụ</a>
        <a href="#">Khóa Học</a>
        <a href="${pageContext.request.contextPath}/View/profile.jsp">Trang cá nhân</a>
        <a href="<%=request.getContextPath()%>/logout">Đăng Xuất</a>
    </div>
</div>

<!-- Sidebar -->
<%@ include file="teacher-sidebar.jsp" %>

<!-- Main Content -->
<div class="main-content">
    <div class="container">
        <h1>Thời Khóa Biểu</h1>

        <!-- Schedule Table -->
        <div class="schedule-table">
            <table>
                <thead>
                <tr>
                    <th class="slot-header">SLOT</th>
                    <th class="day-header">Thứ 2<br><span class="date"></span></th>
                    <th class="day-header">Thứ 3<br><span class="date"></span></th>
                    <th class="day-header">Thứ 4<br><span class="date"></span></th>
                    <th class="day-header">Thứ 5<br><span class="date"></span></th>
                    <th class="day-header">Thứ 6<br><span class="date"></span></th>
                    <th class="day-header">Thứ 7<br><span class="date"></span></th>
                    <th class="day-header">Chủ nhật<br><span class="date"></span></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="slot-cell"></td>

                    <td class="schedule-cell">

                        -

                        <div class="class-item">
                            <div class="class-code"></div>
                            <div class="class-info">
                                Lớp: <br>
                                Phòng: <br>
                                <span class="time-slot">(-)</span>
                            </div>
                        </div>

                    </td>

                </tr>

                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Footer -->
<div class="footer">
    <p>Copyright © 2025 Talent Center Management. SWP391-Group 01.</p>
</div>

</body>
</html>