<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/teacher-sidebar.css">
<!-- Teacher Sidebar -->
<div class="sidebar">
  <nav>
    <div class="nav-section">
      <a href="${pageContext.request.contextPath}/TeacherDashboard" class="nav-item">
        <i class="fas fa-tachometer-alt"></i>
        <span>Dashboard</span>
      </a>
    </div>
    <div class="nav-section">
      <a href="${pageContext.request.contextPath}/TeacherSchedule" class="nav-item">
        <i class="fas fa-calendar-alt"></i>
        <span>Thời khóa biểu</span>
      </a>
    </div>
    <div class="nav-section">
      <a href="${pageContext.request.contextPath}/attendance" class="nav-item">
        <i class="fas fa-user-check"></i>
        <span>Điểm danh</span>
      </a>
    </div>
    <div class="nav-section">
      <a href="${pageContext.request.contextPath}/teacherRequest" class="nav-item">
        <i class="fas fa-paper-plane"></i>
        <span>Gửi đơn</span>
      </a>
    </div>
    <div class="nav-section">
      <a href="${pageContext.request.contextPath}/teacherViewRequest" class="nav-item">
        <i class="fas fa-eye"></i>
        <span>Xem đơn</span>
      </a>
    </div>
    <div class="nav-section">
      <a href="${pageContext.request.contextPath}/teacher-notification" class="nav-item">
        <i class="fas fa-bell"></i>
        <span>Thông báo</span>
      </a>
    </div>
    <div class="nav-section">
      <a href="${pageContext.request.contextPath}/sendNotification" class="nav-item">
        <i class="fa-solid fa-envelope" style="color: #fbfdfe;"></i>
        <span>Gửi thông báo</span>
      </a>
    </div>
    <div class="nav-section">
      <a href="${pageContext.request.contextPath}/TeacherAbsenceRequest" class="nav-item">
        <i class="fa-solid fa-chalkboard-teacher" style="color: #fbfdfe;"></i>
        <span>Xử lý đơn xin nghỉ học</span>
      </a>
    </div>
  </nav>
</div>

<script>
  // Sidebar navigation functionality
  document.addEventListener('DOMContentLoaded', function() {
    // Get current page URL to set active nav item
    const currentPath = window.location.pathname;
    const navItems = document.querySelectorAll('.nav-item');

    // Remove active class from all items
    navItems.forEach(item => item.classList.remove('active'));

    // Set active based on current path
    navItems.forEach(item => {
      const href = item.getAttribute('href');
      if (href && currentPath.includes(href.split('/').pop())) {
        item.classList.add('active');
      }
    });

    // Handle nav item clicks
    navItems.forEach(item => {
      item.addEventListener('click', function(e) {
        // Don't prevent default to allow normal navigation
        // Remove active from all items
        navItems.forEach(nav => nav.classList.remove('active'));
        // Add active to clicked item
        this.classList.add('active');
      });
    });
  });

  // Mobile sidebar toggle function
  function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    if (sidebar) {
      sidebar.classList.toggle('active');
    }
  }
</script>