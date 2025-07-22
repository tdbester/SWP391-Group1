<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 7/22/2025
  Time: Admin Dashboard
  Admin dashboard page for TalentCenter application
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin Dashboard - TALENT01</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">
  <!-- Kế thừa hoặc nhúng lại CSS từ trang chính -->
</head>
<body>
<jsp:include page="header.jsp" />

<!-- dashboard -->
<div class="container">
  <jsp:include page="admin-sidebar.jsp" />
  <div class="main-content">
    <div class="welcome-section">
      <div class="welcome-card">
        <div class="welcome-text">
          <%
            org.example.talentcenter.model.Account account = 
                (org.example.talentcenter.model.Account) session.getAttribute("account");
          %>
          <h1>Xin chào quản trị viên <%= (account != null && account.getFullName() != null) ? account.getFullName() : "" %>!</h1>
          <p>Hôm nay là ngày tuyệt vời để quản lý hệ thống</p>
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
        <h2>Quick Actions</h2>
        <div class="sale-nav-buttons">
          <a href="${pageContext.request.contextPath}/teachers" class="sale-nav-btn">
            <i class="fas fa-chalkboard-teacher"></i>Danh sách giáo viên
          </a>
          <a href="${pageContext.request.contextPath}/TeacherSalary" class="sale-nav-btn">
            <i class="fas fa-calculator"></i>Tính lương giáo viên
          </a>
          <a href="${pageContext.request.contextPath}/AccountManagement" class="sale-nav-btn">
            <i class="fas fa-users"></i>Danh sách tài khoản
          </a>
          <a href="${pageContext.request.contextPath}/courses" class="sale-nav-btn">
            <i class="fas fa-book-open"></i>Quản lý khóa học
          </a>
        </div>
      </div>

      <div class="admin-notifications">
        <h2>🔔 Thông báo hệ thống
          <c:if test="${unreadCount > 0}">
            <span style="background: #dc3545; color: white; padding: 2px 8px;
                         border-radius: 12px; font-size: 12px; margin-left: 10px;">
                ${unreadCount}
            </span>
          </c:if>
        </h2>

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
                        ">
                  <div style="margin-right: 15px; font-size: 24px;">
                    <c:choose>
                      <c:when test="${notification.notificationType == 'SYSTEM_ALERT'}">⚠️</c:when>
                      <c:when test="${notification.notificationType == 'USER_MANAGEMENT'}">👥</c:when>
                      <c:when test="${notification.notificationType == 'TEACHER_SALARY'}">💰</c:when>
                      <c:otherwise>📋</c:otherwise>
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
                      <fmt:formatDate value="${notification.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                    </div>
                  </div>
                </li>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </ul>

        <div style="text-align: center; margin-top: 15px;">
          <a href="${pageContext.request.contextPath}/AdminDashboard?action=notifications"
             style="color: #007bff; text-decoration: none; font-weight: bold; font-size: 14px;">
            <i class="fas fa-list"></i> Xem tất cả thông báo
          </a>
          <c:if test="${unreadCount > 0}">
            <span style="margin: 0 10px;">|</span>
            <a href="${pageContext.request.contextPath}/AdminDashboard?action=markAllRead"
               style="color: #6c757d; text-decoration: none; font-size: 14px;">
              <i class="fas fa-check-double"></i> Đánh dấu đã đọc
            </a>
          </c:if>
        </div>
      </div>

      <!-- Thống kê -->
      <div style="margin-top: 30px;">
        <h2>📊 Thống kê hệ thống</h2>
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin-top: 20px;">

          <!-- Tổng số giáo viên -->
          <div style="background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); border-left: 4px solid #28a745;">
            <div style="display: flex; align-items: center; margin-bottom: 10px;">
              <div style="background: #28a745; color: white; width: 40px; height: 40px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 15px;">
                <i class="fas fa-chalkboard-teacher"></i>
              </div>
              <h3 style="margin: 0; font-size: 14px; color: #666;">Tổng số giáo viên</h3>
            </div>
            <div style="font-size: 28px; font-weight: bold; color: #333; margin: 10px 0;">${totalTeachers}</div>
            <a href="${pageContext.request.contextPath}/teachers" style="color: #28a745; text-decoration: none; font-size: 12px;">
              <i class="fas fa-eye"></i> Xem chi tiết
            </a>
          </div>

          <!-- Tổng số tài khoản -->
          <div style="background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); border-left: 4px solid #007bff;">
            <div style="display: flex; align-items: center; margin-bottom: 10px;">
              <div style="background: #007bff; color: white; width: 40px; height: 40px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 15px;">
                <i class="fas fa-users"></i>
              </div>
              <h3 style="margin: 0; font-size: 14px; color: #666;">Tổng số tài khoản</h3>
            </div>
            <div style="font-size: 28px; font-weight: bold; color: #333; margin: 10px 0;">${totalAccounts}</div>
            <a href="${pageContext.request.contextPath}/AccountManagement" style="color: #007bff; text-decoration: none; font-size: 12px;">
              <i class="fas fa-cog"></i> Quản lý
            </a>
          </div>

          <!-- Tổng số khóa học -->
          <div style="background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); border-left: 4px solid #ffc107;">
            <div style="display: flex; align-items: center; margin-bottom: 10px;">
              <div style="background: #ffc107; color: white; width: 40px; height: 40px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 15px;">
                <i class="fas fa-book-open"></i>
              </div>
              <h3 style="margin: 0; font-size: 14px; color: #666;">Tổng số khóa học</h3>
            </div>
            <div style="font-size: 28px; font-weight: bold; color: #333; margin: 10px 0;">${totalCourses}</div>
            <a href="${pageContext.request.contextPath}/courses" style="color: #ffc107; text-decoration: none; font-size: 12px;">
              <i class="fas fa-list"></i> Xem danh sách
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="footer.jsp" />

<script>
  // Update current date
  function updateCurrentDate() {
    const now = new Date();
    const options = {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    };
    document.getElementById('currentDate').textContent = now.toLocaleDateString('vi-VN', options);
  }

  // Initialize when DOM is loaded
  document.addEventListener('DOMContentLoaded', function() {
    updateCurrentDate();
  });
</script>
</body>
</html>
