<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 5/31/2025
  Time: 11:46 PM
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
<%--    *  Created on:        2025-05-31--%>
<%--    *  Author:            Cù Thị Huyền Trang--%>
<%--    *--%>
<%--    *  ========================== Change History ==========================--%>
<%--    *  Date        | Author               | Description--%>
<%--    *  ------------|----------------------|----------------------------------%>
<%--    *  2025-05-31  | Cù Thị Huyền Trang   | Initial creation--%>
<%--    */--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sale Dashboard - TALENT01</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<!-- dashboard -->
<div class="container">
    <jsp:include page="sale-sidebar.jsp"/>
    <div class="main-content">
        <div class="welcome-section">
            <div class="welcome-card">
                <div class="welcome-text">
                    <h1>Xin chào nhân viên Sale Nguyen Van A!</h1>
                    <p>Hôm nay là ngày tuyệt vời để làm việc</p>
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
                <h2>Điều hướng nhanh</h2>
                <div class="sale-nav-buttons">
                    <a href="consultation-list.jsp" class="sale-nav-btn"><i class="fas fa-users"></i>Xem danh sách tư
                        vấn</a>
                    <a href="blog.jsp" class="sale-nav-btn"><i class="fas fa-user-plus"></i>Viết blog</a>
                    <a href="student-account-request.jsp" class="sale-nav-btn"><i class="fas fa-book-open"></i>Yêu cầu
                        cấp tài khoản học viên</a>
                    <a href="" class="sale-nav-btn"><i class="fas fa-book-open"></i>Xem danh sách khoá học</a>
                </div>
            </div>
            <div class="sale-notifications">
                <h2>🔔 Thông báo mới
                    <c:if test="${unreadCount > 0}">
            <span style="background: #dc3545; color: white; padding: 2px 8px;
                         border-radius: 12px; font-size: 12px; margin-left: 10px;">
                    ${unreadCount}
            </span>
                    </c:if>
                </h2>

                <ul class="sale-notification-list" style="list-style: none; padding: 0;">
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
                                        border-left: 4px solid #007bff; background: #f0f8ff;
                                        </c:if>">
                                    <div style="margin-right: 15px; font-size: 24px;">📋</div>
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
                                        <c:if test="${not empty notification.relatedEntityId}">
                                            <a href="Consultation?action=list&id=${notification.relatedEntityId}"
                                               style="background: #007bff; color: white; padding: 8px 15px;
                                          text-decoration: none; border-radius: 4px;
                                          font-size: 12px; font-weight: bold;">
                                                <i class="fas fa-eye"></i> Xem
                                            </a>
                                        </c:if>
                                    </div>
                                </li>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </ul>

                <div style="text-align: center; margin-top: 15px;">
                    <a href="${pageContext.request.contextPath}/SaleDashboard?action=notifications"
                       style="color: #007bff; text-decoration: none; font-weight: bold; font-size: 14px;">
                        <i class="fas fa-list"></i> Xem tất cả thông báo
                    </a>
                </div>
            </div>

            <div class="sale-new-courses">
                <h2>📋 Khoá học mới cần tư vấn</h2>
                <table class="sale-course-table">
                    <thead>
                    <tr>
                        <th>Khoá học</th>
                        <th>Giá</th>
                        <th>Số lượng lớp</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty latestCoursesWithClass}">
                            <tr>
                                <td colspan="4" style="text-align: center; color: #666;">
                                    Chưa có khóa học nào
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="course" items="${latestCoursesWithClass}">
                                <tr>
                                    <td>
                                        <strong>${course.title}</strong>
                                        <c:if test="${not empty course.information}">
                                            <br><small style="color: #666;">${course.information}</small>
                                        </c:if>
                                    </td>
                                    <td>
                                <span style="font-weight: bold; color: #007bff;">
                                    <fmt:formatNumber value="${course.price}" type="currency" currencySymbol="₫"/>
                                </span>
                                    </td>
                                    <td>
                                <span class="class-count-badge"
                                      style="background: ${course.classCount > 0 ? '#28a745' : '#dc3545'};
                                              color: white; padding: 4px 8px; border-radius: 12px; font-size: 12px;">
                                    ${course.classCount} lớp
                                </span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${course.classCount > 0}">
                                                <a href="SaleDashboard?action=course&courseId=${course.id}"
                                                   class="btn-consult"
                                                   style="background: #007bff; color: white; padding: 6px 12px;
                                                  text-decoration: none; border-radius: 4px; font-size: 12px;">
                                                    <i class="fas fa-comments"></i> Chi tiết
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: #999; font-size: 12px;">Chưa có lớp</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>