<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/1/2025
  Time: 11:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%--/*--%>
<%--*  Copyright (C) 2025 <Group 1>--%>
<%--    *  All rights reserved.--%>
<%--    *--%>
<%--    *  This file is part of the <Talent Center Management> project.--%>
<%--    *  Unauthorized copying of this file, via any medium is strictly prohibited.--%>
<%--    *  Proprietary and confidential.--%>
<%--    *--%>
<%--    *  Created on:        2025-06-01--%>
<%--    *  Author:            Cù Thị Huyền Trang--%>
<%--    *--%>
<%--    *  ========================== Change History ==========================--%>
<%--    *  Date        | Author               | Description--%>
<%--    *  ------------|----------------------|----------------------------------%>
<%--    *  2025-06-01  | Cù Thị Huyền Trang   | Initial creation--%>
<%--    */--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<!-- SIDEBAR -->
    <div class="sidebar">
        <nav>
            <div class="nav-section">
                <a href="${pageContext.request.contextPath}/SaleDashboard" class="nav-item">
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
                    <span>Danh sách đăng kí tư vấn</span>
                </a>
            </div>
            <div class="nav-section">
                <a href="${pageContext.request.contextPath}/blogs?action=list"
                   class="nav-item ${pageContext.request.requestURI.contains('blog.jsp') ? 'active' : ''}"
                   role="link"
                   aria-current="${pageContext.request.requestURI.contains('blog.jsp') ? 'page' : 'false'}">
                    <i class="fas fa-search" aria-hidden="true"></i>
                    <span>Quản lý Blog</span>
                </a>
            </div>
            <div class="nav-section">
                <a href="${pageContext.request.contextPath}/StudentAccountRequest"
                   class="nav-item ${pageContext.request.requestURI.contains('student-account-request.jsp') ? 'active' : ''}"
                   role="link"
                   aria-current="${pageContext.request.requestURI.contains('student-account-request.jsp') ? 'page' : 'false'}">
                    <i class="fas fa-briefcase" aria-hidden="true"></i>
                    <span>Yêu cầu cấp tài khoản học viên</span>
                </a>
            </div>
            <div class="nav-section">
                <a href="CandidateDetails.jsp" class="nav-item ${pageContext.request.requestURI.contains('CandidateDetails.jsp') ? 'active' : ''}" role="link" aria-current="${pageContext.request.requestURI.contains('CandidateDetails.jsp') ? 'page' : 'false'}">
                    <i class="fas fa-user" aria-hidden="true"></i>
                    <span>Danh sách lớp</span>
                </a>
            </div>
            <div class="nav-section">
                <a href="CandidateDetails.jsp" class="nav-item ${pageContext.request.requestURI.contains('CandidateDetails.jsp') ? 'active' : ''}" role="link" aria-current="${pageContext.request.requestURI.contains('CandidateDetails.jsp') ? 'page' : 'false'}">
                    <i class="fas fa-user" aria-hidden="true"></i>
                    <span>Trạng thái nộp tiền</span>
                </a>
            </div>
            <div class="nav-section">
                <a href="${pageContext.request.contextPath}/SaleDashboard?action=notifications" class="nav-item ${pageContext.request.requestURI.contains('notifications') ? 'active' : ''}" role="link" aria-current="${pageContext.request.requestURI.contains('notifications') ? 'page' : 'false'}">
                    <i class="fas fa-user" aria-hidden="true"></i>
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