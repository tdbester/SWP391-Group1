<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/9/2025
  Time: 3:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">

</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <jsp:include page="training-manager-sidebar.jsp"/>
    <div class="main-content">
        <div class="welcome-section">
            <div class="welcome-card">
                <div class="welcome-text">
                    <h1>Xin chào quản lý đào tạo ${account.getFullName()}</h1>
                    <div class="current-time">
                        <i class="fas fa-calendar-alt"></i>
                        <fmt:formatDate value="${currentDate}" pattern="EEEE, dd MMMM yyyy"/>
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
                <h2>Quick Actions</h2>
                <div class="sale-nav-buttons">
                    <a href="${pageContext.request.contextPath}/courses" class="sale-nav-btn">
                        <i class="fas fa-book"></i>Quản lý khoá học
                    </a>
                    <a href="${pageContext.request.contextPath}/training-manager-view-class" class="sale-nav-btn">
                        <i class="fas fa-tasks"></i>Quản lý lớp học
                    </a>
                    <a href="${pageContext.request.contextPath}/ProcessRequest?action=list" class="sale-nav-btn">
                        <i class="fas fa-tasks"></i>Danh sách đơn cần xử lý
                    </a>
                    <a href="${pageContext.request.contextPath}/TrainingManagerDashboard?action=notifications" class="sale-nav-btn">
                        <i class="fas fa-bell"></i>Thông báo
                    </a>
                </div>
            </div>

            <div class="training-manager-notifications">
                <h2>
                    <i class="fas fa-bell"></i> Thông báo mới
                    <c:if test="${unreadCount > 0}">
                    <span style="background: #dc3545; color: white; padding: 2px 8px;
                        border-radius: 12px; font-size: 12px; margin-left: 10px;">${unreadCount}</span>
                    </c:if>
                </h2><br>

                <ul class="notification-list" style="list-style: none; padding: 0;">
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
                                        border-left: 4px solid #28a745; background: #f0fff0;
                                        </c:if>">
                                    <div style="margin-right: 15px; font-size: 20px; color: #17a2b8;">
                                        <c:choose>
                                            <c:when test="${notification.notificationType == 'STUDENT_REQUEST'}">
                                                <i class="fas fa-file-alt"></i>
                                            </c:when>
                                            <c:when test="${notification.notificationType == 'ACCOUNT_CREATION_REQUEST'}">
                                                <i class="fas fa-user-plus"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fas fa-clipboard-list"></i>
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
                                        <c:if test="${not empty notification.relatedEntityId and notification.relatedEntityId > 0}">
                                                    <a href="${pageContext.request.contextPath}/ProcessRequest?action=list&id=${notification.relatedEntityId}"
                                                       style="background: #28a745; color: white; padding: 8px 15px;
                              text-decoration: none; border-radius: 4px;
                              font-size: 12px; font-weight: bold;">
                                                        <i class="fas fa-eye"></i> Xem đơn
                                                    </a>
                                        </c:if>
                                    </div>
                                </li>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </ul>

                <div style="text-align: center; margin-top: 15px;">
                    <a href="${pageContext.request.contextPath}/TrainingManagerDashboard?action=notifications"
                       style="color: #28a745; text-decoration: none; font-weight: bold; font-size: 14px;">
                        <i class="fas fa-list"></i> Xem tất cả thông báo
                    </a>
                    <c:if test="${unreadCount > 0}">
                        <span style="margin: 0 10px;">|</span>
                        <a href="${pageContext.request.contextPath}/TrainingManagerDashboard?action=markAllRead"
                           style="color: #6c757d; text-decoration: none; font-size: 14px;">
                            <i class="fas fa-check-double"></i> Đánh dấu đã đọc
                        </a>
                    </c:if>
                </div>
            </div>


            <!-- Thống kê -->
            <div style="margin-top: 30px;">
                <h2>📊 Thống kê công việc</h2>
                <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin-top: 20px;">

                    <!-- Đơn đã xử lý tuần này -->
                    <div style="background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); border-left: 4px solid #28a745;">
                        <div style="display: flex; align-items: center; margin-bottom: 10px;">
                            <div style="background: #28a745; color: white; width: 40px; height: 40px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 15px;">
                                <i class="fas fa-check-circle"></i>
                            </div>
                            <h3 style="margin: 0; font-size: 14px; color: #666;">Đơn đã xử lý tuần này</h3>
                        </div>
                        <div style="font-size: 28px; font-weight: bold; color: #333; margin: 10px 0;">${processedRequestsThisWeek}</div>
                        <a href="${pageContext.request.contextPath}/ProcessRequest?action=list"
                           style="color: #28a745; text-decoration: none; font-size: 12px;">
                            <i class="fas fa-eye"></i> Xem chi tiết
                        </a>
                    </div>

                    <!-- Đơn chờ xử lý -->
                    <div style="background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); border-left: 4px solid #ffc107;">
                        <div style="display: flex; align-items: center; margin-bottom: 10px;">
                            <div style="background: #ffc107; color: white; width: 40px; height: 40px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 15px;">
                                <i class="fas fa-clock"></i>
                            </div>
                            <h3 style="margin: 0; font-size: 14px; color: #666;">Đơn chờ xử lý</h3>
                        </div>
                        <div style="font-size: 28px; font-weight: bold; color: #333; margin: 10px 0;">${pendingRequests}</div>
                        <a href="${pageContext.request.contextPath}/ProcessRequest?action=list"
                           style="color: #ffc107; text-decoration: none; font-size: 12px;">
                            <i class="fas fa-tasks"></i> Xử lý ngay
                        </a>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
