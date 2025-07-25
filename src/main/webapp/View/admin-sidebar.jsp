<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 7/22/2025
  Time: Admin Sidebar
  Admin sidebar component for TalentCenter application
--%>
<!DOCTYPE html>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Admin Sidebar</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">

<!-- ADMIN SIDEBAR -->
<div class="sidebar">
  <nav>
    <div class="nav-section">
      <a href="${pageContext.request.contextPath}/AdminDashboard" 
         class="nav-item ${pageContext.request.requestURI.contains('admin-dashboard.jsp') ? 'active' : ''}" 
         role="link" 
         aria-current="${pageContext.request.requestURI.contains('admin-dashboard.jsp') ? 'page' : 'false'}">
        <i class="fas fa-tachometer-alt" aria-hidden="true"></i>
        <span>Dashboard</span>
      </a>
    </div>
    <div class="nav-section">
      <a href="${pageContext.request.contextPath}/teachers"
         class="nav-item ${pageContext.request.requestURI.contains('teacher-list.jsp') || pageContext.request.servletPath.contains('/teachers') ? 'active' : ''}"
         role="link"
         aria-current="${pageContext.request.requestURI.contains('teacher-list.jsp') || pageContext.request.servletPath.contains('/teachers') ? 'page' : 'false'}">
        <i class="fas fa-chalkboard-teacher" aria-hidden="true"></i>
        <span>Danh sách giáo viên</span>
      </a>
      <a href="${pageContext.request.contextPath}/teacher-salary"
         class="nav-item ${pageContext.request.requestURI.contains('teacher-salary.jsp') || pageContext.request.servletPath.contains('/TeacherSalary') ? 'active' : ''}"
         role="link"
         aria-current="${pageContext.request.requestURI.contains('teacher-salary.jsp') || pageContext.request.servletPath.contains('/TeacherSalary') ? 'page' : 'false'}">
        <i class="fas fa-calculator" aria-hidden="true"></i>
        <span>Tính lương giáo viên</span>
      </a>
      <a href="${pageContext.request.contextPath}/AccountManagement"
         class="nav-item ${pageContext.request.requestURI.contains('account-list.jsp') || pageContext.request.servletPath.contains('/AccountManagement') ? 'active' : ''}"
         role="link"
         aria-current="${pageContext.request.requestURI.contains('account-list.jsp') || pageContext.request.servletPath.contains('/AccountManagement') ? 'page' : 'false'}">
        <i class="fas fa-users" aria-hidden="true"></i>
        <span>Danh sách tài khoản</span>
      </a>

      <div class="nav-section">
        <a href="${pageContext.request.contextPath}/CreateAccount"
           class="nav-item ${pageContext.request.requestURI.contains('account-request-list.jsp') ? 'active' : ''}"
           role="link"
           aria-current="${pageContext.request.requestURI.contains('account-request-list.jsp') ? 'page' : 'false'}">
          <i class="fas fa-user-check" aria-hidden="true"></i>
          <span>Danh sách yêu cầu cấp tài khoản học viên</span>
        </a>
      </div>
    </div>
  </nav>
</div>

<script>
  document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', function() {
      document.querySelectorAll('.nav-item').forEach(nav => nav.classList.remove('active'));
      this.classList.add('active');
    });
  });

  function toggleSidebar() {
    document.querySelector('.sidebar').classList.toggle('active');
  }
</script>
