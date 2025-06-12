<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/12/2025
  Time: 10:08 PM
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
<div class="dashboard">
    <div class="sidebar">

        <div class="logo">
            <i class="fas fa-users-cog"></i>
            <h2>Trung Tâm Năng Khiếu</h2>
        </div>
        <nav>
            <div class="nav-section">
                <a href="${pageContext.request.contextPath}/View/sale-dashboard.jsp" class="nav-item ${pageContext.request.requestURI.contains('DashBoardTeacher.jsp') ? 'active' : ''}" role="link" aria-current="${pageContext.request.requestURI.contains('DashBoardTeacher.jsp') ? 'page' : 'false'}">
                    <i class="fas fa-tachometer-alt" aria-hidden="true"></i>
                    <span>Dashboard</span>
                </a>
            </div>
            <div class="nav-section">
                <a href="${pageContext.request.contextPath}/Consultation?action=list"
                   class="nav-item ${pageContext.request.requestURI.contains('consultation-list.jsp') ? 'active' : ''}"
                   role="link"
                   aria-current="${pageContext.request.requestURI.contains('consultation-list.jsp') ? 'page' : 'false'}">
                    <i class="fa-solid fa-building" aria-hidden="true"></i>
                    <span>Thời khoá biểu</span>
                </a>
            </div>
            <div class="nav-section">
                <a href="${pageContext.request.contextPath}/blogs?action=list"
                   class="nav-item ${pageContext.request.requestURI.contains('blog.jsp') ? 'active' : ''}"
                   role="link"
                   aria-current="${pageContext.request.requestURI.contains('blog.jsp') ? 'page' : 'false'}">
                    <i class="fas fa-search" aria-hidden="true"></i>
                    <span>Điểm danh</span>
                </a>
            </div>
            <div class="nav-section">
                <a href="${pageContext.request.contextPath}/StudentAccountRequest"
                   class="nav-item ${pageContext.request.requestURI.contains('student-account-request.jsp') ? 'active' : ''}"
                   role="link"
                   aria-current="${pageContext.request.requestURI.contains('student-account-request.jsp') ? 'page' : 'false'}">
                    <i class="fas fa-briefcase" aria-hidden="true"></i>
                    <span>Chuyển lớp</span>
                </a>
            </div>
            <div class="nav-section">
                <a href="CandidateDetails.jsp" class="nav-item ${pageContext.request.requestURI.contains('CandidateDetails.jsp') ? 'active' : ''}" role="link" aria-current="${pageContext.request.requestURI.contains('CandidateDetails.jsp') ? 'page' : 'false'}">
                    <i class="fas fa-user" aria-hidden="true"></i>
                    <span>Tài liệu</span>
                </a>
            </div>
        </nav>
    </div>
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

