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
<!-- SIDEBAR -->
<div class="sidebar">
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
            <a href="${pageContext.request.contextPath}//CreateClassRoomServlet?action=list"
               class="nav-item ${pageContext.request.requestURI.contains('manager-request-list.jsp') ? 'active' : ''}"
               role="link"
               aria-current="${pageContext.request.requestURI.contains('manager-request-list.jsp') ? 'page' : 'false'}">
                <i class="fa-solid fa-building" aria-hidden="true"></i>
                <span>Danh sách đơn cần xử lý</span>
            </a>
            <a href="${pageContext.request.contextPath}/CreateClassRoomServlet"
               class="nav-item ${pageContext.request.requestURI.contains('training-manager-add-classroom.jsp') ? 'active' : ''}"
               role="link"
               aria-current="${pageContext.request.requestURI.contains('training-manager-add-classroom.jsp') ? 'page' : 'false'}">
                <i class="fa-solid fa-building" aria-hidden="true"></i>
                <span>Tạo lớp học</span>
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
