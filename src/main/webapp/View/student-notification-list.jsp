<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 7/9/2025
  Time: 12:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lịch sử thông báo - TALENT01</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">

    <style>
        /* Notification Styles */
        /* Search section */
        .search-section {
            background: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .search-form {
            margin: 0;
        }

        .search-group label {
            display: block;
            font-weight: 600;
            margin-bottom: 8px;
            color: #333;
        }

        .search-input-group {
            display: flex;
            gap: 10px;
            max-width: 500px;
        }

        .search-input-group input {
            flex: 1;
            padding: 10px 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .btn-search, .btn-clear {
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }

        .btn-search {
            background: #007bff;
            color: white;
        }

        .btn-search:hover {
            background: #0056b3;
        }

        .btn-clear {
            background: #6c757d;
            color: white;
        }

        .btn-clear:hover {
            background: #545b62;
            text-decoration: none;
            color: white;
        }

        .btn-delete {
            background: #dc3545;
            color: white;
            padding: 8px 10px;
            border: none;
            border-radius: 4px;
            font-size: 12px;
            cursor: pointer;
            margin-left: 5px;
        }

        .btn-delete:hover {
            background: #c82333;
        }

        /* Notification header styling */
        .notification-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
            flex-wrap: wrap;
            gap: 15px;
        }

        .notification-header-left {
            display: flex;
            align-items: center;
        }

        .notification-header-right {
            display: flex;
            align-items: center;
        }

        .btn-mark-all-read {
            background: #28a745;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            font-size: 14px;
            cursor: pointer;
            transition: background-color 0.2s ease;
        }

        .btn-mark-all-read:hover {
            background: #1e7e34;
        }

        .notification-container {
            margin-bottom: 20px;
        }

        .notification-header {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }

        .notification-title {
            font-size: 24px;
            font-weight: bold;
            color: #333;
            margin: 0;
        }

        .notification-badge {
            background: #28a745;
            color: white;
            padding: 2px 8px;
            border-radius: 12px;
            font-size: 12px;
            margin-left: 10px;
        }

        .notification-list {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .notification-item {
            display: flex;
            padding: 15px;
            background: white;
            margin-bottom: 10px;
            border-radius: 6px;
            border: 1px solid #eee;
            transition: box-shadow 0.2s ease;
        }

        .notification-item:hover {
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .notification-item.unread {
            border-left: 4px solid #007bff;
            background: #f0f8ff;
        }

        .notification-icon {
            margin-right: 15px;
            font-size: 24px;
        }

        .notification-icon.unread {
            color: #007bff;
        }

        .notification-icon.read {
            color: #6c757d;
        }

        .notification-content {
            flex: 1;
        }

        .notification-content-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 5px;
        }

        .notification-content-title {
            font-weight: bold;
            color: #333;
            margin: 0;
        }

        .notification-number {
            font-size: 10px;
            color: #999;
            margin-left: 10px;
        }

        .notification-content-text {
            color: #666;
            margin-bottom: 8px;
            line-height: 1.4;
        }

        .notification-meta {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .notification-time {
            font-size: 11px;
            color: #aaa;
        }

        .notification-status {
            font-size: 11px;
        }

        .status-read {
            color: #28a745;
            background: #d4edda;
            padding: 2px 6px;
            border-radius: 3px;
        }

        .status-unread {
            color: #dc3545;
            background: #f8d7da;
            padding: 2px 6px;
            border-radius: 3px;
        }

        .notification-actions {
            display: flex;
            align-items: center;
            margin-left: 15px;
        }

        .btn-view {
            background: #007bff;
            color: white;
            padding: 8px 15px;
            text-decoration: none;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
            margin-right: 5px;
            transition: background-color 0.2s ease;
        }

        .btn-view:hover {
            background: #0056b3;
            color: white;
            text-decoration: none;
        }

        .btn-mark-read {
            background: #28a745;
            color: white;
            padding: 8px 12px;
            border: none;
            border-radius: 4px;
            font-size: 12px;
            cursor: pointer;
            transition: background-color 0.2s ease;
        }

        .btn-mark-read:hover {
            background: #1e7e34;
        }

        .empty-state {
            padding: 40px 15px;
            background: #f8f9fa;
            border-radius: 6px;
            text-align: center;
            color: #666;
        }

        .empty-state-icon {
            font-size: 48px;
            margin-bottom: 10px;
            color: #ccc;
        }

        .back-link {
            color: #007bff;
            text-decoration: none;
            font-weight: bold;
            margin-bottom: 20px;
            display: inline-block;
        }

        .back-link:hover {
            color: #0056b3;
            text-decoration: none;
        }

        .load-more-container {
            text-align: center;
            margin-top: 20px;
        }

        .btn-load-more {
            background: #6c757d;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            font-size: 14px;
            cursor: pointer;
            transition: background-color 0.2s ease;
        }

        .btn-load-more:hover {
            background: #545b62;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <jsp:include page="student-sidebar.jsp"/>
    <div class="main-content">
        <div class="notification-container">
            <h1>📋 Lịch sử thông báo</h1><br>
            <a href="${pageContext.request.contextPath}/StudentDashboard" class="back-link">
                <i class="fas fa-arrow-left"></i> Quay lại Dashboard
            </a>
        </div>
        <div class="search-section">
            <form action="StudentDashboard" method="get" class="search-form">
                <input type="hidden" name="action" value="notifications">
                <div class="search-group">
                    <label for="searchInput">
                        <i class="fas fa-search"></i> Tìm kiếm thông báo
                    </label>
                    <div class="search-input-group">
                        <input type="text" name="keyword" id="searchInput"
                               placeholder="Nhập từ khóa..." value="${keyword}">
                        <button type="submit" class="btn-search">
                            <i class="fas fa-search"></i> Tìm
                        </button>
                        <a href="StudentDashboard?action=notifications" class="btn-clear">
                            <i class="fas fa-times"></i> Xóa
                        </a>
                    </div>
                </div>
            </form>
        </div>
        <div class="student-notifications">
            <div class="notification-header">
                <div class="notification-header-left">
                    <h2 class="notification-title">🔔 Tất cả thông báo</h2>
                    <c:if test="${not empty allNotifications}">
                <span class="notification-badge">
                    ${allNotifications.size()} thông báo
                </span>
                    </c:if>
                </div>

                <!-- button đánh dấu tất cả đã đọc -->
                <div class="notification-header-right">
                    <c:set var="unreadCount" value="0"/>
                    <c:forEach var="notification" items="${allNotifications}">
                        <c:if test="${!notification.read}">
                            <c:set var="unreadCount" value="${unreadCount + 1}"/>
                        </c:if>
                    </c:forEach>

                    <c:if test="${unreadCount > 0}">
                        <form method="post" action="StudentDashboard" style="margin: 0;">
                            <input type="hidden" name="action" value="markAllAsRead">
                            <button type="submit" class="btn-mark-all-read"
                                    onclick="return confirm('Đánh dấu tất cả ${unreadCount} thông báo là đã đọc?')">
                                <i class="fas fa-check-double"></i>
                                Đánh dấu tất cả đã đọc (${unreadCount})
                            </button>
                        </form>
                    </c:if>

                    <c:if test="${unreadCount == 0 && not empty allNotifications}">
                <span style="color: #28a745; font-weight: 600;">
                    <i class="fas fa-check-circle"></i> Tất cả đã đọc
                </span>
                    </c:if>
                </div>
            </div>

            <ul class="notification-list">
                <c:choose>
                    <c:when test="${empty allNotifications}">
                        <li class="empty-state">
                            <div class="empty-state-icon">
                                <i class="fas fa-inbox"></i>
                            </div>
                            <div>Chưa có thông báo nào</div>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="notification" items="${allNotifications}" varStatus="status">
                            <li class="notification-item ${!notification.read ? 'unread' : ''}">
                                <!-- Icon thông báo -->
                                <div class="notification-icon ${!notification.read ? 'unread' : 'read'}">
                                        ${!notification.read ? '📋' : '📄'}
                                </div>

                                <!-- Nội dung thông báo -->
                                <div class="notification-content">
                                    <div class="notification-content-header">
                                        <h3 class="notification-content-title">
                                                ${notification.title}
                                        </h3>
                                        <span class="notification-number">
                                            #${status.index + 1}
                                        </span>
                                    </div>

                                    <div class="notification-content-text">
                                            ${notification.content}
                                    </div>

                                    <div class="notification-meta">
                                        <div class="notification-time">
                                            <i class="fas fa-clock"></i>
                                            <fmt:formatDate value="${notification.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                        </div>

                                        <div class="notification-status">
                                            <c:choose>
                                                <c:when test="${notification.read}">
                                                    <span class="status-read">
                                                        <i class="fas fa-check"></i> Đã đọc
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="status-unread">
                                                        <i class="fas fa-circle"></i> Chưa đọc
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>

                                <!-- Actions -->
                                <div class="notification-actions">
                                    <c:if test="${not empty notification.relatedEntityId}">
                                        <a href="StudentApplication?action=list&id=${notification.relatedEntityId}" class="btn-view">
                                            <i class="fas fa-eye"></i> Xem
                                        </a>
                                    </c:if>

                                    <c:if test="${!notification.read}">
                                        <form method="post" action="StudentDashboard" style="margin: 0; display: inline;">
                                            <input type="hidden" name="action" value="markAsRead">
                                            <input type="hidden" name="notificationId" value="${notification.id}">
                                            <button type="submit" class="btn-mark-read">
                                                <i class="fas fa-check"></i>
                                            </button>
                                        </form>
                                    </c:if>

                                    <form action="StudentDashboard" method="post" style="display: inline;"
                                          onsubmit="return confirm('Bạn có chắc muốn xóa thông báo này?')">
                                        <input type="hidden" name="action" value="deleteNotification">
                                        <input type="hidden" name="notificationId" value="${notification.id}">
                                        <button type="submit" class="btn-delete">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </form>
                                </div>
                            </li>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </ul>

            <!-- Load more button -->
            <c:if test="${not empty allNotifications && allNotifications.size() >= 20}">
                <div class="load-more-container">
                    <button class="btn-load-more">
                        <i class="fas fa-plus"></i> Tải thêm thông báo
                    </button>
                </div>
            </c:if>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
