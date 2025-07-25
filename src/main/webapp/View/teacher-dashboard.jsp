<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teacher Dashboard - TALENT01</title>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <!-- CSS Files -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/teacher-sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/teacher-dashboard.css">

    <!-- Chart.js for analytics -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<!-- Include Header -->
<%@ include file="header.jsp" %>

<div class="dashboard-container">
    <!-- Include Teacher Sidebar -->
    <%@ include file="teacher-sidebar.jsp" %>

    <!-- Main Content -->
    <main class="main-content">
        <!-- Mobile Header -->
        <div class="mobile-header">
            <button class="sidebar-toggle" onclick="toggleSidebar()">
                <i class="fas fa-bars"></i>
            </button>
            <h1>Dashboard</h1>
        </div>

        <!-- Welcome Section -->
        <div class="welcome-section">
            <div class="welcome-card">
                <div class="welcome-text">
                    <%
                        org.example.talentcenter.model.Account account =
                                (org.example.talentcenter.model.Account) session.getAttribute("account");
                    %>
                    <h1>Xin chào thầy/cô <%= (account != null && account.getFullName() != null) ? account.getFullName() : "" %> !</h1>
                    <p>Hôm nay là ngày tuyệt vời để dạy học</p>
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

        <!-- Quick Actions -->
        <div class="quick-actions">
            <h2 style="color: black;">Thao tác nhanh</h2>
            <div class="actions-grid">
                <a href="${pageContext.request.contextPath}/attendance" class="action-card">
                    <div class="action-icon">
                        <i class="fas fa-user-check"></i>
                    </div>
                    <h3>Điểm danh</h3>
                    <p>Điểm danh học sinh hôm nay</p>
                </a>

                <a href="${pageContext.request.contextPath}/TeacherSchedule" class="action-card">
                    <div class="action-icon">
                        <i class="fas fa-calendar-alt"></i>
                    </div>
                    <h3>Thời khóa biểu</h3>
                    <p>Xem lịch giảng dạy</p>
                </a>

                <a href="${pageContext.request.contextPath}/teacherRequest" class="action-card">
                    <div class="action-icon">
                        <i class="fas fa-paper-plane"></i>
                    </div>
                    <h3>Gửi đơn</h3>
                    <p>Tạo đơn xin nghỉ hoặc yêu cầu</p>
                </a>

                <a href="${pageContext.request.contextPath}/teacher-notification" class="action-card">
                    <div class="action-icon">
                        <i class="fas fa-bell"></i>
                    </div>
                    <h3>Thông báo</h3>
                    <p>Thông báo mới nhất</p>
                </a>
            </div>
        </div>

        <!-- Dashboard Content Grid -->
        <div class="content-grid">
            <!-- Today's Schedule -->
            <div class="content-card">
                <div class="card-header">
                    <h3><i class="fas fa-calendar-day"></i> Lịch hôm nay</h3>
                    <a href="${pageContext.request.contextPath}/TeacherSchedule" class="view-all">Xem tất cả</a>
                </div>
                <div class="card-content">
                    <c:choose>
                        <c:when test="${empty todaySchedules}">
                            <div class="schedule-item">
                                <div class="schedule-info">
                                    <p style="color: #666; font-style: italic; text-align: center; padding: 20px;">
                                        <i class="fas fa-info-circle"></i>
                                        Không có lịch dạy nào hôm nay
                                    </p>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="schedule" items="${todaySchedules}">
                                <div class="schedule-item">
                                    <div class="schedule-time">
                                            ${schedule.slotStartTime} - ${schedule.slotEndTime}
                                    </div>
                                    <div class="schedule-info">
                                        <h4>${schedule.courseTitle}</h4>
                                        <p>Lớp: ${schedule.className} • Phòng: ${schedule.roomCode}</p>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Notification Card -->
            <div class="content-card">
                <div class="card-header">
                    <h3><i class="fas fa-bell"></i> Thông báo gần đây</h3>
                    <a href="${pageContext.request.contextPath}/teacher-notification" class="view-all">Xem tất cả</a>
                </div>
                <div class="card-content">
                    <c:choose>
                        <c:when test="${empty recentNotifications}">
                            <div class="no-data">
                                <i class="fas fa-bell-slash"></i>
                                <p>Không có thông báo nào gần đây</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="notification" items="${recentNotifications}">
                                <div class="notification-item">
                                    <div class="notification-icon ${notification.read ? '' : 'new'}">
                                        <c:choose>
                                            <c:when test="${notification.notificationType == 'SYSTEM'}">
                                                <i class="fas fa-cog"></i>
                                            </c:when>
                                            <c:when test="${notification.notificationType == 'CLASS'}">
                                                <i class="fas fa-chalkboard-teacher"></i>
                                            </c:when>
                                            <c:when test="${notification.notificationType == 'SCHEDULE'}">
                                                <i class="fas fa-calendar-alt"></i>
                                            </c:when>
                                            <c:when test="${notification.notificationType == 'ANNOUNCEMENT'}">
                                                <i class="fas fa-bullhorn"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fas fa-bell"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="notification-content">
                                        <h4>
                                                ${notification.title}
                                            <c:if test="${not notification.read}">
                                                <span class="new-badge">Mới</span>
                                            </c:if>
                                        </h4>
                                        <p>
                                            <c:choose>
                                                <c:when test="${fn:length(notification.content) > 100}">
                                                    ${fn:substring(notification.content, 0, 100)}...
                                                </c:when>
                                                <c:otherwise>
                                                    ${notification.content}
                                                </c:otherwise>
                                            </c:choose>
                                        </p>
                                        <div class="notification-info">
                                            <span class="time">
                                    <i class="fas fa-clock"></i>
                                    <fmt:formatDate value="${notification.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                </span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Recent Requests -->
            <div class="content-card">
                <div class="card-header">
                    <h3><i class="fas fa-paper-plane"></i> Đơn gần đây</h3>
                    <a href="${pageContext.request.contextPath}/teacherViewRequest" class="view-all">Xem tất cả</a>
                </div>
                <div class="card-content">
                    <c:choose>
                        <c:when test="${not empty recentRequests}">
                            <c:forEach var="request" items="${recentRequests}">
                                <div class="request-item">
                                    <div class="request-status ${request.status.toLowerCase()}">
                                        <c:choose>
                                            <c:when test="${request.status == 'Pending'}">
                                                <i class="fas fa-clock"></i>
                                            </c:when>
                                            <c:when test="${request.status == 'Approved'}">
                                                <i class="fas fa-check"></i>
                                            </c:when>
                                            <c:when test="${request.status == 'Rejected'}">
                                                <i class="fas fa-times"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fas fa-question"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="request-content">
                                        <h4>${request.typeName}</h4>
                                        <p>
                                            <c:choose>
                                                <c:when test="${request.reason.length() > 60}">
                                                    ${request.reason.substring(0, 60)}...
                                                </c:when>
                                                <c:otherwise>
                                                    ${request.reason}
                                                </c:otherwise>
                                            </c:choose>
                                        </p>
                                        <span class="request-date">
                                <c:choose>
                                    <c:when test="${request.status == 'Approved' or request.status == 'Rejected'}">
                                        <c:choose>
                                            <c:when test="${request.status == 'Approved'}">
                                                Duyệt: <fmt:formatDate value="${request.responseAt}" pattern="dd/MM/yyyy"/>
                                            </c:when>
                                            <c:otherwise>
                                                Từ chối: <fmt:formatDate value="${request.responseAt}" pattern="dd/MM/yyyy"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        Gửi: <fmt:formatDate value="${request.createdAt}" pattern="dd/MM/yyyy"/>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="no-data">
                                <i class="fas fa-inbox"></i>
                                <p>Không có đơn nào gần đây</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </main>
</div>

<!-- Include Footer -->
<%@ include file="footer.jsp" %>

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

    // Initialize attendance chart
    function initAttendanceChart() {
        const ctx = document.getElementById('attendanceChart').getContext('2d');

        const labels = [
            <c:forEach var="label" items="${chartLabels}" varStatus="loop">
            "<fmt:formatDate value="${label}" pattern="dd/MM" />"
            <c:if test="${!loop.last}">,</c:if>
            </c:forEach>
        ];

        const data = [
            <c:forEach var="value" items="${chartValues}" varStatus="loop">
            ${value}
            <c:if test="${!loop.last}">,</c:if>
            </c:forEach>
        ];

        new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Tỷ lệ điểm danh (%)',
                    data: data,
                    borderColor: '#4CAF50',
                    backgroundColor: 'rgba(76, 175, 80, 0.1)',
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        max: 100
                    }
                }
            }
        });
    }
    // Initialize when DOM is loaded
    document.addEventListener('DOMContentLoaded', function() {
        updateCurrentDate();
        initAttendanceChart();
    });
</script>
</body>
</html>