<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/12/2025
  Time: 10:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%--/*--%>
<%--*  Copyright (C) 2025 <Group 1>--%>
<%--    *  All rights reserved.--%>
<%--    *--%>
<%--    *  This file is part of the <Talent Center Management> project.--%>
<%--    *  Unauthorized copying of this file, via any medium is strictly prohibited.--%>
<%--    *  Proprietary and confidential.--%>
<%--    *--%>
<%--    *  Created on:        2025-06-12--%>
<%--    *  Author:            Cù Thị Huyền Trang--%>
<%--    *--%>
<%--    *  ========================== Change History ==========================--%>
<%--    *  Date        | Author               | Description--%>
<%--    *  ------------|----------------------|----------------------------------%>
<%--    *  2025-06-12  | Cù Thị Huyền Trang   | Initial creation--%>
<%--    */--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard - TALENT01</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container">
    <jsp:include page="student-sidebar.jsp"/>
    <div class="main-content">
        <div class="welcome-section">
            <div class="welcome-card">
                <div class="welcome-text">
                    <h1>Xin chào học sinh Nguyen Van A</h1>
                    <p>Hôm nay là ngày tuyệt vời để học</p>
                    <div class="current-time">
                        <i class="fas fa-calendar-alt"></i>
                        <span id="currentDate"></span>
                    </div>
                </div>
                <div class="welcome-avatar">
                    <a href="<%=request.getContextPath()%>/profile">
                        <i class="fas fa-user-circle"></i>
                    </a>
                </div>
            </div>
        </div>
        <div class="content-area">
            <div class="sale-quick-nav">
                <h2><i class="fas fa-bolt"></i> Thao tác nhanh</h2>
                <div class="sale-nav-buttons">
                    <a href="${pageContext.request.contextPath}/View/student-schedule.jsp" class="sale-nav-btn">
                        <i class="fas fa-calendar-alt"></i>
                        Xem lịch học
                    </a>
                    <a href="${pageContext.request.contextPath}/View/student-attendance-report.jsp" class="sale-nav-btn">
                        <i class="fas fa-user-check"></i>
                        Báo cáo điểm danh
                    </a>
                    <a href="${pageContext.request.contextPath}/View/student-request.jsp" class="sale-nav-btn">
                        <i class="fas fa-paper-plane"></i>
                        Gửi đơn
                    </a>
                    <a href="${pageContext.request.contextPath}/View/student-request-list.jsp" class="sale-nav-btn">
                        <i class="fas fa-paper-plane"></i>
                        Xem đơn
                    </a>
                </div>
            </div>

            <!-- Content Grid -->
            <div class="content-grid">
                <!-- Today's Schedule -->
                <div class="card">
                    <h3 class="card-title"><i class="fas fa-clock"></i> Lịch học hôm nay</h3>
                    <c:choose>
                        <c:when test="${not empty todaySchedules}">
                            <c:forEach var="s" items="${todaySchedules}">
                                <div class="schedule-item">
                                    <div class="schedule-time">
                                            ${fn:substring(s.slotStartTime, 0, 5)}
                                    </div>
                                    <div class="schedule-info">
                                        <div class="schedule-title">${s.courseTitle}</div>
                                        <div class="schedule-details">
                                            Phòng ${s.roomCode} - GV: ${s.teacherName}
                                        </div>
                                    </div>
                                    <div class="schedule-actions">
                                        <button class="btn-primary">Sắp tới</button>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="schedule-item">
                                <p>Không có lịch học hôm nay.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="card">
                    <h3 class="card-title"><i class="fas fa-bell"></i> Thông báo mới</h3>

                    <div class="request-item">
                        <div class="request-title">
                            <i class="fas fa-info-circle"></i> Thông báo nghỉ học
                        </div>
                        <div class="request-date">2 giờ trước</div>
                        <div class="request-status approved">Mới</div>
                    </div>
                    <div class="request-item">
                        <div class="request-title">
                            <i class="fas fa-check-circle"></i> Điểm kiểm tra đã cập nhật
                        </div>
                        <div class="request-date">3 ngày trước</div>
                        <div class="request-status approved">Đã xem</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>