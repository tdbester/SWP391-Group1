<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/9/2025
  Time: 3:58 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Sidebar and Header Demo</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
  <div class="sidebar">
    <nav>
      <div class="nav-section">
        <a href="${pageContext.request.contextPath}/TrainingManagerDashboard"
           class="nav-item ${pageContext.request.requestURI.contains('DashBoardTeacher.jsp') ? 'active' : ''}"
           role="link"
           aria-current="${pageContext.request.requestURI.contains('DashBoardTeacher.jsp') ? 'page' : 'false'}">
          <i class="fas fa-tachometer-alt" aria-hidden="true"></i>
          <span>Dashboard</span>
        </a>
      </div>

      <div class="nav-section">
        <a href="${pageContext.request.contextPath}/courses"
           class="nav-item ${pageContext.request.requestURI.contains('courses') ? 'active' : ''}"
           role="link"
           aria-current="${pageContext.request.requestURI.contains('courses') ? 'page' : 'false'}">
          <i class="fas fa-user-check" aria-hidden="true"></i>
          <span>Quản lý khoá học</span>
        </a>
      </div>

      <div>
          <a href="${pageContext.request.contextPath}/training-manager-view-class"
             class="nav-item ${pageContext.request.requestURI.contains('training-manager-view-class.jsp') ? 'active' : ''}"
             role="link"
             aria-current="${pageContext.request.requestURI.contains('training-manager-view-class.jsp') ? 'page' : 'false'}">
              <i class="fas fa-chalkboard-teacher" aria-hidden="true"></i>
              <span>Quản lý lớp học</span>
          </a>
      </div>


      <div class="nav-section">
        <a href="${pageContext.request.contextPath}/ProcessRequest?action=list"
           class="nav-item ${pageContext.request.requestURI.contains('manager-request-list.jsp') ? 'active' : ''}"
           role="link"
           aria-current="${pageContext.request.requestURI.contains('manager-request-list.jsp') ? 'page' : 'false'}">
          <i class="fas fa-tasks" aria-hidden="true"></i>
          <span>Danh sách đơn cần xử lý</span>
        </a>
      </div>

      <div class="nav-section">
        <a href="${pageContext.request.contextPath}/TrainingManagerDashboard?action=notifications"
           class="nav-item ${pageContext.request.requestURI.contains('notifications') ? 'active' : ''}"
           role="link"
           aria-current="${pageContext.request.requestURI.contains('notifications') ? 'page' : 'false'}">
          <i class="fas fa-bell" aria-hidden="true"></i>
          <span>Thông báo</span>
        </a>
      </div>
    </nav>
</div>
<script>
    document.querySelectorAll('.nav-item').forEach(item => {
        item.addEventListener('click', function() {
            document.querySelectorAll('.nav-item').forEach(nav => nav.classroomList.remove('active'));
            this.classroomList.add('active');
        });
    });

    function toggleSidebar() {
        document.querySelector('.sidebar').classroomList.toggle('active');
    }
</script>

