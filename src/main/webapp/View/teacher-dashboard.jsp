<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                    <h1>Xin chào thầy/cô!</h1>
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

        <!-- Stats Cards -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon students">
                    <i class="fas fa-users"></i>
                </div>
                <div class="stat-content">
                    <h3>120</h3>
                    <p>Học sinh</p>
                    <span class="stat-change positive">+5% so với tháng trước</span>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon classes">
                    <i class="fas fa-chalkboard-teacher"></i>
                </div>
                <div class="stat-content">
                    <h3>8</h3>
                    <p>Lớp học</p>
                    <span class="stat-change positive">+2 lớp mới</span>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon attendance">
                    <i class="fas fa-user-check"></i>
                </div>
                <div class="stat-content">
                    <h3>95%</h3>
                    <p>Tỷ lệ điểm danh</p>
                    <span class="stat-change positive">+3% tuần này</span>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon requests">
                    <i class="fas fa-paper-plane"></i>
                </div>
                <div class="stat-content">
                    <h3>12</h3>
                    <p>Đơn chờ xử lý</p>
                    <span class="stat-change neutral">2 mới hôm nay</span>
                </div>
            </div>
        </div>

        <!-- Quick Actions -->
        <div class="quick-actions">
            <h2>Thao tác nhanh</h2>
            <div class="actions-grid">
                <a href="${pageContext.request.contextPath}/View/attendance.jsp" class="action-card">
                    <div class="action-icon">
                        <i class="fas fa-user-check"></i>
                    </div>
                    <h3>Điểm danh</h3>
                    <p>Điểm danh học sinh hôm nay</p>
                </a>

                <a href="${pageContext.request.contextPath}/View/schedule.jsp" class="action-card">
                    <div class="action-icon">
                        <i class="fas fa-calendar-alt"></i>
                    </div>
                    <h3>Thời khóa biểu</h3>
                    <p>Xem lịch giảng dạy</p>
                </a>

                <a href="${pageContext.request.contextPath}/View/submit-request.jsp" class="action-card">
                    <div class="action-icon">
                        <i class="fas fa-paper-plane"></i>
                    </div>
                    <h3>Gửi đơn</h3>
                    <p>Tạo đơn xin nghỉ hoặc yêu cầu</p>
                </a>

                <a href="${pageContext.request.contextPath}/View/notifications.jsp" class="action-card">
                    <div class="action-icon">
                        <i class="fas fa-bell"></i>
                    </div>
                    <h3>Thông báo</h3>
                    <p>Xem thông báo mới nhất</p>
                </a>
            </div>
        </div>

        <!-- Dashboard Content Grid -->
        <div class="content-grid">
            <!-- Today's Schedule -->
            <div class="content-card">
                <div class="card-header">
                    <h3><i class="fas fa-calendar-day"></i> Lịch hôm nay</h3>
                    <a href="${pageContext.request.contextPath}/View/schedule.jsp" class="view-all">Xem tất cả</a>
                </div>
                <div class="card-content">
                    <div class="schedule-item">
                        <div class="schedule-time">8:00 - 9:30</div>
                        <div class="schedule-info">
                            <h4>Toán cao cấp</h4>
                            <p>Lớp: A1 • Phòng: 201</p>
                        </div>
                    </div>
                    <div class="schedule-item">
                        <div class="schedule-time">10:00 - 11:30</div>
                        <div class="schedule-info">
                            <h4>Lập trình Java</h4>
                            <p>Lớp: B2 • Phòng: Lab1</p>
                        </div>
                    </div>
                    <div class="schedule-item">
                        <div class="schedule-time">14:00 - 15:30</div>
                        <div class="schedule-info">
                            <h4>Cơ sở dữ liệu</h4>
                            <p>Lớp: C3 • Phòng: 301</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Recent Notifications -->
            <div class="content-card">
                <div class="card-header">
                    <h3><i class="fas fa-bell"></i> Thông báo gần đây</h3>
                    <a href="${pageContext.request.contextPath}/View/notifications.jsp" class="view-all">Xem tất cả</a>
                </div>
                <div class="card-content">
                    <div class="notification-item">
                        <div class="notification-icon new">
                            <i class="fas fa-info-circle"></i>
                        </div>
                        <div class="notification-content">
                            <h4>Thông báo họp khoa</h4>
                            <p>Họp khoa vào 15:00 ngày mai tại phòng 501</p>
                            <span class="notification-time">2 giờ trước</span>
                        </div>
                    </div>
                    <div class="notification-item">
                        <div class="notification-icon">
                            <i class="fas fa-user-plus"></i>
                        </div>
                        <div class="notification-content">
                            <h4>Học sinh mới</h4>
                            <p>3 học sinh mới được thêm vào lớp A1</p>
                            <span class="notification-time">1 ngày trước</span>
                        </div>
                    </div>
                    <div class="notification-item">
                        <div class="notification-icon">
                            <i class="fas fa-calendar"></i>
                        </div>
                        <div class="notification-content">
                            <h4>Thay đổi lịch học</h4>
                            <p>Lịch học lớp B2 thay đổi từ thứ 3 tới</p>
                            <span class="notification-time">2 ngày trước</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Attendance Chart -->
            <div class="content-card chart-card">
                <div class="card-header">
                    <h3><i class="fas fa-chart-line"></i> Thống kê điểm danh</h3>
                </div>
                <div class="card-content">
                    <canvas id="attendanceChart"></canvas>
                </div>
            </div>

            <!-- Recent Requests -->
            <div class="content-card">
                <div class="card-header">
                    <h3><i class="fas fa-paper-plane"></i> Đơn gần đây</h3>
                    <a href="${pageContext.request.contextPath}/View/view-request.jsp" class="view-all">Xem tất cả</a>
                </div>
                <div class="card-content">
                    <div class="request-item">
                        <div class="request-status pending">
                            <i class="fas fa-clock"></i>
                        </div>
                        <div class="request-content">
                            <h4>Đơn xin nghỉ</h4>
                            <p>Xin nghỉ ngày 20/06 do có việc cá nhân</p>
                            <span class="request-date">Gửi: 15/06/2025</span>
                        </div>
                    </div>
                    <div class="request-item">
                        <div class="request-status approved">
                            <i class="fas fa-check"></i>
                        </div>
                        <div class="request-content">
                            <h4>Đơn xin đổi lịch</h4>
                            <p>Xin đổi lịch dạy từ thứ 2 sang thứ 3</p>
                            <span class="request-date">Duyệt: 12/06/2025</span>
                        </div>
                    </div>
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
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7'],
                datasets: [{
                    label: 'Tỷ lệ điểm danh (%)',
                    data: [92, 89, 95, 88, 96, 93],
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
                        max: 100,
                        grid: {
                            color: 'rgba(0,0,0,0.1)'
                        }
                    },
                    x: {
                        grid: {
                            color: 'rgba(0,0,0,0.1)'
                        }
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