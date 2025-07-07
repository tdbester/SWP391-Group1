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
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.example.talentcenter.model.Notification" %>
<%@ page import="java.util.ArrayList" %>

<%
    ArrayList<Notification> latestNotifications = (ArrayList<Notification>) request.getAttribute("latestNotifications");
    if (latestNotifications == null) latestNotifications = new ArrayList<>();

    Integer unreadCount = (Integer) request.getAttribute("unreadCount");
    if (unreadCount == null) unreadCount = 0;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>

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
    <jsp:include page="sale-sidebar.jsp" />
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
                    <a href="consultation-list.jsp" class="sale-nav-btn"><i class="fas fa-users"></i>Xem danh sách tư vấn</a>
                    <a href="blog.jsp" class="sale-nav-btn"><i class="fas fa-user-plus"></i>Viết blog</a>
                    <a href="student-account-request.jsp" class="sale-nav-btn"><i class="fas fa-book-open"></i>Yêu cầu cấp tài khoản học viên</a>
                    <a href="" class="sale-nav-btn"><i class="fas fa-book-open"></i>Xem danh sách khoá học</a>
                </div>
            </div>

            <div class="sale-notifications">
                <h2>🔔 Thông báo mới
                <% if (unreadCount > 0) { %>
                <span style="background: #dc3545; color: white; padding: 2px 8px; border-radius: 12px; font-size: 12px; margin-left: 10px;">
                            <%= unreadCount %>
                        </span>
                <% } %>
                </h2>
                <ul class="sale-notification-list" style="list-style: none; padding: 0;">
                    <% if (latestNotifications.isEmpty()) { %>
                    <li style="padding: 15px; background: #f8f9fa; margin-bottom: 10px; border-radius: 6px; text-align: center; color: #666;">
                        Chưa có thông báo mới
                    </li>
                    <% } else { %>
                    <% for (Notification notification : latestNotifications) { %>
                    <li style="display: flex; padding: 15px; background: white; margin-bottom: 10px; border-radius: 6px; border: 1px solid #eee; <%= !notification.isRead() ? "border-left: 4px solid #007bff; background: #f0f8ff;" : "" %>">
                        <div style="margin-right: 15px; font-size: 24px;">📋</div>
                        <div style="flex: 1;">
                            <div style="font-weight: bold; color: #333; margin-bottom: 5px;">
                                <%= notification.getTitle() %>
                            </div>
                            <div style="color: #666; margin-bottom: 8px; line-height: 1.4;">
                                <%= notification.getContent() %>
                            </div>
                            <div style="font-size: 11px; color: #aaa;">
                                <i class="fas fa-clock"></i> <%= dateFormat.format(notification.getCreatedAt()) %>
                            </div>
                        </div>
                        <div style="display: flex; align-items: center;">
                            <% if (notification.getRelatedEntityId() != null) { %>
                            <a href="Consultation?action=edit&id=<%= notification.getRelatedEntityId() %>"
                               style="background: #007bff; color: white; padding: 8px 15px; text-decoration: none; border-radius: 4px; font-size: 12px; font-weight: bold;">
                                <i class="fas fa-eye"></i> Xem
                            </a>
                            <% } %>
                        </div>
                    </li>
                    <% } %>
                    <% } %>
                </ul>

                <!-- Link xem tất cả thông báo -->
                <div style="text-align: center; margin-top: 15px;">
                    <a href="Consultation?action=notifications"
                       style="color: #007bff; text-decoration: none; font-weight: bold; font-size: 14px;">
                        <i class="fas fa-list"></i> Xem tất cả thông báo
                    </a>
                </div>
            </div>

            <div class="sale-new-courses">
                <h2>📋 Courses Needing Consultation</h2>
                <table class="sale-course-table">
                    <thead>
                    <tr>
                        <th>Course</th>
                        <th>Start Date</th>
                        <th>Slots Left</th>
                        <th>Interested Leads</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Piano for Beginners</td>
                        <td>10/07/2025</td>
                        <td>8</td>
                        <td>12</td>
                        <td><a href="#" class="sale-btn-view">View</a></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>