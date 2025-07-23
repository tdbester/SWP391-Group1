<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/12/2025
  Time: 10:08 PM
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
<%--    *  Created on:        2025-06-12--%>
<%--    *  Author:            Cù Thị Huyền Trang--%>
<%--    *--%>
<%--    *  ========================== Change History ==========================--%>
<%--    *  Date        | Author               | Description--%>
<%--    *  ------------|----------------------|----------------------------------%>
<%--    *  2025-06-12  | Cù Thị Huyền Trang   | Initial creation--%>
<%--    */--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="sidebar">
    <nav>
        <div class="nav-section">
            <a href="${pageContext.request.contextPath}/View/student-dashboard.jsp"
               class="nav-item ${pageContext.request.requestURI.contains('student-dashboard.jsp') ? 'active' : ''}"
               role="link"
               aria-current="${pageContext.request.requestURI.contains('student-dashboard.jsp') ? 'page' : 'false'}">
                <i class="fas fa-tachometer-alt" aria-hidden="true"></i>
                <span>Dashboard</span>
            </a>
        </div>
        <div class="nav-section">
            <a href="${pageContext.request.contextPath}/StudentSchedule"
               class="nav-item ${pageContext.request.requestURI.contains('student-schedule.jsp') ? 'active' : ''}"
               role="link"
               aria-current="${pageContext.request.requestURI.contains('student-schedule.jsp') ? 'page' : 'false'}">
                <i class="fas fa-calendar-alt" aria-hidden="true"></i>
                <span>Thời khoá biểu</span>
            </a>
        </div>
        <div class="nav-section">
            <a href="${pageContext.request.contextPath}/StudentAttendanceReport"
               class="nav-item ${pageContext.request.requestURI.contains('student-attendance-report.jsp') ? 'active' : ''}"
               role="link"
               aria-current="${pageContext.request.requestURI.contains('student-attendance-report.jsp') ? 'page' : 'false'}">
                <i class="fas fa-clipboard-check" aria-hidden="true"></i>
                <span>Báo cáo điểm danh</span>
            </a>
        </div>
        <div class="nav-section">
            <a href="${pageContext.request.contextPath}/StudentApplication?action=application"
               class="nav-item ${pageContext.request.requestURI.contains('student-request.jsp') ? 'active' : ''}"
               role="link"
               aria-current="${pageContext.request.requestURI.contains('student-request.jsp') ? 'page' : 'false'}">
                <i class="fas fa-paper-plane" aria-hidden="true"></i>
                <span>Gửi đơn</span>
            </a>
        </div>
        <div class="nav-section">
            <a href="${pageContext.request.contextPath}/StudentApplication?action=list"
               class="nav-item ${pageContext.request.requestURI.contains('student-request-list.jsp') ? 'active' : ''}"
               role="link"
               aria-current="${pageContext.request.requestURI.contains('Student-application-list.jsp') ? 'page' : 'false'}">
                <i class="fas fa-file-alt" aria-hidden="true"></i>
                <span>Xem đơn</span>
            </a>
        </div>
        <div class="nav-section">
            <a href="${pageContext.request.contextPath}/StudentDashboard?action=notifications" class="nav-item ${pageContext.request.requestURI.contains('notifications') ? 'active' : ''}" role="link" aria-current="${pageContext.request.requestURI.contains('notifications') ? 'page' : 'false'}">
                <i class="fas fa-bell" aria-hidden="true"></i>
                <span>Thông báo</span>
            </a>
        </div>
    </nav>
</div>
<script>
    document.querySelectorAll('.nav-item').forEach(item => {
        item.addEventListener('click', function () {
            document.querySelectorAll('.nav-item').forEach(nav => nav.classroomList.remove('active'));
            this.classroomList.add('active');
        });
    });

    function toggleSidebar() {
        document.querySelector('.sidebar').classroomList.toggle('active');
    }
</script>

