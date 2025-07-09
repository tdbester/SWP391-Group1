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
                    <a href="${pageContext.request.contextPath}/View/student-attendance-report.jsp"
                       class="sale-nav-btn">
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
                    <div class="student-notifications">
                        <h3>🔔 Thông báo mới
                            <c:if test="${unreadCount > 0}">
                <span style="background: #dc3545; color: white; padding: 2px 8px;
                             border-radius: 12px; font-size: 12px; margin-left: 10px;">
                        ${unreadCount}
                </span>
                            </c:if>
                        </h3>

                        <ul class="student-notification-list" style="list-style: none; padding: 0;">
                            <c:choose>
                                <c:when test="${empty latestNotifications}">
                                    <li style="padding: 15px; background: #f8f9fa; margin-bottom: 10px;
                       border-radius: 6px; text-align: center; color: #666;">
                                        Chưa có thông báo mới
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="notification" items="${latestNotifications}">
                                        <li style="display: flex; padding: 15px; background: white;
                                                margin-bottom: 10px; border-radius: 6px; border: 1px solid #eee;
                                        <c:if test='${!notification.read}'>
                                                border-left: 4px solid #7a6ad8; background: #f8f9ff;
                                                </c:if>">
                                            <div style="margin-right: 15px; font-size: 24px;">
                                                <c:choose>
                                                    <c:when test="${notification.notificationType eq 'REQUEST_UPDATE'}">
                                                        📝
                                                    </c:when>
                                                    <c:when test="${notification.notificationType eq 'ACCOUNT_CREATED'}">
                                                        👨‍🎓
                                                    </c:when>
                                                    <c:when test="${notification.notificationType eq 'SCHEDULE_UPDATE'}">
                                                        📅
                                                    </c:when>
                                                    <c:otherwise>
                                                        📢
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div style="flex: 1;">
                                                <div style="font-weight: bold; color: #333; margin-bottom: 5px;">
                                                        ${notification.title}
                                                </div>
                                                <div style="color: #666; margin-bottom: 8px; line-height: 1.4;">
                                                        ${notification.content}
                                                </div>
                                                <div style="font-size: 11px; color: #aaa;">
                                                    <i class="fas fa-clock"></i>
                                                    <fmt:formatDate value="${notification.createdAt}"
                                                                    pattern="dd/MM/yyyy HH:mm"/>
                                                </div>
                                            </div>
                                            <div style="display: flex; align-items: center;">
                                                <c:if test="${!notification.read}">
                                                    <form method="post" action="StudentDashboard" style="margin: 0;">
                                                        <input type="hidden" name="action" value="markAsRead">
                                                        <input type="hidden" name="notificationId" value="${notification.id}">

                                                    </form>
                                                </c:if>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </ul>

                        <div style="text-align: center; margin-top: 15px;">
                            <a href="${pageContext.request.contextPath}/StudentDashboard?action=notifications"
                               style="color: #7a6ad8; text-decoration: none; font-weight: bold; font-size: 14px;">
                                <i class="fas fa-list"></i> Xem tất cả thông báo
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>