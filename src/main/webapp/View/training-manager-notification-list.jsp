<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 7/11/2025
  Time: 1:55 AM
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/notification-list.css">

</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <jsp:include page="training-manager-sidebar.jsp"/>
    <div class="main-content">
        <div class="notification-container">
            <h1>📋 Lịch sử thông báo</h1><br>
            <a href="${pageContext.request.contextPath}/TrainingManagerDashboard" class="back-link">
                <i class="fas fa-arrow-left"></i> Quay lại Dashboard
            </a>
        </div>
        <div class="search-section">
            <form action="TrainingManagerDashboard" method="get" class="search-form">
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
                        <a href="TrainingManagerDashboard?action=notifications" class="btn-clear">
                            <i class="fas fa-times"></i> Xóa
                        </a>
                    </div>
                </div>
            </form>
        </div>

        <div class="training-manager-notifications">
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
                        <form method="post" action="TrainingManagerDashboard" style="margin: 0;">
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
                                    <c:choose>
                                        <c:when test="${notification.notificationType == 'STUDENT_REQUEST'}">
                                            ${!notification.read ? '📝' : '📄'}
                                        </c:when>
                                        <c:when test="${notification.notificationType == 'ACCOUNT_CREATION_REQUEST'}">
                                            ${!notification.read ? '👤' : '👥'}
                                        </c:when>
                                        <c:otherwise>
                                            ${!notification.read ? '📋' : '📄'}
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <!-- Nội dung thông báo -->
                                <div class="notification-content">
                                    <div class="notification-content-header">
                                        <h3 class="notification-content-title">
                                                ${notification.title}
                                        </h3>
                                        <div>
                                            <c:if test="${notification.notificationType == 'STUDENT_REQUEST'}">
                                                <span class="notification-type student-request">Đơn học viên</span>
                                            </c:if>
                                            <c:if test="${notification.notificationType == 'ACCOUNT_CREATION_REQUEST'}">
                                                <span class="notification-type account-creation">Tạo tài khoản</span>
                                            </c:if>
                                            <span class="notification-number">
                                                #${status.index + 1}
                                            </span>
                                        </div>
                                    </div>

                                    <div class="notification-content-text">
                                            ${notification.content}
                                    </div>

                                    <div class="notification-meta">
                                        <div class="notification-time">
                                            <i class="fas fa-clock"></i>
                                            <fmt:formatDate value="${notification.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                            <c:if test="${not empty notification.senderName}">
                                                | Từ: ${notification.senderName}
                                            </c:if>
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
                                        <c:choose>
                                            <c:when test="${notification.notificationType == 'STUDENT_REQUEST'}">
                                                <a href="RequestManagement?action=view&id=${notification.relatedEntityId}" class="btn-view">
                                                    <i class="fas fa-eye"></i> Xem đơn
                                                </a>
                                            </c:when>
                                            <c:when test="${notification.notificationType == 'ACCOUNT_CREATION_REQUEST'}">
                                                <a href="AccountManagement?action=pending&id=${notification.relatedEntityId}" class="btn-view account-request">
                                                    <i class="fas fa-user-plus"></i> Xử lý
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="#" class="btn-view">
                                                    <i class="fas fa-eye"></i> Xem
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>

                                    <c:if test="${!notification.read}">
                                        <form action="TrainingManagerDashboard" method="post" style="display: inline;">
                                            <input type="hidden" name="action" value="markAsRead">
                                            <input type="hidden" name="notificationId" value="${notification.id}">
                                            <button type="submit" class="btn-mark-read">
                                                <i class="fas fa-check"></i>
                                            </button>
                                        </form>
                                    </c:if>

                                    <form action="TrainingManagerDashboard" method="post" style="display: inline;"
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
            <div class="d-flex justify-content-center mt-4">
                <nav>
                    <ul class="pagination">
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                <a class="page-link" href="TrainingManagerDashboard?action=notifications&page=${i}">${i}</a>
                            </li>
                        </c:forEach>
                    </ul>
                </nav>
            </div>

        </div>
    </div>
</div>


<jsp:include page="footer.jsp"/>
</body>
</html>