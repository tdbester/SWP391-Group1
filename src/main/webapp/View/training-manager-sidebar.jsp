<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/9/2025
  Time: 3:58 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Sidebar and Header Demo</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<!-- SIDEBAR -->
  <div class="sidebar">

    <div class="logo">
      <i class="fas fa-users-cog"></i>
      <h2>Trung Tâm Năng Khiếu</h2>
    </div>
    <nav>
      <div class="nav-section">
        <a href="${pageContext.request.contextPath}/TrainingManagerDashboard" class="nav-item ${pageContext.request.requestURI.contains('DashBoardTeacher.jsp') ? 'active' : ''}" role="link" aria-current="${pageContext.request.requestURI.contains('DashBoardTeacher.jsp') ? 'page' : 'false'}">
          <i class="fas fa-tachometer-alt" aria-hidden="true"></i>
          <span>Dashboard</span>
        </a>
      </div>
      <div class="nav-section">
        <a href="${pageContext.request.contextPath}/CreateAccount"
           class="nav-item ${pageContext.request.requestURI.contains('account-request-list.jsp') ? 'active' : ''}"
           role="link"
           aria-current="${pageContext.request.requestURI.contains('account-request-list.jsp') ? 'page' : 'false'}">
          <i class="fa-solid fa-building" aria-hidden="true"></i>
          <span>Danh sách yêu cầu cấp tài khoản</span>
        </a>
        <a href="${pageContext.request.contextPath}/ProcessRequest?action=list"
           class="nav-item ${pageContext.request.requestURI.contains('manager-request-list.jsp') ? 'active' : ''}"
           role="link"
           aria-current="${pageContext.request.requestURI.contains('manager-request-list.jsp') ? 'page' : 'false'}">
          <i class="fa-solid fa-building" aria-hidden="true"></i>
          <span>Danh sách đơn cần xử lý</span>
        </a>
        <a href="${pageContext.request.contextPath}/courses"
           class="nav-item ${pageContext.request.requestURI.contains('course.jsp') || pageContext.request.servletPath.contains('/courses') ? 'active' : ''}"
           role="link"
           aria-current="${pageContext.request.requestURI.contains('course.jsp') || pageContext.request.servletPath.contains('/courses') ? 'page' : 'false'}">
          <i class="fa-solid fa-building" aria-hidden="true"></i>
          <span>Danh sách Khóa học</span>
        </a>
        <a href="${pageContext.request.contextPath}/TrainingManagerClassroom"
           class="nav-item ${pageContext.request.requestURI.contains('training-manager-classrooms.jsp') || pageContext.request.servletPath.contains('/TrainingManagerClassroom') ? 'active' : ''}"
           role="link"
           aria-current="${pageContext.request.requestURI.contains('training-manager-classrooms.jsp') || pageContext.request.servletPath.contains('/TrainingManagerClassroom') ? 'page' : 'false'}">
          <i class="fa-solid fa-building" aria-hidden="true"></i>
          <span>Danh sách lớp học</span>
        </a>
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

